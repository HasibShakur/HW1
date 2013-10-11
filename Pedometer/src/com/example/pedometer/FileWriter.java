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

import android.os.Environment;
import android.util.Log;

import com.example.pedometer.AccData;

class AsyncFileWriter implements Runnable {
    private final File file;
    private final PrintWriter out;
    private final BlockingQueue<AccData> queue = new LinkedBlockingQueue<AccData>();
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public AsyncFileWriter() throws IOException {
    	File f = new File(Environment.getExternalStorageDirectory() + File.separator + "trace1-data-acc.csv");
        this.file = f;
		this.out = new PrintWriter(new FileOutputStream(f), true);
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

						// Print the contents to the file
						// Format: time	 x_value  y_value  z_value \n
						out.print(item.acc_time[i]);
						out.print(",");
						out.print(item.acc_x[i]);
						out.print(",");
						out.print(item.acc_y[i]);
						out.print(",");
						out.print(item.acc_z[i]);
						out.println();
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


