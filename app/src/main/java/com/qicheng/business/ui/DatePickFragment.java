package com.qicheng.business.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qicheng.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickFragment extends DialogFragment {


    private String mDialogTitle;

    public String getDialogTitle() {
        return mDialogTitle;
    }

    public void setDialogTitle(String mDialogTitle) {
        this.mDialogTitle = mDialogTitle;
    }

    public DatePickFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v  = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(mDialogTitle)
                .setPositiveButton(R.string.txt_confirm,null).create();
    }

}
