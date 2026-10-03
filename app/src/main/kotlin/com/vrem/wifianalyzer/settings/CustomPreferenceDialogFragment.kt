/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2026 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
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
package com.vrem.wifianalyzer.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updateLayoutParams
import androidx.preference.PreferenceDialogFragmentCompat
import com.vrem.wifianalyzer.databinding.PreferenceDialogSearchBinding

private const val HEIGHT_RATIO = 0.6f

private fun ListView.showCode(
    adapter: CustomPreferenceAdapter,
    code: String?,
) {
    val index = adapter.indexOfCode(code)
    if (index == -1) {
        clearChoices()
        setSelection(0)
    } else {
        setItemChecked(index, true)
        setSelection(index)
    }
}

class CustomPreferenceDialogFragment : PreferenceDialogFragmentCompat() {
    private lateinit var binding: PreferenceDialogSearchBinding

    override fun onCreateDialogView(context: Context): View {
        binding = PreferenceDialogSearchBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.root.updateLayoutParams { height = (resources.displayMetrics.heightPixels * HEIGHT_RATIO).toInt() }
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        val customPreference = preference as CustomPreference
        val adapter = CustomPreferenceAdapter(view.context, customPreference.values)
        bindList(adapter)
        bindSearch(adapter, customPreference.value)
        bindItemSelection(binding.list, adapter, customPreference)
    }

    private fun bindList(adapter: CustomPreferenceAdapter) {
        binding.list.apply {
            emptyView = binding.empty
            this.adapter = adapter
        }
    }

    private fun bindSearch(
        adapter: CustomPreferenceAdapter,
        selectedCode: String?,
    ) {
        val listView = binding.list
        val searchView = binding.customPreferenceSearchText
        searchView.setOnQueryTextListener(Listener(adapter, listView, selectedCode))
        listView.showCode(adapter, selectedCode)
    }

    private fun bindItemSelection(
        listView: ListView,
        adapter: CustomPreferenceAdapter,
        customPreference: CustomPreference,
    ) {
        listView.setOnItemClickListener { _, _, position, _ ->
            val code = adapter.getItem(position)!!.code
            if (customPreference.callChangeListener(code)) {
                customPreference.value = code
            }
            dismiss()
        }
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        super.onPrepareDialogBuilder(builder)
        builder.setPositiveButton(null, null)
    }

    override fun onDialogClosed(positiveResult: Boolean) = Unit

    internal class Listener(
        private val adapter: CustomPreferenceAdapter,
        private val listView: ListView,
        private val selectedCode: String?,
    ) : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = false

        override fun onQueryTextChange(newText: String?): Boolean {
            adapter.update(newText.orEmpty())
            listView.showCode(adapter, selectedCode)
            return true
        }
    }

    companion object {
        fun newInstance(key: String): CustomPreferenceDialogFragment {
            val fragment = CustomPreferenceDialogFragment()
            val bundle = Bundle(1)
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}
