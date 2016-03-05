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

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowScanResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AccessPointsDetailTest {
    private MainActivity activity = RobolectricUtil.INSTANCE.getMainActivity();

    private View view;
    private AccessPointsDetail fixture;

    @Before
    public void setUp() throws Exception {
        view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.access_points_details, null);
        assertNotNull(view);
        fixture = new AccessPointsDetail();
    }

    @Test
    public void testSetViewWithWiFiDetailAsConnection() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail(ShadowScanResult.newInstance("SSID", "BSSID", "capabilities", 1, 2), new WiFiAdditional("VendorName", "IPAddress"));
        // execute
        fixture.setView(activity.getResources(), view, wiFiDetail, false);
        // validate
        validateTextViewValues(wiFiDetail, "SSID");

        validateTextViewValue(wiFiDetail.getWiFiAdditional().getIPAddress(), R.id.ipAddress);
        assertEquals(View.VISIBLE, view.findViewById(R.id.ipAddress).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.configuredImage).getVisibility());

        validateTextViewValue(wiFiDetail.getWiFiAdditional().getVendorName(), R.id.vendor);
        assertEquals(View.VISIBLE, view.findViewById(R.id.vendor).getVisibility());

        assertEquals(View.GONE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupColumn).getVisibility());
    }

    @Test
    public void testSetViewWithWiFiDetailAsScanResult() throws Exception {
        // setup
        WiFiDetail wiFiDetail = new WiFiDetail(ShadowScanResult.newInstance("", "BSSID", "capabilities", 1, 2), new WiFiAdditional("", false));
        // execute
        fixture.setView(activity.getResources(), view, wiFiDetail, true);
        // validate
        validateTextViewValues(wiFiDetail, "***");

        assertEquals(View.GONE, view.findViewById(R.id.ipAddress).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.configuredImage).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.vendor).getVisibility());
        assertEquals(View.VISIBLE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupColumn).getVisibility());
    }

    private void validateTextViewValues(@NonNull WiFiDetail wiFiDetail, @NonNull String ssid) {
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        validateTextViewValue(ssid + " (" + wiFiDetail.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(String.format("%ddBm", wiFiSignal.getLevel()), R.id.level);
        validateTextViewValue(String.format("%d", wiFiSignal.getChannel()), R.id.channel);
        validateTextViewValue(String.format("%dMHz", wiFiSignal.getFrequency()), R.id.frequency);
        validateTextViewValue(String.format("%6.2fm", wiFiSignal.getDistance()), R.id.distance);
        validateTextViewValue(wiFiDetail.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}