package com.example.pedometer;

import java.io.BufferedWriter;
import java.io.File;
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
    private final Writer out;
    private final BlockingQueue<AccData> queue = new LinkedBlockingQueue<AccData>();
    private volatile boolean started = false;
    private volatile boolean stopped = false;

    public AsyncFileWriter(File file) throws IOException {
        this.file = file;
		this.out = new PrintWriter(file);

       // this.out = new BufferedWriter(new java.io.FileWriter(file));
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
                    try {
                    	for (int i = 0; i<20 ; i++ ) {
                    		StringBuilder sb = new StringBuilder();
                    		String s0 = Float.toString(item.acc_x[i]);
                			String s1 = Float.toString(item.acc_y[i]);
                			String s2 = Float.toString(item.acc_z[i]);
                    		sb.append(s0);
                    		sb.append(",");
                    		sb.append(s1);
                    		sb.append(",");
                    		sb.append(s2);
                    		sb.append("\n");
                    		

                    	    /*
                            out.append(s0);
                            out.append("\t");
                            out.append(s1);
                            out.append("\t");
                            out.append(s2);
                            out.append("\n");
                            */
                    		out.write(sb.toString());

                            Log.i("FileWriter", "successfully wrote" + s0 + s1 + s2);
                            //out.close();
                    	}
                    } catch (IOException logme) {
                    } finally {
                    	try {
							out.close();
				            Log.i("FileWriter", "successfully closed");
						} catch (IOException e) {
							e.printStackTrace();
						}
                    }
                }
            } catch (InterruptedException e) {
            }
        }
        try {
            out.close();
            Log.i("FileWriter", "successfully closed");
        } catch (IOException ignore) {
        }
    }

    public void close() {
        this.stopped = true;
    }

    private static interface AccDataInfo {
        void write(Writer out) throws IOException;
    }

    private static class CharSeqItem implements AccDataInfo {
        private final CharSequence sequence;

        public CharSeqItem(CharSequence sequence) {
            this.sequence = sequence;
        }

        public void write(Writer out) throws IOException {
            out.append(sequence);
        }
    }


}