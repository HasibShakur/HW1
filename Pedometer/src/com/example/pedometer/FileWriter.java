package com.example.pedometer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.example.pedometer.AccData;

class AsyncFileWriter implements Runnable {
    private final File file;
    private final PrintWriter out;
    private final BlockingQueue<AccData> queue = new LinkedBlockingQueue<AccData>();
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public AsyncFileWriter(File file) throws IOException {
        this.file = file;
		this.out = new PrintWriter(new FileOutputStream(file), true);
    }

    public boolean append(AccData cur) {
        if (!started) {
            throw new IllegalStateException("open() call expected before append()");
        }
       
        return  queue.offer(cur);
    }

    public void open() {
        this.started = true;
        new Thread(this).start();
    }

    public void run() {
        while (!stopped) {
            try {
                AccData item = queue.take();
                if (item != null) {
                    for (int i = 0; i<20 ; i++ ) {
						StringBuilder sb = new StringBuilder();
						long time = System.nanoTime();
						String string_time = Long.toString(time);
						
						String s0 = Float.toString(item.acc_x[i]);
						String s1 = Float.toString(item.acc_y[i]);
						String s2 = Float.toString(item.acc_z[i]);
					
						sb.append(string_time);
						sb.append(",");
						sb.append(s0);
						sb.append(",");
						sb.append(s1);
						sb.append(",");
						sb.append(s2);
						sb.append("\n");
          
						out.println(sb.toString());
					} 
                }
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
        out.close();
    }

    public void close() {
        this.stopped = true;
    }
}


