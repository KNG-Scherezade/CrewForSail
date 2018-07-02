package com.shahzada.raceorganizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;

public class CreateListing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_listing);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Button crewSelect = (Button) findViewById(R.id.crew);
        final Intent iCrew = new Intent(this, Crew.class);

        crewSelect.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        startActivity(iCrew);
            }
        });

        Button boatSelect = (Button) findViewById(R.id.boatType);
        final Intent iBoat = new Intent(this, Boat.class);

        boatSelect.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        startActivity(iBoat);
                    }
        });
    }

    public void onStart() {
        super.onStart();
    }


}
