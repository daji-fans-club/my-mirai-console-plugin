package com.reimia.myplugin

import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeFriendMessages

object EroPicMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.reimia.myplugin",
        name = "MyPlugin",
        version = "0.1.0"
    ) {
        author("Reimia")

        info(
            """
            这是一个测试插件, 
            在这里描述插件的功能和用法等.
        """.trimIndent()
        )

        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        EroPicConfig.reload()
        EroPicData.reload()

        EroPicConfigManager.register()
        AbstractPermitteeId.AnyContact.permit(EroPicConfigManager.permission)

        globalEventChannel().subscribeAlways<GroupMessageEvent> {

        }

        globalEventChannel().subscribeFriendMessages {

            always {
                if (EroPicConfig.setulai.contains(message.contentToString())) {
                    val eroPic = EroPic()
                    eroPic.getEroPic()
                    EroPicMain.launch {
                        sender.sendMessage(eroPic.toReadString())
                        sender.sendImage(eroPic.eroPicInputStream)
                    }
                    if (EroPicConfig.saveLocal) {
                        EroPicMain.launch {
                            val catching = runCatching {
                                eroPic.writeInFile()
                            }
                            if (catching.isFailure) {
                                bot.getFriend(EroPicConfig.ownerqq)?.sendMessage("图像本地存储失败：${eroPic.toReadString()}")
                            }
                        }
                    }
                    if (EroPicConfig.saveRemote) {
                        EroPicMain.launch {
                            val catching = runCatching {
                                eroPic.upload()
                            }
                            if (catching.isFailure) {
                                bot.getFriend(EroPicConfig.ownerqq)?.sendMessage("图像远程存储失败：${eroPic.toReadString()}")
                            }

                        }
                    }
                }
            }
        }

        globalEventChannel().subscribeAlways<NewFriendRequestEvent> {
            //自动同意好友申请
            accept()
        }
        globalEventChannel().subscribeAlways<BotInvitedJoinGroupRequestEvent> {
            //自动同意加群申请
            accept()
        }

        logger.info("Plugin loaded")

    }

    override fun onDisable() {
        AbstractPermitteeId.AnyContact.cancel(EroPicConfigManager.permission, true)
        EroPicConfigManager.unregister()
        logger.info("Plugin unloaded")
    }
}