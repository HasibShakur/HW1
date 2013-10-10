package com.example.pedometer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


class FileWriter implements Runnable {
   // private final Writer out;
    public final static ArrayBlockingQueue<AccData> queue = new ArrayBlockingQueue<AccData>(20);


    public void run() {
        while (true) {
            try {
                //Item item = queue.poll(100, TimeUnit.MICROSECONDS);
                AccData item = queue.take();
            	if (item != null) {
                   //write to sd card
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