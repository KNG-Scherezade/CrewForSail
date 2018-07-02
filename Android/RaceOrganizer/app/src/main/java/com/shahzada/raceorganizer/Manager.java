package com.shahzada.raceorganizer;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shahzada.raceorganizer.DateStore.DateStores;
import com.squareup.timessquare.CalendarPickerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/** implement 0.90 */

public class Manager extends AppCompatActivity {

    int count;
    ArrayList<TableRow> rows;
    RelativeLayout relativeLayout;
    AdView adView;
    DateStores dateStores;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        relativeLayout = (RelativeLayout) findViewById(R.id.panel);
        generateLayout(relativeLayout);

    }
    public void onStart() {
        super.onStart();
    }

    public void generateLayout(RelativeLayout relativeLayout){
//MODIFICATIONS IN XML
        TextView message = new TextView(this);

        TableLayout listings = new TableLayout(this);
        listings.setMinimumWidth(1000);
        listings.setMinimumHeight(300);
        relativeLayout.addView(listings);

        TableRow trE = new TableRow(this);
        listings.addView(trE, 0);

        //SEARCH LOGIC
        Button search = new Button(this);
        search.setText("Search Listings");
        trE.addView(search, 0);
        TableRow.LayoutParams searchLayoutParams = (TableRow.LayoutParams) search.getLayoutParams();
        searchLayoutParams.span = 3;
        search.setLayoutParams(searchLayoutParams);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSearch = new Intent(Manager.this, Search.class);
                startActivity(iSearch);
            }
        });

        //BACK LOGIC
        Button back = new Button(this);
        back.setText("Back");
        trE.addView(back,1);
        TableRow.LayoutParams backLayoutParams = (TableRow.LayoutParams) back.getLayoutParams();
        backLayoutParams.column = 3;
        back.setLayoutParams(backLayoutParams);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iHome =new Intent(Manager.this, Home.class);
                startActivity(iHome);
            }
        });

        //CHECK IF THERE'S DATA
        boolean dataPresent = false;
        top:
        for (String[] data: SaveAndLoad.storage.storageData){
            for (String entry: data){
                if(entry != null && !entry.equals("")){
                    dataPresent = true;
                    break top;
                }
            }
        }
        //CREATE DATA TABLE
        if(dataPresent){
            rows = new ArrayList<>();
            int textSize = 15;
            final int width = 200;
            int color = Color.BLACK;
            count = 0;
            for (final String[] element: SaveAndLoad.storage.storageData){
                TableRow row = new TableRow(this);
                row.setMinimumHeight(50);
                rows.add(row);
                if(count % 2 == 0){
                    rows.get(count).setBackgroundColor(Color.GRAY);
                }
                rows.get(count).setMinimumHeight(120);
                listings.addView(rows.get(count));

                TextView entryNo = new TextView(this);
                entryNo.setText("Entry # " + (count + 1));
                entryNo.setWidth(width + 50);
                entryNo.setTextColor(color);
                entryNo.setTextSize(textSize);
                entryNo.setGravity(Gravity.CENTER);
                rows.get(count).addView(entryNo);

                final TextView itemType = new TextView(this);
                itemType.setWidth(width + 70);
                itemType.setTextColor(color);
                itemType.setGravity(Gravity.CENTER);
                itemType.setText(element[3]);
                itemType.setTextSize(textSize);
                rows.get(count).addView(itemType);

                final TextView requestType = new TextView(this);
                if(Integer.parseInt(element[1]) == 1){
                    requestType.setText("Crew");

                    requestType.setTextColor(Color.parseColor("#000099"));
                }
                else{
                    requestType.setText("Boat");
                    requestType.setTextColor(Color.parseColor("#006600"));
                }
                requestType.setTextSize(textSize + 2);
                requestType.setWidth(width);
                requestType.setGravity(Gravity.CENTER);
                rows.get(count).addView(requestType);

                Button delete = new Button(this);
                delete.setText("Delete");
                delete.setTextSize(textSize);
                delete.setWidth(width);
                rows.get(count).addView(delete);
                final int INDEX = count;

                data = SaveAndLoad.storage.storageData.get(INDEX);

                final AlertDialog.Builder confirmation = new AlertDialog.Builder(Manager.this);
                confirmation.setTitle("Confirm");
                confirmation.setMessage("Are you sure you want to delete that ?");

                final AlertDialog.Builder modification = new AlertDialog.Builder(Manager.this);
                modification.setTitle("Confirm");
                modification.setMessage("Change listing");

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmation.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                InternetAccess internetAccess = new InternetAccess(Manager.this, null);
                                internetAccess.execute("ID",data[0], "type", data[1], "club", data[2], "boat", data[3],
                                        "crew",data[4],"deadline",data[5],"phone",data[6], "mail", data[7], "boatName", data[8]
                                        ,"name",data[9], "delete", "1","listing", data[11],"specific", data[12]);
                                try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); }
                                SaveAndLoad.storage.storageData.remove(INDEX);
                                File warehouse = new File(getFilesDir() + File.separator + "Warehouse.bin");
                                SaveAndLoad.write(warehouse);
                                refresh();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        confirmation.show();
                    }
                });

                dateStores = new DateStores(data[5]);
                //SET TO TRUE IN DATASTORE
                if(dateStores.checkDates() == true){
                    Button Relist = new Button(this);
                    Relist.setText("Relist");
                    Relist.setTextSize(textSize);
                    Relist.setWidth(width);
                    rows.get(count).addView(Relist);
                    element[12] = "" + INDEX;
                    Relist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modification.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ArrayList<Date> dates = new ArrayList<>();
                                    final ArrayList<DateStore> dateStoreArrayList = new ArrayList<>();
                                    final Date[] date = new Date[1];
                                    final int[] hour = new int[1];
                                    final int[] minute = new int[1];

                                    FragmentManager fm = getSupportFragmentManager();
                                    //DialogFragment calendarDialogue = CalendarDialogue.newInstance();

                                    LayoutInflater layoutInflater = (LayoutInflater)Manager.this.getSystemService
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
                                            date[0] = dateT;
                                            final boolean[] timeset = {false};
                                            TimePickerDialog.OnTimeSetListener tpdlistener = new TimePickerDialog.OnTimeSetListener(){
                                                public void onTimeSet(TimePicker view, int hourT, int minuteT){
                                                    hour[0] = hourT;
                                                    minute[0] = minuteT;
                                                    timeset[0] = true;
                                                    if(dates.size() < 4){
                                                        dateStoreArrayList.add(new DateStore(hour[0], minute[0], date[0]));
                                                        dates.add(date[0]);
                                                    }
                                                    else{
                                                        android.support.v7.app.AlertDialog.Builder ab = new android.support.v7.app.AlertDialog.Builder(Manager.this);
                                                        ab.setMessage("Only 4 Dates per order").setTitle("Too many dates");
                                                        ab.create().show();
                                                        calendar.selectDate(date[0]);
                                                    }
                                                }
                                            };
                                            TimePickerDialog tpd = new TimePickerDialog(Manager.this, tpdlistener, hour[0], minute[0], false);
                                            tpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    if(!timeset[0]){
                                                        calendar.selectDate(date[0]);
                                                    }
                                                    hour[0] = 0;
                                                    minute[0] = 0;
                                                    calendar.scrollToDate(new Date());
                                                }
                                            });
                                            tpd.setMessage("24 Hours Maximum");
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
                                    new android.app.AlertDialog.Builder(Manager.this)
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
                                                        }
                                                    })
                                            .setPositiveButton("Confirm",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(
                                                                final DialogInterface dialogInterface, int i) {
                                                            AlertDialog.Builder confirmation = new AlertDialog.Builder(Manager.this);
                                                            confirmation.setMessage("Submit").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    SaveAndLoad.storage.storageData.get(INDEX)[5] = dateStoreArrayList.toString().replaceAll("\\[","").replaceAll("]","");
                                                                    String data5 =  SaveAndLoad.storage.storageData.get(INDEX)[5];

                                                                    System.out.println(data5);

                                                                    InternetAccess internetAccess = new InternetAccess(Manager.this, null);
                                                                    internetAccess.execute("ID",data[0], "type", data[1], "club", data[2], "boat", data[3],
                                                                            "crew",data[4],"deadline", data5, "phone", data[6], "mail", data[7], "boatName", data[8]
                                                                            ,"name",data[9], "delete", "0","listing", data[11],"specific", data[12]);
                                                                    try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace(); }
                                                                    File warehouse = new File(getFilesDir() + File.separator + "Warehouse.bin");
                                                                    SaveAndLoad.write(warehouse);
                                                                    dialogInterface.dismiss();
                                                                    refresh();
                                                                }
                                                            }).show();
                                                        }
                                                    }).create().show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                            modification.show();
                        }
                    });
                }
                else{
                    Button modify = new Button(this);
                    modify.setText("Modify");
                    modify.setTextSize(textSize);
                    modify.setWidth(width);
                    rows.get(count).addView(modify);
                    element[12] = "" + INDEX;
                    modify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            modification.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent iChange = new Intent();
                                    iChange.putExtra("mainIndex", element[11]);
                                    iChange.putExtra("indexVal",Integer.parseInt(element[12]));
                                    if (Integer.parseInt(element[1]) == 1) {
                                        iChange.setClass(Manager.this, Crew.class);
                                    } else if(Integer.parseInt(element[1]) == 2){
                                        iChange.setClass(Manager.this, Boat.class);
                                    }
                                    startActivity(iChange);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                            modification.show();
                        }
                    });
                }
                //set their index*/
                System.out.println(++count);
            }
        }
    }

    public void refresh(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.panel);
        layout.removeAllViews();
        generateLayout(relativeLayout);
    }
    public void onStop(){
        super.onStop();
        //end file saving
        //and writing data to file
        finish();
    }


}
