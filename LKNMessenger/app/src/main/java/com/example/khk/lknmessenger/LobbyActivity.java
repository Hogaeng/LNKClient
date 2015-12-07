package com.example.khk.lknmessenger;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by KHK on 2015-12-07.
 */
public class LobbyActivity extends TabActivity {
    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobbywindow);

        mTabHost = getTabHost();

        // 탭 1, 2, 3 을 추가하면서 태그를 달아주고, 제목(또는 아이콘)을 설정한다.
        mTabHost.addTab(mTabHost.newTabSpec("Friend").setContent(R.id.tabs1).setIndicator("Friend"));
        mTabHost.addTab(mTabHost.newTabSpec("Talk").setContent(R.id.tabs2).setIndicator("Talk"));

    }
}
