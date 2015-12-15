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
package com.vrem.wifianalyzer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;

import com.vrem.wifianalyzer.wifi.ScannerData;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListViewAdapter<T,U> extends BaseExpandableListAdapter implements ScannerData<T> {

    private final AppCompatActivity activity;
    private List<T> details = new ArrayList<>();

    public BaseListViewAdapter(@NonNull AppCompatActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void addAll(@NonNull List<T> details) {
        this.details.clear();
        this.details = details;
    }

    protected List<T> getDetails() {
        return details;
    }

    @Override
    public int getGroupCount() {
        return this.details.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.details.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.details.get(groupPosition).toString();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    protected View getView(View view, int resource) {
        if (view != null) {
            return view;
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        return inflater.inflate(resource, null);
    }

    protected AppCompatActivity getActivity() {
        return activity;
    }

    @SuppressWarnings("unchecked")
    public T getGroupData(int groupPosition) {
        return (T) getGroup(groupPosition);
    }
    @SuppressWarnings("unchecked")
    public U getChildData(int groupPosition, int childPosition) {
        return (U) getChild(groupPosition, childPosition);
    }

}
