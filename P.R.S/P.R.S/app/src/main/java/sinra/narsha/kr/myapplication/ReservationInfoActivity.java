package sinra.narsha.kr.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

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

public class ReservationInfoActivity extends AppCompatActivity {

    private static String REV_IDX;
    private static String PET_IDX;
    private static String MARKET_NO;
    private static String MARKET_NAME;
    private static String PET_NAME;
    private static String START_TIME;
    private static String END_TIME;

    private static  SwipeMenuListView rsvListView;
    private static  Rsv_listAdapter adapter;
    private static List<Rsv_list> RsvList;

    public static Context CONTEXT;

    public static String USER_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_info);

        USER_NO = getIntent().getStringExtra("USER_NO");

        rsvListView = (SwipeMenuListView) findViewById(R.id.rsvListView);
        TextView empty_text = (TextView) findViewById(R.id.empty_text);
        RsvList = new ArrayList<Rsv_list>();
        adapter = new Rsv_listAdapter(getApplicationContext(), RsvList);
        rsvListView.setAdapter(adapter);

        CONTEXT= this;

        Log.i("USER_NO", " " + USER_NO);

        try {
            RsvList.clear();
            String result;
            Task task = new Task();
            result = task.execute(USER_NO).get();
            Log.i("결과", " " + result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                REV_IDX = URLDecoder.decode(jsonobject.getString("REV_IDX"), "UTF-8");
                MARKET_NO = URLDecoder.decode(jsonobject.getString("MARKET_NO"), "UTF-8");
                MARKET_NAME = URLDecoder.decode(jsonobject.getString("MARKET_NAME"), "UTF-8");
                PET_NAME = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                START_TIME = URLDecoder.decode(jsonobject.getString("START_TIME"), "UTF-8");
                END_TIME = URLDecoder.decode(jsonobject.getString("END_TIME"), "UTF-8");
                PET_IDX = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                Rsv_list rsv_list = new Rsv_list(START_TIME, END_TIME, MARKET_NO, REV_IDX, MARKET_NAME, PET_NAME, PET_IDX);
                RsvList.add(rsv_list);
                Log.i("MARKET_NO==",MARKET_NO);
                Log.i("REV_IDX==",REV_IDX);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        rsvListView.setEmptyView(empty_text);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                editItem.setWidth(150);
                // set item title
                editItem.setTitle("수정");
                // set item title fontsize
                editItem.setTitleSize(20);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);
            }
        };

        // set creator
        rsvListView.setMenuCreator(creator);

        rsvListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(getApplication(), ReservationEditActivity.class);
                        intent.putExtra("REV_IDX",RsvList.get(position).getREV_IDX());
                        intent.putExtra("MARKET_NO",RsvList.get(position).getMARKET_NO());
                        intent.putExtra("START_TIME",RsvList.get(position).getSTART_TIME());
                        intent.putExtra("END_TIME",RsvList.get(position).getEND_TIME());
                        intent.putExtra("PET_NAME",RsvList.get(position).getPET_NAME());
                        intent.putExtra("PET_IDX",RsvList.get(position).getPET_IDX());
                        intent.putExtra("USER_NO",USER_NO);
                        startActivity(intent);
                        Log.v("ss",RsvList.get(position).getREV_IDX());
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        View backbtn = findViewById(R.id.back);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

/*    public static void ListSet(){

        try {
            RsvList.clear();
            String result;
            Task task = new Task();
            result = task.execute(USER_NO).get();
            Log.i("결과====", " " + result);
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                REV_IDX = URLDecoder.decode(jsonobject.getString("REV_IDX"), "UTF-8");
                MARKET_NO = URLDecoder.decode(jsonobject.getString("MARKET_NO"), "UTF-8");
                MARKET_NAME = URLDecoder.decode(jsonobject.getString("MARKET_NAME"), "UTF-8");
                PET_NAME = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                START_TIME = URLDecoder.decode(jsonobject.getString("START_TIME"), "UTF-8");
                END_TIME = URLDecoder.decode(jsonobject.getString("END_TIME"), "UTF-8");
                PET_IDX = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                Rsv_list rsv_list = new Rsv_list(START_TIME, END_TIME, MARKET_NO, REV_IDX, MARKET_NAME, PET_NAME, PET_IDX);
                RsvList.add(rsv_list);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                editItem.setWidth(150);
                // set item title
                editItem.setTitle("수정");
                // set item title fontsize
                editItem.setTitleSize(20);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);
            }
        };

        // set creator
        rsvListView.setMenuCreator(creator);

        rsvListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), ReservationEditActivity.class);
                        intent.putExtra("REV_IDX",RsvList.get(position).getREV_IDX());
                        intent.putExtra("MARKET_NO",RsvList.get(position).getMARKET_NO());
                        intent.putExtra("START_TIME",RsvList.get(position).getSTART_TIME());
                        intent.putExtra("END_TIME",RsvList.get(position).getEND_TIME());
                        intent.putExtra("PET_NAME",RsvList.get(position).getPET_NAME());
                        intent.putExtra("PET_IDX",RsvList.get(position).getPET_IDX());
                        intent.putExtra("USER_NO",USER_NO);
                        startActivity(intent);
                        Log.v("ss",RsvList.get(position).getREV_IDX());
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }*/

    static class Task extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_reservationlist.jsp");
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
}
