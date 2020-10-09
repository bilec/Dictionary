package com.example.dictionary.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.WorkInfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.dictionary.utils.InternetConnectionCheck;
import com.example.dictionary.R;
import com.example.dictionary.data.word.Word;

import com.example.dictionary.databinding.FragmentHomeBinding;
import com.example.dictionary.recyclerView.RVEmptyObserver;
import com.example.dictionary.recyclerView.wordEntry.WordEntryAdapter;
import com.example.dictionary.work.WordSearchViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class HomeFragment extends Fragment {

    public static final String WORD = "WORD";

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private WordSearchViewModel wordSearchViewModel;

    private WordEntryAdapter wordEntryAdapter;
    private RVEmptyObserver rvEmptyObserver;


    public HomeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        wordSearchViewModel = new ViewModelProvider(this).get(WordSearchViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        binding.recyclerViewResults.setHasFixedSize(true);

        wordEntryAdapter = new WordEntryAdapter();
        rvEmptyObserver = new RVEmptyObserver(binding.recyclerViewResults,binding.viewEmpty.getRoot());

        wordEntryAdapter.setWordEntryList(new ArrayList<>());

        homeViewModel.getAllWordEntries().observe(getViewLifecycleOwner(), wordEntries-> {
            wordEntryAdapter.setWordEntryList(wordEntries);
            wordEntryAdapter.notifyDataSetChanged();
        });


        wordEntryAdapter.registerAdapterDataObserver(rvEmptyObserver);

        binding.recyclerViewResults.setAdapter(wordEntryAdapter);
        wordEntryAdapter.notifyDataSetChanged();


        binding.progressLinear.setVisibility(View.GONE);

        return binding.getRoot();
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        wordEntryAdapter.unregisterAdapterDataObserver(rvEmptyObserver);
    }


    private void buttonClick(String wordToSearch)
    {
        if(!InternetConnectionCheck.isNetworkAvailable(requireContext()))
        {
            Snackbar.make(binding.getRoot(), R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry, view -> buttonClick(wordToSearch))
                    .setAnchorView(binding.floatingActionButtonSearch)
                    .show();
            return;
        }

        homeViewModel.insertLocalWord(new Word(wordToSearch));

        wordSearchViewModel.searchWord(wordToSearch, wordEntryAdapter.getWordEntryList()).observe(getViewLifecycleOwner(), workInfo -> {
            if(workInfo == null)
            {
                return;
            }

            WorkInfo.State workState = workInfo.getState();

            boolean isFinished = workState.isFinished();
            if(isFinished)
            {
                binding.progressLinear.setVisibility(View.GONE);

                if(workState.equals(WorkInfo.State.FAILED))
                {
                    Snackbar.make(binding.getRoot(), R.string.search_failed,Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, view -> buttonClick(wordToSearch))
                            .setAnchorView(binding.floatingActionButtonSearch)
                            .show();
                }
            }
            else
            {
                binding.progressLinear.setVisibility(View.VISIBLE);
            }
        });

    }


    //---------------------- MENU ----------------------
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.button_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_query_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buttonClick(query);
                homeViewModel.setWordToSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeViewModel.setWordToSearch(newText);
                return false;
            }
        });

        binding.floatingActionButtonSearch.setOnClickListener(view -> {
            if(!searchItem.isActionViewExpanded())
            {
                searchItem.expandActionView();
            }
            else
            {
                searchView.setQuery("",false);
                InputMethodManager inputMethodManager =  (InputMethodManager)requireContext().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                searchView.requestFocus();
            }


        });

        MenuItem favoriteItem = menu.findItem(R.id.button_favorite);
        favoriteItem.setOnMenuItemClickListener(menuItem -> {

            if(!homeViewModel.getWordToSearch().isEmpty())
            {
                Word word = new Word(homeViewModel.getWordToSearch());
                homeViewModel.insertFavoriteWord(word);
            }

            return false;
        });

        fromAnotherFragmentInMenu(searchItem, searchView);

    }

    private void fromAnotherFragmentInMenu(MenuItem searchItem, SearchView searchView)
    {
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String word = bundle.getString(HomeFragment.WORD);
            homeViewModel.setWordToSearch(word);
            buttonClick(word);

            searchItem.expandActionView();
            searchView.setQuery(word,false);
            InputMethodManager inputMethodManager =  (InputMethodManager)requireContext().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(requireView().getWindowToken(),0);

        }
    }

}