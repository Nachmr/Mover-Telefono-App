/**
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.nacho.practicafinalnpi;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private MediaPlayer mp;
    private boolean reset;

    /**
     * Creación de la actividad e inicialización de componentes
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mp = new MediaPlayer();

        reset = false;
    }

    /**
     * Se ejecuta continuamente recibiendo información de los sensores,
     * cuando el movil esta boca abajo suena y no vuelve a sonar si
     * no lo hemos vuelto a poner boca arriba
     * @param event
     */
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xVal = event.values[0];
            float yVal = event.values[1];
            float zVal = event.values[2];

            if(xVal > -1.0 && xVal < 1.0){
                if(yVal > -1.0 && yVal < 1.0){
                    if(zVal < -9.0 && zVal > -11.0){
                        if(reset){
                            reset = false;
                            mp.reset();
                            try {
                                AssetFileDescriptor afd;
                                afd = getAssets().openFd("01-cha-la-head-cha-la-tv-size RECORTADA.mp3");
                                mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                                mp.prepare();
                                mp.start();

                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if(xVal > -1.0 && xVal < 1.0){
                if(yVal > -1.0 && yVal < 1.0){
                    if(zVal > 9.0 && zVal < 11.0){
                        reset = true;
                    }
                }
            }
        }
    }

    /**
     * Método de creación automática
     * @param sensor
     * @param accuracy
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Activa los sensores mientras la aplicación se ejecuta
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Detiene los sensores al pausar la aplicación
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
