package com.reimia.myplugin

import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

@ConsoleExperimentalApi
suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    EroPicMain.load()
    EroPicMain.enable()
    val bot = MiraiConsole.addBot(EroPicConfig.qq, EroPicConfig.password) {
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()
}