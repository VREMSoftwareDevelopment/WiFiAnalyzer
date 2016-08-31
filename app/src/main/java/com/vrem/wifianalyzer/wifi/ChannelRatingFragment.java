/*
 * Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vrem.wifianalyzer.wifi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.scanner.Broadcast;
import com.vrem.wifianalyzer.wifi.scanner.Receiver;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

public class ChannelRatingFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private Broadcast broadcast;
    private Receiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        View view = inflater.inflate(R.layout.channel_rating_content, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.channelRatingRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        TextView bestChannels = (TextView) view.findViewById(R.id.channelRatingBestChannels);
        ListView listView = (ListView) view.findViewById(R.id.channelRatingView);

        ChannelRatingAdapter channelRatingAdapter = new ChannelRatingAdapter(activity, bestChannels);
        listView.setAdapter(channelRatingAdapter);

        receiver = new Receiver(channelRatingAdapter);
        broadcast = new Broadcast();
        broadcast.register(getActivity(), receiver);
        
        return view;
    }

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        Scanner scanner = MainContext.INSTANCE.getScanner();
        scanner.update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcast.register(getActivity(), receiver);
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcast.unregister(getActivity(), receiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcast.unregister(getActivity(), receiver);
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
