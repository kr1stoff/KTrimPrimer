package org.kmlab.module;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.kmlab.util.TSVReader;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.template.SequenceView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

public class PrimerTrimmer {
    private static final Logger logger = LogManager.getLogger(BwaAligner.class);

    private final String PRIMERS;
    private final int MINIMUM_LENGTH;
    private final int OFFSET;
    private final int PARALLEL;
    private final Boolean INCLUDE;

    public PrimerTrimmer(String primer, int minimumLength, int offset, int parallel, Boolean include) {
        this.PRIMERS = primer;
        this.MINIMUM_LENGTH = minimumLength;
        this.OFFSET = offset;
        this.PARALLEL = parallel;
        this.INCLUDE = include;
    }

    public void trimSingleSamPrimer(String samFile, String outputFastq) {
        String wasteRecordFile = outputFastq + ".waste.txt";

        try (SamReader reader = SamReaderFactory.makeDefault().open(new File(samFile));
             PrintWriter fastqWriter = new PrintWriter(new FileWriter(outputFastq));
             PrintWriter wasteWriter = new PrintWriter(new FileWriter(wasteRecordFile));
        ) {

            for (SAMRecord record : reader) {
                String readName = record.getReadName();
                int flag = record.getFlags();
                String referenceName = record.getReferenceName();
                int start = record.getAlignmentStart();
                int end = record.getAlignmentEnd();
                String cigar = String.valueOf(record.getCigar());
                String sequence = record.getReadString();
                String quality = record.getBaseQualityString();

                if (sequence.length() < MINIMUM_LENGTH) {
                    wasteWriter.println(String.format("%s\t%s\t%d", referenceName, flag, sequence.length()));
                    continue;
                }

                List<Object> bestPrimer = findBestPrimer(readName, start, end, flag);

                if (!bestPrimer.isEmpty()) {
                    int trimLeftLength = (int) bestPrimer.get(5);
                    int trimRightLength = (int) bestPrimer.get(6);
                    String[] newSequenceQuality = trimSingleSequence(trimLeftLength, trimRightLength, sequence, quality);
                    List<String> position5Value = bestPrimer.subList(0, 5).stream().map(Object::toString).toList();
                    String trimMapKey = String.join("-", position5Value);
                    Object[] continueSequenceQuality = processAndFilterSequence(flag, newSequenceQuality);
                } else if (INCLUDE) {
                    fastqWriter.println(String.format("%s 1:N:0:0\\n%s\\n+\\n%s\\n", readName, sequence, quality));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open the sam/fastq/waste file" + e.getMessage());
        }
    }

    /**
     * Processes and filters the sequence and quality strings based on the provided flag.
     *
     * @param flag an integer representing the flags
     * @param newSequenceQuality a string array where the first element is the sequence and the second is the quality
     * @return an Object array containing a boolean indicating whether to continue, the processed sequence, and the processed quality
     */
    public Object[] processAndFilterSequence(int flag, String[]newSequenceQuality) {
        boolean boolContinue = false;
        String sequence = newSequenceQuality[0];
        String quality = newSequenceQuality[1];

        if ((flag & 4) == 4 || (flag & 256) == 256 || (flag & 2048) == 2048) {
            boolContinue = true;
        } else if ((flag & 16) == 16) {
            sequence = reverseComplement(newSequenceQuality[0]);
            quality = new StringBuilder(newSequenceQuality[1]).reverse().toString();
        } else {
            boolContinue = true;
            System.out.println("Other unknown case FLAG: " + flag);
        }

        if (sequence.length() < MINIMUM_LENGTH) {
            boolContinue = true;
        }

        return new Object[]{boolContinue, sequence, quality};
    }

    /**
     * Computes the reverse complement of the given DNA sequence using BioJava 6.1.0.
     *
     * @param sequence The DNA sequence to be reverse complemented.
     * @return The reverse complemented DNA sequence.
     */
    private String reverseComplement(String sequence) {
        DNASequence dnaSequence;
        try {
            dnaSequence = new DNASequence(sequence);
        } catch (CompoundNotFoundException e) {
            throw new RuntimeException("Failed to create DNASequence: " + e.getMessage());
        }
        SequenceView<NucleotideCompound> reverseComplement = dnaSequence.getReverseComplement();
        return reverseComplement.getSequenceAsString();
    }

    /**
     * Trims the given sequence and quality strings by the specified left and right trim lengths.
     *
     * @param trimLeftLength  The number of bases to trim from the left end of the sequence.
     * @param trimRightLength The number of bases to trim from the right end of the sequence.
     * @param sequence        The sequence string to be trimmed.
     * @param quality         The quality string to be trimmed.
     * @return An array containing the trimmed sequence and quality strings.
     * @throws IllegalArgumentException if the trimming lengths are invalid.
     */
    public String[] trimSingleSequence(int trimLeftLength, int trimRightLength, String sequence, String quality) {
        if (trimLeftLength < 0 || trimRightLength < 0 || trimLeftLength + trimRightLength > sequence.length()) {
            throw new IllegalArgumentException("Invalid trimming lengths");
        }

        int sequenceLength = sequence.length();
        int rightPosition = sequenceLength - trimRightLength;
        String newSequence = sequence.substring(trimLeftLength, rightPosition);
        String newQuality = quality.substring(trimLeftLength, rightPosition);

        return new String[]{newSequence, newQuality};
    }

    /**
     * Finds the best primer for the given read name and coordinates.
     * containing chromosome name, left start position, left end position, right start position, right end position,
     * trim left length, and trim right length.
     *
     * @param readName The name of the read (chromosome).
     * @param start    The start position of the read.
     * @param end      The end position of the read.
     * @param flag     The flag indicating strand information.
     * @return The best primer as a list of objects, or null if no suitable primer is found.
     */
    public List<Object> findBestPrimer(String readName, int start, int end, int flag) {
        List<List<Object>> primersList = getPrimersList();
        Map<Integer, List<Object>> betterPrimerMap = new HashMap<>();

        for (List<Object> primer : primersList) {
            if (isPrimerMatchingRead(primer, readName, start, end, flag)) {
                int[] trimLengths = calculateTrimLengths(primer, start, end, flag);
                int trimLeftLength = trimLengths[0];
                int trimRightLength = trimLengths[1];
                int trimLength = trimLeftLength + trimRightLength;
                if (trimLength != 0) {
                    primer.add(trimLeftLength);
                    primer.add(trimRightLength);
                    betterPrimerMap.put(trimLength, primer);
                }
            }
        }

        if (!betterPrimerMap.isEmpty()) {
            int maxKey = Collections.max(betterPrimerMap.keySet());
            return betterPrimerMap.get(maxKey);
        } else {
            return null;
        }
    }

    /**
     * Checks if the given primer matches the read based on the specified coordinates and flag.
     *
     * @param primer   The primer to check.
     * @param readName The name of the read (chromosome).
     * @param start    The start position of the read.
     * @param end      The end position of the read.
     * @param flag     The flag indicating strand information.
     * @return True if the primer matches the read, false otherwise.
     */
    private boolean isPrimerMatchingRead(List<Object> primer, String readName, int start, int end, int flag) {
        String chrom = (String) primer.get(0);
        int leftStart = (int) primer.get(1) - OFFSET;
        int leftEnd = (int) primer.get(2);
        int rightStart = (int) primer.get(3);
        int rightEnd = (int) primer.get(4) + OFFSET;

        return chrom.equals(readName) &&
                (leftStart <= start && start <= leftEnd && rightStart <= end && end <= rightEnd ||
                        (leftStart <= start && start <= leftEnd && flag == 0) ||
                        (rightStart <= end && end <= rightEnd && (flag & 16) == 16));
    }

    /**
     * Calculates the trim lengths for the given primer based on the read coordinates and flag.
     *
     * @param primer The primer for which to calculate trim lengths.
     * @param start  The start position of the read.
     * @param end    The end position of the read.
     * @param flag   The flag indicating strand information.
     * @return An array containing the left and right trim lengths.
     */
    private int[] calculateTrimLengths(List<Object> primer, int start, int end, int flag) {
        int leftStart = (int) primer.get(1) - OFFSET;
        int leftEnd = (int) primer.get(2);
        int rightStart = (int) primer.get(3);
        int rightEnd = (int) primer.get(4) + OFFSET;
        int trimLeftLength = 0;
        int trimRightLength = 0;

        if (leftStart <= start && start <= leftEnd && rightStart <= end && end <= rightEnd) {
            trimLeftLength = leftEnd - start + 1;
            trimRightLength = end - rightStart + 1;
        } else if (leftStart <= start && start <= leftEnd && flag == 0) {
            trimLeftLength = leftEnd - start + 1;
        } else if (rightStart <= end && end <= rightEnd && (flag & 16) == 16) {
            trimRightLength = end - rightStart + 1;
        }

        return new int[]{trimLeftLength, trimRightLength};
    }

    /**
     * Parses a TSV file to retrieve a list of primers.
     * Each primer is represented as a list of objects containing chromosome name,
     * left start position, left end position, right start position, and right end position.
     *
     * @return A list of primers, each represented as a list of objects.
     * @throws RuntimeException if an I/O error occurs while parsing the file.
     */
    public List<List<Object>> getPrimersList() {
        List<List<Object>> primersList = new ArrayList<>();

        try (CSVParser csvParser = TSVReader.parseTSV(PRIMERS, true)) {
            for (CSVRecord record : csvParser) {
                String chrom = record.get("chrom");
                int leftStart = Integer.parseInt(record.get("left_start"));
                int leftEnd = Integer.parseInt(record.get("left_end"));
                int rightStart = Integer.parseInt(record.get("right_start"));
                int rightEnd = Integer.parseInt(record.get("right_end"));
                primersList.add(Arrays.asList(chrom, leftStart, leftEnd, rightStart, rightEnd));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse primers file: " + e.getMessage());
        }

        return primersList;
    }
}