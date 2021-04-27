package com.reimia.myplugin

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object EroPicData : AutoSavePluginData("EroPic") {

    @ValueDescription("已下载过的瑟图pid集合")
    val eroPicPidSet: MutableSet<Int> by value()

    @ValueDescription("瑟图剩余请求数量")
    val memberEroPicLeftCount: MutableMap<Long, Int> by value()
}