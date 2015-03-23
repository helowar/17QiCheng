/*
 * Copyright(c) 2015, QiCheng, Inc. All rights reserved.
 * This software is the confidential and proprietary information of QiCheng, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with QiCheng.
 */

package com.qicheng.business.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qicheng.R;
import com.qicheng.business.module.Benefit;

/**
 * Created by NO1 on 2015/3/21.
 */
public class TicketFragment extends DialogFragment{

    private static final String EXTRA_TICKET = "bundle_ticket";

    public static TicketFragment newInstance(Benefit ticket){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TICKET, ticket);
        TicketFragment fragment = new TicketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TicketFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout)inflaterDl.inflate(R.layout.dialog_ticket, null );
        //对话框
        final Dialog dialog = new android.app.AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        return dialog;
    }


}
