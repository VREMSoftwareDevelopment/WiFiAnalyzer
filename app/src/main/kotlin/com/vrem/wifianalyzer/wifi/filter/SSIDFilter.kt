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
package com.vrem.wifianalyzer.wifi.filter

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.vrem.util.SPACE_SEPARATOR
import com.vrem.util.specialTrim
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.wifi.filter.adapter.SSIDAdapter

internal class SSIDFilter(ssidAdapter: SSIDAdapter, alertDialog: AlertDialog) {
    internal class OnChange(private val ssidAdapter: SSIDAdapter) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // Do nothing
        }

        override fun afterTextChanged(s: Editable) {
            ssidAdapter.selections = "$s".specialTrim().split(String.SPACE_SEPARATOR).toSet()
        }
    }

    init {
        val value: String = ssidAdapter.selections.toTypedArray().joinToString(separator = String.SPACE_SEPARATOR).specialTrim()
        val editText: EditText = alertDialog.findViewById(R.id.filterSSIDtext)
        editText.setText(value)
        editText.addTextChangedListener(OnChange(ssidAdapter))
        alertDialog.findViewById<View>(R.id.filterSSID).visibility = View.VISIBLE
    }
}