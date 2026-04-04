# Performance Tweaks
Improves performance on client and server side!

## Credit
Copyright is important to me. The reason for adding most of these mods comes down to the fact that they are no longer
maintained or supported, or that I wanted to implement new functionality

https://modrinth.com/mod/alternate-current
Licensed MIT (March 2026)

https://www.curseforge.com/minecraft/mc-mods/let-me-despawn
Licensed GNU LESSER GENERAL PUBLIC LICENSE (March 2026)

https://www.curseforge.com/minecraft/mc-mods/entity-collision-fps-fix
Creative Commons Legal Code CC0 1.0 Universal (March 2026)

https://www.curseforge.com/minecraft/mc-mods/fxs-rail-optimization
Licensed GNU LESSER GENERAL PUBLIC LICENSE (March 2026)


## Planned
Mods to add next:

Very many players
* https://github.com/RelativityMC/VMP-forge

Noisium
* https://github.com/Steveplays28/noisium
* https://www.curseforge.com/minecraft/mc-mods/noisium-legacy/
## Access transformers / access wideners (Frustum example)
For Minecraft `1.20.1` multiloader setup in this repo:

- LegacyForge reads `common/src/main/resources/META-INF/accesstransformer.cfg` via `forge/build.gradle`.
- Fabric reads `common/src/main/resources/perf_tweaks.accesswidener` via `fabric/build.gradle` and `fabric.mod.json`.

If IntelliJ still reports `private access` after editing these files:

1. Reimport the Gradle project (`Reload All Gradle Projects`).
2. Regenerate run configs from Gradle tasks (`:fabric:genSources` / `:forge:prepareRuns` if needed).
3. Build with Gradle once from terminal (`./gradlew clean build`) so transformed classpath is refreshed.
4. In IntelliJ, enable *Build and run using: Gradle* for the project.

This keeps IDE classpaths aligned with the transformed access rules used by each loader.
