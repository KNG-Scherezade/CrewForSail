package com.shahzada.raceorganizer;
/**VER 0.50 BETA*/

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;


/**THINGS TO ADD
 *
 * 0.80download files in background         TICK
 * 0.80advertisement at bottom
 * 0.80text storage                         TICK
 * 0.90notification system
 * 0.90airplane mode on off
 * 1.00location system
 *
 * */


public class Home extends AppCompatActivity implements Serializable {


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertDialog.Builder warning = new AlertDialog.Builder(Home.this);
        warning.setMessage("You are not connected to the database\nTry loading the app again.");

        //Create display items and item logic
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //AdView mAdView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //mAdView.loadAd(adRequest);

        final Intent iCreate = new Intent(this,CreateListing.class);
        Button createListing = (Button) findViewById(R.id.create);
        createListing.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(!InternetAccess.connected){
                            warning.show();
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);
                            startActivity(iCreate);
                        }
                    }
                }
        );

        final Intent iManage = new Intent(this,Manager.class);

        Button manageListing = (Button) findViewById(R.id.manage);
        manageListing.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        if(!InternetAccess.connected){
                            warning.show();
                        }
                        else{
                            progressBar.setVisibility(View.VISIBLE);
                            startActivity(iManage);
                        }

                    }
                }
        );

        final Intent iSearch = new Intent(this,Search.class);
        Button searchListing = (Button) findViewById(R.id.search);
        searchListing.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        if(!InternetAccess.connected){
                            warning.show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            startActivity(iSearch);
                        }

                    }
                }
        );
    }
    public void onStart() {
        super.onStart();
        //Start file saving
        //and writing data to file
        Intent fileSave = new Intent(this, SaveAndLoad.class);
        startService(fileSave);

        if(airplaneModeIsOn(this)){
            //http://developer.android.com/guide/topics/ui/dialogs.html
            AlertDialog.Builder airplaneModeCheck = new AlertDialog.Builder(this);
            airplaneModeCheck.setMessage("Airplane mode must be disabled for this app to work.")
                    .setPositiveButton("Exit program ?", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            System.exit(0);
                        }
                    });
            airplaneModeCheck.show();
            return;
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(!InternetAccess.connected){
            InternetAccess internetAccess = new InternetAccess(this, progressBar);
            internetAccess.execute("", "", "type","5");
        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final Intent iSettings = new Intent(this,Settings.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            builder.setMessage("Maybe I'll add this in an update...");
            builder.show();
            //startActivity(iSettings);
            return true;
        }
        else if (id == R.id.action_about){
            builder.setMessage("Release version: Beta 0.90").setTitle("About");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if (id == R.id.action_clear){
            builder.setMessage("Cleared").setTitle("Clear");
            AlertDialog dialog = builder.create();

            File boatSettings = new File(SaveAndLoad.storage.fileNames[1]);
            if(boatSettings.exists()){
                boatSettings.delete();
                System.out.println("2");
            }
            File crewSettings = new File(SaveAndLoad.storage.fileNames[2]);
            if(crewSettings.exists()){
                crewSettings.delete();
                System.out.println("3");
            }
            for (int i = 0 ; i < 3 ; i++){
                System.out.println(SaveAndLoad.storage.fileNames[i]);
            }
            SaveAndLoad.storage.clear();
            SaveAndLoad.write(new File(SaveAndLoad.storage.fileNames[0]));
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    //http://stackoverflow.com/questions/4319212/how-can-one-detect-airplane-mode-on-android
    private static boolean airplaneModeIsOn(Context context) {
        return Global.getInt(context.getContentResolver(), Global.AIRPLANE_MODE_ON, 0) != 0;
    }




    private final String COMMAND_FLIGHT_MODE_1 = "settings put global airplane_mode_on";
    private final String COMMAND_FLIGHT_MODE_2 = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state";

    //http://stackoverflow.com/questions/3249245/how-to-set-the-airplane-mode-on-to-true-or-on
    public void setFlightMode(Context context) {
// Toggle airplane mode.
        Global.putInt(
                context.getContentResolver(),
                Global.AIRPLANE_MODE_ON, 0);

// Post an intent to reload.
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", 1);
        sendBroadcast(intent);
        }

    public void onPause(){
        super.onPause();
        SaveAndLoad.run = false;
        System.out.println("pause");
    }


}
