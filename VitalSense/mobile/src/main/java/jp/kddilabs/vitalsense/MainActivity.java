package jp.kddilabs.vitalsense;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.StartButton);
        btn.setOnClickListener(btnListener);//リスナの登録

        btn  = (Button) findViewById(R.id.StopButton);
        btn.setOnClickListener(btnListener);//リスナの登録
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

}
