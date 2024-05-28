package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class OptionsMapper {
    private static final Logger logger = LogManager.getLogger(OptionsMapper.class);

    public static Map<String, Object> generateMapOptions(CommandLine commandLine) {
        logger.info("命令行参数写入字典");
        Map<String, Object> mapOptions = new HashMap<>();
        mapOptions.put("inputFastq1", commandLine.getOptionValue("input-fastq1"));
        mapOptions.put("reference", commandLine.getOptionValue("reference"));
        mapOptions.put("primers", commandLine.getOptionValue("primers"));
        mapOptions.put("outputFastq", commandLine.getOptionValue("output-fastq", "out.ktrimprimer.fq"));
        mapOptions.put("inputFastq2", commandLine.getOptionValue("input-fastq2", ""));
        mapOptions.put("minimumLength", commandLine.getOptionValue("minimum-length", "40"));
        mapOptions.put("offset", commandLine.getOptionValue("offset", "1"));
        mapOptions.put("include", commandLine.hasOption("include"));
        mapOptions.put("parallel", commandLine.getOptionValue("parallel", "1"));
        mapOptions.forEach((key, value) -> logger.info("{}: {}", key, value));
        return mapOptions;
    }
}
