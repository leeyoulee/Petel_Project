package sinra.narsha.kr.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static String id;
    public static String USER_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getStringExtra("id");
        USER_NO = getIntent().getStringExtra("USER_NO");

        ImageView shopbtn = (ImageView) findViewById(R.id.shopbtn);
        ImageView petbtn = (ImageView) findViewById(R.id.petbtn);
        ImageView reservationbtn = (ImageView) findViewById(R.id.reservationbtn);

        shopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Shop.class);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });

        petbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Animal.class);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });

        reservationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReservationInfoActivity.class);
                intent.putExtra("USER_NO",USER_NO);
                startActivity(intent);
            }
        });
    }
}
