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
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
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

    private Settings settings;
    private View childView;
    private WiFiData wiFiData;
    private AccessPointDetail accessPointDetail;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getMainActivity();
        View view = mainActivity.findViewById(R.id.connection);
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.connectionDetail);
        childView = mainActivity.getLayoutInflater().inflate(AccessPointView.FULL.getLayout(), parent, false);

        accessPointDetail = mock(AccessPointDetail.class);
        wiFiData = mock(WiFiData.class);
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ConnectionView(mainActivity);
        fixture.setAccessPointDetail(accessPointDetail);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        mainActivity.getNavigationMenuView().setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testConnectionGoneWithNoConnectionInformation() throws Exception {
        // setup
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionVisibleWithConnectionInformation() throws Exception {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionWithAccessPointDetailView() throws Exception {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        verifyAccessPointDetailView(connection);
    }

    @Test
    public void testConnectionWithConnectionInformation() throws Exception {
        // setup
        WiFiAdditional wiFiAdditional = withWiFiAdditional();
        WiFiDetail connection = withConnection(wiFiAdditional);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        WiFiConnection wiFiConnection = wiFiAdditional.getWiFiConnection();
        View view = mainActivity.findViewById(R.id.connection);
        assertEquals(wiFiConnection.getIpAddress(), ((TextView) view.findViewById(R.id.ipAddress)).getText().toString());
        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.VISIBLE, linkSpeedView.getVisibility());
        assertEquals(wiFiConnection.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.getText().toString());
    }

    @Test
    public void testConnectionWithInvalidLinkSpeed() throws Exception {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, WiFiConnection.LINK_SPEED_INVALID);
        WiFiDetail connection = withConnection(new WiFiAdditional(StringUtils.EMPTY, wiFiConnection));
        withConnectionInformation(connection);
        withAccessPointDetailView(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        TextView linkSpeedView = (TextView) view.findViewById(R.id.linkSpeed);
        assertEquals(View.GONE, linkSpeedView.getVisibility());
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

    @NonNull
    private WiFiDetail withConnection(@NonNull WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, BSSID, StringUtils.EMPTY,
            new WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55), wiFiAdditional);
    }

    @NonNull
    private WiFiAdditional withWiFiAdditional() {
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        return new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
    }

    private void withAccessPointDetailView(@NonNull WiFiDetail connection) {
        View view = mainActivity.findViewById(R.id.connection);
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.connectionDetail);
        when(accessPointDetail.makeView(parent.getChildAt(0), parent, connection, false)).thenReturn(childView);
    }

    private void verifyAccessPointDetailView(@NonNull WiFiDetail connection) {
        View view = mainActivity.findViewById(R.id.connection);
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.connectionDetail);
        verify(accessPointDetail).makeView(parent.getChildAt(0), parent, connection, false);
    }

    private void withConnectionInformation(@NonNull WiFiDetail connection) {
        when(wiFiData.getConnection()).thenReturn(connection);
        when(wiFiData.getWiFiDetails(settings.getWiFiBand(), settings.getSortBy())).thenReturn(new ArrayList<WiFiDetail>());
    }

    private void verifyConnectionInformation() {
        verify(wiFiData).getConnection();
        verify(wiFiData).getWiFiDetails(settings.getWiFiBand(), settings.getSortBy());
    }

}