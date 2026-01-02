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

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext {
        kotlin_version = '2.2.21'
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Added for plugin resolution
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.13.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "org.jetbrains.kotlin:kotlin-allopen:$kotlin_version"
        classpath "com.github.ben-manes:gradle-versions-plugin:0.53.0"
    }
}

allprojects {
    repositories {
        google()
        maven {
            url = 'https://maven.google.com'
        }
        mavenCentral()
    }
    tasks.withType(JavaCompile).tap {
        configureEach {
            options.compilerArgs << "-Xlint:unchecked" << "-Xlint:deprecation"
        }
    }
}

tasks.register('clean', Delete) {
    delete rootProject.layout.buildDirectory
}

apply from: "dependencyUpdates.gradle"
