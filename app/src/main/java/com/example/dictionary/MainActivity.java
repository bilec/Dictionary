package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.dictionary.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setNightMode();
        setFontSize();

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home,R.id.navigation_favorites,R.id.navigation_history,R.id.navigation_settings).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView,navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp();
    }

    private void setNightMode()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = sharedPreferences.getString(getString(R.string.settings_night_mode_key),getString(R.string.settings_night_mode_default));

        if(key != null)
        {
            if (key.equals(getString(R.string.settings_night_mode_disabled_value))) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (key.equals(getString(R.string.settings_night_mode_always_on_value))) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (key.equals(getString(R.string.settings_night_mode_automatic_value))) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }
    }

    private void setFontSize()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String key = sharedPreferences.getString(getString(R.string.settings_font_size_key),getString(R.string.settings_font_size_default));

        if(key != null)
        {
            if(key.equals(getString(R.string.settings_font_size_small_value)))
            {
                getTheme().applyStyle(R.style.FontStyle_Small,true);
            }
            else if(key.equals(getString(R.string.settings_font_size_normal_value)))
            {
                getTheme().applyStyle(R.style.FontStyle_Normal,true);
            }
            else if(key.equals(getString(R.string.settings_font_size_large_value)))
            {
                getTheme().applyStyle(R.style.FontStyle_Large,true);
            }
            else if(key.equals(getString(R.string.settings_font_size_larger_value)))
            {
                getTheme().applyStyle(R.style.FontStyle_Larger,true);
            }
        }
    }

}
