/*
 * WiFi Analyzer
 * Copyright (C) 2016  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.scanner.Scanner;

public class AccessPointsFragment extends Fragment {
    private static final int ACCESS_COURSE_LOCATION = 1005;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AccessPointsAdapter accessPointsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = getActivity();

        View view = inflater.inflate(R.layout.access_points_content, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.accessPointsRefresh);
        swipeRefreshLayout.setOnRefreshListener(new ListViewOnRefreshListener());

        accessPointsAdapter = new AccessPointsAdapter(activity);
        ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.accessPointsView);
        expandableListView.setAdapter(accessPointsAdapter);

        Scanner scanner = MainContext.INSTANCE.getScanner();
        scanner.register(accessPointsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissions();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == ACCESS_COURSE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                refresh();
                // permission was granted
            }
        }


    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity())
                        .setMessage("Required for Discovering Bluetooth devices in Android OS Marshmallow and above.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        ACCESS_COURSE_LOCATION);
                            }
                        });
                alertDialog.show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_COURSE_LOCATION);
            }
        }
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
        refresh();
    }

    @Override
    public void onDestroy() {
        Scanner scanner = MainContext.INSTANCE.getScanner();
        scanner.unregister(accessPointsAdapter);
        super.onDestroy();
    }

    AccessPointsAdapter getAccessPointsAdapter() {
        return accessPointsAdapter;
    }

    private class ListViewOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            refresh();
        }
    }
}
