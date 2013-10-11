/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.pedometer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 */
public class StepDetector implements SensorEventListener
{
    public File f;
    public AsyncFileWriter w;
    public AccData cur;
    
    public File f_gyro;
    public AsyncFileWriterGyro w_gyro;
    public GyroData cur_gyro;

    
    public ArrayBlockingQueue<AccData> objects = new ArrayBlockingQueue<AccData>(20);
    public ArrayBlockingQueue<GyroData> objects_gyro = new ArrayBlockingQueue<GyroData>(20);
    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    
    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }
    
    
    
    public void onSensorChanged(SensorEvent event) {  

        Sensor sensor = event.sensor; 
        synchronized (this) {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {            
                	if (cur != null) {
	                	for (int i = 0; i < 20 ; i++) {
		                	cur.acc_x[i] = event.values[0];
		        			cur.acc_y[i] = event.values[1];
		        			cur.acc_z[i] = event.values[2];
		        			cur.acc_time[i] = System.nanoTime();
	                	}
	        			
	    				if (w.append(cur)) {
	    					cur = objects.poll();
	    					objects.offer(new AccData());
	    				} 
                	}
                } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                	if (cur_gyro != null) {
                		for (int i=0; i<20; i++) {
                			cur_gyro.gyro_x[i] = event.values[0];
                			cur_gyro.gyro_y[i] = event.values[1];
		        			cur_gyro.gyro_z[i] = event.values[2];
		        			cur_gyro.gyro_time[i] = System.nanoTime();
	                	}
	        			
	    				if (w_gyro.append(cur_gyro)) {
	    					cur_gyro = objects_gyro.poll();
	    					objects_gyro.offer(new GyroData());
	    				}
                	}
                }
            }
        }
    
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}