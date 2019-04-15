package sinra.narsha.kr.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ReservationActivity extends AppCompatActivity {

    public static String MARKET_NO;
    public static String USER_NO;
    public static JSONObject jsonobject;
    public static JSONArray jsonArray;
    public static ArrayList<Rsv_IDX> Rsv_Idx_list;

    final int START_DATE = 1;
    final int START_TIME = 2;
    final int END_DATE = 3;
    final int END_TIME = 4;

    public Spinner petSpinner;
    public TextView startDateText;
    public TextView startTimeText;
    public TextView endDateText;
    public TextView endTimeText;

    int Year;
    int Month;
    int Day;
    int Date;
    String PET_IDX;
    String PET_NAME;
    String SPINNER_IDX;
    String PETS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        MARKET_NO = getIntent().getStringExtra("MARKET_NO");
        USER_NO = getIntent().getStringExtra("USER_NO");

        petSpinner = findViewById(R.id.petSpinner);
        Button startDate = (Button) findViewById(R.id.startDate);
        Button startTime = (Button) findViewById(R.id.startTime);
        Button endDate = (Button) findViewById(R.id.endDate);
        Button endTime = (Button) findViewById(R.id.endTime);
        startDateText = (TextView) findViewById(R.id.startDateText);
        startTimeText = (TextView) findViewById(R.id.startTimeText);
        endDateText = (TextView) findViewById(R.id.endDateText);
        endTimeText = (TextView) findViewById(R.id.endTimeText);

        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_DATE); // 날짜 설정 다이얼로그 띄우기
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_TIME);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_DATE); // 날짜 설정 다이얼로그 띄우기
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_TIME);
            }
        });


        try {
            String result;
            SpinnerTask spinnerTask = new SpinnerTask();
            result = spinnerTask.execute(USER_NO).get();
            jsonArray = new JSONArray(result);
            Log.i("결과", " " + result);
            if (result.equals("[]")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setMessage("동물을 먼저 등록해주세요.");
                // '확인' 버튼이 클릭되면
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();  // AlertDialog를 닫는다.
                        finish();
                    }
                });
                builder.create()
                        .show();
            }
                Rsv_Idx_list = new ArrayList<>();
                ArrayAdapter<CharSequence> adapter;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonobject = jsonArray.getJSONObject(i);
                    PET_IDX = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                    PET_NAME = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                    Rsv_Idx_list.add(new Rsv_IDX(PET_IDX, PET_NAME));
                }
                adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Rsv_Idx_list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                petSpinner.setAdapter(adapter);

            } catch(Exception e){
                e.printStackTrace();
            }

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button rsvBtn = findViewById(R.id.rsvBtn);
        rsvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String pet_spinner = petSpinner.getSelectedItem().toString();
                int pet_spinner_idx = (int) (long) petSpinner.getSelectedItemId();
                SPINNER_IDX =Rsv_Idx_list.get(pet_spinner_idx).getIDX().toString();

                Log.i("SPINNER_IDX", SPINNER_IDX +"");


                if (startDateText.getText().equals("예약 시작 날짜") || startTimeText.getText().equals("예약 시작 시간") || endDateText.getText().equals("예약 종료 날짜") || endTimeText.getText().equals("예약 종료 시간")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("모든 항목을 입력해주세요.");
                    // '확인' 버튼이 클릭되면
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();  // AlertDialog를 닫는다.
                        }
                    });
                    builder.create()
                            .show();
                } else {
                    try {
                        String start_time = startDateText.getText().toString() + " " + startTimeText.getText().toString();
                        String end_time = endDateText.getText().toString() + " " + endTimeText.getText().toString();
                        Date START_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start_time);
                        Date END_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_time);

                        String result;
                        RsvTask rsvTask = new RsvTask();
                        result = rsvTask.execute(start_time, end_time, SPINNER_IDX, USER_NO, MARKET_NO).get();

                        if (START_TIME.getTime() > END_TIME.getTime()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                            builder.setMessage("예약 시간을 다시 설정해주세요.");
                            // '확인' 버튼이 클릭되면
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();  // AlertDialog를 닫는다.
                                }
                            });
                            builder.create()
                                    .show();
                        } else if (START_TIME.getTime() > END_TIME.getTime()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                            builder.setMessage("예약 시간을 다시 설정해주세요.");
                            // '확인' 버튼이 클릭되면
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();  // AlertDialog를 닫는다.
                                }
                            });
                            builder.create()
                                    .show();
                        } else {
                            if (result.equals("success")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                                builder.setMessage("예약하였습니다.");
                                // '확인' 버튼이 클릭되면
                                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();  // AlertDialog를 닫는다.
                                        finish();
                                    }
                                });
                                builder.create()
                                        .show();
                            } else if (result.equals("Overlap date")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                                builder.setMessage("이미 예약된 시간입니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                                builder.setMessage("예약에 실패하였습니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    class SpinnerTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_rsvSpinner.jsp");
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

    class RsvTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.55.59:8080/app/test_shoprsv.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "start_time=" + strings[0] + "&end_time=" + strings[1] + "&SPINNER_IDX=" + strings[2]
                        + "&USER_NO=" + strings[3] + "&MARKET_NO=" + strings[4];
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

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE:
                DatePickerDialog sdpd = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day); // 기본값 현재 연월일
                return sdpd;
            case START_TIME:
                TimePickerDialog stpd = new TimePickerDialog(ReservationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTimeText.setText(hourOfDay + ":" + minute + ":00");
                    }
                }, 0, 00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return stpd;
            case END_DATE:
                DatePickerDialog edpd = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day); // 기본값 현재 연월일
                return edpd;
            case END_TIME:
                TimePickerDialog etpd = new TimePickerDialog(ReservationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endTimeText.setText(hourOfDay + ":" + minute + ":00");
                    }
                }, 0, 00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return etpd;
        }


        return super.onCreateDialog(id);
    }


    private class Rsv_IDX {

        String IDX;
        String NAME;

        public Rsv_IDX(String PET_IDX, String PET_NAME) {
            this.IDX = PET_IDX;
            this.NAME = PET_NAME;
        }

        public String getIDX() {
            return IDX;
        }

        public void setIDX(String PET_IDX) {
            this.IDX = PET_IDX;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String PET_NAME) {
            this.NAME = PET_NAME;
        }

        //to display object as a string in spinner
        @Override
        public String toString() {
            return NAME;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Rsv_IDX) {
                Rsv_IDX rsv_IDX = (Rsv_IDX) obj;
                if (rsv_IDX.getNAME().equals(NAME) && rsv_IDX.getIDX().equals(IDX)) return true;
            }
            return false;
        }

    }
}


