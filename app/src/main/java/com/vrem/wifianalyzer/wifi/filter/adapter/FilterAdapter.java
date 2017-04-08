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

package com.vrem.wifianalyzer.wifi.filter.adapter;

import android.support.annotation.NonNull;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.settings.Settings;
import com.vrem.wifianalyzer.wifi.band.WiFiBand;
import com.vrem.wifianalyzer.wifi.model.Security;
import com.vrem.wifianalyzer.wifi.model.Strength;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

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
        for (BasicFilterAdapter<? extends Serializable> adapter : getFilterAdapters(isAccessPoints())) {
            if (adapter.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        for (BasicFilterAdapter<? extends Serializable> adapter : getFilterAdapters(isAccessPoints())) {
            adapter.reset();
            adapter.save(settings);
        }
    }

    public void save() {
        for (BasicFilterAdapter<? extends Serializable> adapter : getFilterAdapters(isAccessPoints())) {
            adapter.save(settings);
        }
    }

    List<? extends BasicFilterAdapter<? extends Serializable>> getFilterAdapters(boolean accessPoints) {
        if (accessPoints) {
            return Arrays.asList(ssidAdapter, strengthAdapter, securityAdapter, wiFiBandAdapter);
        }
        return Arrays.asList(ssidAdapter, strengthAdapter, securityAdapter);
    }

    public BasicFilterAdapter<String> getSSIDAdapter() {
        return ssidAdapter;
    }

    public EnumFilterAdapter<WiFiBand> getWiFiBandAdapter() {
        return wiFiBandAdapter;
    }

    public EnumFilterAdapter<Strength> getStrengthAdapter() {
        return strengthAdapter;
    }

    public EnumFilterAdapter<Security> getSecurityAdapter() {
        return securityAdapter;
    }

    public void reload() {
        this.ssidAdapter = new SSIDAdapter(settings.getSSIDs());
        this.wiFiBandAdapter = new WiFiBandAdapter(settings.getWiFiBands());
        this.strengthAdapter = new StrengthAdapter(settings.getStrengths());
        this.securityAdapter = new SecurityAdapter(settings.getSecurities());
    }

    private boolean isAccessPoints() {
        return NavigationMenu.ACCESS_POINTS.equals(MainContext.INSTANCE.getMainActivity().getNavigationMenuView().getCurrentNavigationMenu());
    }

}
