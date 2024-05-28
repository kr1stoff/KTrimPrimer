package org.kmlab.cli;

import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import java.util.Map;

public class OptionConstructorTest {
    @Test
    public void testConstructor() {
        String[] args = {"-i", "1",
        "-r", "2",
        "-P", "3"};
        Map<String, Object> mapOptions = OptionConstructor.getOptionMap(args);
    }
}
