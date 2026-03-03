plugins {
    java
}

allprojects {
    group = "com.reignleif.tgl"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "25"
        targetCompatibility = "25"
    }


}