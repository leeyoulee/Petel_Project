package sinra.narsha.kr.myapplication;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Shop extends AppCompatActivity {

    private String MARKET_NAME = "";
    private String PETS = "";
    private String CLASS = "";
    private String SERVICE = "";

    private int count = 0;

    private ListView shopListView;
    private Shop_listAdapter adapter;
    private List<Shop_list> ShopList;

    public static String USER_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        USER_NO = getIntent().getStringExtra("USER_NO");

        shopListView = (ListView) findViewById(R.id.shopListView);
        ShopList = new ArrayList<Shop_list>();
        adapter = new Shop_listAdapter(getApplicationContext(), ShopList);
        shopListView.setAdapter(adapter);
        shopListView.setOnItemClickListener(new ListViewItemClickListener());

        String MARKET_NAME;
        String CLASS;
        String PETS;
        String SERVICE;
        String MARKET_NO;

        try {
            ShopList.clear();
            String result;
            Task task = new Task();
            result = task.execute(" ").get();
            //Log.i("결과", " " + result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                PETS = URLDecoder.decode(jsonobject.getString("PETS"), "UTF-8");
                MARKET_NAME = URLDecoder.decode(jsonobject.getString("MARKET_NAME"), "UTF-8");
                SERVICE = URLDecoder.decode(jsonobject.getString("SERVICE"), "UTF-8");
                CLASS = URLDecoder.decode(jsonobject.getString("CLASS"), "UTF-8");
                MARKET_NO = URLDecoder.decode(jsonobject.getString("MARKET_NO"), "UTF-8");
                Shop_list shop_list = new Shop_list(MARKET_NAME, CLASS, PETS, SERVICE, MARKET_NO);
                ShopList.add(shop_list);
                Log.i("결과", " " + MARKET_NO);
            }

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
    }



    private class ListViewItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent intent = new Intent(getApplication(), ShopInfoActivity.class);
            intent.putExtra("MARKET_NO",ShopList.get(position).getMARKET_NO());
            intent.putExtra("USER_NO",USER_NO);
            startActivity(intent);
            Log.v("ss",ShopList.get(position).getMARKET_NO());
        }
    }



    class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_shoplist.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = " " + strings[0];
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
