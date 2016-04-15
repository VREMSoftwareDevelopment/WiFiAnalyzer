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

package com.vrem.wifianalyzer.vendor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.vendor.model.VendorService;

public class VendorFragment extends ListFragment {
    private VendorAdapter vendorAdapter;
    private VendorService vendorService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendor_content, container, false);
        vendorAdapter = new VendorAdapter(getActivity(), getVendorService().findAll());
        setListAdapter(vendorAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        vendorAdapter.setVendors(getVendorService().findAll());
    }

    // injectors start
    private VendorService getVendorService() {
        if (vendorService == null) {
            vendorService = MainContext.INSTANCE.getVendorService();
        }
        return vendorService;
    }

    protected void setVendorService(@NonNull VendorService vendorService) {
        this.vendorService = vendorService;
    }
    // injectors end

}
