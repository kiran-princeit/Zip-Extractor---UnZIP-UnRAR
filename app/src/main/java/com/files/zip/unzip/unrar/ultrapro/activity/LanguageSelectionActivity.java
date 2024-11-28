package com.files.zip.unzip.unrar.ultrapro.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.files.zip.unzip.unrar.ultrapro.R;
import com.files.zip.unzip.unrar.ultrapro.adapter.LanguageAdapter;
import com.files.zip.unzip.unrar.ultrapro.model.Language;
import com.files.zip.unzip.unrar.ultrapro.utils.LanguagePreference;
import com.files.zip.unzip.unrar.ultrapro.utils.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectionActivity extends BaseActivity {
    private LanguageAdapter languageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        List<Language> languages = new ArrayList<>();
        languages.add(new Language("en", "English"));
        languages.add(new Language("es", "Spanish"));
        languages.add(new Language("de", "German"));
        languages.add(new Language("zh", "Chinese"));
        languages.add(new Language("fr", "French"));
        languages.add(new Language("hi", "Hindi"));
        languages.add(new Language("it", "Italian"));
        languages.add(new Language("ja", "Japanese"));
        languages.add(new Language("ru", "Russian"));

        RecyclerView recyclerView = findViewById(R.id.rvLanguages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter = new LanguageAdapter(languages);
        recyclerView.setAdapter(languageAdapter);

        Button continueButton = findViewById(R.id.ivDone);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedLanguageCode = languageAdapter.getSelectedLanguageCode();
                if (selectedLanguageCode != null && !selectedLanguageCode.isEmpty()) {
                    LanguagePreference.saveLanguage(LanguageSelectionActivity.this, selectedLanguageCode);
                    LocaleHelper.setLocale(LanguageSelectionActivity.this, selectedLanguageCode);

                    Intent intent = new Intent(LanguageSelectionActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LanguageSelectionActivity.this, R.string.please_select_a_language, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}