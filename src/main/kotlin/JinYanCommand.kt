package com.reimia.myplugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Member

/**
 * 瑟图配置命令管理
 */
object JinYanCommand : SimpleCommand(EroPicMain, "禁言") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSender.mute(target: Member, duration: Int) {
        val result = kotlin.runCatching {
            target.mute(duration).toString()
        }.getOrElse {
            it.stackTraceToString()
        } // 失败时返回堆栈信息
    }

}