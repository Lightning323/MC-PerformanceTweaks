package org.lightning323.performancetweaks.optimizations.alternateCurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lightning323.performancetweaks.Constants;
import org.lightning323.performancetweaks.optimizations.alternateCurrent.util.profiler.ACProfiler;
import org.lightning323.performancetweaks.optimizations.alternateCurrent.util.profiler.Profiler;

public class RedstoneOptimization {

    public static final String MOD_NAME = "Redstone Optimization";
    public static final Logger LOGGER = LogManager.getLogger(Constants.MOD_ID);

    public static final boolean DEBUG = false;
    public static boolean on = true;

    public static Profiler createProfiler() {
        return DEBUG ? new ACProfiler() : Profiler.DUMMY;
    }

}
