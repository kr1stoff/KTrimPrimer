package org.kmlab.module;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kmlab.util.SoftwarePathFinder;

public class FASTQPreparator {
    private static final Logger logger = LogManager.getLogger(FASTQPreparator.class);
    private static final String VSEARCH = SoftwarePathFinder.getVsearchPath();

    public static void prepare(String fastqFile1) {

    }

    public static void prepare(String fastqFile1, String fastqFile2, String outputDirectory) {
String command = String.format("%s --fastq_mergepairs %s --reverse %s --fastq_minovlen 5 ",
        VSEARCH, fastqFile1, fastqFile2);
//        {self.vsearch} --fastq_mergepairs {self.fastq1} --reverse {self.fastq2} \
//                --fastq_minovlen 5 \
//                --fastqout {self.dir_tmp}/prepared.fq \
//                --fastqout_notmerged_fwd {self.dir_tmp}/notmerged.1.fq \
//                --fastqout_notmerged_rev {self.dir_tmp}/notmerged.2.fq
//            cat {self.dir_tmp}/notmerged.1.fq {self.dir_tmp}/notmerged.2.fq >> {self.dir_tmp}/prepared.fq
    }
}
