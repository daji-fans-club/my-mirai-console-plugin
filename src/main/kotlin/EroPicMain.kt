package com.reimia.myplugin

import io.ktor.client.*
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

object EroPicMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.reimia.myplugin",
        name = "EroPic",
        version = "0.1.0"
    ) {
        author("Reimia")

        info(
            """
            瑟图插件测试
        """.trimIndent()
        )

        // author 和 info 可以删除.
    }
) {
    lateinit var client: HttpClient

    override fun onEnable() {
        EroPicConfig.reload()
        EroPicData.reload()

        EroPicConfigManager.register()
        JinYanCommand.register()
        JieJinYanCommand.register()
        AbstractPermitteeId.AnyContact.permit(EroPicConfigManager.permission)
        AbstractPermitteeId.AnyContact.permit(JinYanCommand.permission)
        AbstractPermitteeId.AnyContact.permit(JieJinYanCommand.permission)

        eroPicHandler()
        client = HttpClient()
        logger.info("EroPic Plugin loaded")

    }

    override fun onDisable() {
        AbstractPermitteeId.AnyContact.cancel(EroPicConfigManager.permission, true)
        AbstractPermitteeId.AnyContact.cancel(JinYanCommand.permission, true)
        AbstractPermitteeId.AnyContact.cancel(JieJinYanCommand.permission, true)
        EroPicConfigManager.unregister()
        JinYanCommand.unregister()
        JieJinYanCommand.unregister()
        logger.info("EroPic Plugin unloaded")
    }
}