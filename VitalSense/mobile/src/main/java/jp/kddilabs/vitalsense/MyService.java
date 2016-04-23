package jp.kddilabs.vitalsense;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MyService extends WearableListenerService {
    private final String TAG = "WearMobileService";
    private String Server_UrlBase = "http://157.7.242.70/vital/";
    private boolean startFlag = false;

    public MyService() {
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //setup_httpclient();
        startFlag = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        startFlag = false;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (! startFlag) return;
        //Log.d(TAG,"onMessageRecieved");
        if (MainActivity.mHandler != null) {
            Message msg = Message.obtain(MainActivity.mHandler, 1);
            Bundle data = new Bundle();
            data.putString("value", messageEvent.getPath());
            msg.setData(data);
            msg.sendToTarget();
        }
        NetAccess valSend = new NetAccess();
        valSend.execute(messageEvent.getPath());
        //showToast(messageEvent.getPath());
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public class NetAccess extends AsyncTask<String,Integer, Integer> {
        private DefaultHttpClient httpClient;

        @SuppressWarnings("resource")
        @Override
        protected Integer doInBackground(String... params) {
            //Log.d(TAG,"http access");
            setup_httpclient();
            HttpGet httpGet = new HttpGet(Server_UrlBase+params[0]);
            try {
                Log.d(TAG,Server_UrlBase+params[0]);
                HttpResponse response = httpClient.execute(httpGet);
                //Log.d(TAG,String.valueOf(response.getStatusLine().getStatusCode()));
                httpClient.getConnectionManager().shutdown();
            } catch (IOException e) {
                e.printStackTrace();
                setup_httpclient();
            }
            return null;
        }

        private void setup_httpclient() {
            httpClient = getHttpClient();
            HttpParams httpParams = httpClient.getParams();

            //接続確立のタイムアウトを設定（単位：ms）
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);    // 30秒
        }

    }

    /**
     * HttpClientを生成する(httpsの場合、証明書チェックをスルーする)
     * @return HttpClient
     */
    private DefaultHttpClient getHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new LowSecuritySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams p = new BasicHttpParams();
            HttpProtocolParams.setVersion(p, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(p, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(p, registry);
            return new DefaultHttpClient(ccm, p);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自己証明書エラースルー版SSLSocketFactory
     *  (http://ijoru.com/ijoru/?p=197 から拝借)
     */
    private class LowSecuritySSLSocketFactory extends SSLSocketFactory {
        private SSLContext mSslContext = SSLContext.getInstance("TLS");

        public LowSecuritySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager trustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            };

            mSslContext.init(null, new TrustManager[] { trustManager }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return mSslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return mSslContext.getSocketFactory().createSocket();
        }
    }

}
