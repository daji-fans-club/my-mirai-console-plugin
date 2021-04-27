package com.reimia.myplugin

import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeFriendMessages

fun eroPicHandler() {
    GlobalEventChannel.subscribeAlways<GroupMessageEvent> {

    }

    GlobalEventChannel.subscribeFriendMessages {

        always {
            if (EroPicConfig.setulai.contains(message.contentToString())) {
                val eroPic = EroPic<Friend>()
                EroPicMain.launch {
                    sender.sendMessage(eroPic.toReadString())
                    eroPic.messageReceipt = sender.sendImage(eroPic.eroPicInputStream)
                    eroPic.messageReceipt.recallIn(EroPicConfig.recall)
                }
                if (EroPicConfig.saveLocal) {
                    EroPicMain.launch {
                        val catching = runCatching {
                            eroPic.writeInFile()
                        }
                        if (catching.isFailure) {
                            EroPicMain.logger.warning("图像本地存储失败：${eroPic.pid},错误原因：${catching.exceptionOrNull()?.message}")
                            bot.getFriend(EroPicConfig.ownerqq)
                                ?.sendMessage("图像本地存储失败：${eroPic.pid},错误原因：${catching.exceptionOrNull()?.message}")
                        }
                    }
                }
                if (EroPicConfig.saveRemote) {
                    EroPicMain.launch {
                        val catching = runCatching {
                            eroPic.upload()
                        }
                        if (catching.isFailure) {
                            EroPicMain.logger.warning("图像远程存储失败：${eroPic.pid},错误原因：${catching.exceptionOrNull()?.message}")
                            bot.getFriend(EroPicConfig.ownerqq)
                                ?.sendMessage("图像远程存储失败：${eroPic.pid},错误原因：${catching.exceptionOrNull()?.message}")
                        }

                    }
                }
            }
        }
    }

    GlobalEventChannel.subscribeAlways<NewFriendRequestEvent> {
        //自动同意好友申请
        accept()
    }
    GlobalEventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent> {
        //自动同意加群申请
        accept()
    }
}