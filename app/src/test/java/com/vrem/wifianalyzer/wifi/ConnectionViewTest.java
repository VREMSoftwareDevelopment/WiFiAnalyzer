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

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;

import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.R;
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
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionViewTest {
    @Mock
    private Scanner scanner;
    @Mock
    private Activity activity;
    @Mock
    private Resources resources;
    @Mock
    private Configuration configuration;
    @Mock
    private View view;
    @Mock
    private AccessPointsDetail accessPointsDetail;
    @Mock
    private WiFiData wiFiData;

    private ConnectionView fixture;

    @Before
    public void setUp() throws Exception {
        when(activity.getResources()).thenReturn(resources);
        when(activity.findViewById(R.id.connection)).thenReturn(view);

        fixture = new ConnectionView(activity, scanner);
        fixture.setAccessPointsDetail(accessPointsDetail);
        fixture.setConfiguration(configuration);
    }

    @After
    public void tearDown() throws Exception {
        verify(scanner).addUpdateNotifier(fixture);
        verify(activity).findViewById(R.id.connection);
    }

    @Test
    public void testUpdateWithConnectionNotConnected() throws Exception {
        // setup
        WiFiDetail connection = withConnection(WiFiAdditional.EMPTY);
        when(wiFiData.getConnection()).thenReturn(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(view).setVisibility(View.GONE);
    }

    @Test
    public void testUpdateWithConnection() throws Exception {
        // setup
        WiFiDetail connection = withConnection(new WiFiAdditional(StringUtils.EMPTY, "IPADDRESS", 11));
        when(wiFiData.getConnection()).thenReturn(connection);
        // execute
        fixture.update(wiFiData);
        // validate
        verify(activity).getResources();
        verify(view).setVisibility(View.VISIBLE);
        verify(accessPointsDetail).setView(resources, view, connection, false, false);
    }

    private WiFiDetail withConnection(WiFiAdditional wiFiAdditional) {
        return new WiFiDetail("SSID", "BSSID", StringUtils.EMPTY,
                new WiFiSignal(2435, WiFiWidth.MHZ_20, -55), wiFiAdditional);
    }

}