package com.reimia.myplugin

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
    override fun onEnable() {
        EroPicConfig.reload()
        EroPicData.reload()

        EroPicConfigManager.register()
        AbstractPermitteeId.AnyContact.permit(EroPicConfigManager.permission)

        eroPicHandler()

        logger.info("EroPic Plugin loaded")

    }

    override fun onDisable() {
        AbstractPermitteeId.AnyContact.cancel(EroPicConfigManager.permission, true)
        EroPicConfigManager.unregister()
        logger.info("EroPic Plugin unloaded")
    }
}