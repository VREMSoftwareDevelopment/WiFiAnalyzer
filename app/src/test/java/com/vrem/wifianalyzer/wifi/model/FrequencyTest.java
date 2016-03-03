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

package com.vrem.wifianalyzer.wifi.model;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WifiManager.class)
public class FrequencyTest {
    @Mock
    private ScanResult scanResult;

    private Frequency fixture;

    @Before
    public void setUp() throws Exception {
        scanResult.frequency = 2435;
        fixture = new Frequency(scanResult);
    }

    @Test
    public void testGetFrequency() throws Exception {
        // execute
        int actual = fixture.getFrequency();
        // validate
        assertEquals(scanResult.frequency, actual);
    }

    @Test
    public void testGetFrequencyStart() throws Exception {
        // setup
        int expected = scanResult.frequency - WiFiWidth.MHZ_20.getFrequencyWidthHalf();
        // execute
        int actual = fixture.getFrequency();
        // validate
        assertEquals(scanResult.frequency, actual);
    }

    @Test
    public void testGetFrequencyEnd() throws Exception {
        // setup
        int expected = scanResult.frequency - WiFiWidth.MHZ_20.getFrequencyWidthHalf();
        // execute
        int actual = fixture.getFrequency();
        // validate
        assertEquals(scanResult.frequency, actual);
    }

    @Test
    public void testGetChannel() throws Exception {
        // setup
        int expected = 5;
        // execute
        int actual = fixture.getChannel();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetChannelStart() throws Exception {
        // setup
        int expected = 3;
        // execute
        int actual = fixture.getChannelStart();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetChannelEnd() throws Exception {
        // setup
        int expected = 7;
        // execute
        int actual = fixture.getChannelEnd();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWiFiBand() throws Exception {
        // setup
        WiFiBand expected = WiFiBand.GHZ_2;
        // execute
        WiFiBand actual = fixture.getWiFiBand();
        // validate
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWiFiWidth() throws Exception {
        // setup
        WiFiWidth expected = WiFiWidth.MHZ_20;
        // execute
        WiFiWidth actual = fixture.getWiFiWidth();
        // validate
        assertEquals(expected, actual);
    }

/*
    @Override
    public int getChannelStart() {
        return WiFiBand.findChannelByFrequency(getFrequencyStart());
    }

    @Override
    public int getChannelEnd() {
        return WiFiBand.findChannelByFrequency(getFrequencyEnd());
    }

    @Override
    public WiFiBand getWiFiBand() {
        return WiFiBand.findByFrequency(getWiFiFrequency());
    }

    @Override
    public WiFiWidth getWiFiWidth() {
        return WiFiWidth.MHZ_20;
    }
*/

}