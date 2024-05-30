package org.kmlab.other;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class HTSJDKTest {
    @Test
    public void test() {
        String samFile = "/sdbb/bioinfor/mengxf/TASKS/WY240529A/result/ktrimprimer_temp/test.sam";
        try (SamReader reader = SamReaderFactory.makeDefault().open(new File(samFile))) {
            for (SAMRecord record : reader) {
                System.out.println(record.getReadName());
                System.out.println(record.getFlags());
                System.out.println(record.getReferenceName());
                System.out.println(record.getAlignmentStart());
                System.out.println(record.getAlignmentEnd());
                System.out.println(record.getCigar());
                System.out.println(record.getReadString());
                System.out.println(record.getBaseQualityString());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
