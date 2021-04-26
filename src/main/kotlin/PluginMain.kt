package com.reimia.myplugin

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
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
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText

object PluginMain : KotlinPlugin(
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
        MyPluginConfig.reload()
        MyPluginData.reload()

        MyPluginConfigManager.register()
        AbstractPermitteeId.AnyContact.permit(MyPluginConfigManager.permission)

        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            //群消息
            //复读示例
            if (message.contentToString().startsWith("复读")) {
                group.sendMessage(message.contentToString().replace("复读", ""))
            }
            if (message.contentToString() == "hi") {
                //群内发送
                group.sendMessage("hi")
                //向发送者私聊发送消息
                sender.sendMessage("hi")
                //不继续处理
                return@subscribeAlways
            }
            //分类示例
            message.forEach {
                //循环每个元素在消息里
                if (it is Image) {
                    //如果消息这一部分是图片
                    val url = it.queryUrl()
                    group.sendMessage("图片，下载地址$url")
                }
                if (it is PlainText) {
                    //如果消息这一部分是纯文本
                    group.sendMessage("纯文本，内容:${it.content}")
                }
            }
        }

        globalEventChannel().subscribeFriendMessages {
            MyPluginConfig.enableSaveLocal {
                MyPluginConfig.saveLocal = !MyPluginConfig.saveLocal
                sender.sendMessage("切换本地存储设置成功，当前为：" + MyPluginConfig.saveLocal)
            }

            MyPluginConfig.enableUpload {
                MyPluginConfig.upload = !MyPluginConfig.upload
                sender.sendMessage("切换远程存储设置成功，当前为：" + MyPluginConfig.saveLocal)
            }

            always {
                if (MyPluginConfig.setulai.contains(message.contentToString())) {
                    val eroPic = EroPic()
                    eroPic.getEroPic()
                    PluginMain.launch {
                        sender.sendMessage(eroPic.toReadString())
                        sender.sendImage(eroPic.eroPicInputStream)
                    }
                    if (MyPluginConfig.saveLocal) {
                        PluginMain.launch {
                            val catching = runCatching {
                                eroPic.writeInFile()
                            }
                            if (catching.isFailure) {
                                bot.getFriend(MyPluginConfig.ownerqq)?.sendMessage("图像本地存储失败：${eroPic.toReadString()}")
                            }
                        }
                    }
                    if (MyPluginConfig.upload) {
                        PluginMain.launch {
                            val catching = runCatching {
                                eroPic.upload()
                            }
                            if (catching.isFailure) {
                                bot.getFriend(MyPluginConfig.ownerqq)?.sendMessage("图像远程存储失败：${eroPic.toReadString()}")
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
        AbstractPermitteeId.AnyContact.cancel(MyPluginConfigManager.permission, true)
        MyPluginConfigManager.unregister()
        logger.info("Plugin unloaded")
    }
}