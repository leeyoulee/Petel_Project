package sinra.narsha.kr.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class AnimalInfoActivity extends AppCompatActivity {

    public static String PET_IDX;
    public static String USER_NO;
    public static TextView petName;
    public static TextView petType;
    public static TextView petAge;
    public static TextView petGender;
    public static TextView petDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_info);

        PET_IDX = getIntent().getStringExtra("PET_IDX");
        USER_NO = getIntent().getStringExtra("USER_NO");

        petName = findViewById(R.id.petName);
        petType = findViewById(R.id.petType);
        petAge = findViewById(R.id.petAge);
        petGender = findViewById(R.id.petGender);
        petDesc = findViewById(R.id.petDesc);


        try {
            String result;
            Task task = new Task();
            result = task.execute(PET_IDX).get();
            Log.i("리턴 PET_IDX : ", PET_IDX + " ");
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                petName.setText(URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8"));
                petType.setText(URLDecoder.decode(jsonobject.getString("PET_TYPE"), "UTF-8"));
                petAge.setText(URLDecoder.decode(jsonobject.getString("PET_AGE"), "UTF-8"));
                petGender.setText(URLDecoder.decode(jsonobject.getString("PET_GENDER"), "UTF-8"));
                petDesc.setText(URLDecoder.decode(jsonobject.getString("PET_DESC"), "UTF-8"));
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

        View animalEdit = findViewById(R.id.animalEdit);
        animalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AnimalEdit.class);
                intent.putExtra("PET_IDX",PET_IDX);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });
    }

    public static void AnimalInfoSet(){
        try {
            String result;
            Task task = new Task();
            result = task.execute(PET_IDX).get();
            Log.i("리턴 PET_IDX : ", PET_IDX + " ");
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                petName.setText(URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8"));
                petType.setText(URLDecoder.decode(jsonobject.getString("PET_TYPE"), "UTF-8"));
                petAge.setText(URLDecoder.decode(jsonobject.getString("PET_AGE"), "UTF-8"));
                petGender.setText(URLDecoder.decode(jsonobject.getString("PET_GENDER"), "UTF-8"));
                petDesc.setText(URLDecoder.decode(jsonobject.getString("PET_DESC"), "UTF-8"));
            }
            Log.i("리턴 값 : ", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animalinfo.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "PET_IDX=" + strings[0];
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
