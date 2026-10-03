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
package com.vrem.wifianalyzer.wifi.filter

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationMenu
import com.vrem.wifianalyzer.wifi.filter.adapter.FiltersAdapter

class Filter(
    val alertDialog: AlertDialog?,
    private val filtersAdapter: FiltersAdapter = MainContext.INSTANCE.filtersAdapter,
    private val mainActivity: MainActivity = MainContext.INSTANCE.mainActivity,
) {
    private var ssidFilter: SSIDFilter? = null
    internal var wiFiBandFilter: WiFiBandFilter? = null
        private set
    internal var strengthFilter: StrengthFilter? = null
        private set
    internal var securityFilter: SecurityFilter? = null
        private set

    fun show() {
        if (alertDialog != null && !alertDialog.isShowing) {
            alertDialog.show()
            wiFiBandFilter = addWiFiBandFilter(alertDialog)
            ssidFilter = addSSIDFilter(alertDialog)
            strengthFilter = addStrengthFilter(alertDialog)
            securityFilter = addSecurityFilter(alertDialog)
        }
    }

    private fun addSSIDFilter(alertDialog: AlertDialog): SSIDFilter =
        SSIDFilter(filtersAdapter.ssidAdapter(), alertDialog)

    private fun addWiFiBandFilter(alertDialog: AlertDialog): WiFiBandFilter? =
        if (NavigationMenu.ACCESS_POINTS == mainActivity.currentNavigationMenu()) {
            WiFiBandFilter(filtersAdapter.wiFiBandAdapter(), alertDialog)
        } else {
            alertDialog.findViewById<View>(R.id.filterWiFiBand)?.visibility = View.GONE
            null
        }

    private fun addStrengthFilter(alertDialog: AlertDialog): StrengthFilter =
        StrengthFilter(filtersAdapter.strengthAdapter(), alertDialog)

    private fun addSecurityFilter(alertDialog: AlertDialog): SecurityFilter =
        SecurityFilter(filtersAdapter.securityAdapter(), alertDialog)

    private class Close(
        private val filtersAdapter: FiltersAdapter,
    ) : DialogInterface.OnClickListener {
        override fun onClick(
            dialog: DialogInterface,
            which: Int,
        ) {
            dialog.dismiss()
            filtersAdapter.reload()
        }
    }

    private class Apply(
        private val filtersAdapter: FiltersAdapter,
        private val mainActivity: MainActivity,
    ) : DialogInterface.OnClickListener {
        override fun onClick(
            dialog: DialogInterface,
            which: Int,
        ) {
            dialog.dismiss()
            filtersAdapter.save()
            mainActivity.update()
        }
    }

    private class Reset(
        private val filtersAdapter: FiltersAdapter,
        private val mainActivity: MainActivity,
    ) : DialogInterface.OnClickListener {
        override fun onClick(
            dialog: DialogInterface,
            which: Int,
        ) {
            dialog.dismiss()
            filtersAdapter.reset()
            mainActivity.update()
        }
    }

    companion object {
        fun build(
            filtersAdapter: FiltersAdapter = MainContext.INSTANCE.filtersAdapter,
            mainActivity: MainActivity = MainContext.INSTANCE.mainActivity,
            layoutInflater: () -> LayoutInflater = MainContext.INSTANCE::layoutInflater,
        ): Filter = Filter(buildAlertDialog(filtersAdapter, mainActivity, layoutInflater), filtersAdapter, mainActivity)

        private fun buildAlertDialog(
            filtersAdapter: FiltersAdapter,
            mainActivity: MainActivity,
            layoutInflater: () -> LayoutInflater,
        ): AlertDialog? {
            if (mainActivity.isFinishing) {
                return null
            }
            val view = layoutInflater().inflate(R.layout.filter_popup, null)
            return AlertDialog
                .Builder(view.context)
                .setView(view)
                .setTitle(R.string.filter_title)
                .setIcon(R.drawable.ic_filter_list)
                .setNegativeButton(R.string.filter_reset, Reset(filtersAdapter, mainActivity))
                .setNeutralButton(R.string.filter_close, Close(filtersAdapter))
                .setPositiveButton(R.string.filter_apply, Apply(filtersAdapter, mainActivity))
                .create()
        }
    }
}
