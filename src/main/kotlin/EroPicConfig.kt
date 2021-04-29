package com.reimia.myplugin

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

/**
 * 瑟图配置
 */
object EroPicConfig : AutoSavePluginConfig("EroPic") {

    @ValueDescription("来张瑟图指令")
    var setulai: MutableList<String> by value(mutableListOf("来张涩图", "来张瑟图"))

    @ValueDescription("bot所有者的qq号")
    val ownerqq: Long by value()

    @ValueDescription("bot的qq号，在控制台启动用于脱隐")
    val qq: Long by value()

    @ValueDescription("bot的密码，在控制台启动用于脱隐")
    val password: String by value()

    @ValueDescription("lolicon的apikey")
    val apiKey: String by value()

    @ValueDescription("是否r18,0为非R18，1为R18，2为混合")
    var r18: Int by value(0)

    @ValueDescription("瑟图默认每天每人请求数")
    var limit: Int by value(5)

    @ValueDescription("瑟图撤回时间，默认60秒")
    var recall: Long by value(60L)

    @ValueDescription("是否本地存储")
    var saveLocal: Boolean by value(false)

    @ValueDescription("本地存储位置")
    val saveLocalPath: String by value(System.getProperty("user.dir"))

    @ValueDescription("是否远程存储")
    var saveRemote: Boolean by value(false)

    @ValueDescription("远程图床的url")
    val saveRemoteUrl: String by value()

    @ValueDescription("远程图床的secret")
    val saveRemoteSecret: String by value()


}