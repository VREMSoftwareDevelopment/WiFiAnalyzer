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

package com.vrem.wifianalyzer.wifi.timegraph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrem.util.BuildUtilsKt;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.databinding.GraphContentBinding;

import org.apache.commons.collections4.IterableUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TimeGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TimeGraphAdapter timeGraphAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GraphContentBinding binding = GraphContentBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.graphRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        if (BuildUtilsKt.buildVersionP()) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }

        timeGraphAdapter = new TimeGraphAdapter();
        addGraphViews(binding, timeGraphAdapter);

        MainContext.INSTANCE.scannerService.register(timeGraphAdapter);

        return binding.getRoot();
    }

    private void addGraphViews(GraphContentBinding binding, TimeGraphAdapter timeGraphAdapter) {
        IterableUtils.forEach(timeGraphAdapter.graphViews(), graphView -> binding.graphFlipper.addView(graphView));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        MainContext.INSTANCE.scannerService.update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onDestroy() {
        MainContext.INSTANCE.scannerService.unregister(timeGraphAdapter);
        super.onDestroy();
    }

    TimeGraphAdapter getTimeGraphAdapter() {
        return timeGraphAdapter;
    }

}
