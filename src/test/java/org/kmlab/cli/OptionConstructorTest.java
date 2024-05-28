package org.kmlab.cli;

import org.junit.Test;

public class OptionConstructorTest {
    @Test
    public void testConstructor() {
        String[] args = {"-h"};
        OptionConstructor.getOptionMap(args);
    }
}
