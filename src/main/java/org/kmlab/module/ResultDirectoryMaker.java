package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResultDirectoryMaker {
    private static final Logger logger = LogManager.getLogger(ResultDirectoryMaker.class);

    /**
     * Creates a temporary work directory based on the given output FASTQ file path.
     *
     * @param outputFastq the path to the output FASTQ file. The directory for the temporary work directory is derived from this path.
     * @return the path to the created temporary work directory as a string.
     *
     * @throws RuntimeException if an IOException occurs while creating the directory.
     * <p>
     * This method:
     * <ul>
     *     <li>Extracts the directory path from the given output FASTQ file path.</li>
     *     <li>Appends ".ktrimprimer_temp" to the extracted directory path to form the temporary work directory path.</li>
     *     <li>Creates the temporary work directory.</li>
     * </ul>
     */
    public static String makeDirectory(String outputFastq) {
        String outputDirectory = outputFastq.substring(0, outputFastq.lastIndexOf("/"));
        Path workDirectoryPath = Paths.get(outputDirectory, "ktrimprimer_temp");
        logger.info("Making work directory {}", workDirectoryPath.toString());

        try {
            Files.createDirectories(workDirectoryPath);
        } catch (IOException e) {
            throw new RuntimeException("Make work directory failed: {}", e);
        }

        return workDirectoryPath.toString();
    }
}
