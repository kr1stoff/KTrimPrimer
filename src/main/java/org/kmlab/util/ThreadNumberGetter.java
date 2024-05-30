package org.kmlab.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class ThreadNumberGetter {
    private static final Logger logger = LogManager.getLogger(SoftwarePathFinder.class);

    public static int getThreadNumber() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        int availableProcessors = osBean.getAvailableProcessors();
        logger.info("System maximum available thread number: {}", availableProcessors);
        return availableProcessors;
    }
}
