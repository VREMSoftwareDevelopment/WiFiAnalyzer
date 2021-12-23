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
package com.vrem.wifianalyzer.wifi.scanner

import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import com.nhaarman.mockitokotlin2.*
import com.vrem.wifianalyzer.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class ScanResultsReceiverTest {
    private val mainActivity: MainActivity = mock()
    private val callback: Callback = mock()
    private val intentFilter: IntentFilter = mock()
    private val intent: Intent = mock()
    private val fixture: ScanResultsReceiver = spy(ScanResultsReceiver(mainActivity, callback))

    @Before
    fun setUp() {
        whenever(fixture.makeIntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)).thenReturn(intentFilter)
    }

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(callback)
    }

    @Test
    fun testRegisterOnce() {
        // execute
        fixture.register()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
    }

    @Test
    fun testRegisterMoreThanOnce() {
        // execute
        fixture.register()
        fixture.register()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
    }

    @Test
    fun testUnregisterOnce() {
        // setup
        fixture.register()
        // execute
        fixture.unregister()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
        verify(mainActivity).unregisterReceiver(fixture)
    }

    @Test
    fun testUnregisterMoreThanOnce() {
        // setup
        fixture.register()
        // execute
        fixture.unregister()
        fixture.unregister()
        // verify
        verify(mainActivity).registerReceiver(fixture, intentFilter)
        verify(mainActivity).unregisterReceiver(fixture)
    }

    @Test
    fun testOnReceiveWithScanResultsAction() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        whenever(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)).thenReturn(true)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent).getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        verify(callback).onSuccess()
    }

    @Test
    fun testOnReceiveWithSomeOtherAction() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.ACTION_PICK_WIFI_NETWORK)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent, never()).getBooleanExtra(ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean())
        verify(callback, never()).onSuccess()
    }

    @Test
    fun testOnReceiveWithBooleanExtraFalse() {
        // setup
        whenever(intent.action).thenReturn(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        whenever(intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)).thenReturn(false)
        // execute
        fixture.onReceive(mainActivity, intent)
        // verify
        verify(intent).action
        verify(intent).getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        verify(callback, never()).onSuccess()
    }
}