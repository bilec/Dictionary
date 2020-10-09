package com.example.dictionary.fragments.settings.about.licences;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.dictionary.R;
import com.example.dictionary.data.library.LibrariesUsed;
import com.example.dictionary.data.library.Library;
import com.example.dictionary.databinding.FragmentLicencesBinding;
import com.example.dictionary.fragments.settings.about.licences.licence.LicenceFragment;
import com.example.dictionary.recyclerView.ItemClickSupport;
import com.example.dictionary.recyclerView.library.LibraryListAdapter;

import java.util.List;

public class LicencesFragment extends Fragment {

    private FragmentLicencesBinding binding;
    private LibraryListAdapter libraryListAdapter;
    private List<Library> libraryList = LibrariesUsed.getAllLibrariesUsed();

    public LicencesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLicencesBinding.inflate(inflater, container, false);

        binding.recyclerView.setHasFixedSize(true);

        libraryListAdapter = new LibraryListAdapter();
        libraryListAdapter.setLibraryList(libraryList);

        binding.recyclerView.setAdapter(libraryListAdapter);

        binding.recyclerView.addItemDecoration(new DividerItemDecoration(binding.recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ItemClickSupport.addTo(binding.recyclerView).setOnItemClickListener((recyclerView, position, v) ->{
            Bundle bundle = new Bundle();
            bundle.putString(LicenceFragment.TITLE,libraryList.get(position).getLicence().getName());
            bundle.putString(LicenceFragment.BODY,libraryList.get(position).getLicence().getTerms());
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.navigation_licence_licences_about_settings,bundle);
        });

        return binding.getRoot();
    }

}
