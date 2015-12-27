/*
 *    Copyright (C) 2010 - 2015 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

import android.app.ListActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.Utils;
import com.vrem.wifianalyzer.settings.Settings;

public class VendorActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(new Settings(this).themeStyle().themeDeviceDefaultStyle());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_content);

        Utils.enableHomeButton(getActionBar());

        setListAdapter(new VendorListViewAdapter(this, new Database(this).findAll()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
