package sinra.narsha.kr.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class ShopInfoActivity extends AppCompatActivity {

    public static String MARKET_NO;
    public static String USER_NO;

    Bitmap bmImg;
    ImageView marketImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        MARKET_NO = getIntent().getStringExtra("MARKET_NO");
        USER_NO = getIntent().getStringExtra("USER_NO");

        marketImg = findViewById(R.id.marketImg);
        TextView marketName = findViewById(R.id.marketName);
        TextView marketDesc = findViewById(R.id.marketDesc);
        TextView marketPhone = findViewById(R.id.marketPhone);
        TextView marketAdd = findViewById(R.id.marketAdd);
        TextView marketService = findViewById(R.id.marketService);
        TextView marketPets = findViewById(R.id.marketPets);


        try {
            String result;
            Task task = new Task();
            result = task.execute(MARKET_NO).get();
            JSONArray jsonArray = new JSONArray(result);
            Log.i("결과", " " + result);
            for (int i = 0; i < jsonArray.length(); i++) {
                 JSONObject jsonobject = jsonArray.getJSONObject(i);

                String Img = URLDecoder.decode(jsonobject.getString("PMF.STORE_FILE_NAME"), "UTF-8");
                //final String Img = jsonobject.getString("PMF.STORE_FILE_NAME");
                /*InputStream inputStream = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        Img.toString();
                        return 0;
                    }
                };

                http://192.168.55.59:8080/file/5829f4e0f6494402ba2264956bbac689.jpg

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                marketimg.setImageBitmap(bitmap);*/
                Log.i("Img == ", " " + Img);

                ShopImg ShopImgtask = new ShopImg();
                ShopImgtask.execute("http://192.168.55.59:8080/file/"+Img);

                marketName.setText(URLDecoder.decode(jsonobject.getString("PR.MARKET_NAME"), "UTF-8"));
                marketDesc.setText(URLDecoder.decode(jsonobject.getString("PR.MARKET_DESC"), "UTF-8"));
                marketPhone.setText(URLDecoder.decode(jsonobject.getString("PM.USER_PHONE"), "UTF-8"));
                marketAdd.setText(URLDecoder.decode(jsonobject.getString("PR.MARKET_ADDR1"), "UTF-8"));
                marketService.setText(URLDecoder.decode(jsonobject.getString("PR.SERVICE"), "UTF-8"));
                marketPets.setText(URLDecoder.decode(jsonobject.getString("PR.PETS"), "UTF-8"));

                Log.i("결과", " " + MARKET_NO);
            }

            Log.i("리턴 값 : ", result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        View backbtn = findViewById(R.id.back);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View rsvBtn = findViewById(R.id.rsvBtn);

        rsvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReservationActivity.class);
                intent.putExtra("MARKET_NO",MARKET_NO);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });
    }

    class ShopImg extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                bmImg = BitmapFactory.decodeStream(is);
            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }
        protected void onPostExecute(Bitmap img){
            marketImg.setImageBitmap(bmImg);
        }
    }

    class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_shopinfo.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "MARKET_NO=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }
}
