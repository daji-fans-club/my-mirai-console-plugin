package com.reimia.myplugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

/**
 * 瑟图配置命令管理
 */
object EroPicConfigManager : CompositeCommand(EroPicMain, "ero") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @SubCommand("set")
    suspend fun CommandSender.set(property: String, value: String) {
        when (property) {
            "r18" -> EroPicConfig.r18 = value.toInt()
            "detail" -> EroPicConfig.detail = value.toBoolean()
            "recall" -> EroPicConfig.recall = value.toLong()
            "saveLocal" -> EroPicConfig.saveLocal = value.toBoolean()
            "saveRemote" -> EroPicConfig.saveRemote = value.toBoolean()
            else -> return
        }
        sendMessage("设置成功")
    }

    @SubCommand("get")
    suspend fun CommandSender.get(property: String) {
        when (property) {
            "r18" -> sendMessage(EroPicConfig.r18.toString())
            "detail" -> sendMessage(EroPicConfig.detail.toString())
            "recall" -> sendMessage(EroPicConfig.recall.toString())
            "saveLocal" -> sendMessage(EroPicConfig.saveLocal.toString())
            "saveRemote" -> sendMessage(EroPicConfig.saveRemote.toString())
        }
    }
}