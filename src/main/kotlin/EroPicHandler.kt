package com.reimia.myplugin

import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeFriendMessages
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.message.data.Image

/**
 * event处理器
 */
fun eroPicHandler() {

    GlobalEventChannel.subscribeGroupMessages {

        always {
            val splitMessage = message.contentToString().split(" ").toList()
            if (EroPicConfig.setulai.contains(splitMessage[0])) {

                var eroPic = EroPic<Group>(splitMessage)
                if (eroPic.pid == EroPicData.eroLastPic) {
                    eroPic = EroPic(splitMessage)
                    if (eroPic.pid == EroPicData.eroLastPic) {
                        eroPic = EroPic(splitMessage)
                        if (eroPic.pid == EroPicData.eroLastPic) {
                            group.sendMessage("别发了，搜了几遍就这一张")
                            return@always;
                        }
                    }
                }
                if (eroPic.url.isBlank()) {
                    group.sendMessage("暂不支持您的xp，请重新再试")
                    return@always;
                }
                EroPicMain.launch {
                    if (EroPicConfig.detail) {
                        group.sendMessage(eroPic.toReadString())
                        EroPicData.eroLastPic = eroPic.pid
                    }
                    eroPic.messageReceipt = group.sendImage(eroPic.eroPicInputStream)
                    if (eroPic.r18) {
                        eroPic.messageReceipt.recallIn(EroPicConfig.recall)
                    }
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

    GlobalEventChannel.subscribeFriendMessages {

        always {
            val splitMessage = message.contentToString().split(" ").toList()

            if (message.contains(Image)) {
                bot.getGroup(EroPicConfig.group)?.sendMessage(message.plus(friend.nameCardOrNick + "发的，我什么都不知道"))
            }

            if (EroPicConfig.setulai.contains(splitMessage[0])) {

                val eroPic = EroPic<Friend>(splitMessage)
                if (eroPic.url.isBlank()) {
                    sender.sendMessage("暂不支持您的xp，请重新再试")
                    return@always;
                }
                EroPicMain.launch {
                    if (EroPicConfig.detail) {
                        sender.sendMessage(eroPic.toReadString())
                    }
                    eroPic.messageReceipt = sender.sendImage(eroPic.eroPicInputStream)
                    if (EroPicConfig.recall > 0) {
                        eroPic.messageReceipt.recallIn(EroPicConfig.recall)
                    }
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