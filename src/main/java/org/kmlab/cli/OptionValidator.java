package org.kmlab.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class OptionValidator {
    private static final Logger logger = LogManager.getLogger(OptionValidator.class);

    /**
     * Validates the command line options provided in the option map.
     * Ensures necessary files exist and required integer options are set.
     *
     * @param optionMap A map containing the command line options as key-value pairs.
     */
    public static void validate(Map<String, Object> optionMap) {
        logger.info("Validate command line options");

        validateFileExists((String) optionMap.get("inputFastq1"));
        validateFileExists((String) optionMap.get("reference"));
        validateFileExists((String) optionMap.get("primers"));
        validateFastq2Exists((String) optionMap.get("inputFastq2"));

        validateInteger(optionMap, "minimumLength", "40");
        validateInteger(optionMap, "offset", "1");
        validateInteger(optionMap, "parallel", "1");

        optionMap.forEach((key, value) -> logger.info("{}: {}", key, value));
    }


    public static void validateFileExists(String queryFile) {
        if (!Files.exists(Paths.get(queryFile))){
            throw new RuntimeException("File not found: " + queryFile);
        }
    }

    public static void validateFastq2Exists(String queryFile) {
        if (queryFile.isEmpty()) {
            return;
        }
        validateFileExists(queryFile);
    }

    public static void validateInteger(Map<String, Object> optionMap, String key, String defaultValue) {
        String minimumLength = (String) optionMap.get(key);
        if (!minimumLength.matches("\\d+")) {
            logger.warn(String.format("The option %s is not an integer: %s", key, minimumLength));
            optionMap.put(key, defaultValue);
        }
    }
}
