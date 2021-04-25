package org.example.mirai.plugin

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.utils.info
import java.io.ByteArrayInputStream

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.reimia.myplugin",
        name = "MyPlugin",
        version = "0.0.1"
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
        logger.info { "Plugin loaded" }
        //配置文件目录 "${dataFolder.absolutePath}/"

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
                sender.sendMessage("切换本地存储设置成功，当前为："+MyPluginConfig.saveLocal)
            }

            MyPluginConfig.enableUpload {
                MyPluginConfig.upload = !MyPluginConfig.upload
                sender.sendMessage("切换远程存储设置成功，当前为："+MyPluginConfig.saveLocal)
            }

            always {
                if (MyPluginConfig.setulai.contains(message.contentToString())) {
                    val eroPic = EroPic()
                    eroPic.getEroPic()
                    launch {
                        sender.sendMessage(eroPic.toReadString())
                        sender.sendImage(eroPic.eroPicInputStream)
                    }
                    if (MyPluginConfig.saveLocal) {
                        launch {
                            eroPic.writeInFile()
                        }
                    }
                    if (MyPluginConfig.upload) {
                        launch {
                            eroPic.upload()
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
    }

    override fun onDisable() {
        logger.info("Plugin unloaded")
    }
}