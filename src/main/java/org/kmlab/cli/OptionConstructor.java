package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptionConstructor {
    private static final Logger logger = LogManager.getLogger(OptionConstructor.class);

    public static void getOptionMap(String[] args) {
        CommandLine commandLine = CommandLineReceiver.get(args);

        if (commandLine.hasOption("help")) {
            CommandLineReceiver.printHelp();
            System.exit(0);
        }

    }
}