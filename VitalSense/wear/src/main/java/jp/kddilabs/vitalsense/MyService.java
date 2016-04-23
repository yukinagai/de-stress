package jp.kddilabs.vitalsense;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    private final String TAG = "Wear";
    private Service mService;
    private String DevName = "Moto";
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mService = this;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //DevName = Build.MODEL;
        //DevName = DevName.replaceAll(" ", "");

        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(mHeartListener, mHeartRateSensor, 300000);
        return START_STICKY;
    }

    public void onStop() {
        mSensorManager.unregisterListener(mHeartListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    final SensorEventListener mHeartListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            String valStr = String.valueOf(event.values[0]);
            valStr = DevName+"/heartrate/"+valStr;
            sendMessage(mService, valStr);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //Log.d(TAG, "onAccuracyChanged : " + accuracy);
        }
    };

    public void sendMessage(final Context context, final String valStr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GoogleApiClient client = new GoogleApiClient.Builder(context)
                        .addApi(Wearable.API)
                        .build();
                client.blockingConnect(100, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    client.blockingConnect(100, TimeUnit.MILLISECONDS);
                    Wearable.MessageApi.sendMessage(client, nodes.get(0).getId(), valStr, null);
                    client.disconnect();
                }
                client.disconnect();
            }
        }).start();
    }
}
