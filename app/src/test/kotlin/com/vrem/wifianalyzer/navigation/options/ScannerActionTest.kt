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
package com.vrem.wifianalyzer.navigation.options

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.whenever
import com.vrem.wifianalyzer.MainContextHelper
import com.vrem.wifianalyzer.wifi.scanner.ScannerService
import org.junit.After
import org.junit.Test
import org.mockito.Mockito.verify

class ScannerActionTest {
    private val scannerService: ScannerService = MainContextHelper.INSTANCE.scannerService

    @After
    fun tearDown() {
        MainContextHelper.INSTANCE.restore()
    }

    @Test
    fun testExecuteWithPause() {
        // setup
        whenever(scannerService.isRunning).thenReturn(true)
        // execute
        scannerAction()
        // validate
        verify(scannerService).isRunning
        verify(scannerService).pause()
        verify(scannerService, never()).resume()
    }

    @Test
    fun testExecuteActionWithResume() {
        // setup
        whenever(scannerService.isRunning).thenReturn(false)
        // execute
        scannerAction()
        // validate
        verify(scannerService).isRunning
        verify(scannerService, never()).pause()
        verify(scannerService).resume()
    }

}