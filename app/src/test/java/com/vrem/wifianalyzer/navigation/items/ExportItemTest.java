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

package com.vrem.wifianalyzer.navigation.items;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.view.MenuItem;
import android.view.View;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;
import com.vrem.wifianalyzer.wifi.scanner.ScannerService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExportItemTest {
    private static final String TITLE = "title";

    @Mock
    private Intent sendIntent;
    @Mock
    private Intent chooserIntent;
    @Mock
    private MainActivity mainActivity;
    @Mock
    private Resources resources;
    @Mock
    private MenuItem menuItem;
    @Mock
    private PackageManager packageManager;
    @Mock
    private ComponentName componentName;

    private ExportItem fixture;
    private ScannerService scanner;
    private WiFiDetail wiFiDetail;

    @Before
    public void setUp() {
        scanner = MainContextHelper.INSTANCE.getScannerService();

        wiFiDetail = new WiFiDetail("SSID", "BSSID", "capabilities",
            new WiFiSignal(2412, 2422, WiFiWidth.MHZ_40, -40, true));

        fixture = new ExportItem() {
            @Override
            Intent createSendIntent() {
                return sendIntent;
            }

            @Override
            Intent createChooserIntent(@NonNull Intent intent, @NonNull String title) {
                assertEquals(sendIntent, intent);
                assertEquals(getExpectedTitle(), title);
                return chooserIntent;
            }
        };
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }


    @Test
    public void testActivate() {
        // setup
        WiFiData wiFiData = withWiFiData();
        when(scanner.getWiFiData()).thenReturn(wiFiData);
        withResources();
        withResolveActivity();
        // execute
        fixture.activate(mainActivity, menuItem, NavigationMenu.EXPORT);
        // validate
        verify(scanner).getWiFiData();
        verifyResources();
        verifyResolveActivity();
        verifySendIntentInformation(fixture.getExportData());
        verify(mainActivity).startActivity(chooserIntent);
    }

    @Test
    public void testIsRegistered() {
        // execute & validate
        assertFalse(fixture.isRegistered());
    }

    @Test
    public void testGetVisibility() {
        // execute & validate
        assertEquals(View.GONE, fixture.getVisibility());
    }

    @NonNull
    private WiFiData withWiFiData() {
        return new WiFiData(Collections.singletonList(wiFiDetail), WiFiConnection.EMPTY);
    }

    private void verifySendIntentInformation(String sendData) {
        String expectedTitle = getExpectedTitle();
        verify(sendIntent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        verify(sendIntent).setType("text/plain");
        verify(sendIntent).putExtra(Intent.EXTRA_TITLE, expectedTitle);
        verify(sendIntent).putExtra(Intent.EXTRA_SUBJECT, expectedTitle);
        verify(sendIntent).putExtra(Intent.EXTRA_TEXT, sendData);
    }

    private void withResolveActivity() {
        when(mainActivity.getPackageManager()).thenReturn(packageManager);
        when(chooserIntent.resolveActivity(packageManager)).thenReturn(componentName);
    }

    private void verifyResolveActivity() {
        verify(mainActivity).getPackageManager();
        verify(chooserIntent).resolveActivity(packageManager);
    }

    private void withResources() {
        when(mainActivity.getResources()).thenReturn(resources);
        when(resources.getString(R.string.action_access_points)).thenReturn(TITLE);
    }

    private void verifyResources() {
        verify(mainActivity).getResources();
        verify(resources).getString(R.string.action_access_points);
    }

    private String getExpectedTitle() {
        return TITLE + "-" + fixture.getTimestamp();
    }
}