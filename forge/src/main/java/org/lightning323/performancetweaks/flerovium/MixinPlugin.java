//package org.lightning323.performancetweaks.flerovium;
//
//import org.lightning323.performancetweaks.platform.Services;
//import org.objectweb.asm.tree.ClassNode;
//import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
//import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
//
//import java.util.List;
//import java.util.Set;
//
//public class MixinPlugin implements IMixinConfigPlugin {
//    @Override
//    public void onLoad(String mixinPackage) {
//    }
//
//    @Override
//    public String getRefMapperConfig() {
//        return null;
//    }
//
//
//    @Override
//    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
//        return switch (mixinClassName) {
//            case "com.moepus.flerovium.mixins.Entity.ModelPartMixin" -> !Services.PLATFORM.isModLoaded("bendylib") && !Services.PLATFORM.isModLoaded("physicsmod");
//            case "com.moepus.flerovium.mixins.Chunk.FrustumMixin" -> !Services.PLATFORM.isModLoaded("acedium") && !Services.PLATFORM.isModLoaded("nvidium");
//            case "com.moepus.flerovium.mixins.Particle.ParticleEngineMixin",
//                 "com.moepus.flerovium.mixins.Particle.ParticleMixin" -> !Services.PLATFORM.isModLoaded("particle_core");
//            case "com.moepus.flerovium.mixins.Sound.ClientLevelMixin",
//                 "com.moepus.flerovium.mixins.Particle.SkipFarTerrainParticle" ->!Services.PLATFORM.isModLoaded("valkyrienskies");
//            default -> true;
//        };
//    }
//
//    @Override
//    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
//
//    }
//
//    @Override
//    public List<String> getMixins() {
//        return null;
//    }
//
//    @Override
//    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//    }
//
//    @Override
//    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
//
//    }
//}