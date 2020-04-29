/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import java.util.*

private object SyncAvoid {
    val defaultLocale: Locale = Locale.getDefault()
    val countryCodes: Set<String> = Locale.getISOCountries().toSet()
    val availableLocales: List<Locale> = Locale.getAvailableLocales().filter { countryCodes.contains(it.country) }
    var countriesLocales: SortedMap<String, Locale> = availableLocales.map { it.country.capitalize() to it }.toMap().toSortedMap()
    val supportedLocales: List<Locale> = setOf(
            Locale.GERMAN,
            Locale.ENGLISH,
            SPANISH,
            Locale.FRENCH,
            Locale.ITALIAN,
            PORTUGUESE,
            RUSSIAN,
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            defaultLocale)
            .toList()
}

val SPANISH: Locale = Locale("es")

val PORTUGUESE: Locale = Locale("pt")

val RUSSIAN: Locale = Locale("ru")

private const val SEPARATOR: String = "_"

fun findByCountryCode(countryCode: String): Locale =
        SyncAvoid.availableLocales
                .find { countryCode.capitalize() == it.country }
                ?: SyncAvoid.defaultLocale

fun allCountries(): List<Locale> = SyncAvoid.countriesLocales.values.toList()

fun findByLanguageTag(languageTag: String): Locale {
    val languageTagPredicate: (Locale) -> Boolean = {
        val locale: Locale = fromLanguageTag(languageTag)
        it.language == locale.language && it.country == locale.country
    }
    return SyncAvoid.supportedLocales.find(languageTagPredicate) ?: SyncAvoid.defaultLocale
}

fun supportedLanguages(): List<Locale> = SyncAvoid.supportedLocales

fun defaultCountryCode(): String = SyncAvoid.defaultLocale.country

fun defaultLanguageTag(): String = toLanguageTag(SyncAvoid.defaultLocale)

fun toLanguageTag(locale: Locale): String = locale.language + SEPARATOR + locale.country

private fun fromLanguageTag(languageTag: String): Locale {
    val codes: Array<String> = languageTag.split(SEPARATOR).toTypedArray()
    return when (codes.size) {
        1 -> Locale(codes[0])
        2 -> Locale(codes[0], codes[1].capitalize())
        else -> SyncAvoid.defaultLocale
    }
}
