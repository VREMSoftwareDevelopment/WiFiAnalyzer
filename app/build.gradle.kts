/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

import java.math.BigDecimal
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.allopen)
    id("jacoco")
    alias(libs.plugins.ktlint)
}

// dependencies -------------------------------------------------
dependencies {
    // Compile Build Dependencies
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.media)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.google.material)
    implementation(libs.vico.views)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    // Unit Test Dependencies
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.junit.toolbox)
    testImplementation(libs.junit)
    testImplementation(libs.assertj.core)
    testImplementation(libs.hamcrest)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.robolectric)
    testImplementation(libs.slf4j.simple)
    // Android Test Dependencies
    androidTestImplementation(libs.androidx.test.espresso.contrib)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.assertj.core)
    androidTestImplementation(libs.hamcrest)
}

android {
    namespace = "com.vrem.wifianalyzer"
    compileSdk = 37
    buildToolsVersion = "37.0.0"

    sourceSets.all {
        kotlin.directories.add("src/$name/kotlin")
    }

    defaultConfig {
        applicationId = "com.vrem.wifianalyzer"
        minSdk = 24
        targetSdk = 37
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt")))
        }
        named("debug") {
            applicationIdSuffix = ".BETA"
            versionNameSuffix = "-BETA"
            isMinifyEnabled = false
            isDebuggable = true
            enableUnitTestCoverage = true
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

    tasks.withType<Test>().configureEach {
        jvmArgs(
            "-XX:+EnableDynamicAgentLoading",
            "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
        )
        maxHeapSize = "2g"
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceIn(1, 4)
        testLogging {
            events =
                setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                )
            showStandardStreams = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    lint {
        lintConfig = file("lint.xml")
    }
}

allOpen {
    annotation("com.vrem.annotation.OpenClass")
}

// keystore -------------------------------------------------
fun configureSigning() {
    if (isReleaseTask()) {
        val propertiesFile = file("androidkeystore.properties")
        if (propertiesFile.exists()) {
            val properties: Properties = readProperties(propertiesFile)
            println(">>> Signing Config $properties")
            android.signingConfigs.create("releaseConfig") {
                keyAlias = properties["key_alias"].toString()
                keyPassword = properties["key_password"].toString()
                storeFile = file(properties["store_filename"].toString())
                storePassword = properties["store_password"].toString()
            }
            android.buildTypes.getByName("release").signingConfig = android.signingConfigs.getByName("releaseConfig")
        } else {
            System.err.println(">>> No Signing Config found! Missing ${propertiesFile.name} file!")
        }
    }
}
configureSigning()

// version -------------------------------------------------
fun updateVersion() {
    val propertiesFile = file("build.properties")
    val properties: Properties = readProperties(propertiesFile)

    val versionMajor = properties["version_major"].toString().toInt()
    val versionMinor = properties["version_minor"].toString().toInt()
    var versionPatch = properties["version_patch"].toString().toInt()
    var versionBuild = properties["version_build"].toString().toInt()
    var versionStore = properties["version_store"].toString().toInt()

    if (isReleaseTask()) {
        println(">>> Building Release...")
        versionPatch++
        versionStore++
        versionBuild = 0
        properties["version_patch"] = versionPatch.toString()
        properties["version_store"] = versionStore.toString()
        properties["version_build"] = versionBuild.toString()
        writeProperties(propertiesFile, properties)
    }
    if (isTestTask()) {
        println(">>> Running Tests...")
        versionBuild++
        properties["version_build"] = versionBuild.toString()
        writeProperties(propertiesFile, properties)
    }

    var versionName = "$versionMajor.$versionMinor.$versionPatch"
    var applicationId = android.defaultConfig.applicationId!!
    if (!isReleaseTask()) {
        versionName = "$versionName.$versionBuild"
        applicationId = applicationId + android.buildTypes.getByName("debug").applicationIdSuffix
    }
    println(">>> ${project.parent?.name} $versionName ($versionStore) $applicationId")
    android.defaultConfig.versionCode = versionStore
    android.defaultConfig.versionName = versionName
}
updateVersion()

fun isTestTask(): Boolean {
    val tasks = gradle.startParameter.taskNames
    return ":app:testDebugUnitTest" in tasks ||
        "testDebugUnitTest" in tasks ||
        ":app:testReleaseUnitTest" in tasks ||
        "testReleaseUnitTest" in tasks
}

fun isReleaseTask(): Boolean {
    val tasks = gradle.startParameter.taskNames
    return ":app:assembleRelease" in tasks ||
        "assembleRelease" in tasks ||
        ":app:bundleRelease" in tasks ||
        "bundleRelease" in tasks
}

fun readProperties(propertiesFile: File): Properties {
    val properties = Properties()
    if (propertiesFile.canRead()) {
        propertiesFile.inputStream().use { inputStream ->
            properties.load(inputStream)
        }
        return properties
    } else {
        val message = ">>> Could not read ${propertiesFile.name} file!"
        System.err.println(message)
        throw RuntimeException(message)
    }
}

fun writeProperties(
    propertiesFile: File,
    properties: Properties,
) {
    propertiesFile.outputStream().use { outputStream ->
        properties.store(outputStream, "Build Properties")
    }
}

configurations.configureEach {
    exclude(group = "org.hamcrest", module = "hamcrest-core")
    exclude(group = "org.hamcrest", module = "hamcrest-library")
}

// Jacoco configuration -----------------------------------------
configure<JacocoPluginExtension> {
    toolVersion = libs.versions.jacoco.get()
}

val fileFilter =
    listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/databinding/*.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$DefaultImpls.class",
    )

val kotlinSourceDirs = files("src/main/kotlin")
val kotlinClassesDir =
    project.layout.buildDirectory.dir(
        "intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes",
    )
val javaClassesDir = project.layout.buildDirectory.dir("intermediates/javac/debug/compileDebugJavaWithJavac/classes")
val executionDataFile =
    project.layout.buildDirectory.file(
        "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
    )
val filteredClassDirs =
    listOf(
        fileTree(kotlinClassesDir).exclude(fileFilter),
        fileTree(javaClassesDir).exclude(fileFilter),
    )

tasks.withType<Test>().configureEach {
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val jacocoReportProvider =
    tasks.register<JacocoReport>("jacocoTestReport") {
        group = "verification"
        description = "Generates Jacoco coverage reports for the debug build."
        dependsOn("testDebugUnitTest")
        reports {
            csv.required.set(false)
            xml.required.set(true)
            html.required.set(true)
        }
        sourceDirectories.from(kotlinSourceDirs)
        classDirectories.from(filteredClassDirs)
        executionData.from(executionDataFile)
    }

tasks.register<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    group = "verification"
    description = "Verifies Jacoco coverage metrics for the debug build."
    dependsOn(jacocoReportProvider)
    sourceDirectories.from(kotlinSourceDirs)
    classDirectories.from(filteredClassDirs)
    executionData.from(executionDataFile)
    violationRules {
        isFailOnViolation = true
        rule {
            element = "BUNDLE"
            limit {
                counter = "INSTRUCTION"
                minimum = BigDecimal.valueOf(0.98)
            }
            limit {
                counter = "BRANCH"
                minimum = BigDecimal.valueOf(0.95)
            }
            limit {
                counter = "COMPLEXITY"
                minimum = BigDecimal.valueOf(0.96)
            }
            limit {
                counter = "LINE"
                minimum = BigDecimal.valueOf(0.99)
            }
            limit {
                counter = "METHOD"
                minimum = BigDecimal.valueOf(0.98)
            }
            limit {
                counter = "CLASS"
                minimum = BigDecimal.valueOf(0.99)
            }
        }
    }
}
// --------------------------------------------------------------
