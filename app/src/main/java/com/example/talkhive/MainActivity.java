package com.example.talkhive;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.talkhive.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) actionBar.hide();
        __init__();
        addOrReplace(new LoginFragment());
    }

    public void addOrReplace(Fragment fragment) {
        manager.beginTransaction().add(R.id.container, fragment).commit();
    }

    public void nextActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void __init__() {
        manager = getSupportFragmentManager();
    }
}