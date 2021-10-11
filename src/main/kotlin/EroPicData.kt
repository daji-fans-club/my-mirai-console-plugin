package com.reimia.myplugin

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 瑟图存储数据
 */
object EroPicData : AutoSavePluginData("EroPic") {

    @ValueDescription("已下载过的瑟图pid集合")
    val eroPicPidSet: MutableSet<Int> by value()

    @ValueDescription("上一张图片")
    var eroLastPic: Int by value()
}