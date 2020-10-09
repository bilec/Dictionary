package com.example.dictionary.viewPager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class HistoryFragmentAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public HistoryFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public void setFragmentList(List<Fragment> fragmentList)
    {
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
