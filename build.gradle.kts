import com.blamejared.funkyframes.gradle.Properties
import com.blamejared.funkyframes.gradle.Versions
import com.blamejared.modtemplate.Utils
import com.diluv.schoomp.Webhook
import com.diluv.schoomp.message.Message
import com.diluv.schoomp.message.embed.Embed
import java.io.IOException
import java.util.*

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.diluv.schoomp:Schoomp:1.2.5")
    }
}

plugins {
    `java-library`
    idea
    id("com.blamejared.modtemplate")
}

version = Utils.updatingSemVersion(Versions.MOD)

tasks.create("postDiscord") {

    doLast {
        try {

            // Create a new webhook instance for Discord
            val webhook = Webhook(Utils.locateProperty(project, "discordCFWebhook"), "${Properties.NAME} CurseForge Gradle Upload")

            // Craft a message to send to Discord using the webhook.
            val message = Message()
            message.username = Properties.NAME
            message.avatarUrl = Properties.AVATAR
            message.content = "${Properties.NAME} $version for Minecraft ${Versions.MINECRAFT} has been published!"

            val embed = Embed()
            val downloadSources = StringJoiner("\n")

            if (project(":fabric").ext.has("curse_file_url")) {

                downloadSources.add("<:fabric:932163720568782878> [Fabric](${project(":fabric").ext.get("curse_file_url")})")
            }

            if (project(":forge").ext.has("curse_file_url")) {

                downloadSources.add("<:forge:932163698003443804> [Forge](${project(":forge").ext.get("curse_file_url")})")
            }

            downloadSources.add(
                    "<:maven:932165250738970634> `\"com.blamejared.funkyframes:${project(":common").base.archivesName.get()}:${
                        project(":common").version
                    }\"`"
            )
            downloadSources.add(
                    "<:maven:932165250738970634> `\"com.blamejared.funkyframes:${project(":forge").base.archivesName.get()}:${
                        project(":forge").version
                    }\"`"
            )

            // Add Curseforge DL link if available.
            val downloadString = downloadSources.toString()

            if (downloadString.isNotEmpty()) {

                embed.addField("Download", downloadString, false)
            }

            // Just use the Forge changelog for now, the files are the same anyway.
            embed.addField("Changelog", Utils.getCIChangelog(project, Properties.GIT_REPO).take(1000), false)

            embed.color = 0xF16436
            message.addEmbed(embed)

            webhook.sendMessage(message)
        } catch (e: IOException) {

            project.logger.error("Failed to push CF Discord webhook.")
            project.file("post_discord_error.log").writeText(e.stackTraceToString())
        }
    }

}