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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AnimalAdd extends AppCompatActivity {

    EditText petName;
    EditText petType;
    EditText petAge;
    EditText petGender;
    EditText petDesc;

    public static String USER_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_add);

        USER_NO = getIntent().getStringExtra("USER_NO");

        petName = (EditText) findViewById(R.id.petName);
        petType = (EditText) findViewById(R.id.petType);
        petAge = (EditText) findViewById(R.id.petAge);
        petGender = (EditText) findViewById(R.id.petGender);
        petDesc = (EditText) findViewById(R.id.petDesc);

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PET_NAME = petName.getText().toString();
                String PET_TYPE = petType.getText().toString();
                String PET_AGE = petAge.getText().toString();
                String PET_GENDER = petGender.getText().toString();
                String PET_DESC = petDesc.getText().toString();

                try {
                    String result;
                    Task task = new Task();
                    result =task.execute(PET_NAME, PET_TYPE, PET_AGE, PET_GENDER, PET_DESC, USER_NO).get();
                    if(result.equals("success"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalAdd.this);
                        builder.setMessage("동물 등록에 성공하였습니다.");
                        // '확인' 버튼이 클릭되면
                        builder.setPositiveButton( "확인", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                dialog.dismiss();  // AlertDialog를 닫는다.
                                finish();
                        Animal.ListSet();
                            }
                        });
                        builder.create()
                                .show();
                    } else if (result.equals("null check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalAdd.this);
                        builder.setMessage("특이사항을 제외한 모든 항목을 입력해주세요.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalAdd.this);
                        builder.setMessage("동물 등록에 실패하였습니다.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    }
                } catch (Exception e) {

                }
            }
        });

        View backbtn = findViewById(R.id.backBtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animaladd.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "PET_NAME="+strings[0]+"&PET_TYPE="+strings[1]+"&PET_AGE=" +strings[2]
                        +"&PET_GENDER="+strings[3] +"&PET_DESC="+strings[4] +"&USER_NO="+strings[5];
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
