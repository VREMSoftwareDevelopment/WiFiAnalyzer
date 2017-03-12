/*
 * WiFiAnalyzer
 * Copyright (C) 2017  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.band.WiFiWidth;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.WiFiAdditional;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;
import com.vrem.wifianalyzer.wifi.model.WiFiSignal;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.AllPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccessPointsPredicateTest {

    private static final String SSID = "SSID";
    private static final String WPA2 = "WPA2";

    @Mock
    private Settings settings;

    private AccessPointsPredicate fixture;

    @Test
    public void testEvaluateToTrue() throws Exception {
        // setup
        withSettings();
        fixture = new AccessPointsPredicate(settings);
        WiFiDetail wiFiDetail = makeWiFiDetail(SSID, WPA2);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertTrue(actual);
        verifySettings();
    }

    @Test
    public void testEvaluateWithSecurityToFalse() throws Exception {
        // setup
        withSettings();
        fixture = new AccessPointsPredicate(settings);
        WiFiDetail wiFiDetail = makeWiFiDetail(SSID, "WPA");
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertFalse(actual);
        verifySettings();
    }

    @Test
    public void testEvaluateWithSSIDToFalse() throws Exception {
        // setup
        withSettings();
        fixture = new AccessPointsPredicate(settings);
        WiFiDetail wiFiDetail = makeWiFiDetail("WIFI", WPA2);
        // execute
        boolean actual = fixture.evaluate(wiFiDetail);
        // validate
        assertFalse(actual);
        verifySettings();
    }

    @Test
    public void testGetPredicateWithSomeValuesIsAnyPredicate() throws Exception {
        // setup
        withSettings();
        fixture = new AccessPointsPredicate(settings);
        // execute
        Predicate<WiFiDetail> actual = fixture.getPredicate();
        // validate
        assertTrue(actual instanceof AllPredicate);
        verifySettings();
    }

    @Test
    public void testGetPredicateWithAllValuesIsTruePredicate() throws Exception {
        // setup
        when(settings.getSSIDFilter()).thenReturn(StringUtils.EMPTY);
        when(settings.getWiFiBandFilter()).thenReturn(EnumUtils.values(WiFiBand.class));
        when(settings.getStrengthFilter()).thenReturn(EnumUtils.values(Strength.class));
        when(settings.getSecurityFilter()).thenReturn(EnumUtils.values(Security.class));
        fixture = new AccessPointsPredicate(settings);
        // execute
        Predicate<WiFiDetail> actual = fixture.getPredicate();
        // validate
        assertTrue(actual instanceof TruePredicate);
        verifySettings();
    }

    private void withSettings() {
        when(settings.getSSIDFilter()).thenReturn(SSID);
        when(settings.getWiFiBandFilter()).thenReturn(Collections.singleton(WiFiBand.GHZ2));
        when(settings.getStrengthFilter()).thenReturn(new HashSet<>(Arrays.asList(Strength.TWO, Strength.FOUR)));
        when(settings.getSecurityFilter()).thenReturn(new HashSet<>(Arrays.asList(Security.WEP, Security.WPA2)));
    }

    private void verifySettings() {
        verify(settings).getSSIDFilter();
        verify(settings).getWiFiBandFilter();
        verify(settings).getStrengthFilter();
        verify(settings).getSecurityFilter();
    }

    private WiFiDetail makeWiFiDetail(String ssid, String security) {
        WiFiSignal wiFiSignal = new WiFiSignal(2445, 2445, WiFiWidth.MHZ_20, -40);
        return new WiFiDetail(ssid, "BSSID", security, wiFiSignal, WiFiAdditional.EMPTY);
    }

}
