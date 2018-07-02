package com.shahzada.raceorganizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Search extends AppCompatActivity {

    TableLayout layout;
    ArrayList<TableRow> tr;
    ArrayList<String[]> dataList;
    private int i;//Because I hate java this much.
    ProgressBar progressBar;



    int click1 = 0;
    int click2 = 0;
    int click3 = 0;

    //indicator of recieving data;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(new Bundle());
        setContentView(R.layout.activity_search);

        //GENERATE ADVERT
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //ASSIGN LAYOUTS
        layout = (TableLayout) findViewById(R.id.table);
        layout.setHorizontalScrollBarEnabled(true);
        tr = new ArrayList<>();

        //SEARCH
        EditText search = (EditText) findViewById(R.id.search);
        assert search != null;
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tr.clear();
                layout.removeViewsInLayout(1,layout.getChildCount() - 1);
                addData(Search.this, layout, tr, dataList, s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //SORT BY WANT/NEED
        Button type = (Button) findViewById(R.id.type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr.clear();
                layout.removeViewsInLayout(1,layout.getChildCount() - 1);
                addDataAlphabetic(Search.this, layout, tr, dataList, 0);
            }
        });
        //SORT BY BOAT
        Button boat = (Button) findViewById(R.id.boatType);
        boat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr.clear();
                layout.removeViewsInLayout(1,layout.getChildCount() - 1);
                addDataAlphabetic(Search.this, layout, tr, dataList, 2);
            }
        });
        //SORT BY NAME
        Button listName =(Button) findViewById(R.id.listName);
        listName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr.clear();
                layout.removeViewsInLayout(1,layout.getChildCount() - 1);
                addDataAlphabetic(Search.this, layout, tr, dataList, 8);
            }
        });
        //BACK TO MAIN MENU
        Button back = (Button) findViewById(R.id.back);
        final Intent iHome = new Intent(this, Home.class);
        back.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                startActivity(iHome);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        InternetAccess internetAccess = new InternetAccess(Search.this, progressBar);
        internetAccess.execute("","","type","3");
        String status = "";
        int count = 0;
        while(!status.equals("[Connected, to, Database.]") && count < 20){

            try {
                status = Arrays.toString(internetAccess.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            count++;
            System.out.println(status);
        }
        dataList = SaveAndLoad.storage.searchData;
        addData(Search.this, layout, tr, dataList);
    }
    public void onStart() {
        super.onStart();
        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);
    }

    private void addDataAlphabetic(Search search, TableLayout layout, ArrayList<TableRow> tr,ArrayList<String[]> dataList, int col) {
        if(!dataList.isEmpty()){
            float textSize = 18;
            final String[][] dataListCopy = arrayListToArray(dataList);
            final int column = col;

            int clickCol = 0;
            if(col == 0) {
                click1++;
                clickCol = click1;
            }

            else if(col == 2) {
                click2++;
                clickCol = click2;
            }

            else if(col == 8) {
                click3++;
                clickCol = click3;
            }
System.out.println("clickCol " + clickCol);
            if(clickCol % 2 == 0){
                //reverse alpha
                Arrays.sort(dataListCopy, new Comparator<String[]>() {
                    @Override
                    public int compare(final String[] entry1, final String[] entry2) {
                        System.out.println(entry1[column]);
                        final String query1 = entry1[column];
                        final String query2 = entry2[column];
                        return  query1.compareTo(query2);
                    }
                });
            }
            else{
                //standard alpha
                Arrays.sort(dataListCopy, new Comparator<String[]>() {
                    @Override
                    public int compare(final String[] entry1, final String[] entry2) {
                        final String query2 = entry2[column];
                        final String query1 = entry1[column];
                        return  query2.compareTo(query1);
                    }
                });
            }

            for (i = 0; i < dataListCopy.length; i++ ){
                tr.add(new TableRow(search));
                if(i % 2 == 1){
                    tr.get(i).setBackgroundColor(Color.GRAY);
                }
                tr.get(i).setMinimumHeight(120);
                layout.addView(tr.get(i));


                TextView type = new TextView(search);
                type.setText(dataListCopy[i][0]);
                type.setGravity(Gravity.CENTER);
                if(dataListCopy[i][0].equals("Boat")){
                    type.setTextColor(Color.parseColor("#006600"));
                }
                else{
                    type.setTextColor(Color.parseColor("#000099"));
                }

                type.setTextSize(textSize);

                TextView boat = new TextView(search);
                boat.setText(dataListCopy[i][2]);
                boat.setTextSize(textSize );
                boat.setGravity(Gravity.CENTER);
                boat.setTextColor(Color.BLACK);

                TextView name = new TextView(search);
                name.setText(dataListCopy[i][8]);
                name.setGravity(Gravity.CENTER);
                name.setTextColor(Color.BLACK);
                name.setTextSize(textSize);

                tr.get(i).addView(type,0);
                tr.get(i).addView(boat,1);
                tr.get(i).addView(name,2);

                final AlertDialog.Builder trAlert = new AlertDialog.Builder(search).setMessage(alertMessage(dataListCopy[i][0],
                        dataListCopy[i][1],dataListCopy[i][2],dataListCopy[i][3],dataListCopy[i][4],
                        dataListCopy[i][5],dataListCopy[i][6],dataListCopy[i][7],dataListCopy[i][8],
                        dataListCopy[i][9]))
                        .setTitle("About").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                tr.get(i).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        trAlert.show();
                    }
                });
            }
        }
    }

    private void addData(Search search, TableLayout layout, ArrayList<TableRow> tr,ArrayList<String[]> dataList) {
        if(!dataList.isEmpty()){
            float textSize = 18;
            for (i = 0; i < dataList.size(); i++ ){
                tr.add(new TableRow(search));
                if(i % 2 == 1){
                    tr.get(i).setBackgroundColor(Color.GRAY);
                }
                tr.get(i).setMinimumHeight(120);
                layout.addView(tr.get(i));

                TextView type = new TextView(search);
                type.setText(dataList.get(i)[0]);
                type.setGravity(Gravity.CENTER);
                if(dataList.get(i)[0].equals("Boat")){
                    type.setTextColor(Color.parseColor("#006600"));
                }
                else{
                    type.setTextColor(Color.parseColor("#000099"));
                }

                type.setTextSize(textSize);


                TextView boat = new TextView(search);
                boat.setText(dataList.get(i)[2]);
                boat.setTextSize(textSize );
                boat.setGravity(Gravity.CENTER);
                boat.setTextColor(Color.BLACK);

                TextView name = new TextView(search);
                name.setText(dataList.get(i)[8]);
                name.setGravity(Gravity.CENTER);
                name.setTextColor(Color.BLACK);
                name.setTextSize(textSize);

                tr.get(i).addView(type,0);
                tr.get(i).addView(boat,1);
                tr.get(i).addView(name,2);

                final AlertDialog.Builder trAlert =new AlertDialog.Builder(search).setMessage(alertMessage(dataList.get(i)[0],
                        dataList.get(i)[1],dataList.get(i)[2],dataList.get(i)[3],dataList.get(i)[4],
                        dataList.get(i)[5],dataList.get(i)[6],dataList.get(i)[7],dataList.get(i)[8],
                        dataList.get(i)[9]))
                        .setTitle("About").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                tr.get(i).setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        trAlert.show();
                    }
                });
            }
        }
    }

    private void addData(Search search, TableLayout layout, ArrayList<TableRow> tr,ArrayList<String[]> dataList, CharSequence filter) {
        if(!dataList.isEmpty()){
            float textSize = 18;
            int filterLength = filter.length();
            String restriction = (filter.toString()).toLowerCase();
            int index = 0;

            for (i = 0; i < dataList.size(); i++ ){
                //prevent some data from being added
                String entry1;
                String entry2;
                try{
                    entry1 = (dataList.get(i)[2].substring(0,filterLength)).toLowerCase();
                    entry2 = (dataList.get(i)[8].substring(0,filterLength)).toLowerCase();

                }catch (StringIndexOutOfBoundsException e){
                    entry1 = dataList.get(i)[2].toLowerCase();
                    entry2 = dataList.get(i)[8].toLowerCase();
                    System.out.println("INDEX OOB");
                }


                System.out.println(!(entry1.contains(restriction) || entry2.contains(restriction)));
                System.out.println(!restriction.contains(" "));
                System.out.println(!(restriction.equalsIgnoreCase(dataList.get(i)[0])));
                System.out.println(" ");
                if(!(entry1.contains(restriction) || entry2.contains(restriction)) && !restriction.contains(" ") && !(restriction.equalsIgnoreCase(dataList.get(i)[0]))){
                    continue;
                }
                else{
                    tr.add(index,new TableRow(search));
                    if(index % 2 == 1){
                        tr.get(index).setBackgroundColor(Color.GRAY);
                    }
                    tr.get(index).setMinimumHeight(120);
                    layout.addView(tr.get(index));
                    TextView type = new TextView(search);
                    type.setText(dataList.get(i)[0]);
                    type.setGravity(Gravity.CENTER);
                    if(dataList.get(i)[0].equals("Boat")){
                        type.setTextColor(Color.parseColor("#006600"));
                    }
                    else{
                        type.setTextColor(Color.parseColor("#000099"));
                    }

                    type.setTextSize(textSize);


                    TextView boat = new TextView(search);
                    boat.setText(dataList.get(i)[2]);
                    boat.setTextSize(textSize );
                    boat.setGravity(Gravity.CENTER);
                    boat.setTextColor(Color.BLACK);

                    TextView name = new TextView(search);
                    name.setText(dataList.get(i)[8]);
                    name.setGravity(Gravity.CENTER);
                    name.setTextColor(Color.BLACK);
                    name.setTextSize(textSize);

                    tr.get(index).addView(type,0);
                    tr.get(index).addView(boat,1);
                    tr.get(index).addView(name,2);

                    final AlertDialog.Builder trAlert = new AlertDialog.Builder(search).setMessage(alertMessage(dataList.get(i)[0],
                            dataList.get(i)[1],dataList.get(i)[2],dataList.get(i)[3],dataList.get(i)[4],
                            dataList.get(i)[5],dataList.get(i)[6],dataList.get(i)[7],dataList.get(i)[8],
                            dataList.get(i)[9]))
                            .setTitle("About").
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                    tr.get(index).setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            trAlert.show();
                        }
                    });

                    index++;
                }
            }
        }
    }

    private String[][] arrayListToArray(ArrayList<String[]> arrayList){
        String[][] resultingArray = new String[arrayList.size()][11];
        for (int i = 0 ; i < arrayList.size() ; i++) {
            resultingArray[i] = arrayList.get(i);
        }
        return resultingArray;
    }

    //by refreshing you pull data from the database. everything is deleted and refilled
    public void refresh(View v){
        InternetAccess internetAccess = new InternetAccess(this, progressBar, "Data Refreshed");
        internetAccess.execute("","","type","3");
        tr.clear();
        layout.removeViewsInLayout(1,layout.getChildCount() - 1);
        progressBar.setVisibility(View.VISIBLE);
        String status = "";
        int count = 0;
        while(!status.equals("[Connected, to, Database.]") && count < 30){
            try {
                status = Arrays.toString(internetAccess.get());
                Thread.sleep(100);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println(status);
        }
        dataList = SaveAndLoad.storage.searchData;
        addData(Search.this, layout, tr, dataList);
    }

    public String alertMessage(String...args){
        String[] tags = {"Requesting a", "Yacht Club: ",  "Boat:" , "Crew type:",
                "Deadline:","Phone:", "Email:","Boat Name/Number:", "Name:", "Specific Information:"};
        String[] altTags = {"Requesting a", "Yacht Club:",  "Preferred Boat:" , "Crew type:",
                "Deadline:","Phone:", "Email:","Boat Name/Number:", "Name:","Specific Information:"};

        String message = "";
        if (args[0].equals("Crew")){
            message += tags[0] + " " + args[0] + "\n";
            message += tags[1] + " " + args[1] + "\n";
            message += tags[2] + " " + args[2] + "\n";
            message += tags[7] + " " + args[7] + "\n";
            message += tags[3] + " " + args[3] + "\n";
            message += tags[4] + " " + args[4] + "\n";
            message += tags[5] + " " + args[5] + "\n";
            message += tags[6] + " " + args[6] + "\n";
            message += tags[8] + " " + args[8] + "\n";
            message += tags[9] + " " + args[9] + "\n";
        }
        else{
            message += altTags[0] + " " + args[0] + "\n";
            //message += altTags[1] + " " + args[1] + "\n";
            message += altTags[2] + " " + args[2] + "\n";
            //message += altTags[7] + " " + args[7] + "\n";
            //message += altTags[3] + " " + args[3] + "\n";
            message += altTags[4] + " " + args[4] + "\n";
            message += altTags[5] + " " + args[5] + "\n";
            message += altTags[6] + " " + args[6] + "\n";
            message += altTags[8] + " " + args[8] + "\n";
            message += altTags[9] + " " + args[9] + "\n";
        }
        return message;
    }

}


