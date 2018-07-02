package com.shahzada.raceorganizer;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.Inflater;

/**
 * 0.80 ask user for old data TICK
 * 0.80 Store old data  TICK
 * 0.80 put user inputs into a .txt TICK
 * 0.80 Non input error messages TICK
 * 0.80 Load and Store settings TICK
 *
 * 0.90User prefrences
 * 0.90Location for yachtclubs
 * 0.90OnTouch Listeners
 * 0.90Clock
 * 0.90Dynamic Crew
 */
public class Crew extends AppCompatActivity {

    /*
     */
    int minute;
    int hour;
    Date date;
    ArrayList<DateStore> dateStoreArrayList = new ArrayList<>();
    ArrayList<Date> dates = new ArrayList<>();
    ArrayList<Date> datesSession = new ArrayList<>();
    String time;

    RadioGroup boatType;
    TextView deadline;
    TextView defaultText;
    TextView defaultVal;
    Spinner yachtClub;
    Spinner crew;
    EditText boatName;
    EditText name;
    EditText phone;
    EditText mail;
    EditText yatchClubAlt;
    EditText crewAlt;
    EditText specificInfo;


    RadioButton yachtRadio;
    RadioButton dingyRadio;

    static String[] data = new String[14];
    int index;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //detect widgets
        boatName = (EditText) findViewById(R.id.boatName);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.email);
        specificInfo = (EditText) findViewById(R.id.specificInfo);

        yachtClub = (Spinner) findViewById(R.id.yachtClub);
        yatchClubAlt = (EditText) findViewById(R.id.yachtClubAlt);

        boatType = (RadioGroup) findViewById(R.id.boatGroupA);

        crew = (Spinner) findViewById(R.id.crewSpinner);
        crewAlt = (EditText) findViewById(R.id.crewAlt);
        deadline = (TextView) findViewById(R.id.deadline);

        defaultVal = (TextView) findViewById(R.id.defaultVal);
        defaultText = (TextView) findViewById(R.id.dl);

        dingyRadio = (RadioButton) findViewById(R.id.dingyA);
        yachtRadio = (RadioButton) findViewById(R.id.yachtA);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //yachtclub logic
        ArrayAdapter<CharSequence> clubs = ArrayAdapter.createFromResource(Crew.this,
                R.array.yachtClubs, android.R.layout.simple_spinner_item);
        clubs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yachtClub.setAdapter(clubs);

        yachtClub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("Other".equals(yachtClub.getSelectedItem().toString())){
                    yatchClubAlt.setVisibility(View.VISIBLE);
                    yachtClub.setVisibility(View.INVISIBLE);
                }
            }@Override
            public void onNothingSelected(AdapterView<?> parent) {        }      });


        //Crew logic
        final ArrayAdapter<CharSequence> yachtCrews = ArrayAdapter.createFromResource(this, R.array.yachtCrews, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> dingyCrews = ArrayAdapter.createFromResource(this, R.array.dingyCrews, android.R.layout.simple_spinner_item);
        yachtCrews.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crew.setAdapter(yachtCrews);

        crew.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("Other".equals(crew.getSelectedItem().toString())) {
                    crewAlt.setVisibility(View.VISIBLE);
                    crew.setVisibility(View.INVISIBLE);
                }
            }             @Override
            public void onNothingSelected(AdapterView<?> parent) {      }   });

        //Boat listener
        boatType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //System.out.println(checkedId);
                //System.out.println(dingyRadio.getId());
                //System.out.println(yachtRadio.getId());
                if (checkedId == dingyRadio.getId()) {
                    dingyCrews.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    crew.setAdapter(dingyCrews);
                }
                else if(checkedId == yachtRadio.getId()) {
                    yachtCrews.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    crew.setAdapter(yachtCrews);
                }
            }
        });

        //Deadline logic. dialogue fragment
        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                //DialogFragment calendarDialogue = CalendarDialogue.newInstance();

                LayoutInflater layoutInflater = (LayoutInflater)Crew.this.getSystemService
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
                                    AlertDialog.Builder ab = new AlertDialog.Builder(Crew.this);
                                    ab.setMessage("Only 4 Dates per order").setTitle("Too many dates");
                                    ab.create().show();
                                    calendar.selectDate(date);
                                }
                            }
                        };
                        TimePickerDialog tpd = new TimePickerDialog(Crew.this, tpdlistener,hour, minute, false);
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
                new android.app.AlertDialog.Builder(Crew.this)
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
        //DID IT START FROM MANAGER OR BY NORMAL
        if(getIntent().getExtras() != null){
            index = getIntent().getExtras().getInt("indexVal");
            data = SaveAndLoad.storage.storageData.get(index);
            data[11] = getIntent().getExtras().getString("mainIndex");
            System.out.println(Arrays.toString(data));
            autofill(data);
            filled = true;
        }
        else{
            index = -1;
        }
        //autofill logic
        SaveAndLoad.storage.fileNames[2] = getCacheDir() + File.separator + "CrewSettings.txt";
        final File settings = new File(SaveAndLoad.storage.fileNames[2]);
        if (settings.exists() && !filled){
            final AlertDialog.Builder autofillDialog = new AlertDialog.Builder(this);
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

        for (int i = 0 ; i < 3 ; i++){
            System.out.println(SaveAndLoad.storage.fileNames[i]);
        }
        SaveAndLoad.write(new File(SaveAndLoad.storage.fileNames[0]));
    }
    public void onStart(){
        super.onStart();
    }



    public void autofill(File settings){
        AlertDialog error = new AlertDialog.Builder(Crew.this).create();
        String errLog = "";
        try{
            errLog = "Assign reader";
            Scanner reader = new Scanner(settings);
            errLog = "type";
            reader.nextLine();
            errLog = "boat type";
            String boatType = reader.nextLine();
            /*if(boatType.equals("Yacht")){
                yachtRadio.setChecked(true);
            }
            else if(boatType.equals("Dingy")){
                dingyRadio.setChecked(true);
            }*/
            yachtRadio.setChecked(true);
            errLog = "club";
            String club = reader.nextLine();
            if(club.equals("PCYC")){
                yachtClub.setSelection(0);
            }
            else if(club.equals("BYC")){
                yachtClub.setSelection(1);
            }
            else if(club.equals("RStLYC")){
                yachtClub.setSelection(2);
            }
            else if(club.equals("LRYC")){
                yachtClub.setSelection(3);
            }
            else if(club.equals("BDYC")){
                yachtClub.setSelection(4);
            }
            else{
                yachtClub.setSelection(5);
                yatchClubAlt.setText(club);
            }
            errLog = "crewtype";
            String crewType = reader.nextLine();
            if(crewType.equals("Skipper")){
                crew.setSelection(0);
            }
            else if(crewType.equals("Crew")){
                crew.setSelection(1);
            }
            else if (crewType.equals("Foredeck")){
                crew.setSelection(2);
            }
            else if (crewType.equals("Other")){
                crew.setSelection(3);
                crewAlt.setText("crewType");
            }
            else{
                crew.setSelection(0);
            }
            errLog = "deadline";
            deadline.setText(reader.nextLine());
            defaultVal.setVisibility(View.INVISIBLE);
            errLog = "boatName";
            boatName.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "specific";
            specificInfo.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "name";
            name.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "phone";
            phone.setText(reader.nextLine(), TextView.BufferType.EDITABLE);
            errLog = "mail";
            mail.setText(reader.nextLine(), TextView.BufferType.EDITABLE);


            //
            reader.close();
        } catch (Exception e) {
            error.setMessage("Error2 - Couldn't read data\nDebug info:\nBroke at " + errLog + "\nOld data cleared for corruption");
            settings.delete();
            error.show();
         }
    }
    public void autofill(String[] data){
        AlertDialog error = new AlertDialog.Builder(Crew.this).create();
        String clubText =  data[2].replace("_", " ");
        //String boatTypeText = data[3].replace("_", " "); NEED A LIST OF BOAT CLASSIFICATIONS
        yachtRadio.setChecked(true);
        String crewTypeText = data[4].replace("_", " ").trim();
        String deadlineText = data[5].replace("-", " ").trim();
        String phoneText = data[6].replace("_", " ").trim();
        String mailText =  data[7].replace("_", " ").trim();
        String boatNameText = data[8].replace("_", " ").trim();
        String nameText = data[9].replace("_", " ").trim();
        String specific = data[13].replace("_", " ").trim();
        if(clubText.equals("PCYC")){
            yachtClub.setSelection(0);
        }
        else if(clubText.equals("BYC")){
            yachtClub.setSelection(1);
        }
        else if(clubText.equals("RStLYC")){
            yachtClub.setSelection(2);
        }
        else if(clubText.equals("LRYC")){
            yachtClub.setSelection(3);
        }
        else if(clubText.equals("BDYC")){
            yachtClub.setSelection(4);
        }
        else{
            yachtClub.setVisibility(View.INVISIBLE);
            yatchClubAlt.setVisibility(View.VISIBLE);
            yatchClubAlt.setText(clubText);
        }
        if(crewTypeText.equals("Skipper")){
            crew.setSelection(0);
        }
        else if(crewTypeText.equals("Crew")){
            crew.setSelection(1);
        }
        else if (crewTypeText.equals("Foredeck")){
            crew.setSelection(2);
        }
        else if (crewTypeText.equals("Other")){
            crew.setVisibility(View.INVISIBLE);
            crewAlt.setVisibility(View.VISIBLE);
            crewAlt.setText(crewTypeText);
        }
        deadline.setText(deadlineText);
        defaultVal.setVisibility(View.INVISIBLE);
        boatName.setText(boatNameText);
        specificInfo.setText(specific);
        name.setText(nameText);
        phone.setText(phoneText);
        mail.setText(mailText);
    }

    //switch intents and store data
    public void storeData(View v){

        String errLog = "";
        AlertDialog error = new AlertDialog.Builder(this).create();

        try{
            //check and store values
            boolean problem = false;
            String boatType = "";
            if (this.boatType.getCheckedRadioButtonId() == dingyRadio.getId()){
                boatType = "Dingy";
            }
            else if(this.boatType.getCheckedRadioButtonId() == yachtRadio.getId()){
                boatType = "yacht";
            }
            else{
                errLog = "Nothing entered on boat type";
                problem = true;
            }
            //indicator of sending crew data;
            errLog += "Strings";
            int dataType = 1;
            String yachtClub = ((Spinner) findViewById(R.id.yachtClub)).getSelectedItem().toString();
            if(yachtClub.equals("Other")) yachtClub = ((TextView) findViewById(R.id.yachtClubAlt)).getText().toString();
            String crew = ((Spinner) findViewById(R.id.crewSpinner)).getSelectedItem().toString();
            if(crew.equals("Other")) crew = ((TextView) findViewById(R.id.crewAlt)).getText().toString();
            String deadline = ((TextView) findViewById(R.id.deadline)).getText().toString();
            String boatName = ((EditText) findViewById(R.id.boatName)).getText().toString();
            String specific = ((EditText) findViewById(R.id.specificInfo)).getText().toString();
            String name = ((EditText) findViewById(R.id.name)).getText().toString();
            String phone = ((EditText) findViewById(R.id.phone)).getText().toString();
            String mail = ((EditText) findViewById(R.id.email)).getText().toString();

            if(deadline.equals("Click to set")){
                errLog += "\nNothing entered on deadline";
                problem = true;
            }
            if(boatName.replaceAll(" ", "").equals("")){
                errLog = "\nNothing entered on boat name/number";
                problem = true;
            }
            if(problem){
                throw new Exception();
            }

            //store data for reuse and transfer

            File userFile = new File(SaveAndLoad.storage.fileNames[2]);

            try{
                errLog = ("userFile created\n");
                if(!userFile.exists())
                    userFile.createNewFile();
                errLog =("file locked\n");
                FileOutputStream fileOutputStream = new FileOutputStream((userFile));
                errLog =("stream locked\n");
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                errLog =("flushed\n");
                printWriter.flush();
                errLog = ("written Type\n");
                printWriter.println(dataType);
                errLog = ("written boatType\n");
                printWriter.println(boatType);
                errLog = ("written YC\n");
                printWriter.println(yachtClub);
                errLog = ("written crew\n");
                printWriter.println(crew);
                errLog = ("written deadline\n");
                printWriter.println(deadline);
                errLog = ("written Bname\n");
                printWriter.println(boatName);
                errLog = ("Specific Infor\n");
                printWriter.println(specific);
                errLog = ("written name\n");
                printWriter.println(name);
                errLog = ("written phone\n");
                printWriter.println(phone);
                errLog = ("written mail\n");
                printWriter.println(mail);

                errLog =("closed\n");
                printWriter.close();

                try{
                    Bundle state = new Bundle();
                    if(index != -1){
                        state.putBoolean("state", true);
                        data[1] = "" + 14;
                    }
                    else{
                        state.putBoolean("state", false);
                    }
                    errLog = "ID";
                    data[0] = "";
                    errLog = "Request type";
                    data[1] = ("" + (dataType)).replace(" ", "_");
                    errLog = "yacht club";
                    data[2] = yachtClub.replace(" ", "_");
                    errLog = "Boat type";
                    data[3] = "";
                    errLog = "Crew";
                    data[4] = crew;
                    errLog = "Deadline";
                    data[5] = deadline.replaceAll(" ", "-");
                    data[5] = data[5].replaceAll(",-", ",_");
                    errLog = "Phone";
                    data[6] = phone.replace(" ", "_");
                    errLog = "Mail";
                    data[7] = mail.replace(" ", "_");
                    errLog = "Boat name";
                    data[8] = boatName.replace(" ", "_");
                    errLog = "name";
                    data[9] = name.replace(" ", "_");
                    errLog = "Delete type";
                    data[10] = "0";
                    errLog = "listing no";
                    //where the file intent from
                    if(data[11] == null){
                        data[11] = alphaRandom().replace(" ", "_");
                    }
                    else{
                        data[10] = "14";
                    }
                    errLog = "Specific info";
                    data[13] = specific.replace(" ", "_");

                    Intent iBoatSelect = new Intent(this, BoatSelection.class);
                    iBoatSelect.putExtras(state);
                    if(index != -1){
                        iBoatSelect.putExtra("indexVal",index);
                    }
                    File warehouse = new File(SaveAndLoad.storage.fileNames[0]);

                    SaveAndLoad.write(warehouse);
                    finish();
                    startActivity(iBoatSelect);
                }catch(Exception e){
                    error.setMessage("Error5 - Error storing data, can not progress\nDebug info:\nBroke at " + errLog);
                    error.show();
                    System.out.println(e.getStackTrace());
                }
            }
            catch(Exception e) {
                error.setMessage("Error4 - CrewSettings.txt not written\nDebug info:\nBroke at " + errLog);
                error.show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            error.setTitle("Some information was not entered");
            error.setMessage(errLog);
            error.show();
        }
    }
    public String alphaRandom(){
        Random rand = new Random();
        long digits = rand.nextLong();
        String alpha = (rand.nextLong() + "").toCharArray().toString();
        alpha.replaceAll("[^a-zA-Z0-9]","A");
        return digits + alpha;
    }
}



