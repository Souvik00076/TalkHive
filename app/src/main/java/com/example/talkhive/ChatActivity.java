package com.example.talkhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.talkhive.utilities.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatActivity extends AppCompatActivity {
    private static final String FRAGEMENT_CHAT = "Chat";
    private static final String FRAGMENT_USERS = "Users";
    private ViewPager2 viewPager;
    private TabLayout layout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        new TabLayoutMediator(layout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(FRAGEMENT_CHAT);
                    break;
                case 1:
                    tab.setText(FRAGMENT_USERS);
                    break;
            }
        }).attach();
    }

    private void init() {
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        layout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }
}