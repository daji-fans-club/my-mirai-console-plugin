package org.example.mirai.plugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object MyPluginConfig : AutoSavePluginConfig("MyConfig") {

    @ValueDescription("来张瑟图指令")
    var setulai: MutableList<String> by value(mutableListOf("涩图", "瑟图"))

    @ValueDescription("bot的qq号")
    val qq: Long by value()

    @ValueDescription("bot的密码")
    val password: String by value()

    @ValueDescription("lolicon的apikey")
    val apiKey: String by value()

    @ValueDescription("启停本地存储")
    val enableSaveLocal: String by value("切换本地存储")

    @ValueDescription("是否本地存储")
    var saveLocal: Boolean by value(false)

    @ValueDescription("本地存储位置")
    val saveLocalPath: String by value(System.getProperty("user.dir"))

    @ValueDescription("启停远程存储")
    val enableUpload :String by value("切换远程存储")

    @ValueDescription("是否远程存储")
    var upload: Boolean by value(false)

    @ValueDescription("远程图床的url")
    val uploadUrl: String by value()

    @ValueDescription("远程图床的secret")
    val uploadSecret: String by value()


}