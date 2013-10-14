package com.example.pedometer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Environment;

class AsyncFileWriterGyro implements Runnable {
    private final PrintWriter out;
    private final BlockingQueue<GyroData> queue = new LinkedBlockingQueue<GyroData>();
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public AsyncFileWriterGyro() throws IOException {
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "trace-data-gyro.csv");
        this.out = new PrintWriter(new FileOutputStream(f), true);
    }
    
    
	public boolean append(GyroData cur_gyro) {
		if (!started) {
			throw new IllegalStateException("open() call expected before append()");
		}
		return queue.offer(cur_gyro);
	}

    public void open() {
        this.started = true;
        new Thread(this).start();
    }

    public void run() {
        while (!stopped) {
            try {            	
            	GyroData item_gyro = queue.take();
                if (item_gyro != null) {
                	for (int i = 0; i< 20 ; i++ ) {
						// Print the contents to the file
						// Format: time	 x_value  y_value  z_value \n
						out.print(item_gyro.gyro_time[i]);
						out.print(",");
						out.print(item_gyro.gyro_x[i]);
						out.print(",");
						out.print(item_gyro.gyro_y[i]);
						out.print(",");
						out.print(item_gyro.gyro_z[i]);
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


