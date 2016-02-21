/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScannerTest {
    @Mock private Handler handler;
    @Mock private Settings settings;
    @Mock private WifiManager wifiManager;
    @Mock private UpdateNotifier updateNotifier;
    @Mock private WifiInfo wifiInfo;

    private Scanner fixture;

    @Before
    public void setUp() throws Exception {
        MainContext mainContext = MainContext.INSTANCE;
        mainContext.setSettings(settings);
        mainContext.setHandler(handler);
        mainContext.setWifiManager(wifiManager);

        fixture = new Scanner();
    }

    @Test
    public void testPeriodicScanIsSet() throws Exception {
        assertNotNull(fixture.getPeriodicScan());
    }

    @Test
    public void testUpdate() throws Exception {
        // setup
        fixture.addUpdateNotifier(updateNotifier);
        withWiFiManager();
        // execute
        fixture.update();
        // validate
        verify(updateNotifier).update(fixture.getWifiData());

        verifyWiFiManager();
    }

    private void verifyWiFiManager() {
        verify(wifiManager).isWifiEnabled();
        verify(wifiManager).setWifiEnabled(true);
        verify(wifiManager).startScan();
        verify(wifiManager).getScanResults();
        verify(wifiManager).getConnectionInfo();
    }

    private void withWiFiManager() {
        when(wifiManager.isWifiEnabled()).thenReturn(false);
        when(wifiManager.startScan()).thenReturn(true);
        when(wifiManager.getScanResults()).thenReturn(new ArrayList<ScanResult>());
        when(wifiManager.getConnectionInfo()).thenReturn(wifiInfo);
    }

}