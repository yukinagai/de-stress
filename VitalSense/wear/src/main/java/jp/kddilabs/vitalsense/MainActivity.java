package jp.kddilabs.vitalsense;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {
    private final String TAG = "Wear";
    private String DevName = "";
    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = (Activity) this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        DevName = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE).getName();

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
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
            String valStr = String.valueOf(event.values[0]);
            mTextView.setText(valStr);
            valStr = DevName+"/heartrate/"+valStr;
            sendMessage(mActivity, valStr);
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
    }}
