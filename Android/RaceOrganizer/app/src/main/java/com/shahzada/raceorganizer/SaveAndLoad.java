package com.shahzada.raceorganizer;

import android.app.IntentService;
import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Scherezade on 5/5/2016.
 */

public class SaveAndLoad extends IntentService implements Serializable {
    public static Storage storage;
    private static final long serialVersionUID = 125;
    String TAG = "com.shahzada.raceorganizer";
    public static boolean run = false;


    public SaveAndLoad(String name, boolean run) {
        super(name);
        this.run = run;
    }
    public SaveAndLoad(){
        this("", false);
    }

    @Override
    protected void onHandleIntent(Intent workIntent)  {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();

        long ID;
        String fileString = getFilesDir() + File.separator + "Warehouse.bin";
        File warehouse = new File(fileString);

        //warehouse.delete();
        Random idCreator = new Random();
        try {
            Thread.sleep(250);
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(warehouse));
            storage = (Storage) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println("--------------->Read");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("XXXXXXXFailurexXXXX-->Read");
        }

        if(!warehouse.exists() || storage == null || storage.ID == 0){
            //0.8 Create data
            ID = Math.abs(idCreator.nextLong());
            try {
                warehouse.createNewFile();
                System.out.println("<-------Create-------->");
                storage = new Storage(ID);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("XXFailureXX --Create-- XXFailureX");
            }

        }
        storage.fileNames[0] = fileString;
        String errLog ="";
        do{
            write(warehouse);
        }while(run);
    }

    public static void write(File warehouse){
        try {
            String errLog;
            errLog = "OOS (FOS (WH))";
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(warehouse));
            errLog = "write obj";
            objectOutputStream.writeObject(storage);
            System.out.println("Write<---------------");
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Write<---XXXXXXXFailureXXXXX----   :");
        }
    }



    public void onDestroy(){
        super.onDestroy();
        System.out.println("Destroy");
    }


}