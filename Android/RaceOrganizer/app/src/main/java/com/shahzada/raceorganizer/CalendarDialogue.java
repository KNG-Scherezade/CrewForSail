package com.shahzada.raceorganizer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarDialogue extends DialogFragment{
    View view;

    public int hour;
    public int minute;
    public Date date;
    ArrayList<DateStore> dateStoreArrayList = new ArrayList<>();


    Calendar nextMonth = Calendar.getInstance();
    Calendar lastMonth = Calendar.getInstance();



    public CalendarDialogue(){
        lastMonth.set(Calendar.MONTH,Calendar.MONTH + 1);
        nextMonth.add(Calendar.MONTH, 1);
        System.out.println(lastMonth.get(Calendar.MONTH));
        System.out.println(nextMonth.get(Calendar.MONTH));

    }


    static CalendarDialogue newInstance() {
        CalendarDialogue f = new CalendarDialogue();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){


        new AlertDialog.Builder(getActivity())
                .setTitle("Select your dates")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();

        view = inflater.inflate(R.layout.activity_time_set, container);
        final CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.deadlinePickerM2);
        Calendar today = Calendar.getInstance();
        calendar.init(lastMonth.getTime(), nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        calendar.scrollToDate(today.getTime());
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date dateT) {
                date = dateT;
                TimePickerDialog.OnTimeSetListener tpdlistener = new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker view, int hourT, int minuteT){
                        hour = hourT;
                        minute = minuteT;
                        dateStoreArrayList.add(new DateStore(hour, minute, date));
                    }
                };
                TimePickerDialog tpd = new TimePickerDialog(view.getContext(), tpdlistener,hour, minute, false);
                tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        hour = 0;
                        minute = 0;
                        //unset date
                        calendar.scrollToDate(new Date());
                    }
                });
                tpd.setMessage("24 Hours Maximum");
                tpd.show();
            }

            @Override
            public void onDateUnselected(Date date) {
                int index = dateStoreArrayList.indexOf(new DateStore(hour, minute, date));
                if(index > -1){
                    dateStoreArrayList.remove(index);
                }
            }
        });
        return view;
    }


}
