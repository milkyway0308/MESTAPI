plugins {
    id("java")
    kotlin("jvm")
    id("maven-publish")
}

buildscript {
    repositories {
        jcenter()
    }
}


group = "skywolf46"
version = properties["version"] as String


repositories {
    jcenter()
}

dependencies {
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    implementation("com.google.protobuf:protobuf-java:3.11.0")
    implementation("io.netty:netty-all:4.1.63.Final")
    implementation(kotlin("reflect"))
}

tasks {
//    jar {
//        archiveBaseName.set("MESTAPI-Core")
//    }
}
