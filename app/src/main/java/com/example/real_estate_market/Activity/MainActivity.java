package com.example.real_estate_market.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import androidx.core.content.ContextCompat;

import com.example.real_estate_market.R;

public class MainActivity extends TabActivity {
    TabHost tabHost;
    int defaultValue;
    private int currentTab = 0;
    private int lastTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        getTabWidget().getChildAt(currentTab).setBackgroundColor(Color.parseColor("#F0F2F5"));
    }
    private void addControls() {
        tabHost = findViewById(android.R.id.tabhost);
    }
    private void addEvents() {
        tabHost.setup();
        createTab();
        getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId)
            {
                currentTab = getTabHost().getCurrentTab();
                Log.i("P", String.valueOf(currentTab));
                setCurrentTabColor();
                lastTab =currentTab;
            }
        });
        getTabWidget().getChildAt(lastTab).setBackgroundColor(Color.WHITE);
    }
    private void createTab() {

        addTab("HOME", ContextCompat.getDrawable(MainActivity.this,R.drawable.icons8home), HomeActivity.class);
        addTab("MY POSTS", ContextCompat.getDrawable(MainActivity.this,R.drawable.icons8list64), MyPostsActivity.class);
        addTab("PROFILE", ContextCompat.getDrawable(MainActivity.this,R.drawable.icons8profiles64), MyProfileActivity.class);
    };

    public void setCurrentTabColor(){
        getTabWidget().getChildAt(currentTab).setBackgroundColor(Color.parseColor("#F0F2F5"));
        getTabWidget().getChildAt(lastTab).setBackgroundColor(Color.WHITE);
    }


    private void addTab(String labelId, Drawable drawable, Class<?> c) {
        TabHost.TabSpec spec;
        tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this)
                .inflate(R.layout.tab_indicator, getTabWidget(), false);

        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageDrawable(drawable);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
}

