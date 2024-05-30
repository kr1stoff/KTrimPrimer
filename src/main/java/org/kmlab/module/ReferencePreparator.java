package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.util.CommandExecutor;

import java.nio.file.Paths;

public class ReferencePreparator {
    private static final Logger logger = LogManager.getLogger(ReferencePreparator.class);

    /**
     * Builds the reference index for the provided reference file.
     * Links the reference file to the working directory and builds the index using BWA.
     *
     * @param reference The path to the reference file.
     * @param workDirectory The directory where the processed files will be stored.
     * @param bwa The path to the BWA executable.
     */
    public static void build(String reference, String workDirectory, String bwa) {
        logger.info("Building reference");
        String referenceLink = linkReference(reference, workDirectory);
        buildIndex(bwa, referenceLink, workDirectory);
    }

    public static String linkReference(String reference, String workDirectory) {
        String referenceLink = Paths.get(workDirectory, "reference.fa").toString();
        String command = String.format("ln -sf %s %s", reference, referenceLink);
        CommandExecutor.execute(command);
        return referenceLink;
    }

    public static void buildIndex(String bwa, String referenceLink, String workDirectory) {
        String logFile = Paths.get(workDirectory, "bwa_index.log").toString();
        String command = String.format("%s index %s", bwa, referenceLink);
        CommandExecutor.execute(command, logFile);
    }
}