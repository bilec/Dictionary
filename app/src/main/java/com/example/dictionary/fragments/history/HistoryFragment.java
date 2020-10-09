package com.example.dictionary.fragments.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dictionary.R;
import com.example.dictionary.databinding.FragmentHistoryBinding;
import com.example.dictionary.fragments.history.global.HistoryGlobalFragment;
import com.example.dictionary.fragments.history.local.HistoryLocalFragment;
import com.example.dictionary.viewPager2.HistoryFragmentAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryFragmentAdapter historyFragmentAdapter;

    private List<Integer> titleList = new ArrayList<>(Arrays.asList(R.string.title_local,R.string.title_global));
    private List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(new HistoryLocalFragment(), new HistoryGlobalFragment()));

    public HistoryFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        historyFragmentAdapter = new HistoryFragmentAdapter(this);
        historyFragmentAdapter.setFragmentList(fragmentList);

        binding.viewPager.setAdapter(historyFragmentAdapter);
        binding.viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(titleList.get(position))).attach();

        return binding.getRoot();
    }


}
