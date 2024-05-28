package org.kmlab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SoftwarePathFinder {
    private static final Logger logger = LogManager.getLogger(SoftwarePathFinder.class);

    public static Map<String, String> getSoftwareMap() {
        logger.debug("获取软件路径映射.");
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
                throw new RuntimeException("找不到配置文件: " + propertyFilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败: " + e.getMessage());
        }
        return softwarePathMap;
    }

    public static String getBwaPath() {
        logger.debug("获取BWA路径.");
        Map<String, String> softwareMap = getSoftwareMap();
        return softwareMap.get("bwa");
    }

    public static String getVsearchPath() {
        logger.debug("获取VSEARCH路径.");
        Map<String, String> softwareMap = getSoftwareMap();
        return softwareMap.get("vsearch");
    }
}
