package org.lightning323.performancetweaks.optimizations.server.redstone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lightning323.performancetweaks.optimizations.server.redstone.util.profiler.ACProfiler;
import org.lightning323.performancetweaks.optimizations.server.redstone.util.profiler.Profiler;

public class AlternateCurrentMod {

    public static final String MOD_NAME = "Alternate Current";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final boolean DEBUG = false;
    public static boolean on = true;

    public static Profiler createProfiler() {
        return DEBUG ? new ACProfiler() : Profiler.DUMMY;
    }

}
