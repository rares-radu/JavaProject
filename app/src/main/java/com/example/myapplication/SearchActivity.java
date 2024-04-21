package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

public class SearchActivity extends AppCompatActivity {

    Button search_button;
    FragmentContainerView fragmentContainerView;
    SwitchCompat switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search_button = findViewById(R.id.searchButton);
        fragmentContainerView = findViewById(R.id.fragment_container_view);
        switchCompat = findViewById(R.id.search_switch);
        FragmentManager fragmentManager = getSupportFragmentManager();
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, MunicipalityBriefInfoFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("")
                            .commit();
                    switchCompat.setText("Brief");
                }
                else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, MunicipalityInfoFragment.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("")
                            .commit();
                    switchCompat.setText("Detailed");
                }
            }
        });
    }
}
