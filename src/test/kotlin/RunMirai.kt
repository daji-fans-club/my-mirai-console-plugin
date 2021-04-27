package com.reimia.myplugin

import net.mamoe.mirai.BotFactory
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
    val bot = BotFactory.newBot(EroPicConfig.qq, EroPicConfig.password) {
        fileBasedDeviceInfo()
    }
    bot.login()

    MiraiConsole.job.join()
}