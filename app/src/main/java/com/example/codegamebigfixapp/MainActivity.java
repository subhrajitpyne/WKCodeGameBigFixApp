package com.example.codegamebigfixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    RViewAdapter rViewAdapter;
    ArrayList<String> bigfxData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.mainView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(DisplayData.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Getting Data from Login page
            Intent intent = getIntent();
            bigfxData = (ArrayList<String>) intent.getSerializableExtra("Return");
            rViewAdapter = new RViewAdapter(bigfxData,MainActivity.this);
            recyclerView.setAdapter(rViewAdapter);
        }

}