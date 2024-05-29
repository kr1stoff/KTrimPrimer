package org.kmlab.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OptionReceiver {
    private static final Logger logger = LogManager.getLogger(OptionReceiver.class);

    private static final Options options = new Options();
    private static final CommandLineParser parser = new DefaultParser();

    static {
        options.addOption(Option.builder("i")
                .longOpt("input-fastq1")
                .hasArg(true)
                .required(true)
                .argName("file")
                .desc("(必需) 单端FQ或双端FQ1.")
                .build());
        options.addOption(Option.builder("r")
                .longOpt("reference")
                .hasArg(true)
                .required(true)
                .argName("file")
                .desc("(必需) 参考基因组,与引物信息文件参考一致.")
                .build());
        options.addOption(Option.builder("P")
                .longOpt("primers")
                .hasArg(true)
                .required(true)
                .argName("file")
                .desc("(必需) 引物信息文件,五列chrom,left_start,left_end,right_start,right_end,详见fgbio TrimPrimers.")
                .build());
        options.addOption(Option.builder("o")
                .longOpt("output-fastq")
                .hasArg(true)
                .argName("file")
                .desc("(可选) 输出FQ文件. [default: ./out.ktrimprimer.fq]")
                .build());
        options.addOption(Option.builder("I")
                .longOpt("input-fastq2")
                .hasArg(true)
                .argName("file")
                .desc("(可选) 双端FQ2.")
                .build());
        options.addOption(Option.builder("m")
                .longOpt("minimum-length")
                .hasArg(true)
                .argName("int")
                .desc("(可选) 允许剪切后read长度的最小阈值,低于该值丢弃. [default: 40]")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("offset")
                .hasArg(true)
                .argName("int")
                .desc("(可选) 允许primer与read间的偏移值. [default: 1]")
                .build());
        options.addOption(Option.builder("c")
                .longOpt("include")
                .hasArg(false)
                .argName("boolean")
                .desc("(可选) 默认丢弃掉无primer的read.如果加上该参数,则保留没有primer的read.")
                .build());
        options.addOption(Option.builder("p")
                .longOpt("parallel")
                .hasArg(true)
                .argName("int")
                .desc("(可选) 最大并行数. [default: 1]")
                .build());
    }

    /**
     * Parses the command line arguments using the provided options.
     *
     * @param args the command line arguments to be parsed
     * @return a CommandLine object containing the parsed options
     */
    public static CommandLine parseOptions(String[] args) {
        logger.info("Parse command line options.");
        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            printHelp();
            throw new RuntimeException("Parse command line options failed: " + e.getMessage());
        }
    }

    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("KTrimPrimer",
                "去引物程序,原始FQ到去引物后FQ. 小数据量单进程,大数据量多进程.", options, null);
    }
}