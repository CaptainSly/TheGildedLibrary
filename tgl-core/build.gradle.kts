plugins {
    java
}

group = "com.reignleif.tgl"
version = "0.1.0"

val lwjglVersion = "3.4.1"
val lwjglNatives = "natives-linux"
val tinylogVersion = "2.7.0"

dependencies {
    implementation(project(":tgl-api"))

    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-renderdoc")
    implementation("org.lwjgl", "lwjgl-sdl")
    implementation("org.lwjgl", "lwjgl-meshoptimizer")
    implementation("org.lwjgl", "lwjgl-vma")
    implementation("org.lwjgl", "lwjgl-vulkan")
    implementation("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-sdl", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-zstd", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-vma", classifier = lwjglNatives)

    implementation("org.tinylog:tinylog-impl:$tinylogVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    create("testPlugin") {
        java.srcDir("src/testPlugin/java")
        compileClasspath += sourceSets["main"].output + configurations["testCompileClasspath"]
    }
}

val testPluginJar = tasks.register<Jar>("testPluginJar") {
    from(sourceSets["testPlugin"].output)
    archiveFileName.set("test-plugin.jar")
    destinationDirectory.set(layout.buildDirectory.dir("testPlugins"))
}

tasks.test {
    dependsOn(testPluginJar)
    useJUnitPlatform()
}