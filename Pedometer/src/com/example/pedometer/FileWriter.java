package com.example.pedometer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.os.Environment;
import android.util.Log;


class FileWriter implements Runnable {
   // private final Writer out;
    public final static ArrayBlockingQueue<AccData> queue = new ArrayBlockingQueue<AccData>(20);


    public void run() {
        while (true) {
            try {
                AccData item = queue.take();
            	if (item != null) {
            		File f = new File(Environment.getExternalStorageDirectory() + File.separator + "example.txt");
                    try {
            			f.createNewFile();
            			PrintWriter fo = new PrintWriter(f);
            			for (int i=0; i<=20 ; i++) {
            				String s0 = Float.toString(item.acc_x[i]);
            				String s1 = Float.toString(item.acc_y[i]);
            				String s2 = Float.toString(item.acc_z[i]);

            				java.util.Date date= new java.util.Date();
            				Timestamp timestamp = new Timestamp(date.getTime());
	            			fo.append(s0);
	            			fo.append(s1);
	            			fo.append(s2);
            			}
                    	fo.close();
            			Log.i("[TAG]", "successfully wrote to " + f);
                    } catch (Exception e) {
                    	System.out.println("Exception : " + e);
                    } 
                }
            } catch (InterruptedException e) {
            }
        }
    }
}

/*
void run() {
	while (true) {
		AccData cur = objects.take();
		[save to disk]
	}
}
*/