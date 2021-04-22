package org.example.mirai.plugin

import kotlinx.serialization.Serializable

/**
 * 瑟图请求响应字符串
 */
@Serializable
data class EroPicResponseJson (
    val code: Int,
    val msg: String = "",
    val quota: Int,
    val quota_min_ttl: Int,
    val count: Int,
    val data: List<EroPicJson>? = null
) {
    @Serializable
    data class EroPicJson(
        val pid: Int,
        val p: Int,
        val uid: Int,
        val title: String,
        val author: String,
        val url: String,
        val r18: Boolean,
        val width: Int,
        val height: Int,
        val tags: List<String>
    )
}