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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.band.WiFiChannelCountry;

import java.util.ArrayList;
import java.util.List;

public class ChannelAvailableFragment extends ListFragment {
    private ChannelAvailableAdapter channelAvailableAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.channel_available_content, container, false);
        channelAvailableAdapter = new ChannelAvailableAdapter(getActivity(), getChannelAvailable());
        setListAdapter(channelAvailableAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        channelAvailableAdapter.clear();
        channelAvailableAdapter.addAll(getChannelAvailable());
    }

    private List<WiFiChannelCountry> getChannelAvailable() {
        List<WiFiChannelCountry> results = new ArrayList<>();
        results.add(WiFiChannelCountry.find(MainContext.INSTANCE.getSettings().getCountryCode()));
        if (MainContext.INSTANCE.getConfiguration().isDevelopmentMode()) {
            results.addAll(WiFiChannelCountry.getAll());
        }
        return results;
    }

}
