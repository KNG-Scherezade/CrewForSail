package com.shahzada.raceorganizer;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Storage implements Serializable{
    private static final long serialVersionUID = 124;

    public ArrayList<String[]> searchData = new ArrayList<String[]>();
    public String [] data = new String[14];
    public ArrayList<String[]> storageData = new ArrayList<String[]>();
    public String[] fileNames = new String[3];
    public long ID;
    String[] stuff = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
    String[] oStuff = {"a","b","c"};

    public Storage(long ID){
        this.ID = ID;
        data = stuff;
        fileNames = oStuff;
    }

    public Storage(Storage storage){
        this.storageData = storage.storageData;
        this.data = stuff;
        this.searchData = storage.searchData;
        this.ID = storage.ID;
        fileNames = oStuff;
    }

    public void addStorageData(String[] storage) throws IOException{
        storageData.add(storage);
    }
    public void setStorageData(ArrayList<String[]> storageData) {
        this.storageData = storageData;
    }
    public void setSearchData(String[] data){
        this.data = data;
    }
    public void setID(long id){
        this.ID = id;
    }
    public String toString(){
        if(data == null){
            return "ID: " + ID + "\tStorage Length: " + storageData.size() + "\tdata reference: NULL";
        }
        return storageData.toString();
     }
    public void clear(){
        storageData = new ArrayList<String[]>();
    }
}
