package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;

import java.util.Map;

public class OptionConstructor {

    public static Map<String, Object> getOptionMap(String[] args) {
        CommandLine commandLine = OptionReceiver.parseOptions(args);
        return OptionMapper.generateMapOptions(commandLine);
    }
}