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
import com.vrem.wifianalyzer.wifi.model.Details;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;
import com.vrem.wifianalyzer.wifi.model.WiFiFrequency;

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
public class AccessPointsDetailsTest {
    private MainActivity activity = RobolectricUtil.INSTANCE.getMainActivity();

    private View view;
    private AccessPointsDetails fixture;

    @Before
    public void setUp() throws Exception {
        view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.access_points_details, null);
        assertNotNull(view);
        fixture = new AccessPointsDetails();
    }

    @Test
    public void testSetViewWithWiFiDetailsAsConnection() throws Exception {
        // setup
        WiFiDetails wiFiDetails = Details.makeConnection(ShadowScanResult.newInstance("SSID", "BSSID", "capabilities", 1, 2), "VendorName", "IPAddress");
        // execute
        fixture.setView(activity.getResources(), view, wiFiDetails, false);
        // validate
        validateTextViewValues(wiFiDetails, "SSID");

        validateTextViewValue(wiFiDetails.getIPAddress(), R.id.ipAddress);
        assertEquals(View.VISIBLE, view.findViewById(R.id.ipAddress).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.configuredImage).getVisibility());

        validateTextViewValue(wiFiDetails.getVendorName(), R.id.vendor);
        assertEquals(View.VISIBLE, view.findViewById(R.id.vendor).getVisibility());

        assertEquals(View.GONE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupColumn).getVisibility());
    }

    @Test
    public void testSetViewWithWiFiDetailsAsScanResult() throws Exception {
        // setup
        WiFiDetails wiFiDetails = Details.makeScanResult(ShadowScanResult.newInstance("", "BSSID", "capabilities", 1, 2), "", false);
        // execute
        fixture.setView(activity.getResources(), view, wiFiDetails, true);
        // validate
        validateTextViewValues(wiFiDetails, "***");

        assertEquals(View.GONE, view.findViewById(R.id.ipAddress).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.configuredImage).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.vendor).getVisibility());
        assertEquals(View.VISIBLE, view.findViewById(R.id.tab).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.groupColumn).getVisibility());
    }

    private void validateTextViewValues(@NonNull WiFiDetails wiFiDetails, @NonNull String ssid) {
        WiFiFrequency wiFiFrequency = wiFiDetails.getWiFiFrequency();
        validateTextViewValue(ssid + " (" + wiFiDetails.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(String.format("%ddBm", wiFiDetails.getLevel()), R.id.level);
        validateTextViewValue(String.format("%d", wiFiFrequency.getChannel()), R.id.channel);
        validateTextViewValue(String.format("%dMHz", wiFiFrequency.getFrequency()), R.id.frequency);
        validateTextViewValue(String.format("%6.2fm", wiFiDetails.getDistance()), R.id.distance);
        validateTextViewValue(wiFiDetails.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}