/*
 * WiFiAnalyzer
 * Copyright (C) 2018  VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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

package com.vrem.wifianalyzer.about;

import android.app.AlertDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vrem.util.FileUtils;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContextHelper;
import com.vrem.wifianalyzer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AboutActivityTest {

    private MenuItem menuItem;
    private Configuration configuration;
    private AboutActivity fixture;

    @Before
    public void setUp() {
        menuItem = mock(MenuItem.class);
        configuration = MainContextHelper.INSTANCE.getConfiguration();
        fixture = Robolectric.setupActivity(AboutActivity.class);
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
    }

    @Test
    public void testOnOptionsItemSelectedWithHome() throws Exception {
        // setup
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
        when(menuItem.getItemId()).thenReturn(-android.R.id.home);
        // execute
        boolean actual = fixture.onOptionsItemSelected(menuItem);
        // validate
        assertFalse(actual);
        verify(menuItem).getItemId();
    }

    @Test
    public void testVersionNumber() throws Exception {
        // setup
        String expected = BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE
            + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")";
        // execute
        TextView actual = fixture.findViewById(R.id.about_version_info);
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getText());
    }

    @Test
    public void testVersionNumberWithConfiguration() throws Exception {
        // setup
        String expected = BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE + "SL"
            + " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")";
        when(configuration.isSizeAvailable()).thenReturn(true);
        when(configuration.isLargeScreen()).thenReturn(true);
        fixture = Robolectric.setupActivity(AboutActivity.class);
        // execute
        TextView actual = fixture.findViewById(R.id.about_version_info);
        // validate
        assertNotNull(actual);
        assertEquals(expected, actual.getText());
    }

    @Test
    public void testPackageName() throws Exception {
        // execute
        TextView actual = fixture.findViewById(R.id.about_package_name);
        // validate
        assertNotNull(actual);
        assertEquals(BuildConfig.APPLICATION_ID, actual.getText());
    }

    @Test
    public void testApplicationName() throws Exception {
        // setup
        String expectedName = fixture.getString(R.string.about_application_name);
        // execute
        TextView actual = fixture.findViewById(R.id.about_application_name);
        // validate
        assertNotNull(actual);
        assertEquals(expectedName, actual.getText());
    }

    @Test
    public void testWriteReview() throws Exception {
        // setup
        View actual = fixture.findViewById(R.id.writeReview);
        // execute
        fixture.writeReview(actual);
        // validate
        assertNotNull(actual);
    }

    @Test
    public void testShowALv2() throws Exception {
        // setup
        View view = fixture.findViewById(R.id.license);
        // execute
        fixture.showALv2(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.al);
        validateTitle(fixture, alertDialog, R.string.al);
    }

    @Test
    public void testShowGPLv3() throws Exception {
        // setup
        View view = fixture.findViewById(R.id.license);
        // execute
        fixture.showGPLv3(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.gpl);
        validateTitle(fixture, alertDialog, R.string.gpl);
    }

    @Test
    public void testShowContributors() throws Exception {
        // setup
        View view = fixture.findViewById(R.id.contributors);
        // execute
        fixture.showContributors(view);
        // validate
        AlertDialog alertDialog = fixture.getAlertDialog();
        assertNotNull(alertDialog);
        validateMessage(fixture, alertDialog, R.raw.contributors);
        validateTitle(fixture, alertDialog, R.string.about_contributor_title);
    }

    private void validateMessage(@NonNull AboutActivity aboutActivity, @NonNull AlertDialog alertDialog, @RawRes int id) {
        String expected = FileUtils.readFile(aboutActivity.getResources(), id);
        TextView messageView = alertDialog.findViewById(android.R.id.message);
        assertEquals(expected, messageView.getText().toString());
    }

    private void validateTitle(@NonNull AboutActivity aboutActivity, @NonNull AlertDialog alertDialog, @RawRes int id) {
        String expected = aboutActivity.getResources().getString(id);
        int titleId = aboutActivity.getResources().getIdentifier("alertTitle", "id", "android");
        TextView titleView = alertDialog.findViewById(titleId);
        assertEquals(expected, titleView.getText().toString());
    }

}