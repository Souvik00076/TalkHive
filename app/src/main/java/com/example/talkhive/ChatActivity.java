package com.example.talkhive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.talkhive.utilities.adapters.ViewPagerAdapter;
import com.example.talkhive.utilities.dialogs.ProfileUserDialog;
import com.example.talkhive.utilities.model.ChatModel;
import com.example.talkhive.utilities.model.UpdateUserModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ChatActivity extends AppCompatActivity {
    private static final String FRAGMENT_CHAT = "Chats";
    private static final String FRAGMENT_USERS = "Users";
    private static final String PROFILE_TAG = "Profile";
    private static final String FRAGMENT_REQUEST = "Requests";
    private ViewPager2 viewPager;
    private TabLayout layout;
    private ViewPagerAdapter adapter;
    private FragmentManager manager;
    private static final String USER_DETAIL = "USER_DETAILS";
    private static final String IMAGE_URI = "URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        new TabLayoutMediator(layout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(FRAGMENT_CHAT);
                    break;
                case 1:
                    tab.setText(FRAGMENT_USERS);
                    break;
            }
        }).attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                ProfileUserDialog dialog = new ProfileUserDialog();
                dialog.show(getSupportFragmentManager(), PROFILE_TAG);
        }
        return super.onOptionsItemSelected(item);
    }

    public void addChatScreen(final UpdateUserModel model) {
        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra("USER_DETAILS", model);
        startActivity(intent);
    }

    public void addChatScreen(final ChatModel model) {
        Log.i("Hello", model.getSender());
        Intent intent = new Intent(this, ChatScreen.class);
        intent.putExtra("CHAT_DETAILS", model);
        startActivity(intent);
    }

    private void init() {
        viewPager = (ViewPager2) findViewById(R.id.viewPager);
        layout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        manager = getSupportFragmentManager();
    }
}