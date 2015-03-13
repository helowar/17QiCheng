package com.qicheng.business.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.qicheng.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.qicheng.business.ui.DatePickFragment.date";

    private String mDialogTitle;

    private Date mDate;

    private DatePicker mDatePicker;

    public String getDialogTitle() {
        return mDialogTitle;
    }

    public void setDialogTitle(String mDialogTitle) {
        this.mDialogTitle = mDialogTitle;
    }

    public static DatePickFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickFragment fragment = new DatePickFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public DatePickFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        initDatePicker();
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(mDialogTitle)
                .setPositiveButton(R.string.txt_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                        dismiss();
                    }
                }).create();
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (mDate != null) {
            calendar.setTime(mDate);
        } else {
            mDate = calendar.getTime();
        }
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new
                DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                        getArguments().putSerializable(EXTRA_DATE, mDate);
                    }
                });
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null){
            return;
        }
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }

}
