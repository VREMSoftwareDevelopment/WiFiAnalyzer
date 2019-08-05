/*
 * WiFiAnalyzer
 * Copyright (C) 2019  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.timegraph;

import android.os.Build;

import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class TimeGraphFragmentTest {

    private TimeGraphFragment fixture;
    private ScannerService scanner;

    @Before
    public void setUp() {
        RobolectricUtil.INSTANCE.getActivity();
        scanner = MainContextHelper.INSTANCE.getScannerService();
        fixture = new TimeGraphFragment();
        RobolectricUtil.INSTANCE.startFragment(fixture);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnCreateView() {
        // validate
        assertNotNull(fixture);
        verify(scanner).update();
        verify(scanner).register(fixture.getTimeGraphAdapter());
    }

    @Test
    public void testOnResume() {
        // execute
        fixture.onResume();
        // validate
        verify(scanner, times(2)).update();
    }

    @Test
    public void testOnDestroy() {
        // execute
        fixture.onDestroy();
        // validate
        verify(scanner).unregister(fixture.getTimeGraphAdapter());
    }

    @Test
    public void testRefreshDisabled() {
        // validate
        SwipeRefreshLayout swipeRefreshLayout = fixture.getView().findViewById(R.id.graphRefresh);
        assertFalse(swipeRefreshLayout.isRefreshing());
        assertFalse(swipeRefreshLayout.isEnabled());
    }

}