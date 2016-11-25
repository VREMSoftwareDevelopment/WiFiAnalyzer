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

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;

import com.vrem.wifianalyzer.MainActivity;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;
import com.vrem.wifianalyzer.wifi.model.WiFiDetail;

public class AccessPointPopup {
    private final AccessPointDetail accessPointDetail;
    private MainActivity mainActivity;
    private View popupView;

    public AccessPointPopup(@NonNull AccessPointDetail accessPointDetail) {
        this.accessPointDetail = accessPointDetail;
        MainContext mainContext = MainContext.INSTANCE;
        mainActivity = mainContext.getMainActivity();
        popupView = mainContext.getLayoutInflater().inflate(R.layout.access_point_view_popup, null);
    }

    public Dialog show(WiFiDetail wiFiDetail) {
        Dialog dialog = new Dialog(mainActivity);
        dialog.setContentView(popupView);
        accessPointDetail.setViewPopup(mainActivity.getResources(), popupView, wiFiDetail);
        dialog.findViewById(R.id.popupButton).setOnClickListener(new PopupDialogCloseListener(dialog));
        dialog.show();
        return dialog;
    }

    View getPopupView() {
        return popupView;
    }

    private class PopupDialogCloseListener implements OnClickListener {
        private final Dialog dialog;

        PopupDialogCloseListener(@NonNull Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }
}
