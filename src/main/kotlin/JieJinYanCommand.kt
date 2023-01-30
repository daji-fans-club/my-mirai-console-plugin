package com.reimia.myplugin

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.NormalMember

/**
 * 瑟图配置命令管理
 */
object JieJinYanCommand : SimpleCommand(EroPicMain, "解禁") {

    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override val prefixOptional: Boolean = true

    @Handler
    suspend fun CommandSender.mute(target: Member) {
        val result = kotlin.runCatching {
            if (target is NormalMember){
                target.unmute()
            }
        }.getOrElse {
            it.stackTraceToString()
        } // 失败时返回堆栈信息
    }

}