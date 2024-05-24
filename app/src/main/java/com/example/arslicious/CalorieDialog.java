package com.example.arslicious;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CalorieDialog extends AppCompatActivity {


    ImageView ivDialogImage;
    TextView tvDialogCaloriesNumber, tvDialogProteinNumber, tvDialogCarbsNumber, tvDialogFatsNumber, tvItemName;

    AppCompatButton btnCalorieDialogContinue;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_dialog);
        ivDialogImage = findViewById(R.id.ivDialogImage);
        tvDialogCaloriesNumber = findViewById(R.id.tvDialogCaloriesNumber);
        tvDialogProteinNumber = findViewById(R.id.tvDialogProteinNumber);
        tvDialogCarbsNumber = findViewById(R.id.tvDialogCarbsNumber);
        tvDialogFatsNumber = findViewById(R.id.tvDialogFatsNumber);
        tvItemName = findViewById(R.id.tvItemName);
        btnCalorieDialogContinue = findViewById(R.id.btnCalorieDialogContinue);

        btnCalorieDialogContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra("EXTRA_IMAGE");
        String name = intent.getStringExtra("EXTRA_NAME");
        String calories = intent.getStringExtra("EXTRA_CALORIES");
        String protein = intent.getStringExtra("EXTRA_PROTEIN");
        String carbs = intent.getStringExtra("EXTRA_CARBS");
        String fats = intent.getStringExtra("EXTRA_FATS");

        ModelClass modelClass = new ModelClass();

        Glide.with(getApplicationContext()).load(imageUrl).into(ivDialogImage);
        tvItemName.setText(name);
        tvDialogCaloriesNumber.setText(calories);
        tvDialogProteinNumber.setText(protein+"g");
        tvDialogCarbsNumber.setText(carbs+"g");
        tvDialogFatsNumber.setText(fats+"g");

    }

    public void showRiderInfoDialog(){
        CardView parentCardView = findViewById(R.id.parentCardView);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_calorie_dialog, parentCardView);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create();
        alertDialog.show();
    }
}