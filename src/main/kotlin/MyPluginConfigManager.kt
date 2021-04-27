package com.reimia.myplugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object MyPluginConfigManager : SimpleCommand(PluginMain, "set") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSender.r18(property: String, value: String) {
        when (property) {
            "r18" -> {
                MyPluginConfig.r18 = value.toInt()
            }
            "saveLocal" -> {
                MyPluginConfig.saveLocal = value.toBoolean()
            }
            "saveRemote" -> {
                MyPluginConfig.saveRemote = value.toBoolean()
            }
        }
        sendMessage("设置成功")
    }
}