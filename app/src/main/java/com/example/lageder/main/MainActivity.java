package com.example.lageder.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kakaolinkage.KakaoLoginActivity;

import java.io.File;
import java.security.MessageDigest;

public class MainActivity  extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name_path=getFilesDir(). getAbsolutePath()+"/name.txt";
        File name_file = new File(name_path);
        //getAppKeyHash();

        //카톡 연동 체크
        if(name_file.exists() != true){
            Intent graph_intent = new Intent(getApplicationContext(), KakaoLoginActivity.class);
            startActivity(graph_intent);
        }

        //뒤로가기 두번 = 종료 설정
        backPressCloseHandler = new BackPressCloseHandler(this);


        //기본 툴바, 레이아웃, 뷰페이저 설정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //탭 추가
        final TabLayout.Tab profile_tab = tabLayout.newTab();
        final TabLayout.Tab graph_tab = tabLayout.newTab();
        final TabLayout.Tab setting_tab = tabLayout.newTab();

        addTap(profile_tab,R.drawable.profile_32,"프로필", 0);
        addTap(graph_tab,R.drawable.graph_32,"최근 경향", 1);
        addTap(setting_tab,R.drawable.settings_32,"환경설정", 2);


        tabLayout.setTabTextColors(Color.rgb(255,255,255),Color.rgb(255,255,255));
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(255,255,255));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab){}

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    /*private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }

    }
*/
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


    public void addTap(TabLayout.Tab tab,int tap_icon, String tap_text,int count){
        View view = getLayoutInflater().inflate(R.layout.custom_tab,null);

        TextView text = (TextView) view.findViewById(R.id.tab_text);
        text.setText(tap_text);

        ImageView icon = (ImageView) view.findViewById(R.id.tab_icon);
        icon.setImageResource(tap_icon);

        Typeface font_gabia = Typeface.createFromAsset(getAssets(), "gabia_solmee.ttf");
        text.setTypeface(font_gabia);

        tab.setCustomView(view);
        tabLayout.addTab(tab,count);
    }

}
