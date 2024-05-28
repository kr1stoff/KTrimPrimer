package org.kmlab.cli;

import org.junit.Test;

import java.util.Map;

public class OptionConstructorTest {
    @Test
    public void testConstructor() {
        String[] args = {"-i", "/sdbb/bioinfor/mengxf/TASKS/WY240529A/2601WY240208_S1_R1_001.fastq.gz",
        "-r", "/sdbb/share/database/genome/NC_045512.2/NC_045512.2.fasta",
        "-P", "/sdbb/share/pipeline/Sars_Cov2_Amplicon/etc/SARS-COV-2.FLEX_With_AddonV1V2O_primer_info.tab",
        "-p", "a",
        "-s", "b",
        "-m", "c"};
        Map<String, Object> optionMap = OptionConstructor.getOptionMap(args);
    }
}
