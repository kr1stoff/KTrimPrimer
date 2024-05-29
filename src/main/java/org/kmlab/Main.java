package org.kmlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.cli.OptionConstructor;
import org.kmlab.module.ResultDirectoryMaker;

import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Start analyzing.");
        // 获取命令行参数
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);

        //
        final String INPUT_FASTQ1 = (String) optionMap.get("inputFastq1");
        final String INPUT_FASTQ2 = (String) optionMap.get("inputFastq2");
        final String OUTPUT_FASTQ = (String) optionMap.get("outputFastq");

        // 创建结果目录
        String workDirectory = ResultDirectoryMaker.makeDirectory(OUTPUT_FASTQ);
    }
}