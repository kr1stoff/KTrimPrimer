package org.kmlab.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class OptionValidator {
    private static final Logger logger = LogManager.getLogger(OptionValidator.class);

    /**
     * Validates the command line options provided in the optionMap. This includes checking for the existence of required files and validating integer options.
     *
     * @param optionMap a map containing the command line options to validate. Expected keys and their corresponding validations include:
     *                  <ul>
     *                      <li>"inputFastq1" - the path to the first input FASTQ file, validated to ensure the file exists.</li>
     *                      <li>"reference" - the path to the reference file, validated to ensure the file exists.</li>
     *                      <li>"primers" - the path to the primers file, validated to ensure the file exists.</li>
     *                      <li>"inputFastq2" - the path to the second input FASTQ file, validated to ensure the file exists if present.</li>
     *                      <li>"minimumLength" - an integer value, validated to ensure it is a valid integer, defaulting to "40" if not provided.</li>
     *                      <li>"offset" - an integer value, validated to ensure it is a valid integer, defaulting to "1" if not provided.</li>
     *                      <li>"parallel" - an integer value, validated to ensure it is a valid integer, defaulting to "1" if not provided.</li>
     *                  </ul>
     *
     * This method logs the validation process and all provided options.
     *
     * @throws IllegalArgumentException if any of the required files do not exist or if any of the integer options are invalid.
     */
    public static void validate(Map<String, Object> optionMap) {
        logger.info("Validate command line options.");
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
