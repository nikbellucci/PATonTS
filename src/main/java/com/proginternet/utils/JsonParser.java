package com.proginternet.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;

public class JsonParser<T>{

    public void writeOnJson(ArrayList<T> data, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(fileName);
            gson.toJson(data, writer);
            writer.close();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<T> readOnJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<T> data = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            data = gson.fromJson(reader, ArrayList.class);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
 
        return data;
    }

}
