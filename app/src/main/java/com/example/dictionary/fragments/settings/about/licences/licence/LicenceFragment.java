package com.example.dictionary.fragments.settings.about.licences.licence;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dictionary.databinding.FragmentLicenceBinding;

public class LicenceFragment extends Fragment {

    public static final String TITLE = "TITLE";
    public static final String BODY = "BODY";

    private FragmentLicenceBinding binding;

    public LicenceFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLicenceBinding.inflate(inflater, container, false);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            String title = bundle.getString(TITLE,"");
            String body = bundle.getString(BODY, "");

            ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
            if(actionBar != null)
            {
                actionBar.setTitle(title);
            }

            binding.bodyTextView.setText(body);
        }

        return binding.getRoot();
    }
}
