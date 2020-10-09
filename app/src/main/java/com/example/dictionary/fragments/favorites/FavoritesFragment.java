package com.example.dictionary.fragments.favorites;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import com.example.dictionary.R;
import com.example.dictionary.data.word.Word;
import com.example.dictionary.databinding.FragmentFavoritesBinding;
import com.example.dictionary.fragments.home.HomeFragment;
import com.example.dictionary.recyclerView.ItemClickSupport;
import com.example.dictionary.recyclerView.RVEmptyObserver;
import com.example.dictionary.recyclerView.word.WordListAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel favoritesViewModel;

    private WordListAdapter wordListAdapter;
    private RVEmptyObserver rvEmptyObserver;

    private boolean isMultiSelect = false;
    private ActionMode actionMode;

    private ActionMode.Callback callback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu_favorites, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.button_select_all:{
                    selectAll();
                    return true;
                }

                case R.id.button_delete_selected:{
                    deleteSelected(mode);
                    return true;
                }

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelectFinish();
            actionMode = null;
        }


    };

    private Observer<List<Word>> observer = words -> {
        wordListAdapter.setWords(words);
        wordListAdapter.notifyDataSetChanged();
    };

    public FavoritesFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false);
        binding.recyclerViewResults.setHasFixedSize(true);

        wordListAdapter = new WordListAdapter(getContext());
        rvEmptyObserver = new RVEmptyObserver(binding.recyclerViewResults,binding.viewEmpty.getRoot());

        wordListAdapter.setWords(new ArrayList<>());
        wordListAdapter.registerAdapterDataObserver(rvEmptyObserver);

        binding.recyclerViewResults.setAdapter(wordListAdapter);
        wordListAdapter.notifyDataSetChanged();

        ItemClickSupport.addTo(binding.recyclerViewResults).setOnItemLongClickListener((recyclerView, position, v) -> {

            if(actionMode == null)
            {
                AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
                actionMode = appCompatActivity.startSupportActionMode(callback);
                isMultiSelect = true;
            }

            return false;
        });

        ItemClickSupport.addTo(binding.recyclerViewResults).setOnItemClickListener((recyclerView, position, v) -> {

            if(isMultiSelect)
            {
                multiSelectAction(position);
            }
            else
            {
                findWord(wordListAdapter.getWords().get(position).getWord());
            }

        });


        return binding.getRoot();
    }

    private void selectAll()
    {
        if(wordListAdapter.getSelectedWords().containsAll(wordListAdapter.getWords()))
        {
            wordListAdapter.getSelectedWords().clear();
            wordListAdapter.notifyDataSetChanged();
        }
        else
        {
            wordListAdapter.getSelectedWords().clear();
            wordListAdapter.getSelectedWords().addAll(wordListAdapter.getWords());
            wordListAdapter.notifyDataSetChanged();
        }

        setTitleWhenSelected();
    }

    private void deleteSelected(ActionMode actionMode)
    {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_word_question)
                .setPositiveButton(R.string.delete, (dialogInterface, i) -> {

                    for(Word word : wordListAdapter.getSelectedWords())
                    {
                        favoritesViewModel.deleteWord(word);
                    }

                    multiSelectFinish();
                    actionMode.finish();

                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {})
                .show();


    }

    private void multiSelectAction(int position)
    {
        Word word = wordListAdapter.getWords().get(position);

        if(!wordListAdapter.getSelectedWords().remove(word))
        {
            wordListAdapter.getSelectedWords().add(word);
        }

        setTitleWhenSelected();

        wordListAdapter.notifyDataSetChanged();
    }

    private void setTitleWhenSelected()
    {
        if(actionMode != null)
        {
            actionMode.setTitle(""+wordListAdapter.getSelectedWords().size());
        }
    }

    private void multiSelectFinish()
    {
        wordListAdapter.getSelectedWords().clear();
        wordListAdapter.notifyDataSetChanged();
        isMultiSelect = false;
    }

    private void findWord(String word)
    {
        NavController navController = NavHostFragment.findNavController(this);

        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.WORD,word);

        navController.navigate(R.id.navigation_home,bundle);
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        wordListAdapter.unregisterAdapterDataObserver(rvEmptyObserver);

        if(actionMode != null)
        {
            multiSelectFinish();
            actionMode.finish();
        }
    }


    //---------------------- MENU ----------------------
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_favorites,menu);

        int activeButtonId = checkInt(getIntFromSharedPreferences(SELECTED_SORT_BY_BUTTON,DEFAULT_SORT_BY_BUTTON));

        MenuItem activeButton = menu.findItem(activeButtonId);

        onOptionsItemSelected(activeButton);
    }

    private int checkInt(int toCheck)
    {
        switch (toCheck)
        {
            case R.id.favorites_button_time:
            case R.id.favorites_button_alphabetically_az:
            case R.id.favorites_button_alphabetically_za:
                return toCheck;

            default:
                return DEFAULT_SORT_BY_BUTTON;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.favorites_button_time:
            {
                favoritesViewModel.getAllWords().observe(getViewLifecycleOwner(), observer);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());

                break;
            }

            case R.id.favorites_button_alphabetically_az:
            {
                favoritesViewModel.getAlphabetizedAscWords().observe(getViewLifecycleOwner(), observer);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());

                break;
            }

            case R.id.favorites_button_alphabetically_za:
            {
                favoritesViewModel.getAlphabetizedDescWords().observe(getViewLifecycleOwner(), observer);

                menuItem.setChecked(true);
                saveIntToSharedPreferences(SELECTED_SORT_BY_BUTTON,menuItem.getItemId());

                break;
            }
        }

        return super.onOptionsItemSelected(menuItem);
    }

    private static final String SELECTED_SORT_BY_BUTTON = "SELECTED_SORT_BY_BUTTON_FAVORITES";
    private static final int DEFAULT_SORT_BY_BUTTON = R.id.favorites_button_time;

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
