package sinra.narsha.kr.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class Animal extends AppCompatActivity {
    private int count = 0;
    public static String USER_NO;

    public static ListView animalListView;
    public static TextView empty_text;
    private static Animal_listAdapter adapter;
    private static List<Animal_list> AnimalList;
    int selectedPos = -1;

    static String petType;
    static String petName;
    static String petAge;
    static String petGender;
    static String petIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        USER_NO = getIntent().getStringExtra("USER_NO");

        animalListView = (ListView) findViewById(R.id.animalListView);
        empty_text = (TextView) findViewById(R.id.empty_text);
        AnimalList = new ArrayList<Animal_list>();
        adapter = new Animal_listAdapter(getApplicationContext(), AnimalList);
        animalListView.setAdapter(adapter);
        animalListView.setOnItemLongClickListener( new ListViewItemLongClickListener() );
        animalListView.setOnItemClickListener(new ListViewItemClickListener());

        try {
            AnimalList.clear();
            String result;
            Task task = new Task();
            result = task.execute(USER_NO).get();
            Log.i("결과", " " + result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                petIdx = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                petType = URLDecoder.decode(jsonobject.getString("PET_TYPE"), "UTF-8");
                petName = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                petAge = URLDecoder.decode(jsonobject.getString("PET_AGE"), "UTF-8");
                petGender = URLDecoder.decode(jsonobject.getString("PET_GENDER"), "UTF-8");
                Animal_list Animal_list = new Animal_list(petType, petName, petAge, petGender, petIdx);
                AnimalList.add(Animal_list);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animalListView.setEmptyView(empty_text);

        View backbtn = findViewById(R.id.back);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View animalAdd = findViewById(R.id.animalAdd);

        animalAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AnimalAdd.class);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(getApplication(), AnimalInfoActivity.class);
            intent.putExtra("PET_IDX",AnimalList.get(position).getPET_IDX());
            intent.putExtra("USER_NO",USER_NO);
            startActivity(intent);
            Log.v("ss",AnimalList.get(position).getPET_IDX());
        }
    }

    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectedPos = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(Animal.this);

            // '삭제' 버튼이 클릭되면
            builder.setPositiveButton("삭제", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    try{
                        String result;
                        String PET_IDX = AnimalList.get(selectedPos).getPET_IDX();
                        DeleteTask deleteTask = new DeleteTask();
                        result = deleteTask.execute(PET_IDX).get();
                        if(result.equals("success")){
                            dialog.dismiss();  // AlertDialog를 닫는다.
                            Animal.ListSet();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            // '취소' 버튼이 클릭되면
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });
            builder.setMessage(String.format( getString(R.string.alert_msg_delete), AnimalList.get(selectedPos).getPetName()) );
            builder.create()
                    .show();
            return true;
        }

    }

    public static void ListSet(){
        try {
            AnimalList.clear();
            String resulted;
            Task task = new Task();
            resulted = task.execute(USER_NO).get();
            Log.i("결과", " " + resulted);
            JSONArray jsonArray = new JSONArray(resulted);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                petIdx = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                petType = URLDecoder.decode(jsonobject.getString("PET_TYPE"), "UTF-8");
                petName = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                petAge = URLDecoder.decode(jsonobject.getString("PET_AGE"), "UTF-8");
                petGender = URLDecoder.decode(jsonobject.getString("PET_GENDER"), "UTF-8");
                Animal_list Animal_list = new Animal_list(petType, petName, petAge, petGender, petIdx);
                AnimalList.add(Animal_list);
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        animalListView.setEmptyView(empty_text);
    }

    static class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animallist.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "USER_NO=" + strings[0];
                osw.write(sendMsg);
                osw.flush();
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        Log.i("str", str);

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

    class DeleteTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_animaldel.jsp");
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
                        Log.i("str", str);

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
