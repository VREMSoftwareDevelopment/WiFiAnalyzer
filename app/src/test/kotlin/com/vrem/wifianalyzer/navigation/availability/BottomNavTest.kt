/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2024 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.navigation.availability

import android.view.View
import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever

class BottomNavTest {
    private val mainActivity: MainActivity = mock()
    private val view: View = mock()

    @After
    fun tearDown() {
        verifyNoMoreInteractions(mainActivity)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun navigationOptionBottomNavOff() {
        // setup
        whenever<View>(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view)
        // execute
        navigationOptionBottomNavOff(mainActivity)
        // validate
        verify(mainActivity).findViewById<View>(R.id.nav_bottom)
        verify(view).visibility = View.GONE
    }

    @Test
    fun navigationOptionBottomNavOn() {
        // setup
        whenever<View>(mainActivity.findViewById(R.id.nav_bottom)).thenReturn(view)
        // execute
        navigationOptionBottomNavOn(mainActivity)
        // validate
        verify(mainActivity).findViewById<View>(R.id.nav_bottom)
        verify(view).visibility = View.VISIBLE
    }

}

