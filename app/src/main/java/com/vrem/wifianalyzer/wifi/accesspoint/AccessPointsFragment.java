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

package com.vrem.wifianalyzer.wifi.accesspoint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrem.util.BuildUtilsKt;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.databinding.AccessPointsContentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

public class AccessPointsFragment extends Fragment implements OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointsAdapter accessPointsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AccessPointsContentBinding binding = AccessPointsContentBinding.inflate(inflater, container, false);

        swipeRefreshLayout = binding.accessPointsRefresh;
        swipeRefreshLayout.setOnRefreshListener(this);
        if (BuildUtilsKt.buildVersionP()) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }

        accessPointsAdapter = new AccessPointsAdapter();
        binding.accessPointsView.setAdapter(accessPointsAdapter);
        accessPointsAdapter.setExpandableListView(binding.accessPointsView);

        MainContext.INSTANCE.getScannerService().register(accessPointsAdapter);

        return binding.getRoot();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        MainContext.INSTANCE.getScannerService().update();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onDestroy() {
        MainContext.INSTANCE.getScannerService().unregister(accessPointsAdapter);
        super.onDestroy();
    }

    AccessPointsAdapter getAccessPointsAdapter() {
        return accessPointsAdapter;
    }

}
