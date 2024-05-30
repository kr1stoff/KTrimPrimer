package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.util.CommandExecutor;
import org.kmlab.util.ThreadNumberGetter;

import java.nio.file.Paths;

public class BwaAligner {
    private static final Logger logger = LogManager.getLogger(BwaAligner.class);

    /**
     * Aligns the reads to the reference genome using BWA.
     * Constructs and executes the BWA command with appropriate options.
     *
     * @param bwa The path to the BWA executable.
     * @param outputDirectory The directory where the alignment output will be stored.
     */
    public static void align(String bwa, String outputDirectory) {
        logger.info("Aligning reads to reference genome using BWA");
        String logFile = Paths.get(outputDirectory, "bwa_align.log").toString();
        int threadNumber = ThreadNumberGetter.getThreadNumber() / 2;

        String command = String.format("cd %s " +
                        "&& %s mem -t %s -M -Y -R '@RG\\tID:trim\\tSM:trim' reference.fa prepared.fq > raw.sam",
                outputDirectory, bwa, threadNumber);
        CommandExecutor.execute(command, logFile);
    }
}
