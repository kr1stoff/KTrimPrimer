package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.util.CommandExecutor;
import org.kmlab.util.SoftwarePathFinder;

public class FastqPreparator {
    private static final Logger logger = LogManager.getLogger(FastqPreparator.class);

    /**
     * Prepares the provided FASTQ files for processing.
     * If only one FASTQ file is provided, it either decompresses or links the file.
     * If two FASTQ files are provided, it runs the vsearch tool on them.
     *
     * @param fastqFile1 The path to the first FASTQ file.
     * @param fastqFile2 The path to the second FASTQ file. Can be empty if not provided.
     * @param workDirectory The directory where the processed files will be stored.
     * @param vsearch The path to the vsearch executable.
     */
    public static void prepare(String fastqFile1, String fastqFile2, String workDirectory, String vsearch) {
        logger.info("Prepare fastq file");

        if (fastqFile2.isEmpty()) {
            if (fastqFile1.endsWith(".gz")) {
                zcatFastqFile(fastqFile1, workDirectory);
            } else {
                linkFastqFile(fastqFile1, workDirectory);
            }
        } else {
            runVsearch(fastqFile1, fastqFile2, workDirectory, vsearch);
        }
    }

    public static void linkFastqFile(String fastqFile1, String workDirectory) {
        String command = String.format("ln -sf %s %s/prepared.fq", fastqFile1, workDirectory);
        CommandExecutor.execute(command);
    }

    public static void zcatFastqFile(String fastqFile, String workDirectory) {
        String command = String.format("zcat %s > %s/prepared.fq", fastqFile, workDirectory);
        CommandExecutor.execute(command);
    }

    public static void runVsearch(String fastqFile1, String fastqFile2, String workDirectory, String vsearch) {
        String logFile = workDirectory + "/vsearch.log";
        String command = String.format("cd %s && " +
                        "%s --fastq_mergepairs %s --reverse %s --fastq_minovlen 5 --fastqout prepared.fq " +
                        "--fastqout_notmerged_fwd notmerged.1.fq --fastqout_notmerged_rev notmerged.2.fq && " +
                        "cat notmerged.1.fq notmerged.2.fq >> prepared.fq",
                workDirectory, vsearch, fastqFile1, fastqFile2);
        CommandExecutor.execute(command, logFile);
    }
}
