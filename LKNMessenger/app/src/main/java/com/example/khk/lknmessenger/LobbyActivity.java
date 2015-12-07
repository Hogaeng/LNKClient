package com.example.khk.lknmessenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by KHK on 2015-12-07.
 */
public class LobbyActivity extends Activity {

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobbywindow);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab1").setContent(R.id.Friend).setIndicator(getString(R.string.tab1));
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab2").setContent(R.id.Talk).setIndicator(getString(R.string.tab2));
        tabHost.addTab(spec2);

        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = 60;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = 60;

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        RelativeLayout r11 = (RelativeLayout) tabHost.getTabWidget().getChildAt(0);
        r11.setGravity(Gravity.CENTER_VERTICAL);
        TextView tv1 = (TextView) r11.getChildAt(1);
        tv1.setLayoutParams(tvParams);
        tv1.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tv1.setPadding(10, 0, 10, 0);
        tv1.setGravity(Gravity.CENTER);

        RelativeLayout r12 = (RelativeLayout) tabHost.getTabWidget().getChildAt(1);
        r12.setGravity(Gravity.CENTER_VERTICAL);
        TextView tv2 = (TextView) r11.getChildAt(1);
        tv2.setLayoutParams(tvParams);
        tv2.setTextAppearance(this, android.R.style.TextAppearance_Medium);
        tv2.setPadding(10, 0, 10, 0);
        tv2.setGravity(Gravity.CENTER);
    }
}
