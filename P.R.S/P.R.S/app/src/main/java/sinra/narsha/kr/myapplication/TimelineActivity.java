package sinra.narsha.kr.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class TimelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        View searchbtn = findViewById(R.id.search);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });
        View alrambtn = findViewById(R.id.alarm);

        alrambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), AlramActivity.class);
                startActivity(intent);
            }
        });

        ImageButton listbtn = (ImageButton)findViewById(R.id.listbtn);
        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        ImageButton probtn = (ImageButton)findViewById(R.id.profile);
        probtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimelineActivity.this, "준비중", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton petbtn = (ImageButton)findViewById(R.id.pet_info);
        petbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReservationInfoActivity.class);
                startActivity(intent);
            }
        });
        ImageButton shopbtn = (ImageButton)findViewById(R.id.shop);
        shopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReservationInfoActivity.class);
                startActivity(intent);
            }
        });
        ImageButton opbtn = (ImageButton)findViewById(R.id.option);
        opbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimelineActivity.this, "준비중", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton resbtn = (ImageButton)findViewById(R.id.reservation_info);
        resbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ReservationInfoActivity.class);
                startActivity(intent);
            }
        });
        ImageButton montobtn = (ImageButton)findViewById(R.id.monitoring);
        montobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MonitoringActivity.class);
                startActivity(intent);
            }
        });
        ImageButton commubtn = (ImageButton)findViewById(R.id.community);
        commubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CommunityActivity.class);
                startActivity(intent);
            }
        });
        ImageButton cosbtn = (ImageButton)findViewById(R.id.costomer);
        cosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TimelineActivity.this, "준비중", Toast.LENGTH_LONG).show();
            }
        });
        ImageButton logoutbtn = (ImageButton)findViewById(R.id.logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL); //  xml 에서도 set 가능

        // set viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}
