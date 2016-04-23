package jp.kddilabs.vitalsense;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static public Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.StartButton);
        btn.setOnClickListener(btnListener);//リスナの登録

        btn  = (Button) findViewById(R.id.StopButton);
        btn.setOnClickListener(btnListener);//リスナの登録

        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                String val = msg.getData().getString("value");
                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText(getNowDate()+"   "+val);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch(v.getId()){
                case R.id.StartButton://startServiceでサービスを起動
                    startService(new Intent(MainActivity.this, MyService.class));
                    break;
                case R.id.StopButton://stopServiceでサービスの終了
                    stopService(new Intent(MainActivity.this, MyService.class));
                    break;
            }
        }
    };

    public static String getNowDate(){
        final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
