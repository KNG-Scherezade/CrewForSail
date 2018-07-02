package com.shahzada.raceorganizer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

public class BoatSelection extends AppCompatActivity implements Serializable {

    EditText other;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat_selection);

        other = (EditText) findViewById(R.id.boatSelect);

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void confirm(View v){
        next(null);
    }
    public void etchels(View v){
        next("Etchels");
    }
    public void shark(View v){
        next("Shark");
    }
    public void tanzer22(View v){
        next("Tanzer22");
    }
    public void tanzer26(View v){
        next("Tanzer26");
    }
    public void fireball(View v){
        next("Fireball");
    }

    public void next(String type){
        Intent iManager = new Intent(this, Manager.class);
        String boatType;
        if (type == null){
            boatType = other.getText().toString();
            if (boatType == ""){
                return;
            }
        }
        else{
            boatType = type;
        }
        boolean success = transferData(boatType);
        if (success){
            Crew.data = new String[14];
            File warehouse = new File(getFilesDir() + File.separator + "Warehouse.bin");
            SaveAndLoad.write(warehouse);
            finish();
            startActivity(iManager);
        }

    }

    //DATA TRANSFER
    public boolean transferData(String boatType){
        String errLog = "";
        AlertDialog error = new AlertDialog.Builder(this).create();

        if (boatType.replaceAll(" ", "").equals("")){
            error.setTitle("Some information was not entered");
            error.setMessage("Boat type was not entered");
            error.show();
        }
        else{
            Crew.data[0] = "" + SaveAndLoad.storage.ID;
            Crew.data[3] = boatType.replace(" ", "_");
            try {
                errLog = "create Object";
                File transferFile = new File(getCacheDir() + File.separator + "TransferData.txt");
                errLog = "create file";
                transferFile.createNewFile();
                errLog = "create FIS";
                FileOutputStream fileOutputStream = new FileOutputStream(transferFile);
                errLog = "create Printwriter";
                PrintWriter printWriter = new PrintWriter(fileOutputStream);
                int count = 0;
                for (String data : Crew.data) {
                    count++;
                    errLog = "creating line " + count;
                    printWriter.println(data);
                }

                //tag headings
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar4);
                progressBar.setVisibility(View.VISIBLE);
                InternetAccess internetAccess = new InternetAccess(this, progressBar);
                internetAccess.execute("ID", Crew.data[0], "type", Crew.data[1], "club", Crew.data[2], "boat", Crew.data[3],
                        "crew", Crew.data[4], "deadline", Crew.data[5], "phone", Crew.data[6], "mail", Crew.data[7], "boatName", Crew.data[8]
                        , "name", Crew.data[9], "delete", Crew.data[10], "listing", Crew.data[11], "specific", Crew.data[13]);
                try {
                    errLog = "Writing to log";
                    if (!getIntent().getExtras().getBoolean("state") == true) {
                        SaveAndLoad.storage.addStorageData(Crew.data);
                    }
                    else{
                        int index = getIntent().getExtras().getInt("indexVal");
                        SaveAndLoad.storage.addStorageData(Crew.data);
                        SaveAndLoad.storage.storageData.remove(index);
                    }
                    //STATEMENT STREAM ENDS
                    return true;
                } catch (Exception e) {
                    error.setMessage("Error7 - Error storing data, can not progress\nDebug info:\nBroke at " + errLog);
                    error.show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public void onStart() {
        super.onStart();
        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);
    }

}
