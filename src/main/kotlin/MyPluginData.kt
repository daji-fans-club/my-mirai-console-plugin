package org.example.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MyPluginData : AutoSavePluginData("MyData") {

    @ValueDescription("已下载过的瑟图pid集合")
    val eroPicPidSet: MutableSet<Int> by value()


}