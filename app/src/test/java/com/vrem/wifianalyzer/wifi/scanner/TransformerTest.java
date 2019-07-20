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

package com.vrem.wifianalyzer.wifi.scanner;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.WiFiConnection;
import com.vrem.wifianalyzer.wifi.model.WiFiData;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransformerTest {
    private static final String SSID_1 = "SSID_1-123";
    private static final String BSSID_1 = "BSSID_1-123";
    private static final String SSID_2 = "SSID_2-123";
    private static final String BSSID_2 = "BSSID_2-123";
    private static final String SSID_3 = "SSID_3-123";
    private static final String BSSID_3 = "BSSID_3-123";
    private static final String WPA = "WPA";
    private static final int FREQUENCY = 2435;
    private static final int LEVEL = -40;
    private static final int IP_ADDRESS_VALUE = 123456789;
    private static final String IP_ADDRESS = "21.205.91.7";
    private static final int LINK_SPEED = 21;

    @Mock
    private WifiInfo wifiInfo;
    @Mock
    private ScanResult scanResult1;
    @Mock
    private ScanResult scanResult2;
    @Mock
    private ScanResult scanResult3;

    private List<CacheResult> cacheResults;
    private Transformer fixture;

    @Before
    public void setUp() {
        fixture = new Transformer();
    }

    @Test
    public void testTransformWithNulls() {
        assertTrue(fixture.transformCacheResults(null).isEmpty());
        assertEquals(WiFiConnection.EMPTY, fixture.transformWifiInfo(null));
    }

    @Test
    public void testTransformWifiInfoNotConnected() {
        when(wifiInfo.getNetworkId()).thenReturn(-1);
        assertEquals(WiFiConnection.EMPTY, fixture.transformWifiInfo(wifiInfo));
        verify(wifiInfo).getNetworkId();
    }

    @Test
    public void testTransformScanResults() {
        // setup
        withCacheResults();
        // execute
        List<WiFiDetail> actual = fixture.transformCacheResults(cacheResults);
        // validate
        assertEquals(cacheResults.size(), actual.size());
        validateWiFiDetail(SSID_1, BSSID_1, actual.get(0));
        validateWiFiDetail(SSID_2, BSSID_2, actual.get(1));
        validateWiFiDetail(SSID_3, BSSID_3, actual.get(2));
    }

    private void validateWiFiDetail(String SSID, String BSSID, WiFiDetail wiFiDetail) {
        assertEquals(SSID, wiFiDetail.getSSID());
        assertEquals(BSSID, wiFiDetail.getBSSID());
        assertEquals(WPA, wiFiDetail.getCapabilities());
        WiFiSignal wiFiSignal = wiFiDetail.getWiFiSignal();
        assertEquals(FREQUENCY, wiFiSignal.getPrimaryFrequency());
        assertEquals(FREQUENCY, wiFiSignal.getCenterFrequency());
        assertEquals(LEVEL, wiFiSignal.getLevel());
    }

    @Test
    public void testWiFiData() {
        // setup
        WiFiConnection expectedWiFiConnection = new WiFiConnection(SSID_1, BSSID_1, IP_ADDRESS, LINK_SPEED);
        withCacheResults();
        withWiFiInfo();
        // execute
        WiFiData actual = fixture.transformToWiFiData(cacheResults, wifiInfo);
        // validate
        assertEquals(expectedWiFiConnection, actual.getWiFiConnection());
        assertEquals(cacheResults.size(), actual.getWiFiDetails().size());
    }

    @Test
    public void testGetWiFiWidth() {
        // execute
        WiFiWidth actual = fixture.getWiFiWidth(scanResult1);
        // validate
        assertEquals(WiFiWidth.MHZ_20, actual);
    }

    @Test
    public void testGetWiFiWidthWithWiFiWidth() {
        // setup
        fixture = new Transformer() {
            @Override
            int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) {
                assertEquals(Fields.channelWidth, field);
                return WiFiWidth.MHZ_160.ordinal();
            }
        };
        // execute
        WiFiWidth actual = fixture.getWiFiWidth(scanResult1);
        // validate
        assertEquals(WiFiWidth.MHZ_160, actual);
    }

    @Test
    public void testGetCenterFrequency() {
        // setup
        scanResult1.frequency = FREQUENCY;
        // execute
        int actual = fixture.getCenterFrequency(scanResult1, WiFiWidth.MHZ_20);
        // validate
        assertEquals(FREQUENCY, actual);
    }

    @Test
    public void testGetCenterFrequencyWithFrequency() {
        // setup
        int expected = FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf();
        fixture = new Transformer() {
            @Override
            int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) {
                assertEquals(Fields.centerFreq0, field);
                return FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf();
            }
        };
        scanResult1.frequency = FREQUENCY;
        // execute
        int actual = fixture.getCenterFrequency(scanResult1, WiFiWidth.MHZ_20);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCenterFrequencyWithExtFrequencyAfter() {
        // setup
        int expected = FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf();
        fixture = new Transformer() {
            @Override
            int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) {
                assertEquals(Fields.centerFreq0, field);
                return FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf();
            }
        };
        scanResult1.frequency = FREQUENCY;
        // execute
        int actual = fixture.getCenterFrequency(scanResult1, WiFiWidth.MHZ_40);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetCenterFrequencyWithExtFrequencyBefore() {
        // setup
        int expected = FREQUENCY - WiFiWidth.MHZ_20.getFrequencyWidthHalf();
        fixture = new Transformer() {
            @Override
            int getFieldValue(@NonNull ScanResult scanResult, @NonNull Fields field) {
                assertEquals(Fields.centerFreq0, field);
                return FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf();
            }
        };
        scanResult1.frequency = FREQUENCY;
        // execute
        int actual = fixture.getCenterFrequency(scanResult1, WiFiWidth.MHZ_40);
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testIsExtensionFrequencyWith2GHz() {
        // setup
        scanResult1.frequency = FREQUENCY;
        // execute & validate
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf()));
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf()));

        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidth()));
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidth()));
    }

    @Test
    public void testIsExtensionFrequencyWith5GHz() {
        // setup
        scanResult1.frequency = 5100;
        // execute & validate
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf()));
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidthHalf()));

        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidth()));
        assertTrue(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_40, FREQUENCY - WiFiWidth.MHZ_40.getFrequencyWidth()));
    }

    @Test
    public void testIsNotExtensionFrequency() {
        // setup
        scanResult1.frequency = FREQUENCY;
        // execute & validate
        assertFalse(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_20, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf()));
        assertFalse(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_80, FREQUENCY + WiFiWidth.MHZ_40.getFrequencyWidthHalf()));
        assertFalse(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_20, FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf()));
        assertFalse(fixture.isExtensionFrequency(scanResult1, WiFiWidth.MHZ_80, FREQUENCY + WiFiWidth.MHZ_20.getFrequencyWidthHalf()));
    }

    private void withCacheResults() {
        scanResult1.SSID = SSID_1;
        scanResult1.BSSID = BSSID_1;
        scanResult1.capabilities = WPA;
        scanResult1.frequency = FREQUENCY;
        scanResult1.level = LEVEL;

        scanResult2.SSID = SSID_2;
        scanResult2.BSSID = BSSID_2;
        scanResult2.capabilities = WPA;
        scanResult2.frequency = FREQUENCY;
        scanResult2.level = LEVEL;

        scanResult3.SSID = SSID_3;
        scanResult3.BSSID = BSSID_3;
        scanResult3.capabilities = WPA;
        scanResult3.frequency = FREQUENCY;
        scanResult3.level = LEVEL;

        cacheResults = Arrays.asList(
            new CacheResult(scanResult1, scanResult1.level),
            new CacheResult(scanResult2, scanResult2.level),
            new CacheResult(scanResult3, scanResult2.level));
    }

    private void withWiFiInfo() {
        when(wifiInfo.getNetworkId()).thenReturn(0);
        when(wifiInfo.getSSID()).thenReturn(SSID_1);
        when(wifiInfo.getBSSID()).thenReturn(BSSID_1);
        when(wifiInfo.getIpAddress()).thenReturn(IP_ADDRESS_VALUE);
        when(wifiInfo.getLinkSpeed()).thenReturn(LINK_SPEED);
    }

}