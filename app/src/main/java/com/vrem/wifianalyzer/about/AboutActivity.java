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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vrem.util.FileUtils;
import com.vrem.wifianalyzer.ActivityUtils;
import com.vrem.wifianalyzer.BuildConfig;
import com.vrem.wifianalyzer.Configuration;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.settings.ThemeStyle;

public class AboutActivity extends AppCompatActivity {
    private AlertDialog alertDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ActivityUtils.createContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeStyle.getDefaultTheme());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_content);
        setExtraInformation();
        ActivityUtils.setActionBarOptions(getSupportActionBar());
    }

    private void setExtraInformation() {
        String text = BuildConfig.VERSION_NAME + " - " + BuildConfig.VERSION_CODE;
        Configuration configuration = MainContext.INSTANCE.getConfiguration();
        if (configuration != null) {
            if (configuration.isSizeAvailable()) {
                text += "S";
            }
            if (configuration.isLargeScreen()) {
                text += "L";
            }
        }
        text += " (" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT + ")";
        setText(R.id.about_version_info, text);
        setText(R.id.about_package_name, BuildConfig.APPLICATION_ID);
    }

    private void setText(int id, String text) {
        TextView version = findViewById(id);
        if (version != null) {
            version.setText(text);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return false;
    }

    public void writeReview(@NonNull View view) {
        String url = "market://details?id=" + BuildConfig.APPLICATION_ID;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void showALv2(@NonNull View view) {
        alertDialog = show(view, R.string.al, R.raw.al);
        changeFont(alertDialog);
    }

    public void showGPLv3(@NonNull View view) {
        alertDialog = show(view, R.string.gpl, R.raw.gpl);
        changeFont(alertDialog);
    }

    public void showContributors(@NonNull View view) {
        alertDialog = show(view, R.string.about_contributor_title, R.raw.contributors);
    }

    private AlertDialog show(@NonNull View view, @StringRes int titleId, @RawRes int id) {
        String text = FileUtils.readFile(getResources(), id);
        AlertDialog dialog = new AlertDialog
            .Builder(view.getContext())
            .setTitle(titleId)
            .setMessage(text)
            .setNeutralButton(android.R.string.ok, new Close())
            .create();
        dialog.show();
        return dialog;
    }

    private void changeFont(AlertDialog alertDialog) {
        TextView textView = alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(8);
    }

    AlertDialog getAlertDialog() {
        return alertDialog;
    }

    private static class Close implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

}
