package com.example.talkhive.utilities.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.talkhive.fragments.ChatFragment;
import com.example.talkhive.fragments.UsersFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int TOTAL_PAGES = 2;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new UsersFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return TOTAL_PAGES;
    }
}
