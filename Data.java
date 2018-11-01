package com.example.joeym.playground;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Data {
    public static void putData(String name, String data) {
        try {
            PrintWriter p = new PrintWriter(new FileWriter(name));
            p.write(data);
            p.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getData(String name, String defaultData) {
        String data = null;
        try {
            BufferedReader b = new BufferedReader(new FileReader(name));
            data = "";
            while (b.ready()) {
                data += b.readLine();
            }
            b.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException f) {
            f.printStackTrace();
        }
        if (data == null) {
            putData(name, defaultData);
            return defaultData;
        }
        return data;
    }
}
