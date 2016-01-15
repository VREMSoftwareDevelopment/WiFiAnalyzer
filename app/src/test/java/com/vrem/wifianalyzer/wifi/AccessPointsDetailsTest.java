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

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricBaseTest;
import com.vrem.wifianalyzer.wifi.model.Details;
import com.vrem.wifianalyzer.wifi.model.WiFiDetails;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.shadows.ShadowScanResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccessPointsDetailsTest extends RobolectricBaseTest {

    private View view;
    private AccessPointsDetails fixture;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        view = MainContext.INSTANCE.getLayoutInflater().inflate(R.layout.access_points_details, null);
        assertNotNull(view);
    }

    @Test
    public void testSetViewWithWiFiDetailsAsConnection() throws Exception {
        // setup
        ScanResult scanResult = ShadowScanResult.newInstance("SSID", "BSSID", "capabilities", 1, 2);
        WiFiDetails wifiDetails = Details.makeConnection(scanResult, "VendorName", "IPAddress");
        // execute
        AccessPointsDetails.setView(resources, view, wifiDetails);
        // validate
        validateTextViewValues(wifiDetails, "SSID");

        validateTextViewValue(wifiDetails.getIPAddress(), R.id.ipAddress);
        assertEquals(View.VISIBLE, view.findViewById(R.id.ipAddress).getVisibility());

        assertEquals(View.VISIBLE, view.findViewById(R.id.configuredImage).getVisibility());

        validateTextViewValue(wifiDetails.getVendorName(), R.id.vendor);
        assertEquals(View.VISIBLE, view.findViewById(R.id.vendor).getVisibility());
    }

    @Test
    public void testSetViewWithWiFiDetailsAsScanResult() throws Exception {
        // setup
        ScanResult scanResult = ShadowScanResult.newInstance("", "BSSID", "capabilities", 1, 2);
        WiFiDetails wifiDetails = Details.makeScanResult(scanResult, "", false);
        // execute
        AccessPointsDetails.setView(resources, view, wifiDetails);
        // validate
        validateTextViewValues(wifiDetails, "***");

        assertEquals(View.GONE, view.findViewById(R.id.ipAddress).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.configuredImage).getVisibility());
        assertEquals(View.GONE, view.findViewById(R.id.vendor).getVisibility());
    }

    private void validateTextViewValues(@NonNull WiFiDetails wifiDetails, @NonNull String ssid) {
        validateTextViewValue(ssid + " (" + wifiDetails.getBSSID() + ")", R.id.ssid);
        validateTextViewValue(String.format("%ddBm", wifiDetails.getLevel()), R.id.level);
        validateTextViewValue(String.format("CH %d", wifiDetails.getChannel()), R.id.channel);
        validateTextViewValue(String.format("%dMHz", wifiDetails.getFrequency()), R.id.frequency);
        validateTextViewValue(String.format("%6.2fm", wifiDetails.getDistance()), R.id.distance);
        validateTextViewValue(wifiDetails.getCapabilities(), R.id.capabilities);
    }

    private void validateTextViewValue(@NonNull String expected, int id) {
        assertEquals(expected, ((TextView) view.findViewById(id)).getText().toString());
    }
}