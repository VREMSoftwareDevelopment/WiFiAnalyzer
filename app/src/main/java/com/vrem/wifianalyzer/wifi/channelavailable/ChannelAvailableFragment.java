/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.wifi.channelavailable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.databinding.ChannelAvailableContentBinding;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

public class ChannelAvailableFragment extends ListFragment {
    private ChannelAvailableAdapter channelAvailableAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ChannelAvailableContentBinding binding = ChannelAvailableContentBinding.inflate(inflater, container, false);
        channelAvailableAdapter = new ChannelAvailableAdapter(getActivity(), getChannelAvailable());
        setListAdapter(channelAvailableAdapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        channelAvailableAdapter.clear();
        channelAvailableAdapter.addAll(getChannelAvailable());
    }

    @NonNull
    private List<WiFiChannelCountry> getChannelAvailable() {
        List<WiFiChannelCountry> results = new ArrayList<>();
        results.add(WiFiChannelCountry.find(MainContext.INSTANCE.getSettings().countryCode()));
        return results;
    }

}
