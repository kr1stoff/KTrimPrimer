package org.kmlab.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TSVReader {
    private static final Logger logger = LogManager.getLogger(TSVReader.class);

    /**
     * Parses a TSV (Tab-Separated Values) file and returns a CSVParser object.
     *
     * @param inFile the path to the TSV file to be parsed.
     * @return a CSVParser object for the given TSV file.
     * @throws RuntimeException if there is an error reading the TSV file.
     */
    public static CSVParser parseTSV(String inFile) {
        logger.info("Read TSV file: {}", inFile);
        try {
            Reader reader = new FileReader(inFile);
            return new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setDelimiter('\t')
                    .setTrim(true)
                    .build());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tsv file: " + e.getMessage());
        }
    }

    /**
     * 解析TSV文件的函数。
     *
     * @param inFile    指定要解析的TSV文件的路径。
     * @param hasHeader 指示TSV文件是否包含头部行。如果为true，则解析时会将第一行作为列名。
     * @return 返回一个CSVParser对象，用于进一步解析TSV文件的内容。
     */
    public static CSVParser parseTSV(String inFile, boolean hasHeader) {
        logger.info("Read TSV file(hasHeader): {}", inFile);
        try {
            Reader reader = new FileReader(inFile);
            return new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setDelimiter('\t')
                    .setHeader()
                    .setSkipHeaderRecord(hasHeader)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tsv file: " + e.getMessage());
        }
    }
}

