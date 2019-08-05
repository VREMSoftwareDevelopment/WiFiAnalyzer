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

package com.vrem.wifianalyzer.wifi.filter.adapter;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.settings.Settings;

import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class FilterAdapter {
    private final Settings settings;
    private SSIDAdapter ssidAdapter;
    private WiFiBandAdapter wiFiBandAdapter;
    private StrengthAdapter strengthAdapter;
    private SecurityAdapter securityAdapter;

    public FilterAdapter(@NonNull Settings settings) {
        this.settings = settings;
        reload();
    }

    public boolean isActive() {
        return IterableUtils.find(getFilterAdapters(isAccessPoints()), new ActivePredicate()) != null;
    }

    public void reset() {
        IterableUtils.forEach(getFilterAdapters(isAccessPoints()), new ResetClosure());
    }

    public void save() {
        IterableUtils.forEach(getFilterAdapters(isAccessPoints()), new SaveClosure());
    }

    @NonNull
    List<? extends BasicFilterAdapter<? extends Serializable>> getFilterAdapters(boolean accessPoints) {
        if (accessPoints) {
            return Arrays.asList(ssidAdapter, strengthAdapter, securityAdapter, wiFiBandAdapter);
        }
        return Arrays.asList(ssidAdapter, strengthAdapter, securityAdapter);
    }

    @NonNull
    public SSIDAdapter getSSIDAdapter() {
        return ssidAdapter;
    }

    @NonNull
    public WiFiBandAdapter getWiFiBandAdapter() {
        return wiFiBandAdapter;
    }

    @NonNull
    public StrengthAdapter getStrengthAdapter() {
        return strengthAdapter;
    }

    @NonNull
    public SecurityAdapter getSecurityAdapter() {
        return securityAdapter;
    }

    public void reload() {
        this.ssidAdapter = new SSIDAdapter(settings.getSSIDs());
        this.wiFiBandAdapter = new WiFiBandAdapter(settings.getWiFiBands());
        this.strengthAdapter = new StrengthAdapter(settings.getStrengths());
        this.securityAdapter = new SecurityAdapter(settings.getSecurities());
    }

    private boolean isAccessPoints() {
        return NavigationMenu.ACCESS_POINTS.equals(MainContext.INSTANCE.getMainActivity().getCurrentNavigationMenu());
    }

    private class ActivePredicate implements Predicate<BasicFilterAdapter<? extends Serializable>> {
        @Override
        public boolean evaluate(BasicFilterAdapter<? extends Serializable> adapter) {
            return adapter.isActive();
        }
    }

    private class ResetClosure implements Closure<BasicFilterAdapter<? extends Serializable>> {
        @Override
        public void execute(BasicFilterAdapter<? extends Serializable> adapter) {
            adapter.reset();
            adapter.save(settings);
        }
    }

    private class SaveClosure implements Closure<BasicFilterAdapter<? extends Serializable>> {
        @Override
        public void execute(BasicFilterAdapter<? extends Serializable> adapter) {
            adapter.save(settings);
        }
    }

}
