package org.kmlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.cli.OptionConstructor;

import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("开始分析.");
        // 获取命令行参数
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);
    }
}