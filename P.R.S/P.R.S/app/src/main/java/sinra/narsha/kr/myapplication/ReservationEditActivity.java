package sinra.narsha.kr.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ReservationEditActivity extends AppCompatActivity {

    private static String MARKET_NO;
    private static String USER_NO;
    private static String REV_IDX;
    private static String START_TIME;
    private static String END_TIME;
    private static String PET_NAME;
    private static String PET_IDX;
    private static JSONObject jsonobject;
    private static JSONArray jsonArray;
    private static ArrayList<Rsv_IDX> Rsv_Idx_list;

    final int START_DATE_TEXT = 1;
    final int START_TIME_TEXT = 2;
    final int END_DATE_TEXT = 3;
    final int END_TIME_TEXT = 4;

    public Spinner petSpinner;
    public TextView startDateText;
    public TextView startTimeText;
    public TextView endDateText;
    public TextView endTimeText;

    int Year;
    int Month;
    int Day;
    int Date;
    int spinnerCount = 0;
    String PET_IDX_TEXT;
    String PET_NAME_TEXT;
    String SPINNER_IDX;
    String PETS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_edit);

        MARKET_NO = getIntent().getStringExtra("MARKET_NO");
        USER_NO = getIntent().getStringExtra("USER_NO");
        REV_IDX = getIntent().getStringExtra("REV_IDX");
        START_TIME = getIntent().getStringExtra("START_TIME");
        END_TIME = getIntent().getStringExtra("END_TIME");
        PET_NAME = getIntent().getStringExtra("PET_NAME");
        PET_IDX = getIntent().getStringExtra("PET_IDX");

        int STARTidx = START_TIME.indexOf(" ");
        String startdate = START_TIME.substring(0, STARTidx);
        String starttime = START_TIME.substring(STARTidx + 1);

        int ENDidx = END_TIME.indexOf(" ");
        String enddate = END_TIME.substring(0, ENDidx);
        String endtime = END_TIME.substring(ENDidx + 1);

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

        startDateText.setText(startdate);
        startTimeText.setText(starttime);
        endDateText.setText(enddate);
        endTimeText.setText(endtime);

        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_DATE_TEXT); // 날짜 설정 다이얼로그 띄우기
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_TIME_TEXT);
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_DATE_TEXT); // 날짜 설정 다이얼로그 띄우기
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_TIME_TEXT);
            }
        });

        try {
            String result;
            SpinnerTask spinnerTask = new SpinnerTask();
            result = spinnerTask.execute(USER_NO).get();
            jsonArray = new JSONArray(result);
            Log.i("결과", " " + result);
            Rsv_Idx_list = new ArrayList<>();
            ArrayAdapter<CharSequence> adapter;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonobject = jsonArray.getJSONObject(i);
                PET_IDX_TEXT = URLDecoder.decode(jsonobject.getString("PET_IDX"), "UTF-8");
                PET_NAME_TEXT = URLDecoder.decode(jsonobject.getString("PET_NAME"), "UTF-8");
                Rsv_Idx_list.add(new Rsv_IDX(PET_IDX_TEXT, PET_NAME_TEXT));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Rsv_Idx_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            petSpinner.setAdapter(adapter);

/*            for (int i = 0; i < jsonArray.length(); i++) {
                if(PET_IDX != PET_IDX_TEXT){
                    spinnerCount ++;
                }
            }
            Log.i("spinnerCount",(spinnerCount-1) +" ");
            petSpinner.setSelection(spinnerCount-1);*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String pet_spinner = petSpinner.getSelectedItem().toString();
                int pet_spinner_idx = (int) (long) petSpinner.getSelectedItemId();
                SPINNER_IDX = Rsv_Idx_list.get(pet_spinner_idx).getIDX().toString();

                Log.i("SPINNER_IDX", SPINNER_IDX + "");

                try {
                    String start_time = startDateText.getText().toString() + " " + startTimeText.getText().toString();
                    String end_time = endDateText.getText().toString() + " " + endTimeText.getText().toString();
                    Date START_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(start_time);
                    Date END_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end_time);

                    String result;
                    RsvTask rsvTask = new RsvTask();
                    result = rsvTask.execute(start_time, end_time, SPINNER_IDX, USER_NO, MARKET_NO, REV_IDX).get();

                    if (START_TIME.getTime() > END_TIME.getTime()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationEditActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationEditActivity.this);
                            builder.setMessage("수정하였습니다.");
                            // '확인' 버튼이 클릭되면
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();  // AlertDialog를 닫는다.
                                    finish();
                                    //ReservationInfoActivity.ListSet();
                                }
                            });
                            builder.create()
                                    .show();
                        } else if (result.equals("Overlap date")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationEditActivity.this);
                            builder.setMessage("이미 예약된 시간입니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ReservationEditActivity.this);
                            builder.setMessage("수정에 실패하였습니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                URL url = new URL("http://192.168.55.59:8080/app/test_shoprsvedit.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "start_time=" + strings[0] + "&end_time=" + strings[1] + "&SPINNER_IDX=" + strings[2]
                        + "&USER_NO=" + strings[3] + "&MARKET_NO=" + strings[4] + "&REV_IDX=" + strings[5];
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
            case START_DATE_TEXT:
                DatePickerDialog sdpd = new DatePickerDialog(ReservationEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day); // 기본값 현재 연월일
                return sdpd;
            case START_TIME_TEXT:
                TimePickerDialog stpd = new TimePickerDialog(ReservationEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTimeText.setText(hourOfDay + ":" + minute + ":00");
                    }
                }, 0, 00, false); // 기본값 시분 등록
                // true : 24 시간(0~23) 표시
                // false : 오전/오후 항목이 생김
                return stpd;
            case END_DATE_TEXT:
                DatePickerDialog edpd = new DatePickerDialog(ReservationEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, Year, Month, Day); // 기본값 현재 연월일
                return edpd;
            case END_TIME_TEXT:
                TimePickerDialog etpd = new TimePickerDialog(ReservationEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
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


