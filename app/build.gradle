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

plugins {
    id "com.android.application"
    id "kotlin-android"
    id "kotlin-allopen"
    id "jacoco"
    id "org.jlleitschuh.gradle.ktlint" version "14.0.1"
}

apply {
    from "jacoco.gradle"
}

// dependencies -------------------------------------------------
dependencies {
    // Compile Build Dependencies
    implementation fileTree(include: ["*.jar"], dir: "libs")
    implementation 'com.google.android.material:material:1.13.0'
    implementation 'androidx.annotation:annotation:1.9.1'
    implementation 'androidx.appcompat:appcompat:1.7.1'
    implementation 'androidx.collection:collection-ktx:1.5.0'
    implementation 'androidx.core:core-ktx:1.17.0'
    implementation 'androidx.core:core-splashscreen:1.2.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0'
    implementation 'androidx.media:media:1.7.1'
    implementation 'androidx.preference:preference-ktx:1.2.1'
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'com.jjoe64:graphview:4.2.2'
    // Unit Test Dependencies
    testImplementation 'androidx.test.ext:junit:1.3.0'
    testImplementation 'com.googlecode.junit-toolbox:junit-toolbox:2.4'
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.21.0'
    testImplementation 'org.mockito.kotlin:mockito-kotlin:6.1.0'
    testImplementation 'org.robolectric:robolectric:4.16'
    testImplementation "org.jetbrains.kotlin:kotlin-test:$kotlin_version"
    testImplementation "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
    testImplementation 'org.slf4j:slf4j-simple:2.0.17'
    testImplementation 'org.assertj:assertj-core:3.27.6'
    // Android Test Dependencies
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.7.0'
    androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.7.0'
    androidTestImplementation 'androidx.test.ext:junit-ktx:1.3.0'
    androidTestImplementation 'androidx.test:rules:1.7.0'
    androidTestImplementation 'org.assertj:assertj-core:3.27.6'
}

android {
    namespace = 'com.vrem.wifianalyzer'
    compileSdk = 36
    buildToolsVersion '36.1.0'

    sourceSets.each {
        it.java.srcDirs += "src/$it.name/kotlin"
    }

    defaultConfig {
        applicationId = "com.vrem.wifianalyzer"
        minSdkVersion 24
        targetSdkVersion 36
        versionCode
        versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            minifyEnabled = true
            shrinkResources = true
            proguardFiles getDefaultProguardFile("proguard-android.txt")
            signingConfig
        }
        debug {
            applicationIdSuffix = ".BETA"
            versionNameSuffix = "-BETA"
            minifyEnabled = false
            shrinkResources = false
            debuggable = true
            enableUnitTestCoverage = true
        }
    }

    testOptions {
        unitTests {
            includeAndroidResources = true
            all {
                jvmArgs("-XX:+EnableDynamicAgentLoading")
                testLogging {
                    events = ["passed", "skipped", "failed", "standardOut", "standardError"]
                    outputs.upToDateWhen { false }
                    showStandardStreams = true
                }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    lint {
        lintConfig = file("lint.xml")
    }
}

allOpen {
    annotation("com.vrem.annotation.OpenClass")
}

// keystore -------------------------------------------------
signingConfig()

def signingConfig() {
    if (isReleaseTask()) {
        def propertiesFile = file("androidkeystore.properties")
        if (propertiesFile.exists()) {
            Properties properties = readProperties(propertiesFile)
            System.out.println(">>> Signing Config " + properties)
            android.signingConfigs.create("releaseConfig") {
                keyAlias = properties["key_alias"].toString()
                keyPassword = properties["key_password"].toString()
                storeFile = file(properties["store_filename"].toString())
                storePassword = properties["store_password"].toString()
            }
            android.buildTypes.release.signingConfig = android.signingConfigs.releaseConfig
        } else {
            System.err.println(">>> No Signing Config found! Missing '" + propertiesFile.name + "' file!")
        }
    }
}

// version -------------------------------------------------
updateVersion()

def updateVersion() {
    def propertiesFile = file("build.properties")
    Properties properties = readProperties(propertiesFile)

    def versionMajor = properties["version_major"].toString().toInteger()
    def versionMinor = properties["version_minor"].toString().toInteger()
    def versionPatch = properties["version_patch"].toString().toInteger()
    def versionBuild = properties["version_build"].toString().toInteger()
    def versionStore = properties["version_store"].toString().toInteger()

    if (isReleaseTask()) {
        System.out.println(">>> Building Release...")
        versionPatch++
        versionStore++
        versionBuild = 0
        properties["version_patch"] = versionPatch.toString()
        properties["version_store"] = versionStore.toString()
        properties["version_build"] = versionBuild.toString()
        writeProperties(propertiesFile, properties)
    }
    if (isTestTask()) {
        System.out.println(">>> Running Tests...")
        versionBuild++
        properties["version_build"] = versionBuild.toString()
        writeProperties(propertiesFile, properties)
    }

    def versionName = "${versionMajor}.${versionMinor}.${versionPatch}"
    def applicationId = android.defaultConfig.applicationId
    if (!isReleaseTask()) {
        versionName = versionName + ".${versionBuild}"
        applicationId = applicationId + android.buildTypes.debug.applicationIdSuffix
    }
    System.out.println(">>> " + project.parent.name + " " + versionName + " (" + versionStore + ") " + applicationId)
    android.defaultConfig.versionCode = versionStore
    android.defaultConfig.versionName = versionName
}

def isTestTask() {
    def tasks = gradle.getStartParameter().getTaskNames()
    return ":app:testDebugUnitTest" in tasks || "testDebugUnitTest" in tasks ||
            ":app:testReleaseUnitTest" in tasks || "testReleaseUnitTest" in tasks
}

def isReleaseTask() {
    def tasks = gradle.getStartParameter().getTaskNames()
    return ":app:assembleRelease" in tasks || "assembleRelease" in tasks ||
            ":app:bundleRelease" in tasks || "bundleRelease" in tasks
}

static Properties readProperties(propertiesFile) {
    if (propertiesFile.canRead()) {
        Properties properties = new Properties()
        def inputStream = new FileInputStream(propertiesFile)
        properties.load(inputStream)
        inputStream.close()
        return properties
    } else {
        def message = ">>> Could not read " + propertiesFile.name + " file!"
        System.err.println(message)
        throw new RuntimeException(message)
    }
}

def static writeProperties(propertiesFile, properties) {
    def writer = propertiesFile.newWriter()
    properties.store(writer, "Build Properties")
    writer.close()
}
