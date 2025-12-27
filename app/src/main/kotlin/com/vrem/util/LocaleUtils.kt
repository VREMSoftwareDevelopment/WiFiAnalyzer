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
val DUTCH: Locale = Locale.forLanguageTag("nl")
val GREEK: Locale = Locale.forLanguageTag("el")
val HUNGARIAN: Locale = Locale.forLanguageTag("hu")
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
        Locale.SIMPLIFIED_CHINESE,
        Locale.TRADITIONAL_CHINESE,
        Locale.ENGLISH,
        Locale.FRENCH,
        Locale.GERMAN,
        Locale.ITALIAN,
        Locale.JAPANESE,
        POLISH,
        PORTUGUESE_BRAZIL,
        PORTUGUESE_PORTUGAL,
        SPANISH,
        RUSSIAN,
        TURKISH,
        UKRAINIAN,
    ).toList()

private const val SEPARATOR: String = "_"

fun findByCountryCode(countryCode: String): Locale =
    availableLocales.firstOrNull { countryCode.toCapitalize(Locale.getDefault()) == it.country }
        ?: currentLocale

fun allCountries(): List<Locale> = countriesLocales.values.toList()

fun supportedLanguages(): List<Locale> =
    (baseSupportedLocales + currentLocale).distinct()

fun findByLanguageTag(languageTag: String): Locale {
    val languageTagPredicate: (Locale) -> Boolean = {
        val locale: Locale = fromLanguageTag(languageTag)
        it.language == locale.language && it.country == locale.country
    }
    return supportedLanguages().firstOrNull(languageTagPredicate) ?: currentLocale
}

fun currentCountryCode(): String = currentLocale.country

fun currentLanguageTag(): String = toLanguageTag(currentLocale)

fun toLanguageTag(locale: Locale): String = locale.language + SEPARATOR + locale.country

private fun fromLanguageTag(languageTag: String): Locale {
    val codes: Array<String> = languageTag.split(SEPARATOR).toTypedArray()
    return when (codes.size) {
        1 -> Locale.forLanguageTag(codes[0])
        2 -> Locale.forLanguageTag("${codes[0]}-${codes[1].toCapitalize(currentLocale)}")
        else -> currentLocale
    }
}
