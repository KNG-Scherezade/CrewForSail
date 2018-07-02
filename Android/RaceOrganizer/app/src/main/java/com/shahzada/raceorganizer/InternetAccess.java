package com.shahzada.raceorganizer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;


/**
 * Created on 30/04/2016.
 */


public class InternetAccess extends AsyncTask<String,String,String[]> {
    public enum RequestType {
        DELETE(0), TRANSMITCREW(1), TRANSMITBOAT(2), RECIEVE(3), MODIFY(4), CONNECT(5);
        public final int value;

        RequestType(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }

    }

    static boolean connected = false;
    int type;
    Context context;
    AlertDialog alertDialog;
    ProgressBar progressBar;
    String refreshMsg;

    public InternetAccess() {
    }
    public InternetAccess(Context ctx, ProgressBar progressBar) {
        this.context = ctx;
        this.progressBar = progressBar;
    }
    public InternetAccess(Context ctx, ProgressBar progressBar, String refreshMessage) {
        this.context = ctx;
        this.progressBar = progressBar;
        refreshMsg = refreshMessage;
    }

    @Override
    protected void onPreExecute() {
        if(context != null){
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("login status");
        }
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /*      KEY = VALUE             name1=value1&name2=value2
        0 = "ID";
        1 = ID
        2 = "type";
        3 = TYPE
        4 = "club";
        5 = yachtClub;
        6 = "boat";
        7 = BOATTYPE
        8= "crew";
        9 = crew;
        10 = "deadline";
        11= deadline;
        12= "phone";
        13 = phone;
        14= "mail";
        15 = mail;
        16= "boatName";
        17= boatName;
        18 = "name";
        19 = name;
        20 = "delete";
        21 = 0;
    */
    protected String[] doInBackground(String... args) {
        type = Integer.parseInt(args[3]);
        int numberOfStatements = 13;
        String HTTPSlogin_url = "https://70.83.102.219:443/relay.php";
        String login_url = "http://70.83.102.219:9002/relay.php";
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = null;
        OutputStream outputStream = null;
        HttpURLConnection conn = null;
        BufferedWriter bufferedWriter = null;
        try {
            url = new URL(login_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            outputStream = conn.getOutputStream();
            conn.setConnectTimeout(500);
            conn.setReadTimeout(500);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        } catch (MalformedURLException e) {
            System.out.println("err1: MUE");
            e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("err2: PE");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("err3: IOE");
            e.printStackTrace();
        }

        //builds url object and creates http request to post data. forloop creates the encoded string. whileloop creates response
        if (type == RequestType.TRANSMITBOAT.getValue() || type == RequestType.TRANSMITCREW.getValue()) {
            //0 indexed
            try {
                String post_data = "";
                int count = 0;
                for (String argument : args) {
                    if((argument.trim()).equals("")){
                        argument = "N/A";
                    }
                    if (++count % 2 == 1)
                        post_data += URLEncoder.encode(argument, "UTF-8") + "=";
                    else {
                        post_data += URLEncoder.encode(argument, "UTF-8");
                        if (--numberOfStatements != 0) {
                            post_data += "&";
                        }
                    }
                }
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                System.out.println("Transmited: " + post_data);

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;

                line = bufferedReader.readLine();
                result += line;

                System.out.println("Recieved: " + result);
                bufferedReader.close();
                inputStream.close();

                conn.disconnect();
                connected = true;
                return (result).split(" ");
            } catch (MalformedURLException e) {
                System.out.println("err4: MUE");
                return ("MalformedURLException").split(" ");
            } catch (IOException e) {
                System.out.println("err5: IOE");
                return ("IOException").split(" ");
            }
        } else if (type == RequestType.RECIEVE.getValue()) {
            try {
                String post_data =URLEncoder.encode("a", "UTF-8") + "=" + URLEncoder.encode("b", "UTF-8") + "&" + URLEncoder.encode(args[2], "UTF-8") + "=" + URLEncoder.encode(args[3], "UTF-8");
                bufferedWriter.write(post_data);
                System.out.println("Transmitted to Relay: " + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String[] result;
                String line;
                int counter = -1;
                SaveAndLoad.storage.searchData.clear();
                while ((line = bufferedReader.readLine()) != null) {
                    String minusHTML = line.replaceAll("<br />","");
                    if (minusHTML.replaceAll(" ", "") != ""){
                        System.out.println("Received: " +  minusHTML );
                        result = minusHTML.split(" ");
                        int count = 0;
                        for (String entry : result){
                            result[count] = entry.replaceAll("_", " ");
                            count++;
                        }
                        if (result[0].equals("0") || result[0].equals("Need Crew")){
                            result[0] = "Crew";
                        }
                        else{
                            result[0] = "Boat";
                        }
                        SaveAndLoad.storage.searchData.add(result);
                    }
                    counter++;
                }
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                connected = true;
                publishProgress("Fin");
                return "Connected to Database.".split(" ");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //for all intents and purposes, identical to the first if statement
        else if (type % 10 == RequestType.MODIFY.getValue()) {
            //0 indexed
            try {
                String post_data = "";
                int count = 0;
                for (String argument : args) {
                    if((argument.trim()).equals("")){
                        argument = "N/A";
                    }
                    if (++count % 2 == 1)
                        post_data += URLEncoder.encode(argument, "UTF-8") + "=";
                    else {
                        post_data += URLEncoder.encode(argument, "UTF-8");
                        if (--numberOfStatements != 0) {
                            post_data += "&";
                        }
                    }
                }
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                System.out.println("Transmited: " + post_data);

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                System.out.println("Recieved: " + result);
                bufferedReader.close();
                inputStream.close();

                conn.disconnect();
                connected = true;
                return (result).split(" ");
            } catch (MalformedURLException e) {
                System.out.println("err4: MUE");
                return ("MalformedURLException").split(" ");
            } catch (IOException e) {
                System.out.println("err5: IOE");
                return ("IOException").split(" ");
            }
        }
        else if(type == RequestType.DELETE.getValue()){
            //0 indexed
            try {
                String post_data = "";
                int count = 0;
                for (String argument : args) {
                    if((argument.trim()).equals("")){
                        argument = "N/A";
                    }
                    if (++count % 2 == 1)
                        post_data += URLEncoder.encode(argument, "UTF-8") + "=";
                    else {
                        post_data += URLEncoder.encode(argument, "UTF-8");
                        if (--numberOfStatements != 0) {
                            post_data += "&";
                        }
                    }
                }
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                System.out.println("Transmited: " + post_data);

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                System.out.println("Recieved: " + result);
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                connected = true;
                return ("Connection Succeeded").split(" ");
            } catch (MalformedURLException e) {
                System.out.println("err4: MUE");
                return ("MalformedURLException").split(" ");
            } catch (IOException e) {
                System.out.println("err5: IOE");
                return ("IOException").split(" ");
            }
        }
        else if(type == RequestType.CONNECT.getValue()){
            try {
                String post_data = URLEncoder.encode(args[2], "UTF-8") + "=" + URLEncoder.encode(args[3], "UTF-8");
                bufferedWriter.write(post_data);
                System.out.println("Transmitted to Relay: " + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                bufferedReader.close();
                inputStream.close();
                conn.disconnect();
                connected = true;
                return "Connected to Database.".split(" ");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connected = false;
        return ("Connection failed").split(" ");
    }

    protected void onPostExecute(String[] result) {
        if(type != RequestType.TRANSMITBOAT.getValue() && type != RequestType.TRANSMITCREW.getValue()){
            String message = Arrays.toString(result);
            message = message.replace("[","");
            message = message.replace(",","");
            message = message.replace("]","");
            if(refreshMsg != null){
                alertDialog.setMessage(refreshMsg);
                alertDialog.setTitle("");
                alertDialog.show();
            }
            if(context != null && refreshMsg == null){
                alertDialog.setMessage(message);
                alertDialog.show();
            }
            if(progressBar != null){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}



