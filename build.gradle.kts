plugins {
    java
}

allprojects {

    group = "com.reignleif.tgl"
    version = "0.1.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

}

subprojects {
    apply(plugin = "java")


    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(25)
            vendor = JvmVendorSpec.GRAAL_VM
        }
    }

}