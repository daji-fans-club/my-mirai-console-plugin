plugins {
    val kotlinVersion = "1.4.31"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("net.mamoe.mirai-console") version "2.7.1"
}

mirai{
    version = "2.7.1"
}

group = "com.reimia.myplugin"
version = "0.1.3"

repositories {
    mavenLocal()
    maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    mavenCentral()
    jcenter()
}

dependencies {

}