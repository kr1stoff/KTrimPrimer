package org.kmlab;

import org.kmlab.cli.OptionConstructor;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 获取命令行参数
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);
    }
}