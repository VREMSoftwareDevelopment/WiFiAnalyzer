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

import android.support.annotation.NonNull;

import com.vrem.util.EnumUtils;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.SSIDPredicate;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.SecurityPredicate;
import com.vrem.wifianalyzer.wifi.model.Strength;
import com.vrem.wifianalyzer.wifi.model.StrengthPredicate;
import com.vrem.wifianalyzer.wifi.model.WiFiBandPredicate;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

class AccessPointsPredicate implements Predicate<WiFiDetail> {

    private final Predicate<WiFiDetail> predicate;

    AccessPointsPredicate(@NonNull Settings settings) {
        predicate = buildPredicate(settings);
    }

    @Override
    public boolean evaluate(WiFiDetail object) {
        return predicate.evaluate(object);
    }

    Predicate<WiFiDetail> getPredicate() {
        return predicate;
    }

    private Predicate<WiFiDetail> buildPredicate(@NonNull Settings settings) {
        Predicate<WiFiDetail> ssidPredicate = makeSSIDPredicate(settings.getSSIDFilter());
        Predicate<WiFiDetail> wiFiBandPredicate = EnumUtils.predicate(settings.getWiFiBandFilter(), WiFiBand.values(), new WiFiBandTransformer());
        Predicate<WiFiDetail> strengthPredicate = EnumUtils.predicate(settings.getStrengthFilter(), Strength.values(), new StrengthTransformer());
        Predicate<WiFiDetail> securityPredicate = EnumUtils.predicate(settings.getSecurityFilter(), Security.values(), new SecurityTransformer());
        List<Predicate<WiFiDetail>> predicates = Arrays.asList(ssidPredicate, wiFiBandPredicate, strengthPredicate, securityPredicate);
        return PredicateUtils.allPredicate(CollectionUtils.select(predicates, new NoTruePredicate()));
    }

    private Predicate<WiFiDetail> makeSSIDPredicate(String ssid) {
        return StringUtils.isBlank(ssid) ? PredicateUtils.<WiFiDetail>truePredicate() : new SSIDPredicate(ssid);
    }

    private class WiFiBandTransformer implements Transformer<WiFiBand, Predicate<WiFiDetail>> {
        @Override
        public Predicate<WiFiDetail> transform(WiFiBand input) {
            return new WiFiBandPredicate(input);
        }
    }

    private class StrengthTransformer implements Transformer<Strength, Predicate<WiFiDetail>> {
        @Override
        public Predicate<WiFiDetail> transform(Strength input) {
            return new StrengthPredicate(input);
        }
    }

    private class SecurityTransformer implements Transformer<Security, Predicate<WiFiDetail>> {
        @Override
        public Predicate<WiFiDetail> transform(Security input) {
            return new SecurityPredicate(input);
        }
    }

    private class NoTruePredicate implements Predicate<Predicate<WiFiDetail>> {
        @Override
        public boolean evaluate(Predicate<WiFiDetail> object) {
            return !PredicateUtils.truePredicate().equals(object);
        }
    }
}
