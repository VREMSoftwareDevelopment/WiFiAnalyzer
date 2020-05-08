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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView.OnQueryTextListener;

import com.vrem.util.TextUtilsKt;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.databinding.VendorContentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

public class VendorFragment extends ListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        VendorContentBinding binding = VendorContentBinding.inflate(inflater, container, false);
        VendorAdapter vendorAdapter = new VendorAdapter(getActivity(), MainContext.INSTANCE.getVendorService());
        setListAdapter(vendorAdapter);

        binding.vendorSearchText.setOnQueryTextListener(new Listener(vendorAdapter));

        return binding.getRoot();
    }

    static class Listener implements OnQueryTextListener {
        private final VendorAdapter vendorAdapter;

        Listener(@NonNull VendorAdapter vendorAdapter) {
            this.vendorAdapter = vendorAdapter;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            vendorAdapter.update(TextUtilsKt.trim(newText));
            return true;
        }
    }

}
