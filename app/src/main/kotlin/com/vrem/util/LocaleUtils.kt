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
package com.vrem.util

import java.util.Locale
import java.util.SortedMap

private object SyncAvoid {
    val countryCodes: Set<String> = Locale.getISOCountries().toSet()
    val availableLocales: List<Locale> = Locale.getAvailableLocales().filter { countryCodes.contains(it.country) }

    val countriesLocales: SortedMap<String, Locale> =
        availableLocales
            .associateBy { it.country.toCapitalize(Locale.getDefault()) }
            .toSortedMap()
    val supportedLocales: List<Locale> =
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
}

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

private const val SEPARATOR: String = "_"

private val CHINESE_SCRIPT_COUNTRIES: Map<String, String> =
    mapOf("Hans" to Locale.SIMPLIFIED_CHINESE.country, "Hant" to Locale.TRADITIONAL_CHINESE.country)

fun findByCountryCode(countryCode: String): Locale =
    SyncAvoid.availableLocales.firstOrNull { countryCode.toCapitalize(Locale.getDefault()) == it.country }
        ?: Locale.ROOT

fun allCountries(): List<Locale> = SyncAvoid.countriesLocales.values.toList()

fun toLegacyLanguageTag(locale: Locale): String = locale.language + SEPARATOR + locale.country

fun findByLegacyLanguageTag(legacyLanguageTag: String): Locale? =
    SyncAvoid.supportedLocales.firstOrNull { toLegacyLanguageTag(it) == legacyLanguageTag }

fun findSupportedLanguage(locale: Locale): Locale? {
    val candidates = SyncAvoid.supportedLocales.filter { it.language == locale.language }
    return CHINESE_SCRIPT_COUNTRIES[locale.script]?.let { country -> candidates.firstOrNull { it.country == country } }
        ?: candidates.firstOrNull { it.country == locale.country }
        ?: candidates.singleOrNull()
}

fun supportedLanguages(): List<Locale> = SyncAvoid.supportedLocales
