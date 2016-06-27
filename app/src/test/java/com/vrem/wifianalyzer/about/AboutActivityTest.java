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

package com.vrem.wifianalyzer.about;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.RobolectricUtil;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class AboutActivityTest {

    private AboutActivity fixture;
    private boolean isDevelopmentMode;

    @Before
    public void setUp() throws Exception {
        RobolectricUtil.INSTANCE.getMainActivity();

        fixture = Robolectric.setupActivity(AboutActivity.class);

        isDevelopmentMode = MainContext.INSTANCE.getConfiguration().isDevelopmentMode();
    }

    @Test
    public void testTitle() throws Exception {
        // setup
        String expected = fixture.getResources().getString(R.string.action_about);
        // execute
        ActionBar actual = fixture.getSupportActionBar();
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getTitle());
        assertNull(fixture.getActionBar());
    }

    @Test
    public void testOnOptionsItemSelectedWithHome() throws Exception {
        // setup
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.getItemId()).thenReturn(android.R.id.home);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertTrue(actual);
        verify(menuItem).getItemId();
    }

    @Test
    public void testOnOptionsItemSelected() throws Exception {
        // setup
        MenuItem menuItem = mock(MenuItem.class);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertFalse(actual);
    }

    @Test
    public void testVersionNumber() throws Exception {
        // setup
        String packageName = fixture.getPackageName();
        PackageInfo packageInfo = fixture.getPackageManager().getPackageInfo(packageName, 0);
        String expected = packageInfo.versionName;
        if (isDevelopmentMode) {
            expected += " - " + packageInfo.versionCode +  " SDK:" + Build.VERSION.SDK_INT;
        }
        // execute
        TextView actual = (TextView) fixture.findViewById(R.id.about_version_info);
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getText());
    }

    @Test
    public void testPackageName() throws Exception {
        // setup
        String expectedName = StringUtils.EMPTY;
        int expectedVisibility = View.GONE;
        if (isDevelopmentMode) {
            expectedName = fixture.getPackageName();
            expectedVisibility = View.VISIBLE;
        }
        // execute
        TextView actual = (TextView) fixture.findViewById(R.id.about_package_name);
        // validate
        assertNotNull(actual);
        assertEquals(expectedVisibility, actual.getVisibility());
        assertEquals(expectedName, actual.getText());
    }

    @Test
    public void testApplicationName() throws Exception {
        // setup
        String expectedName = fixture.getString(R.string.app_name);
        if (isDevelopmentMode) {
            expectedName += " " + MainActivity.WI_FI_ANALYZER_BETA;
        }
        // execute
        TextView actual = (TextView) fixture.findViewById(R.id.about_app_name);
        // validate
        assertNotNull(actual);
        assertEquals(View.VISIBLE, actual.getVisibility());
        assertEquals(expectedName, actual.getText());
    }
}