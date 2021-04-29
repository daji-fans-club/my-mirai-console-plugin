package com.reimia.myplugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object EroPicConfigManager : CompositeCommand(EroPicMain, "ero") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @SubCommand("set")
    suspend fun CommandSender.set(property: String, value: String) {
        when (property) {
            "r18" -> EroPicConfig.r18 = value.toInt()
            "saveLocal" -> EroPicConfig.saveLocal = value.toBoolean()
            "saveRemote" -> EroPicConfig.saveRemote = value.toBoolean()
        }
        sendMessage("设置成功")
    }
    @SubCommand("get")
    suspend fun CommandSender.get(property: String){
        when (property) {
            "r18" -> sendMessage(EroPicConfig.r18.toString())
            "saveLocal" -> sendMessage(EroPicConfig.saveLocal.toString())
            "saveRemote" -> sendMessage(EroPicConfig.saveRemote.toString())
        }
    }
}