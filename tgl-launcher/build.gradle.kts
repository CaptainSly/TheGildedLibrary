plugins {
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.reignleif.tgl"
version = "0.1.0"

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(project(":tgl-api"))
    implementation(project(":tgl-core"))
}