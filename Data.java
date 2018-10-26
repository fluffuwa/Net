package com.example.joeym.playground;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Data {
    public static void putData(String name, String data) {
        try {
            PrintWriter p = new PrintWriter(new FileWriter(name));
            p.write(data);
            p.close();
        } catch (Exception e) {
        }
    }

    public static String getData(String name) {
        try {
            BufferedReader b = new BufferedReader(new FileReader(name));
            String temp = "";
            while (b.ready()) {
                temp += b.readLine();
            }
            b.close();
            return temp;
        } catch (Exception e) {
        }
        return null;
    }

    public static String getData(String name, String defaultData) {
        String data = getData(name);
        if (data == null)
            return defaultData;
        return data;
    }
}
