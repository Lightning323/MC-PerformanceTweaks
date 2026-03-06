package org.lightning323.alternate.current;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.lightning323.alternate.current.command.AlternateCurrentCommand;
import org.lightning323.alternate.current.util.profiler.ACProfiler;
import org.lightning323.alternate.current.util.profiler.Profiler;


public class AlternateCurrentMod {

    public static final String MOD_ID = "alternate_current";
    public static final String MOD_NAME = "Alternate Current";
    public static final String MOD_VERSION = "1.9.2";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final boolean DEBUG = false;

    public static boolean on = true;

    public static Profiler createProfiler() {
        return DEBUG ? new ACProfiler() : Profiler.DUMMY;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        AlternateCurrentCommand.register(dispatcher);
    }

}
