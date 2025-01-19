package com.example.mybmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class ShowReport extends AppCompatActivity {

    TextView name, bmi, gender, categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_report);

        name = findViewById(R.id.tvName);
        bmi = findViewById(R.id.tvBmi);
        gender = findViewById(R.id.tvGender);
        categorie = findViewById(R.id.tvCategorie);

        Intent r = getIntent();

        name.setText("Name: "+r.getStringExtra("NAME"));
        bmi.setText("BMI= "+r.getDoubleExtra("BMI", 0));
        categorie.setText("Categorie: "+r.getStringExtra("CATEGORIE"));
        gender.setText("Gender: "+r.getStringExtra("GENDER"));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Back2(View view) {
        finish();
    }
}