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
package com.vrem.wifianalyzer.wifi.accesspoint

import com.vrem.wifianalyzer.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ConnectionViewTypeTest {
    @Test
    fun connectionViewTypeCount() {
        assertThat(ConnectionViewType.entries).hasSize(3)
    }

    @Test
    fun getLayout() {
        assertThat(ConnectionViewType.COMPLETE.layout).isEqualTo(R.layout.access_point_view_complete)
        assertThat(ConnectionViewType.COMPACT.layout).isEqualTo(R.layout.access_point_view_compact)
        assertThat(ConnectionViewType.HIDE.layout).isEqualTo(R.layout.access_point_view_hide)
    }

    @Test
    fun isHide() {
        assertThat(ConnectionViewType.COMPLETE.hide).isFalse()
        assertThat(ConnectionViewType.COMPACT.hide).isFalse()
        assertThat(ConnectionViewType.HIDE.hide).isTrue()
    }

}