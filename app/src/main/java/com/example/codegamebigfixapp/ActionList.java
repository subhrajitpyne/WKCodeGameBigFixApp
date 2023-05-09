package com.example.codegamebigfixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
//import android.widget.Toast;

import java.util.ArrayList;

public class ActionList extends AppCompatActivity {
    ArrayList<String> actionData=new ArrayList<>();
    RecyclerView actionListView;
    ActionListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        actionListView=findViewById(R.id.actionListView);
        //recyclerView.setLayoutManager(new LinearLayoutManager(DisplayData.this));
        actionListView.setLayoutManager(new LinearLayoutManager(ActionList.this));

        //Getting Data from Second Page
        Intent intent = getIntent();
        actionData = (ArrayList<String>) intent.getSerializableExtra("Action");

        adapter = new ActionListViewAdapter(actionData,actionData.size(),ActionList.this);
        actionListView.setAdapter(adapter);
    }
}