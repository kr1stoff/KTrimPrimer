package org.kmlab;

import org.kmlab.cli.OptionConstructor;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);
    }
}