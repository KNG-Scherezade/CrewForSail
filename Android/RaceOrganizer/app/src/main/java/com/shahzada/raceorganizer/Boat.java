package com.shahzada.raceorganizer;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Boat extends AppCompatActivity implements Serializable {

    RadioGroup boatType;
    RadioButton DinghyRadio;
    RadioButton yachtRadio;
    RadioButton eitherRadio;
    EditText name;
    EditText phone;
    EditText mail;
    TextView deadline;
    TextView defaultVal;
    TextView defaultText;
    EditText specific;

    int minute;
    int hour;
    Date date;
    ArrayList<DateStore> dateStoreArrayList = new ArrayList<>();
    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Date> datesSession = new ArrayList<>();
    String time;



    static String[] data = new String[14];
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        specific  = (EditText) findViewById(R.id.specificInfo);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.email);

        boatType = (RadioGroup) findViewById(R.id.boatGroupB);
        deadline = (TextView) findViewById(R.id.deadline);

        defaultVal = (TextView) findViewById(R.id.defaultVal);
        defaultText = (TextView) findViewById(R.id.dl);

        DinghyRadio = (RadioButton) findViewById(R.id.dingyB);
        yachtRadio = (RadioButton) findViewById(R.id.yachtB);
        eitherRadio = (RadioButton) findViewById(R.id.eitherB);

        AdView mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        //Deadline logic
        //Create a calender. On calender select pick a date. Then pick a time.
        //onTimeSet() processes time objects on the interface and stores time variables
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                //DialogFragment calendarDialogue = CalendarDialogue.newInstance();

                LayoutInflater layoutInflater = (LayoutInflater)Boat.this.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                final CalendarPickerView calendar = (CalendarPickerView) layoutInflater
                        .inflate(R.layout.activity_time_set, null, false);

                Calendar nextMonth = Calendar.getInstance();
                Calendar lastMonth = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                lastMonth.set(Calendar.MONTH,Calendar.MONTH + 1);
                nextMonth.add(Calendar.MONTH, 1);

                calendar.init(today.getTime(), nextMonth.getTime()
                ).inMode(CalendarPickerView.SelectionMode.MULTIPLE)
                        .withSelectedDates(dates);
                calendar.scrollToDate(today.getTime());
                calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(Date dateT) {
                        date = dateT;
                        final boolean[] timeset = {false};
                        TimePickerDialog.OnTimeSetListener tpdlistener = new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker view, int hourT, int minuteT){
                                hour = hourT;
                                minute = minuteT;
                                timeset[0] = true;
                                if(dates.size() < 4){
                                    dateStoreArrayList.add(new DateStore(hour, minute, date));
                                    dates.add(date);
                                }
                                else{
                                    android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(Boat.this);
                                    ab.setMessage("Only 4 Dates per order").setTitle("Too many dates");
                                    ab.create().show();
                                    calendar.selectDate(date);
                                }
                            }
                        };
                        TimePickerDialog tpd = new TimePickerDialog(Boat.this, tpdlistener,hour, minute, false);
                        tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(!timeset[0]){
                                    calendar.selectDate(date);
                                }
                                hour = 0;
                                minute = 0;
                                calendar.scrollToDate(new Date());
                            }
                        });
                        tpd.setMessage("Set your time");
                        tpd.show();
                    }

                    @Override
                    public void onDateUnselected(Date date) {
                        int index = dateStoreArrayList.indexOf(new DateStore(0, 0, date));
                        if(index > -1){
                            dateStoreArrayList.remove(index);
                            dates.remove(index);
                        }
                        System.out.println(dateStoreArrayList);
                    }
                });
                new android.app.AlertDialog.Builder(Boat.this)
                        .setTitle("Set your dates")
                        // R.string.select_date
                        .setView(calendar)
                        .setNegativeButton("Clear",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        dateStoreArrayList.clear();
                                        dates.clear();
                                        calendar.clearHighlightedDates();
                                        deadline.setText("Click to set");
                                    }
                                })
                        .setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        if(dateStoreArrayList.size() == 0){
                                            deadline.setText("Click to set");
                                        }
                                        else{
                                            deadline.setText((dateStoreArrayList.toString().replaceAll("\\[", "").replaceAll("\\]","")));
                                            defaultVal.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }).create().show();


                //calendarDialogue.show(fm, "activity_time_set");
            }
        });
        //default calender setup
        deadline.setText("Click to set");


        boolean filled = false;
        //DID IT START FROM MANAGER OR WAS IT CREATED
        if(getIntent().getExtras() != null){
            //MANAGER
            index = getIntent().getExtras().getInt("indexVal");
            data = SaveAndLoad.storage.storageData.get(index);
            autofill(data);
            data[11] = getIntent().getExtras().getString("mainIndex");

            filled = true;
        }
        else{
            //CREATED
            index = -1;
        }

        //autofill logic
        SaveAndLoad.storage.fileNames[1] = getCacheDir() + File.separator + "BoatSettings.txt";
        final File settings = new File(SaveAndLoad.storage.fileNames[1]);
        if (settings.exists() && !filled){
            final AlertDialog.Builder  autofillDialog = new AlertDialog.Builder(this);
            autofillDialog.setMessage("AutoFill ?").setPositiveButton("yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    autofill(settings);
                }
            }).setNegativeButton("no",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                }
            }).setNeutralButton("delete data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    settings.delete();
                }
            });
            autofillDialog.show();
        }
        final Intent iBoatSelect = new Intent(this, BoatSelection.class);
        Button next = (Button) findViewById(R.id.confirm);

        for (int i = 0 ; i < 3 ; i++){
            System.out.println(SaveAndLoad.storage.fileNames[i]);
        }
        SaveAndLoad.write(new File(SaveAndLoad.storage.fileNames[0]));
    }
    public String setZeros(int...args){
        String zeroTime = "";
        int count = 0;
        for (int element:args){
            if (element < 10){
                zeroTime += "" +  0 + args[count] + ":";
            }
            else{
                zeroTime += "" + args[count] + ":";
            }
            count++;
        }
        return zeroTime;
    }
    public int formatHour(int...args){
        hour = args[0];
        return hour % 24;
    }
    public String formatAMPM(int hour, String AMPM){
        if (hour / 12 == 1){
            return "PM";
        }
        else{
            return "AM";
        }
    }
    //switch intents and store data
    public void storeData(View v){
        String errLog = "";
        AlertDialog error = new AlertDialog.Builder(this).create();

        try{
            //check and store values
            errLog = "Boat type";
            String boatType = "";
            boolean problem = false;
            if (this.boatType.getCheckedRadioButtonId() == DinghyRadio.getId()){
                boatType = "Dinghy";
            }
            else if(this.boatType.getCheckedRadioButtonId() == yachtRadio.getId()){
                boatType = "yacht";
            }
            else if(this.boatType.getCheckedRadioButtonId() == eitherRadio.getId()){
                boatType = "Either";
            }
            else{
                problem = true;
                errLog = "Nothing entered on boat type";
            }
            //indicator of sending boat data;
            int type = 2;
            String name = ((EditText) findViewById(R.id.name)).getText().toString();
            String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
            String mail = ((EditText) findViewById(R.id.email)).getText().toString();
            String deadline = ((TextView) findViewById(R.id.deadline)).getText().toString();
            String specific = ((EditText) findViewById(R.id.specificInfo)).getText().toString();


            if(name.replaceAll(" ","").equals("")){
                problem = true;
                errLog += "\nNothing entered for name";
            }
            if(problem){
                throw new Exception();
            }
            //store data for reuse and transfer
            File userFile = new File(SaveAndLoad.storage.fileNames[1]);

            try{
                errLog = ("userFile created\n");
                userFile.createNewFile();
                errLog =("file locked\n");
                FileOutputStream fileOutputStream = new FileOutputStream((userFile));
                errLog =("stream locked\n");
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                errLog =("flushed\n");
                printWriter.flush();
                errLog = ("written Type\n");
                printWriter.println(type);
                errLog = ("written boatType\n");
                printWriter.println(boatType);
                errLog = ("written Name\n");
                printWriter.println(name);
                errLog = ("written Phone\n");
                printWriter.println(phone);
                errLog = ("written mail\n");
                printWriter.println(mail);
                errLog = ("written Deadline\n");
                printWriter.println(deadline);
                errLog = ("written specific\n");
                printWriter.println(specific);
                errLog =("closed\n");
                printWriter.close();
                fileOutputStream.close();

                try{
                    errLog = "ID";
                    data[0] = SaveAndLoad.storage.ID + "";
                    errLog = "Request type";
                    data[1] = (type + "").replace(" ", "_");
                    errLog = "yacht club";
                    data[2] = "";
                    errLog = "Boat type";
                    data[3] = boatType.replace(" ", "_");
                    errLog = "Crew";
                    data[4] = "";
                    errLog = "Deadline";
                    data[5] = deadline.replaceAll(" ", "-");
                    data[5] = data[5].replaceAll(",-", ",_");
                    errLog = "Phone";
                    data[6] = phone.replace(" ", "_");
                    errLog = "Mail";
                    data[7] = mail.replace(" ", "_");
                    errLog = "Boat name";
                    data[8] = "";
                    errLog = "name";
                    data[9] = name.replace(" ", "_");
                    errLog = "Delete type";
                    data[10] = "0";
                    errLog = "Listing No";
                    if(data[11] == null){
                        data[11] = alphaRandom().replace(" ", "_");
                    }
                    else{
                        data[10] = "24";
                    }
                    data[13] = specific.replace(" ", "_");

                    try {
                        errLog = "Writing to log";
                        InternetAccess internetAccess = new InternetAccess(this, null);
                        if(index == -1){
                            internetAccess.execute("ID",Boat.data[0], "type", Boat.data[1], "club", Boat.data[2], "boat", Boat.data[3],
                                    "crew",Boat.data[4],"deadline",Boat.data[5],"phone",Boat.data[6], "mail", Boat.data[7], "boatName", Boat.data[8]
                                    ,"name",Boat.data[9], "delete", Boat.data[10], "listing", Boat.data[11],"specific",data[13]);

                            SaveAndLoad.storage.addStorageData(data);
                        }
                        else{
                            internetAccess.execute("ID",Boat.data[0], "type", Boat.data[1], "club", Boat.data[2], "boat", Boat.data[3],
                                    "crew",Boat.data[4],"deadline",Boat.data[5],"phone",Boat.data[6], "mail", Boat.data[7], "boatName", Boat.data[8]
                                    ,"name",Boat.data[9], "delete", Boat.data[10],"listing", Boat.data[11],"specific",data[13]);
                            internetAccess.execute(data);
                        }
                    }catch(Exception e) {
                        error.setMessage("Error6 - Error storing data, can not progress\nDebug info:\nBroke at " + errLog);
                        error.show();
                    }

                     /*if(progressBar.getVisibility() == View.VISIBLE){
                        throw new Exception();
                    }*/
                    Intent iManager = new Intent(this, Manager.class);
                    data = new String[14];
                    File warehouse = new File(SaveAndLoad.storage.fileNames[0]);
                    SaveAndLoad.write(warehouse);
                    finish();
                    startActivity(iManager);
                }catch(Exception e){
                    error.setMessage("Error5 - Error storing data, can not progress\nDebug info:\nBroke at " + errLog);
                    error.show();
                }
            }
            catch(Exception e) {
                error.setMessage("Error4 - BoatSettings.txt not written\nDebug info:\nBroke at " + errLog);
                error.show();
            }
        }
        catch(Exception e){
            error.setTitle("Some information was not entered");
            error.setMessage(errLog);
            error.show();
        }

    }

    public void autofill(File settings){
        AlertDialog error = new AlertDialog.Builder(Boat.this).create();
        String errLog = "";
        try {
            errLog = "Assign reader";
            Scanner reader = new Scanner(settings);
            errLog = "type";
            System.out.println(reader.nextLine());
            errLog = "boat type";
            String boatType = reader.nextLine();
            System.out.println(boatType);
            if(boatType.equals("Yacht")){
                yachtRadio.setChecked(true);
            }
            else if(boatType.equals("Dinghy")){
                DinghyRadio.setChecked(true);
            }
            else if(boatType.equals("Either")) {
                eitherRadio.setChecked(true);
            }
            errLog = "name";
            name.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "specific";
            errLog = "phone";
            phone.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "mail";
            mail.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "deadline";
            deadline.setText(reader.nextLine());
            defaultVal.setVisibility(View.INVISIBLE);
            errLog = "Specific";
            specific.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            //
            reader.close();
        } catch (Exception e) {
            error.setMessage("Error2 - Couldn't read data\nDebug info:\nBroke at " + errLog);
            error.show();
        }
    }
    public void autofill(String[] data){
        AlertDialog error = new AlertDialog.Builder(Boat.this).create();
        String errLog = "";
        try {
            String clubText =  data[2].replace("_", " ").trim();
            String boatTypeText = data[3].replace("_", " ").trim();
            String crewTypeText = data[4].replace("_", " ").trim();
            String deadlineText = data[5].replace("-", " ").trim();
            String phoneText = data[6].replace("_", " ").trim();
            String mailText =  data[7].replace("_", " ").trim();
            String boatNameText = data[8].replace("_", " ").trim();
            String nameText = data[9].replace("_", " ").trim();
            String specificText = data[13].replace("_", " ").trim();
            if(boatTypeText.equals("yacht")){
                yachtRadio.setChecked(true);
            }
            else if(boatType.equals("Dinghy")){
                DinghyRadio.setChecked(true);
            }
            else if(boatType.equals("Either")){
                eitherRadio.setChecked(true);
            }
            errLog = "name";
            name.setText(nameText);
            errLog = "phone";
            phone.setText(phoneText);
            errLog = "mail";
            mail.setText(mailText);
            errLog = "deadline";
            deadline.setText(deadlineText);
            defaultVal.setVisibility(View.INVISIBLE);
            specific.setText(specificText);
        } catch (Exception e) {
            error.setMessage("Error2 - Couldn't read data\nDebug info:\nBroke at " + errLog);
            error.show();
        }
    }
    public void onStart() {
        super.onStart();
    }

    public String alphaRandom(){
        Random rand = new Random();
        long digits = rand.nextLong();
        String alpha = (rand.nextLong() + "").toCharArray().toString();
        alpha.replaceAll("[^a-zA-Z0-9]","A");
        return digits + alpha;
    }

}
