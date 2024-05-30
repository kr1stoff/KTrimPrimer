package org.kmlab.module;

import org.junit.Test;
import org.kmlab.util.SoftwarePathFinder;

public class FastqPreparatorTest {
    @Test
    public void testPrepare() {
        FastqPreparator.prepare("/sdbb/bioinfor/mengxf/TASKS/WY240529A/CX2023021_S1_L001_R1_001.fastq.gz",
                "/sdbb/bioinfor/mengxf/TASKS/WY240529A/CX2023021_S1_L001_R2_001.fastq.gz", "/sdbb/bioinfor/mengxf/TASKS/WY240529A/result/ktrimprimer_temp",
                SoftwarePathFinder.getVsearchPath());
    }
}