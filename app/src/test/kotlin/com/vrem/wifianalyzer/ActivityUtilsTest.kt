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
package com.vrem.wifianalyzer

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.BAKLAVA])
class ActivityUtilsTest {
    private val window: Window = mock()
    private val actionBar: ActionBar = mock()
    private val toolbar: Toolbar = mock()
    private val intent: Intent = mock()
    private val intentArgumentCaptor = argumentCaptor<Intent>()
    private val mainActivity = MainContextHelper.INSTANCE.mainActivity
    private val settings = MainContextHelper.INSTANCE.settings

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(toolbar)
        verifyNoMoreInteractions(actionBar)
        verifyNoMoreInteractions(window)
        verifyNoMoreInteractions(settings)
        verifyNoMoreInteractions(intent)
    }

    @Test
    fun setupToolbar() {
        // setup
        doReturn(toolbar).whenever(mainActivity).findViewById<View>(R.id.toolbar)
        doReturn(actionBar).whenever(mainActivity).supportActionBar
        // execute
        val actual = mainActivity.setupToolbar()
        // validate
        assertThat(actual).isEqualTo(toolbar)
        verify(mainActivity).findViewById<View>(R.id.toolbar)
        verify(mainActivity).supportActionBar
        verify(mainActivity).setSupportActionBar(toolbar)
        verify(actionBar).setHomeButtonEnabled(true)
        verify(actionBar).setDisplayHomeAsUpEnabled(true)
    }

    @Test
    fun keepScreenOnSwitchOn() {
        // setup
        doReturn(true).whenever(settings).keepScreenOn()
        doReturn(window).whenever(mainActivity).window
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun keepScreenOnSwitchOff() {
        // setup
        doReturn(false).whenever(settings).keepScreenOn()
        doReturn(window).whenever(mainActivity).window
        // execute
        mainActivity.keepScreenOn()
        // validate
        verify(settings).keepScreenOn()
        verify(mainActivity).window
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    @Test
    fun startWiFiSettings() {
        // execute
        mainActivity.startWiFiSettings()
        // validate
        verify(mainActivity).startActivity(intentArgumentCaptor.capture())
        assertThat(intentArgumentCaptor.firstValue.action).isEqualTo(Settings.Panel.ACTION_WIFI)
    }

    @Test
    fun startLocationSettings() {
        // execute
        mainActivity.startLocationSettings()
        // validate
        verify(mainActivity).startActivity(intentArgumentCaptor.capture())
        assertThat(intentArgumentCaptor.firstValue.action).isEqualTo(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }
}
