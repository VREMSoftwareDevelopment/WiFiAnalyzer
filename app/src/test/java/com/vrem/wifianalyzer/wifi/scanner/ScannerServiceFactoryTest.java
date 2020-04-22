/*
 * WiFiAnalyzer
 * Copyright (C) 2020  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.scanner;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerServiceFactoryTest {

    @Mock
    private MainActivity mainActivity;
    @Mock
    private WifiManager wifiManager;
    @Mock
    private Handler handler;
    @Mock
    private Settings settings;
    @Mock
    private Context context;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(wifiManager);
        verifyNoMoreInteractions(handler);
        verifyNoMoreInteractions(settings);
    }

    @Test
    public void testMakeScannerService() {
        // setup
        when(mainActivity.getApplicationContext()).thenReturn(context);
        when(context.getSystemService(Context.WIFI_SERVICE)).thenReturn(wifiManager);
        // execute
        ScannerService actual = ScannerServiceFactory.makeScannerService(mainActivity, handler, settings);
        // validate
        assertTrue(actual instanceof Scanner);
        verify(mainActivity).getApplicationContext();
        verify(context).getSystemService(Context.WIFI_SERVICE);
        verify(handler).removeCallbacks(any(PeriodicScan.class));
        verify(handler).postDelayed(any(PeriodicScan.class), eq(PeriodicScan.DELAY_INITIAL));
    }
}