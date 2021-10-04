package com.vrem.wifianalyzer.apalias;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;

import static android.app.Activity.RESULT_OK;

public class APAliasFragment extends ListFragment {

    public static final int IMPORT_AP_LIST = 1001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.apalias_content, container, false);

        final APAliasAdapter aliasAdapter = new APAliasAdapter(getActivity(), MainContext.INSTANCE.getaPAliasService());
        setListAdapter(aliasAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("", "");
            }
        });

        FloatingActionButton impfab = view.findViewById(R.id.impfab);
        impfab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                {
                    getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMPORT_AP_LIST);
                }
                else
                {
                    browseFileFromStorage();
                }
            }
        });

        return view;
    }

    private void browseFileFromStorage()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), IMPORT_AP_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == IMPORT_AP_LIST && resultCode == RESULT_OK)
        {
            try
            {
                BufferedReader reader;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                {
                    reader = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(data.getData()), "UTF-8"));
                }
                else
                {
                    reader = new BufferedReader(new FileReader(new File(data.getData().getPath())));
                }

                String line;
                while (!TextUtils.isEmpty((line = reader.readLine())))
                {
                    String[] s = line.split(",");

                    if (s.length >= 2)
                    {
                        MainContext.INSTANCE.getaPAliasService().addAliasForBSSID(s[0], s[1]);
                    }
                }

                ((ArrayAdapter<String>) getListAdapter()).clear();
                ((ArrayAdapter<String>) getListAdapter()).addAll(MainContext.INSTANCE.getaPAliasService().getBSSIDs());
                ((ArrayAdapter<String>) getListAdapter()).notifyDataSetChanged();
            }
            catch (Exception ex)
            {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == IMPORT_AP_LIST && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
        {
            browseFileFromStorage();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                showDialog(adapterView.<TextView>findViewById(R.id.apalias_bssid).getText().toString(), adapterView.<TextView>findViewById(R.id.apalias_alias).getText().toString());
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                MainContext.INSTANCE.getaPAliasService().removeAliasForBSSID(parent.<TextView>findViewById(R.id.apalias_bssid).getText().toString());
                ((ArrayAdapter<String>) getListAdapter()).clear();
                ((ArrayAdapter<String>) getListAdapter()).addAll(MainContext.INSTANCE.getaPAliasService().getBSSIDs());
                ((ArrayAdapter<String>) getListAdapter()).notifyDataSetChanged();

                return true;
            }
        });
    }

    private void showDialog(final String bssid, String alias)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(R.string.action_apalias);
        dialog.setContentView(R.layout.apalias_dialog);

        final EditText edtBssid = dialog.findViewById(R.id.apalias_dialog_bssid);
        final EditText edtAlias = dialog.findViewById(R.id.apalias_dialog_alias);

        edtBssid.setText(bssid);
        edtAlias.setText(alias);

        Button okBtn = dialog.findViewById(R.id.apalias_dialog_btn_ok);
        okBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (!TextUtils.isEmpty(bssid))
                {
                    MainContext.INSTANCE.getaPAliasService().removeAliasForBSSID(bssid);
                }

                MainContext.INSTANCE.getaPAliasService().addAliasForBSSID(edtBssid.getText().toString(), edtAlias.getText().toString());

                ((ArrayAdapter<String>) getListAdapter()).clear();
                ((ArrayAdapter<String>) getListAdapter()).addAll(MainContext.INSTANCE.getaPAliasService().getBSSIDs());
                ((ArrayAdapter<String>) getListAdapter()).notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        Button cancelBtn = dialog.findViewById(R.id.apalias_dialog_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
