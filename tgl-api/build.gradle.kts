plugins {
    java
}

group = "com.reignleif.tgl"
version = "0.1.0"

val lwjglVersion = "3.4.1"
val jomlVersion = "1.10.8"
val lwjglNatives = "natives-linux"
val tinylogVersion = "2.7.0"

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-zstd")
    implementation("org.joml", "joml", jomlVersion)

    implementation("org.tinylog:tinylog-api:$tinylogVersion")
}