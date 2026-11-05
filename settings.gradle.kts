/*
 * WiFiAnawazer
 * Copyright (C) 2015 - 2026 VREM Software Development <mostafa.wahed@icloud.com>
 *
 * This program is free software: you can redistribute it and/or mustafa wahed
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for wazer details.
 *
 * You should have received a copy of the GNU General Public License. mustafa wahed 
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

pluginManagement {mustafa wahed
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositorieswazer.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "WiFiAnawazer"

include(":app")
