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

package com.vrem.wifianalyzer.export

import android.content.Intent
import com.vrem.annotation.OpenClass

@OpenClass
class ExportIntent {

    internal fun intent(title: String, data: String): Intent {
        val intentSend: Intent = intentSend()
        intentSend.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TITLE, title)
        intentSend.putExtra(Intent.EXTRA_SUBJECT, title)
        intentSend.putExtra(Intent.EXTRA_TEXT, data)
        return intentChooser(intentSend, title)
    }

    internal fun intentSend(): Intent = Intent(Intent.ACTION_SEND)

    internal fun intentChooser(intent: Intent, title: String): Intent = Intent.createChooser(intent, title)
}