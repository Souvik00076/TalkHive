package com.example.talkhive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.talkhive.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        __init__();
        addOrReplace(new LoginFragment());
    }

    public void addOrReplace(Fragment fragment) {
        manager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    private void __init__() {
        manager = getSupportFragmentManager();
    }
}