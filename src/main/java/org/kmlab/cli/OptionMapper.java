package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class OptionMapper {
    private static final Logger logger = LogManager.getLogger(OptionMapper.class);

    /**
     * Generates a map of options based on the provided CommandLine object.
     *
     * @param commandLine the CommandLine object containing the command line arguments
     * @return a Map<String, Object> containing the extracted options
     */
    public static Map<String, Object> generateMapOptions(CommandLine commandLine) {
        logger.info("Write command line options to map.");
        Map<String, Object> optionMap = new HashMap<>();
        optionMap.put("inputFastq1", commandLine.getOptionValue("input-fastq1"));
        optionMap.put("reference", commandLine.getOptionValue("reference"));
        optionMap.put("primers", commandLine.getOptionValue("primers"));
        optionMap.put("outputFastq", commandLine.getOptionValue("output-fastq", "out.ktrimprimer.fq"));
        optionMap.put("inputFastq2", commandLine.getOptionValue("input-fastq2", ""));
        optionMap.put("minimumLength", commandLine.getOptionValue("minimum-length", "40"));
        optionMap.put("offset", commandLine.getOptionValue("offset", "1"));
        optionMap.put("include", commandLine.hasOption("include"));
        optionMap.put("parallel", commandLine.getOptionValue("parallel", "1"));
        return optionMap;
    }
}