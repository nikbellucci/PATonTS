package com.proginternet.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;


public class JsonParser<T> {

    public void writeOnJson(String filename, ArrayList<T> data) {
        T[] array = this.toArray(data);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter writer = new FileWriter(filename);
            gson.toJson(data, writer);
            writer.close();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<T> readOnJson(String filename, Class model) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        T[] data = null;
        try {
            JsonReader reader = new JsonReader(new FileReader(filename));
            data = gson.fromJson(reader, model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<T> tmp = Arrays.asList(data);
        return new ArrayList<T>(tmp);
    }

    private <T> T[] toArray(List<T> list) {
        T[] toR = (T[]) java.lang.reflect.Array.newInstance(list.get(0).getClass(), list.size());
        for (int i = 0; i < list.size(); i++) {
            toR[i] = list.get(i);
        }
        return toR;
    }

}
