/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2025 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.util

import java.util.Locale
import java.util.SortedMap

private val currentLocale: Locale get() = Locale.getDefault()
private val countryCodes: Set<String> = Locale.getISOCountries().toSet()
private val availableLocales: List<Locale> = Locale.getAvailableLocales().filter { countryCodes.contains(it.country) }
private val countriesLocales: SortedMap<String, Locale> =
    availableLocales
        .associateBy { it.country.toCapitalize(currentLocale) }
        .toSortedMap()

val BULGARIAN: Locale = Locale.forLanguageTag("bg")

val CHINESE_SIMPLIFIED: Locale = Locale.forLanguageTag("zh-Hans")

val CHINESE_TRADITIONAL: Locale = Locale.forLanguageTag("zh-Hant")
val DUTCH: Locale = Locale.forLanguageTag("nl")
val ENGLISH: Locale = Locale.forLanguageTag("en")
val FRENCH: Locale = Locale.forLanguageTag("fr")
val GERMAN: Locale = Locale.forLanguageTag("de")
val GREEK: Locale = Locale.forLanguageTag("el")
val HUNGARIAN: Locale = Locale.forLanguageTag("hu")
val ITALIAN: Locale = Locale.forLanguageTag("it")
val JAPANESE: Locale = Locale.forLanguageTag("ja")
val POLISH: Locale = Locale.forLanguageTag("pl")
val PORTUGUESE_PORTUGAL: Locale = Locale.forLanguageTag("pt-PT")
val PORTUGUESE_BRAZIL: Locale = Locale.forLanguageTag("pt-BR")
val SPANISH: Locale = Locale.forLanguageTag("es")
val RUSSIAN: Locale = Locale.forLanguageTag("ru")
val TURKISH: Locale = Locale.forLanguageTag("tr")
val UKRAINIAN: Locale = Locale.forLanguageTag("uk")

val baseSupportedLocales: List<Locale> =
    setOf(
        BULGARIAN,
        DUTCH,
        GREEK,
        HUNGARIAN,
        CHINESE_SIMPLIFIED,
        CHINESE_TRADITIONAL,
        ENGLISH,
        FRENCH,
        GERMAN,
        ITALIAN,
        JAPANESE,
        POLISH,
        PORTUGUESE_BRAZIL,
        PORTUGUESE_PORTUGAL,
        SPANISH,
        RUSSIAN,
        TURKISH,
        UKRAINIAN,
    ).toList()

fun findByCountryCode(countryCode: String): Locale =
    availableLocales.firstOrNull { countryCode.uppercase(Locale.ROOT) == it.country }
        ?: currentLocale

fun allCountries(): List<Locale> = countriesLocales.values.toList()

fun supportedLanguages(): List<Locale> = (baseSupportedLocales + currentLocale).distinct()

fun supportedLanguageTags(): List<String> = listOf("") + baseSupportedLocales.map { it.toLanguageTag() }

private fun normalizeLanguageTag(languageTag: String): String = languageTag.replace('_', '-').trim()

fun findByLanguageTag(languageTag: String): Locale {
    val normalizedLanguageTag = normalizeLanguageTag(languageTag)
    if (normalizedLanguageTag.isEmpty()) return currentLocale

    val target = Locale.forLanguageTag(normalizedLanguageTag)
    if (target.language.isEmpty()) return currentLocale

    return baseSupportedLocales.find { it == target }
        ?: baseSupportedLocales.find { it.language == target.language && it.script == target.script }
        ?: baseSupportedLocales.find { it.language == target.language && it.country == target.country }
        ?: baseSupportedLocales.find { it.language == target.language }
        ?: currentLocale
}

fun currentCountryCode(): String = currentLocale.country

fun currentLanguageTag(): String = currentLocale.toLanguageTag()

fun toLanguageTag(locale: Locale): String = locale.toLanguageTag()

fun Locale.toSupportedLocaleTag(): String = findByLanguageTag(this.toLanguageTag()).toLanguageTag()
