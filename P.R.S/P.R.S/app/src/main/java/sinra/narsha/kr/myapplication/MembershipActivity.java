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

public class MembershipActivity extends AppCompatActivity {

    EditText userName;
    EditText userID;
    EditText userPW;
    EditText userAdd;
    EditText userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        userName = (EditText) findViewById(R.id.userName);
        userID = (EditText) findViewById(R.id.userID);
        userPW = (EditText) findViewById(R.id.userPW);
        userAdd = (EditText) findViewById(R.id.userAdd);
        userPhone = (EditText) findViewById(R.id.userPhone);

        Button joinbtn = findViewById(R.id.join);
        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = userName.getText().toString();
                String id = userID.getText().toString();
                String pw = userPW.getText().toString();
                String add = userAdd.getText().toString();
                String phone = userPhone.getText().toString();

                try {
                    String result;
                    Task task = new Task();
                    result =task.execute(name,id,pw,add,phone).get();
                    Log.v("result","result==="+result);
                    if(result.equals("success")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("회원가입에 성공하였습니다.");
                        // '확인' 버튼이 클릭되면
                        builder.setPositiveButton( "확인", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick( DialogInterface dialog, int which )
                            {
                                dialog.dismiss();  // AlertDialog를 닫는다.
                                finish();
                            }
                        });
                        builder.create()
                                .show();
                    } else if(result.equals("null check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("모든항목을 입력해주세요.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else if(result.equals("id overlap check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("이미 존재하는 아이디입니다.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else if(result.equals("id check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("아이디는 공백이나 특수문자를 입력할 수 없습니다.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else if(result.equals("pw check")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("비밀번호는 6자이상 20자이내," + "\n" +"영어숫자를 혼합하여 입력하여주세요.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    } else if(result.equals("unsuccess")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MembershipActivity.this);
                        builder.setMessage("회원가입에 실패하였습니다.")
                                .setPositiveButton("확인",null)
                                .create()
                                .show();
                    }
                } catch (Exception e) {

                }
            }
        });

        Button cancelbtn = findViewById(R.id.cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_register.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "name="+strings[0]+"&id="+strings[1]+"&pw="+strings[2]
                        +"&add="+strings[3] +"&phone="+strings[4];
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