package org.lightning323.performancetweaks;

import org.lightning323.frikinjay.letmedespawn.LetMeDespawn;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lightning323.alternate.current.command.AlternateCurrentCommand;

import java.io.File;

public final class Performancetweaks {
    public static final String MOD_ID = "perf_tweaks";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final File LET_ME_DESPAWN_CONFIG_FILE = new File("config/perf_tweaks/letmedespawn.json");


    //common init code
    public static void init() {
        LOGGER.info("Performance Tweaks initialized");
        LetMeDespawn.init();
    }

    //common command registration
    public static void onRegisterCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        AlternateCurrentCommand.register(dispatcher);
    }
}
