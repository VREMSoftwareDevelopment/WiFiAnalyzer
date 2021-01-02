/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2021 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.permission

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.vrem.wifianalyzer.RobolectricUtil
import com.vrem.wifianalyzer.permission.PermissionDialog.CancelClick
import com.vrem.wifianalyzer.permission.PermissionDialog.OkClick
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class PermissionDialogTest {
    private val activity = RobolectricUtil.INSTANCE.activity
    private val fixture = PermissionDialog(activity)

    @Test
    fun testShow() {
        // execute
        fixture.show()
    }

    @Test
    fun testOkClick() {
        // setup
        val activity: Activity = mock()
        val dialog: DialogInterface = mock()
        val fixture = OkClick(activity)
        // execute
        fixture.onClick(dialog, 0)
        // validate
        verify(activity).requestPermissions(ApplicationPermission.PERMISSIONS, ApplicationPermission.REQUEST_CODE)
        verify(dialog).dismiss()
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(dialog)
    }

    @Test
    fun testCancelClick() {
        // setup
        val activity: Activity = mock()
        val dialog: DialogInterface = mock()
        val fixture = CancelClick(activity)
        // execute
        fixture.onClick(dialog, 0)
        // validate
        verify(activity).finish()
        verify(dialog).dismiss()
        verifyNoMoreInteractions(activity)
        verifyNoMoreInteractions(dialog)
    }
}