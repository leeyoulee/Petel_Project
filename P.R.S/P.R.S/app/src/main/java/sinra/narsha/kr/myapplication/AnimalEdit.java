package sinra.narsha.kr.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class AnimalEdit extends AppCompatActivity {

    EditText petName;
    EditText petType;
    EditText petAge;
    EditText petGender;
    EditText petDesc;

    public static String PET_IDX;
    public static String USER_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_edit);

        PET_IDX = getIntent().getStringExtra("PET_IDX");
        USER_NO = getIntent().getStringExtra("USER_NO");

        petName = (EditText) findViewById(R.id.petName);
        petType = (EditText) findViewById(R.id.petType);
        petAge = (EditText) findViewById(R.id.petAge);
        petGender = (EditText) findViewById(R.id.petGender);
        petDesc = (EditText) findViewById(R.id.petDesc);

        try {
            String result;
            Task_setText task_setText = new Task_setText();
            result =task_setText.execute(PET_IDX).get();

            Log.i("result 결과", result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                petName.setText(URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8"));
                petType.setText(URLDecoder.decode(jsonobject.getString("PET_TYPE"), "UTF-8"));
                petAge.setText(URLDecoder.decode(jsonobject.getString("PET_AGE"), "UTF-8"));
                petGender.setText(URLDecoder.decode(jsonobject.getString("PET_GENDER"), "UTF-8"));
                petDesc.setText(URLDecoder.decode(jsonobject.getString("PET_DESC"), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        View backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View editBtn = findViewById(R.id.editBtn);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PET_NAME = petName.getText().toString();
                String PET_TYPE = petType.getText().toString();
                String PET_AGE = petAge.getText().toString();
                String PET_GENDER = petGender.getText().toString();
                String PET_DESC = petDesc.getText().toString();

                try {
                    String Edit;
                    Task_Edit task_Edit = new Task_Edit();
                    Edit =task_Edit.execute(PET_NAME, PET_TYPE, PET_AGE, PET_GENDER, PET_DESC, PET_IDX).get();
                    Log.i("edit==", Edit);
                    if(Edit.equals("success"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalEdit.this);
                        builder.setMessage("수정하였습니다.");
                                // '확인' 버튼이 클릭되면
                                builder.setPositiveButton( "확인", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                dialog.dismiss();  // AlertDialog를 닫는다.
                                finish();
                                AnimalInfoActivity.AnimalInfoSet();
                                Animal.ListSet();
                            }
                        });
                        builder.create()
                                .show();
                    }else if (Edit.equals("null check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalEdit.this);
                        builder.setMessage("특이사항을 제외한 모든 항목을 입력해주세요.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalEdit.this);
                        builder.setMessage("동물 수정에 실패하였습니다.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    }
                } catch (Exception e) {

                }
            }
        });
    }

     class Task_setText extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animaldel_setText.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "PET_IDX="+strings[0];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }

     class Task_Edit extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animaldel_edit.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "PET_NAME="+strings[0]+"&PET_TYPE="+strings[1]+"&PET_AGE=" +strings[2]
                        +"&PET_GENDER="+strings[3] +"&PET_DESC="+strings[4] +"&PET_IDX="+strings[5];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
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
