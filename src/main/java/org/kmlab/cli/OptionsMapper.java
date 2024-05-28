package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;

import java.util.Map;

public class OptionsMapper {
    private static void fillMapOptions(CommandLine commandLine, Map<String, Object> mapOptions) {
        mapOptions.put("inputFastq1", commandLine.getOptionValue("input-fastq1"));
        mapOptions.put("reference", commandLine.getOptionValue("reference"));
        mapOptions.put("primers", commandLine.getOptionValue("primers"));
        mapOptions.put("outputFastq", commandLine.getOptionValue("output-fastq", "out.ktrimprimer.fq"));
        mapOptions.put("inputFastq2", commandLine.getOptionValue("input-fastq2", ""));
        mapOptions.put("minimumLength", commandLine.getOptionValue("minimum-length", "40"));
        mapOptions.put("offset", commandLine.getOptionValue("offset", "1"));
        mapOptions.put("include", commandLine.hasOption("include"));
        mapOptions.put("parallel", commandLine.getOptionValue("parallel", "1"));
    }
}
