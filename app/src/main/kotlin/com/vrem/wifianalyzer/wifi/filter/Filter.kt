/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
import android.content.DialogInterface
import com.vrem.wifianalyzer.MainContext
import com.vrem.wifianalyzer.R
import com.vrem.wifianalyzer.navigation.NavigationMenu

class Filter(val alertDialog: AlertDialog?) {
    internal var ssidFilter: SSIDFilter? = null
        private set
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
            SSIDFilter(MainContext.INSTANCE.filtersAdapter.ssidAdapter(), alertDialog)

    private fun addWiFiBandFilter(alertDialog: AlertDialog): WiFiBandFilter? =
            if (NavigationMenu.ACCESS_POINTS == MainContext.INSTANCE.mainActivity.currentNavigationMenu()) {
                WiFiBandFilter(MainContext.INSTANCE.filtersAdapter.wiFiBandAdapter(), alertDialog)
            } else null

    private fun addStrengthFilter(alertDialog: AlertDialog): StrengthFilter =
            StrengthFilter(MainContext.INSTANCE.filtersAdapter.strengthAdapter(), alertDialog)

    private fun addSecurityFilter(alertDialog: AlertDialog): SecurityFilter =
            SecurityFilter(MainContext.INSTANCE.filtersAdapter.securityAdapter(), alertDialog)

    private class Close : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            dialog.dismiss()
            MainContext.INSTANCE.filtersAdapter.reload()
        }
    }

    private class Apply : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            dialog.dismiss()
            val mainContext = MainContext.INSTANCE
            mainContext.filtersAdapter.save()
            mainContext.mainActivity.update()
        }
    }

    private class Reset : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface, which: Int) {
            dialog.dismiss()
            val mainContext = MainContext.INSTANCE
            mainContext.filtersAdapter.reset()
            mainContext.mainActivity.update()
        }
    }

    companion object {
        @JvmStatic
        fun build(): Filter {
            return Filter(buildAlertDialog())
        }

        private fun buildAlertDialog(): AlertDialog? {
            val mainContext = MainContext.INSTANCE
            if (mainContext.mainActivity.isFinishing) {
                return null
            }
            val view = mainContext.layoutInflater.inflate(R.layout.filter_popup, null)
            return AlertDialog.Builder(view.context)
                    .setView(view)
                    .setTitle(R.string.filter_title)
                    .setIcon(R.drawable.ic_filter_list)
                    .setNegativeButton(R.string.filter_reset, Reset())
                    .setNeutralButton(R.string.filter_close, Close())
                    .setPositiveButton(R.string.filter_apply, Apply())
                    .create()
        }
    }

}