package alternate.current;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Taken from
 * https://github.com/SpaceWalkerRS/alternate-current/tree/forge
 */
public class AlternateCurrentMod {
//	public static final String MOD_ID = "alternate_current";
//	public static final String MOD_VERSION = "1.7.0";

	public static final String MOD_NAME = "Alternate Current";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final boolean DEBUG = false;
	public static boolean on = false;

//	@EventBusSubscriber(modid = PerfTweaks.MODID)
//	public static class ModEvents {
//		@SubscribeEvent
//		public static void onRegisterCommands(RegisterCommandsEvent event) {
//			AlternateCurrentCommand.register(event.getDispatcher());
//		}
//	}
}
