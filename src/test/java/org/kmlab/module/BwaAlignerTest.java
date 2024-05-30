package org.kmlab.module;

import org.junit.Test;
import org.kmlab.util.SoftwarePathFinder;

public class BwaAlignerTest {
    @Test
    public void testBwaAligner() {
        BwaAligner.align(SoftwarePathFinder.getBwaPath(),
                "/sdbb/bioinfor/mengxf/TASKS/WY240529A/result/ktrimprimer_temp");
    }
}
