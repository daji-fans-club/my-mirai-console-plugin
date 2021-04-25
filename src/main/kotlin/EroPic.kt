package com.reimia.myplugin

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream

/**
 * 瑟图对象
 */
class EroPic {

    //瑟图属性
    private var uid: Int = 0
    private var author: String = ""
    private var r18: Boolean = false
    private var pid: Int = 0
    private var title: String = ""
    private var url: String = ""
    private var tags = listOf<String>()

    //瑟图的可重复输入流
    lateinit var eroPicInputStream: ByteArrayInputStream

    //瑟图的可重复输出流
    private val eroPicOutputStream = ByteArrayOutputStream()

    fun getEroPic() {
        val eroPicRequestUrl = "http://api.lolicon.app/setu"
        val client = HttpClient()
        val responseData = runBlocking {
            client.get<String>(eroPicRequestUrl) {
                parameter("apikey", MyPluginConfig.apiKey)
                parameter("r18", MyPluginConfig.r18)
                parameter("size1200", "true")
            }
        }
        println("原始请求数据：$responseData")
        val eroPicResponseJson: EroPicResponseJson = Json.decodeFromString(responseData)
        val eroPicJson = eroPicResponseJson.data!![0]
        uid = eroPicJson.uid
        author = eroPicJson.author
        r18 = eroPicJson.r18
        pid = eroPicJson.pid
        title = eroPicJson.title
        url = eroPicJson.url
        tags = eroPicJson.tags

        val inputStream = runBlocking {
            client.get<InputStream>(url)
        }
        this.eroPicOutputStream.flush()
        this.eroPicOutputStream.writeBytes(inputStream.readAllBytes())
        eroPicInputStream = ByteArrayInputStream(eroPicOutputStream.toByteArray())
        inputStream.close()
        PluginMain.logger.info("下载完成：$this")
    }

    fun writeInFile() {
        if (MyPluginData.eroPicPidSet.contains(pid)) {
            PluginMain.logger.info("重复图片，不再保存")
            return
        }
        val path = "${MyPluginConfig.saveLocalPath}/$pid.png"
        val fileOutputStream = FileOutputStream(path)
        eroPicOutputStream.writeTo(fileOutputStream)
        MyPluginData.eroPicPidSet.add(pid)
        PluginMain.logger.info("文件写入成功，文件位置：$path")
    }

    suspend fun upload() {
        if (MyPluginData.eroPicPidSet.contains(pid)) {
            PluginMain.logger.info("重复图片，不再上传")
            return
        }
        PluginMain.logger.info("开始上传，上传地址：${MyPluginConfig.uploadUrl}?key=${MyPluginConfig.uploadSecret}&format=json")
        val client = HttpClient()
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "${MyPluginConfig.uploadUrl}?key=${MyPluginConfig.uploadSecret}&format=json",
            formData = formData {
                append("source", eroPicOutputStream.toByteArray(), Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, """filename=${pid}.png""")
                })
            }
        )
        MyPluginData.eroPicPidSet.add(pid)
        PluginMain.logger.info("上传完成，响应${response.readText()}")
    }

    override fun toString(): String {
        return "EroPic(uid=$uid, author='$author', r18=$r18, pid=$pid, title='$title', url='$url', tags=$tags)"
    }

    fun toReadString(): String {
        return "标题：$title\n" +
            "作者：$author\n" +
            "标签：$tags"
    }

}