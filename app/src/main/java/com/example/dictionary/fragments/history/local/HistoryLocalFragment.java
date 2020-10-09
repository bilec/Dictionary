package com.example.dictionary.fragments.history.local;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import com.example.dictionary.R;
import com.example.dictionary.data.word.Word;
import com.example.dictionary.databinding.FragmentHistoryListBinding;
import com.example.dictionary.fragments.home.HomeFragment;
import com.example.dictionary.recyclerView.ItemClickSupport;
import com.example.dictionary.recyclerView.RVEmptyObserver;
import com.example.dictionary.recyclerView.word.WordListAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryLocalFragment extends Fragment {

    private FragmentHistoryListBinding binding;
    private HistoryLocalViewModel historyLocalViewModel;

    private WordListAdapter wordListAdapter;
    private RVEmptyObserver rvEmptyObserver;

    private Observer<List<Word>> observer = words -> {
        wordListAdapter.setWords(words);
        wordListAdapter.notifyDataSetChanged();
    };

    private Observer<List<Word>> reversedObserver = words -> {
        Collections.reverse(words);
        wordListAdapter.setWords(words);
        wordListAdapter.notifyDataSetChanged();
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        historyLocalViewModel = new ViewModelProvider(this).get(HistoryLocalViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryListBinding.inflate(inflater, container, false);
        binding.recyclerViewResults.setHasFixedSize(true);

        wordListAdapter = new WordListAdapter(getContext());
        rvEmptyObserver = new RVEmptyObserver(binding.recyclerViewResults,binding.viewEmpty.getRoot());

        wordListAdapter.setWords(new ArrayList<>());
        wordListAdapter.registerAdapterDataObserver(rvEmptyObserver);

        binding.recyclerViewResults.setAdapter(wordListAdapter);
        wordListAdapter.notifyDataSetChanged();

        ItemClickSupport.addTo(binding.recyclerViewResults).setOnItemClickListener((recyclerView, position, v) -> findWord(wordListAdapter.getWords().get(position).getWord()));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wordListAdapter.unregisterAdapterDataObserver(rvEmptyObserver);
    }

    private void findWord(String word)
    {
        NavController navController = NavHostFragment.findNavController(this);

        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.WORD,word);

        navController.navigate(R.id.navigation_home,bundle);
    }

    //---------------------- MENU ----------------------
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        inflater.inflate(R.menu.toolbar_menu_history,menu);

        int activeButtonId = checkInt(getIntFromSharedPreferences(SELECTED_SORT_BY_BUTTON,DEFAULT_SORT_BY_BUTTON));

        MenuItem activeButton = menu.findItem(activeButtonId);

        onOptionsItemSelected(activeButton);
    }

    private int checkInt(int toCheck)
    {
        switch (toCheck)
        {
            case R.id.history_button_time:
            case R.id.history_button_alphabetically_az:
            case R.id.history_button_alphabetically_za:
                return toCheck;

            default:
                return DEFAULT_SORT_BY_BUTTON;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.history_button_time:
            {
                historyLocalViewModel.getAllWords().observe(getViewLifecycleOwner(), reversedObserver);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());
                break;
            }

            case R.id.history_button_alphabetically_az:
            {
                historyLocalViewModel.getAlphabetizedAscWords().observe(getViewLifecycleOwner(), observer);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());
                break;
            }

            case R.id.history_button_alphabetically_za:
            {
                historyLocalViewModel.getAlphabetizedDescWords().observe(getViewLifecycleOwner(), observer);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());
                break;
            }

            case R.id.history_button_delete_all:
            {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_all_words_question)
                        .setPositiveButton(R.string.delete, (dialogInterface, i) -> historyLocalViewModel.deleteAll())
                        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})
                        .show();
                break;
            }
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private static final String SELECTED_SORT_BY_BUTTON = "SELECTED_SORT_BY_BUTTON_LOCAL";
    private static final int DEFAULT_SORT_BY_BUTTON = R.id.history_button_time;

    private void saveIntToSharedPreferences(String key, int value)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int getIntFromSharedPreferences(String key, int defaultValue)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        return sharedPreferences.getInt(key,defaultValue);
    }

}
