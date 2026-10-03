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
package com.vrem.wifianalyzer.settings

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vrem.util.EMPTY
import com.vrem.util.specialTrim
import com.vrem.wifianalyzer.R
import java.text.Normalizer

private val DIACRITICS = "\\p{Mn}+".toRegex()

private fun String.withoutDiacritics(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(DIACRITICS, String.EMPTY)

private fun Data.matches(query: String): Boolean =
    query.isEmpty() ||
        name.withoutDiacritics().contains(query, ignoreCase = true) ||
        code.contains(query, ignoreCase = true)

internal class CustomPreferenceAdapter(
    context: Context,
    private val allValues: List<Data>,
) : ArrayAdapter<Data>(
        context,
        R.layout.preference_dialog_search_item,
        allValues.toMutableList(),
    ) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View = (super.getView(position, convertView, parent) as TextView).apply { text = getItem(position)!!.name }

    fun update(query: String) {
        setNotifyOnChange(false)
        clear()
        val normalizedQuery = query.specialTrim().withoutDiacritics()
        addAll(allValues.filter { it.matches(normalizedQuery) })
        notifyDataSetChanged()
    }

    fun indexOfCode(code: String?): Int = (0 until count).indexOfFirst { getItem(it)!!.code == code }
}
