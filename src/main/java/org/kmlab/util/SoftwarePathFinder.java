package org.kmlab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SoftwarePathFinder {
    private static final Logger logger = LogManager.getLogger(SoftwarePathFinder.class);

    /**
     * Retrieves a map of software paths from the `software.properties` file.
     *
     * @return a map where the keys are the software names and the values are the paths to the software.
     *
     * @throws RuntimeException if the property file cannot be found or if there is an issue loading the properties from the file.
     * <p>
     * This method:
     * <ul>
     *     <li>Loads the `software.properties` file from the classpath.</li>
     *     <li>Reads each property and populates a map with the software names as keys and their corresponding paths as values.</li>
     * </ul>
     *
     * This method logs the process of obtaining the software path map.
     */
    public static Map<String, String> getSoftwareMap() {
        logger.info("Obtain the software path map");
        String propertyFilePath = "software.properties";
        Properties properties = new Properties();
        Map<String, String> softwarePathMap = new HashMap<>();
        try (var inputStream = SoftwarePathFinder.class.getClassLoader().getResourceAsStream(propertyFilePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
                for (String key : properties.stringPropertyNames()) {
                    softwarePathMap.put(key, properties.getProperty(key));
                }
            } else {
                throw new RuntimeException("Property file not found: " + propertyFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Command execution failed to load the configuration file: " + e.getMessage());
        }
        return softwarePathMap;
    }

    public static String getBwaPath() {
        logger.debug("Get BWA path");
        Map<String, String> softwareMap = getSoftwareMap();
        return softwareMap.get("bwa");
    }

    public static String getVsearchPath() {
        logger.debug("Get VSEARCH path");
        Map<String, String> softwareMap = getSoftwareMap();
        return softwareMap.get("vsearch");
    }
}
