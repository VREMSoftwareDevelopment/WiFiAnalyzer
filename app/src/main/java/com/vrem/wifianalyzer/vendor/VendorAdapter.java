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

package com.vrem.wifianalyzer.vendor;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.databinding.VendorDetailsBinding;
import com.vrem.wifianalyzer.vendor.model.VendorService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class VendorAdapter extends ArrayAdapter<String> {
    private final VendorService vendorService;

    VendorAdapter(@NonNull Context context, @NonNull VendorService vendorService) {
        super(context, R.layout.vendor_details, vendorService.findVendors());
        this.vendorService = vendorService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Binding binding = view == null ? new Binding(create(parent)) : new Binding(view);
        String vendorName = getItem(position);
        binding.getVendorName().setText(vendorName);
        binding.getVendorMacs().setText(getMacs(vendorName));
        return binding.root;
    }

    void update(@NonNull String filter) {
        clear();
        addAll(vendorService.findVendors(filter));
    }

    private String getMacs(String vendorName) {
        return TextUtils.join(", ", vendorService.findMacAddresses(vendorName));
    }

    private VendorDetailsBinding create(@NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = MainContext.INSTANCE.getLayoutInflater();
        return VendorDetailsBinding.inflate(layoutInflater, parent, false);
    }

    private class Binding {
        private final View root;
        private final TextView vendorName;
        private final TextView vendorMacs;

        Binding(@NonNull VendorDetailsBinding binding) {
            root = binding.getRoot();
            vendorName = binding.vendorName;
            vendorMacs = binding.vendorMacs;
        }

        Binding(@NonNull View view) {
            root = view;
            vendorName = view.findViewById(R.id.vendor_name);
            vendorMacs = view.findViewById(R.id.vendor_macs);
        }

        @NonNull
        View getRoot() {
            return root;
        }

        @NonNull
        TextView getVendorName() {
            return vendorName;
        }

        @NonNull
        TextView getVendorMacs() {
            return vendorMacs;
        }
    }

}
