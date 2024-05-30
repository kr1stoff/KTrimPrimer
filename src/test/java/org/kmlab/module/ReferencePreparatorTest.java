package org.kmlab.module;

import org.junit.Test;
import org.kmlab.util.SoftwarePathFinder;

public class ReferencePreparatorTest {
    @Test
    public void testReferencePrepare() {
        ReferencePreparator.build(
                "/sdbb/bioinfor/Database/references/NC_045512.2/NC_045512.2.fasta",
                "/sdbb/bioinfor/mengxf/TASKS/WY240529A/result/ktrimprimer_temp",
                SoftwarePathFinder.getBwaPath());
    }
}