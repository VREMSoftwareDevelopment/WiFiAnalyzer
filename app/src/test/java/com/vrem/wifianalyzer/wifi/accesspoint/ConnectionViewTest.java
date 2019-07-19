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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.net.wifi.WifiInfo;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.P)
@LooperMode(PAUSED)
public class ConnectionViewTest {
    private static final String SSID = "SSID";
    private static final String BSSID = "BSSID";
    private static final String IP_ADDRESS = "IPADDRESS";

    private MainActivity mainActivity;
    private ConnectionView fixture;

    private Settings settings;
    private WiFiData wiFiData;
    private AccessPointDetail accessPointDetail;
    private AccessPointPopup accessPointPopup;

    @Before
    public void setUp() {
        mainActivity = RobolectricUtil.INSTANCE.getActivity();

        accessPointDetail = mock(AccessPointDetail.class);
        accessPointPopup = mock(AccessPointPopup.class);

        wiFiData = mock(WiFiData.class);
        settings = MainContextHelper.INSTANCE.getSettings();

        fixture = new ConnectionView(mainActivity);
        fixture.setAccessPointDetail(accessPointDetail);
        fixture.setAccessPointPopup(accessPointPopup);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        mainActivity.setCurrentNavigationMenu(NavigationMenu.ACCESS_POINTS);
    }

    @Test
    public void testConnectionGoneWithNoConnectionInformation() {
        // setup
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        withConnectionInformation(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionGoneWithConnectionInformationAndHideType() {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.HIDE);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.getAccessPointViewType());
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionVisibleWithConnectionInformation() {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.getAccessPointViewType());
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.connection).getVisibility());
        verifyConnectionInformation();
    }

    @Test
    public void testConnectionWithConnectionInformation() {
        // setup
        WiFiAdditional wiFiAdditional = withWiFiAdditional();
        WiFiDetail connection = withConnection(wiFiAdditional);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.getAccessPointViewType());
        // execute
        fixture.update(wiFiData);
        // validate
        WiFiConnection wiFiConnection = wiFiAdditional.getWiFiConnection();
        View view = mainActivity.findViewById(R.id.connection);
        TextView ipAddressView = view.findViewById(R.id.ipAddress);
        assertEquals(wiFiConnection.getIpAddress(), ipAddressView.getText().toString());
        TextView linkSpeedView = view.findViewById(R.id.linkSpeed);
        assertEquals(View.VISIBLE, linkSpeedView.getVisibility());
        assertEquals(wiFiConnection.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS, linkSpeedView.getText().toString());
    }

    @Test
    public void testConnectionWithInvalidLinkSpeed() {
        // setup
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, WiFiConnection.LINK_SPEED_INVALID);
        WiFiDetail connection = withConnection(new WiFiAdditional(StringUtils.EMPTY, wiFiConnection));
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        withConnectionInformation(connection);
        withAccessPointDetailView(connection, ConnectionViewType.COMPLETE.getAccessPointViewType());
        // execute
        fixture.update(wiFiData);
        // validate
        View view = mainActivity.findViewById(R.id.connection);
        TextView linkSpeedView = view.findViewById(R.id.linkSpeed);
        assertEquals(View.GONE, linkSpeedView.getVisibility());
    }

    @Test
    public void testNoDataIsVisibleWithNoWiFiDetails() {
        // setup
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.no_data).getVisibility());
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.no_location).getVisibility());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithWiFiDetails() {
        // setup
        WiFiDetail wiFiDetail = withConnection(WiFiAdditional.EMPTY);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(wiFiDetail);
        when(wiFiData.getWiFiDetails()).thenReturn(Collections.singletonList(wiFiDetail));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.no_data).getVisibility());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testNoDataIsGoneWithNavigationMenuThatDoesNotHaveOptionMenu() {
        // setup
        mainActivity.setCurrentNavigationMenu(NavigationMenu.VENDORS);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.no_data).getVisibility());
        verify(wiFiData, never()).getWiFiDetails();
    }

    @Test
    public void testScanningIsVisibleWithNoWiFiDetails() {
        // setup
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.VISIBLE, mainActivity.findViewById(R.id.scanning).getVisibility());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testScanningIsGoneWithWiFiDetails() {
        // setup
        WiFiDetail wiFiDetail = withConnection(WiFiAdditional.EMPTY);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(wiFiDetail);
        when(wiFiData.getWiFiDetails()).thenReturn(Collections.singletonList(wiFiDetail));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.scanning).getVisibility());
        verify(wiFiData).getWiFiDetails();
    }

    @Test
    public void testScanningIsGoneWithNavigationMenuThatDoesNotHaveOptionMenu() {
        // setup
        mainActivity.setCurrentNavigationMenu(NavigationMenu.VENDORS);
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPLETE);
        when(wiFiData.getConnection()).thenReturn(withConnection(WiFiAdditional.EMPTY));
        // execute
        fixture.update(wiFiData);
        // validate
        assertEquals(View.GONE, mainActivity.findViewById(R.id.scanning).getVisibility());
        verify(wiFiData, never()).getWiFiDetails();
    }

    @Test
    public void testViewCompactAddsPopup() {
        // setup
        WiFiDetail connection = withConnection(withWiFiAdditional());
        when(settings.getConnectionViewType()).thenReturn(ConnectionViewType.COMPACT);
        withConnectionInformation(connection);
        View view = withAccessPointDetailView(connection, ConnectionViewType.COMPACT.getAccessPointViewType());
        // execute
        fixture.update(wiFiData);
        // validate
        verify(accessPointPopup).attach(view.findViewById(R.id.attachPopup), connection);
        verify(accessPointPopup).attach(view.findViewById(R.id.ssid), connection);
    }

    private WiFiDetail withConnection(@NonNull WiFiAdditional wiFiAdditional) {
        return new WiFiDetail(SSID, BSSID, StringUtils.EMPTY,
            new WiFiSignal(2435, 2435, WiFiWidth.MHZ_20, -55, true),
            wiFiAdditional);
    }

    private WiFiAdditional withWiFiAdditional() {
        WiFiConnection wiFiConnection = new WiFiConnection(SSID, BSSID, IP_ADDRESS, 11);
        return new WiFiAdditional(StringUtils.EMPTY, wiFiConnection);
    }

    private View withAccessPointDetailView(@NonNull WiFiDetail connection, @NonNull AccessPointViewType accessPointViewType) {
        ViewGroup parent = mainActivity.findViewById(R.id.connection).findViewById(R.id.connectionDetail);
        View view = mainActivity.getLayoutInflater().inflate(accessPointViewType.getLayout(), parent, false);
        when(accessPointDetail.makeView(null, parent, connection, false, accessPointViewType)).thenReturn(view);
        when(accessPointDetail.makeView(parent.getChildAt(0), parent, connection, false, accessPointViewType)).thenReturn(view);
        return view;
    }

    private void withConnectionInformation(@NonNull WiFiDetail connection) {
        when(wiFiData.getConnection()).thenReturn(connection);
    }

    private void verifyConnectionInformation() {
        verify(wiFiData).getConnection();
    }

}