package com.reimia.myplugin

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 瑟图对象
 */
class EroPic<C : Contact>(splitMessage: List<String>) {

    //瑟图属性
    var uid: Int = 0
    var author: String = ""
    var r18: Boolean = false
    var pid: Int = 0
    var title: String = ""
    var url: String = ""
    var tags = listOf<String>()

    //瑟图的可重复输入流
    lateinit var eroPicInputStream: ByteArrayInputStream

    //瑟图的可重复输出流
    private val eroPicOutputStream = ByteArrayOutputStream()

    //图片消息的回执，用于撤回和引用
    lateinit var messageReceipt: MessageReceipt<C>

    init {
        val eroPicRequestUrl = "https://api.lolicon.app/setu/v2"
        var tagList: List<String> = ArrayList()
        if (splitMessage.size > 1) {
            tagList = splitMessage.drop(1)
        }
        val responseData = runBlocking {
            EroPicMain.client.get<String>(eroPicRequestUrl) {
                parameter("r18", EroPicConfig.r18)
                parameter("size", "regular")
                tagList.forEach { item: String -> parameter("tag", item) }
            }
        }
        println("原始请求数据：$responseData")
        val eroPicResponseJson: EroPicResponseJson = Json.decodeFromString(responseData)
        if (eroPicResponseJson.data!!.isNotEmpty()) {
            val eroPicJson = eroPicResponseJson.data[0]
            uid = eroPicJson.uid
            author = eroPicJson.author
            r18 = eroPicJson.r18
            pid = eroPicJson.pid
            title = eroPicJson.title
            url = eroPicJson.urls["regular"]!!
            tags = eroPicJson.tags
            val replaceUrl = url.replace("i.pixiv.cat", "i.pixiv.re")
            val inputStream = runBlocking {
                EroPicMain.client.get<InputStream>(replaceUrl)
            }
            this.eroPicOutputStream.writeBytes(inputStream.readAllBytes())
            eroPicInputStream = ByteArrayInputStream(eroPicOutputStream.toByteArray())
            inputStream.close()
            EroPicMain.logger.info("下载完成：$this")
        }
    }

    fun writeInFile() {
        if (EroPicData.eroPicPidSet.contains(pid)) {
            EroPicMain.logger.info("重复图片，不再保存")
            return
        }
        val path = "${EroPicConfig.saveLocalPath}/$pid.png"
        val fileOutputStream = FileOutputStream(path)
        eroPicOutputStream.writeTo(fileOutputStream)
        EroPicData.eroPicPidSet.add(pid)
        EroPicMain.logger.info("文件写入成功，文件位置：$path")
    }

    suspend fun upload() {
        if (EroPicData.eroPicPidSet.contains(pid)) {
            EroPicMain.logger.info("重复图片，不再上传")
            return
        }
        EroPicMain.logger.info("开始上传，上传地址：${EroPicConfig.saveRemoteUrl}?key=${EroPicConfig.saveRemoteSecret}&format=json")
        val response: HttpResponse = EroPicMain.client.submitFormWithBinaryData(
            url = "${EroPicConfig.saveRemoteUrl}?key=${EroPicConfig.saveRemoteSecret}&format=json",
            formData = formData {
                append("source", eroPicOutputStream.toByteArray(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, """filename=${pid}.png""")
                })
            }
        )
        EroPicData.eroPicPidSet.add(pid)
        EroPicMain.logger.info("上传完成，响应${response.readText()}")
    }

    override fun toString(): String {
        return "EroPic(uid=$uid, author='$author', r18=$r18, pid=$pid, title='$title', url='$url', tags=$tags)"
    }

    fun toReadString(): String {
        return "标题：$title\n" +
            "作者：$author\n" +
            "标签：$tags\n" +
            "pid：$pid\n" +
            "url：$url"
    }

}