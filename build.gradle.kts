plugins {
    kotlin("jvm") version "1.4.30"
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "2.0.4"
}

buildscript {
    repositories {
        mavenCentral()
    }
}


group = "skywolf46"
version = properties["version"] as String
java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
//    processResources {
//        outputs.upToDateWhen { false }
//        filesMatching("plugin.yml") {
//            expand("version" to project.properties["version"])
//        }
//    }
//    jar {
//        enabled = false
//    }
//    shadowJar {
//        archiveClassifier.set("all")
//    }
//    jar {
//        dependsOn(shadowJar)
//    }
}

repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation(kotlin("stdlib"))
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/FUNetwork/SkywolfExtraUtility")
            credentials {
                username = properties["gpr.user"] as String
                password = properties["gpr.key"] as String
            }
        }
    }
    publications {
        create<MavenPublication>("jar") {
            from(components["java"])
            groupId = "skywolf46"
            artifactId = "exutil"
            version = properties["version"] as String
            pom {
                url.set("https://github.com/FUNetwork/SkywolfExtraUtility.git")
            }
        }
    }
}

