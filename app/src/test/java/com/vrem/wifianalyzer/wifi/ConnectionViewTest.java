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

package com.vrem.wifianalyzer.wifi;

import android.view.View;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConnectionViewTest {
    private MainActivity mainActivity;
    private ConnectionView fixture;

    private Scanner scanner;
    private Configuration configuration;
    private Settings settings;

    private WiFiData wiFiData;
    private AccessPointsDetail accessPointsDetail;

    @Before
    public void setUp() throws Exception {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        accessPointsDetail = mock(AccessPointsDetail.class);
        wiFiData = mock(WiFiData.class);

        configuration = MainContextHelper.INSTANCE.getConfiguration();
        scanner = MainContextHelper.INSTANCE.getScanner();
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ConnectionView(mainActivity);
        fixture.setAccessPointsDetail(accessPointsDetail);
    }

    @After
    public void tearDown() throws Exception {
        MainContextHelper.INSTANCE.restore();
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testConnectionView() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
    }

    @Test
    public void testConnectionGoneWithNoConnectionInformation() throws Exception {
        // setup
        WiFiDetail connection = withConnection(WiFiAdditional.EMPTY);
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        assertEquals(View.GONE, view.getVisibility());

        verify(wiFiData).getConnection();
        verify(configuration, never()).isLargeScreenLayout();
        verify(accessPointsDetail, never()).setView(mainActivity.getResources(), view, connection, false, false);
    }

    @Test
    public void testConnectionVisibleWithConnectionInformation() throws Exception {
        // setup
        WiFiDetail connection = withConnection(new WiFiAdditional(StringUtils.EMPTY, "IPADDRESS", 11));
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        assertEquals(View.VISIBLE, view.getVisibility());

        verify(wiFiData).getConnection();
        verify(configuration).isLargeScreenLayout();
        verify(accessPointsDetail).setView(mainActivity.getResources(), view, connection, false, false);
    }

    @Test
    public void testNoDataIsVisibleWithNoWiFiDetails() throws Exception {
        // setup
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithNonWiFiBandSwitchableNavigationMenu() throws Exception {
        // setup
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.VENDOR_LIST);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        verify(wiFiData, never()).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData, never()).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithWiFiDetails() throws Exception {
        // setup
        WiFiDetail wiFiDetail = withConnection(WiFiAdditional.EMPTY);
        when(wiFiData.getConnection()).thenReturn(wiFiDetail);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(Arrays.asList(wiFiDetail));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodata).getVisibility());
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageo).getVisibility());
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData, never()).getWiFiDetails();
    }

    private WiFiDetail withConnection(WiFiAdditional wiFiAdditional) {
        return new WiFiDetail("SSID", "BSSID", StringUtils.EMPTY,
                new WiFiSignal(2435, WiFiWidth.MHZ_20, -55), wiFiAdditional);
    }

}