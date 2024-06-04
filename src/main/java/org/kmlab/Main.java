package org.kmlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.cli.OptionConstructor;
import org.kmlab.module.BwaAligner;
import org.kmlab.module.FastqPreparator;
import org.kmlab.module.ReferencePreparator;
import org.kmlab.module.ResultDirectoryMaker;
import org.kmlab.util.SoftwarePathFinder;

import java.util.Map;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Start analyzing");
        // 获取命令行参数
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);

        // 配置参数
        final String INPUT_FASTQ1 = (String) optionMap.get("inputFastq1");
        final String INPUT_FASTQ2 = (String) optionMap.get("inputFastq2");
        final String OUTPUT_FASTQ = (String) optionMap.get("outputFastq");
        final String REFERENCE = (String) optionMap.get("reference");
        final String PRIMERS =  (String) optionMap.get("primers");
        final int MINIMUM_LENGTH = (int) optionMap.get("minimumLength");
        final String OFFSET = (String) optionMap.get("offset");
        final String PARALLEL = (String) optionMap.get("parallel");
        final Boolean INCLUDE = (Boolean) optionMap.get("include");

        final String VSEARCH = SoftwarePathFinder.getVsearchPath();
        final String BWA = SoftwarePathFinder.getBwaPath();

        // 创建结果目录
        String workDirectory = ResultDirectoryMaker.makeDirectory(OUTPUT_FASTQ);

        // 准备 FASTQ
        FastqPreparator.prepare(INPUT_FASTQ1, INPUT_FASTQ2, workDirectory, VSEARCH);

        // 准备参考
        ReferencePreparator.build(REFERENCE, workDirectory, BWA);

        // 比对 FASTQ 到参考
        BwaAligner.align(BWA, workDirectory);
    }
}