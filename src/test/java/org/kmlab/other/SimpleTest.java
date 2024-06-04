package org.kmlab.other;

import org.junit.Test;

public class SimpleTest {
    @Test
    public void test() {
        // test substring
        String outputFastq = "/home/kmlab/Desktop/test.fastq";
        String outputDirectory = outputFastq.substring(0, outputFastq.lastIndexOf("/"));
        System.out.println(outputDirectory);
        System.out.println("Hello world!");
        }
}
