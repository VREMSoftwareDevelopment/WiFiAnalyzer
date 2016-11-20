/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi;

import android.net.wifi.WifiInfo;
import android.view.View;
import android.widget.TextView;

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
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConnectionViewTest {
    private static final String SSID = "SSID";
    private static final String BSSID = "BSSID";
    private static final String IP_ADDRESS = "IPADDRESS";
    private static final String GATEWAY = "GATEWAY";

    private MainActivity mainActivity;
    private ConnectionView fixture;

    private Configuration configuration;
    private Settings settings;

    private WiFiData wiFiData;
    private AccessPointsDetail accessPointsDetail;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();

        accessPointsDetail = mock(AccessPointsDetail.class);
        wiFiData = mock(WiFiData.class);

        configuration = MainContextHelper.INSTANCE.getConfiguration();
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ConnectionView(mainActivity);
        fixture.setAccessPointsDetail(accessPointsDetail);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
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
        verify(accessPointsDetail, never()).setView(mainActivity.getResources(), view, connection, false);
    }

    @Test
    public void testConnectionVisibleWithConnectionInformation() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
        WiFiDetail connection = withConnection(wiFiAdditional);
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        assertEquals(View.VISIBLE, view.getVisibility());
    }

    @Test
    public void testConnectionWithConnectionInformation() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        wiFiConnection.setGateway(GATEWAY);
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
        WiFiDetail connection = withConnection(wiFiAdditional);
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);

        TextView ipAddressView = (TextView) view.findViewById(R.id.ipAddress);
        assertEquals(IP_ADDRESS + "/" + GATEWAY, ipAddressView.getText().toString());

        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.VISIBLE, linkSpeedView.getVisibility());
        assertEquals(wiFiConnection.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.getText().toString());

        verify(wiFiData).getConnection();
        verify(accessPointsDetail).setView(mainActivity.getResources(), view, connection, false);
    }

    @Test
    public void testConnectionWithEmptyGateway() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
        WiFiDetail connection = withConnection(wiFiAdditional);
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);

        TextView ipAddressView = (TextView) view.findViewById(R.id.ipAddress);
        assertEquals(IP_ADDRESS, ipAddressView.getText().toString());

        verify(wiFiData).getConnection();
        verify(accessPointsDetail).setView(mainActivity.getResources(), view, connection, false);
    }

    @Test
    public void testConnectionWithInvalidLinkSpeed() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, WiFiConnection.LINK_SPEED_INVALID);
        WiFiAdditional wiFiAdditional = new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
        WiFiDetail connection = withConnection(wiFiAdditional);
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);

        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.GONE, linkSpeedView.getVisibility());

        verify(wiFiData).getConnection();
        verify(accessPointsDetail).setView(mainActivity.getResources(), view, connection, false);
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
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
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
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
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
        assertEquals(View.GONE, mainActivity.findViewById(R.id.nodatageourl).getVisibility());
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
        verify(wiFiData, never()).getWiFiDetails();
    }

    private WiFiDetail withConnection(WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, BSSID, StringUtils.EMPTY,
            new WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55), wiFiAdditional);
    }

}