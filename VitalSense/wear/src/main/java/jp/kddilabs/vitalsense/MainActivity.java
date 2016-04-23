package jp.kddilabs.vitalsense;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
                mSensorManager.registerListener(mHeartListener, mHeartRateSensor, 3);

                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(mHeartRateSensor.getName());
            }
        });
    }
    /*
    @Override
    public void onSensorChanged(SensorEvent event) {
        mTextView.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/

    final SensorEventListener mHeartListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mTextView.setText(String.valueOf(event.values[0]));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Log.d(TAG, "onAccuracyChanged : " + accuracy);
        }
    };

}
