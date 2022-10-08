import com.blamejared.funkyframes.gradle.Properties
import com.blamejared.funkyframes.gradle.Versions

plugins {
    id("com.blamejared.funkyframes.default")
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

minecraft {
    version(Versions.MINECRAFT)
    accessWideners(project.file("src/main/resources/${Properties.MODID}.accesswidener"))
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
}