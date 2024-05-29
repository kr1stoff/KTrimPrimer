package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.util.CommandExecutor;
import org.kmlab.util.SoftwarePathFinder;

public class FastqPreparator {
    private static final Logger logger = LogManager.getLogger(FastqPreparator.class);
    private static final String VSEARCH = SoftwarePathFinder.getVsearchPath();

    public static void prepare(String fastqFile1, String fastqFile2, String workDirectory) {
        logger.info("Prepare fastq file.");
        runVsearch(fastqFile1, fastqFile2, workDirectory);
    }

    public static void linkFastqFile(String fastqFile1, String workDirectory) {
        String command = String.format("ln -s %s prepared.fq", fastqFile1);
        CommandExecutor.execute(command);
    }

    public static void runVsearch(String fastqFile1, String fastqFile2, String workDirectory) {
        String logFile = workDirectory + "/vsearch.log";
        String command = String.format("cd %s && " +
                        "%s --fastq_mergepairs %s --reverse %s --fastq_minovlen 5 " +
                        "--fastqout prepared.fq --fastqout_notmerged_fwd notmerged.1.fq && " +
                        "cat notmerged.1.fq notmerged.2.fq >> prepared.fq",
                workDirectory, VSEARCH, fastqFile1, fastqFile2);
        CommandExecutor.execute(command, logFile);
    }
}
