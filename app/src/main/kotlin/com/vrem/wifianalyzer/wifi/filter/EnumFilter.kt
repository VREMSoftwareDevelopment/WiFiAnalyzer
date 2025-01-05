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
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.vrem.wifianalyzer.wifi.filter.adapter.EnumFilterAdapter

internal abstract class EnumFilter<T : Enum<T>, U : EnumFilterAdapter<T>>(
    internal val ids: Map<T, Int>,
    private val filter: U,
    alertDialog: AlertDialog,
    id: Int
) {

    private fun setColor(view: View, value: T) {
        this.filter.color(value).let {
            val color = ContextCompat.getColor(view.context, it)
            when (view) {
                is TextView -> view.setTextColor(color)
                is ImageView -> view.setColorFilter(color)
            }
        }
    }

    init {
        ids.keys.forEach { value -> ids[value]?.let { process(alertDialog, it, value) } }
        alertDialog.findViewById<View>(id).visibility = View.VISIBLE
    }

    private fun process(alertDialog: AlertDialog, id: Int, value: T) {
        val view = alertDialog.findViewById<View>(id)
        view.setOnClickListener { onClickListener(value, it) }
        setColor(view, value)
    }

    private fun onClickListener(value: T, view: View) {
        filter.toggle(value)
        setColor(view, value)
    }
}