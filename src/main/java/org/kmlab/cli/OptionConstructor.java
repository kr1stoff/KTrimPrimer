package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class OptionConstructor {
    private static final Logger logger = LogManager.getLogger(OptionConstructor.class);

    public static Map<String, Object> getOptionMap(String[] args) {
        logger.info("Run the command line argument constructor");
        CommandLine commandLine = OptionReceiver.parseOptions(args);
        Map<String, Object> optionMap = OptionMapper.generateMapOptions(commandLine);
        OptionValidator.validate(optionMap);
        return optionMap;
    }
}
