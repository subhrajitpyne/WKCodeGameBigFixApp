package com.example.codegamebigfixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActionPropertiesOfSingleAction extends AppCompatActivity {
    TextView actionName,actionID,actionDetailedStatus,actionTargetedComputers,
    actionRestartFlag,actionIssuedDate,actionIssuerName,actionEndDate,actionState;

    Button btnActionStop;

    ArrayList<String> actionData;
    String data="";
    ActionDetailsAPI action;
    ActionStopCall actionStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_properties_of_single_action);

        //Suppressing Network Force Policy
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }

        actionName=findViewById(R.id.viewActionName);
        actionID=findViewById(R.id.viewActionID);
        actionDetailedStatus=findViewById(R.id.viewActionDetailed);
        actionTargetedComputers=findViewById(R.id.viewTargetedComputers);
        actionRestartFlag=findViewById(R.id.viewRestartFlag);
        actionIssuedDate=findViewById(R.id.viewIssuedDate);
        actionIssuerName=findViewById(R.id.viewIssuerName);
        actionEndDate=findViewById(R.id.viewEndDate);
        actionState=findViewById(R.id.viewActionCurrentState);
        btnActionStop=findViewById(R.id.btnStopAction);

        Intent intent=getIntent();
        data=intent.getStringExtra("Action");

        //Calling Action Details of a single action
        action=new ActionDetailsAPI(data.split(",")[0],data.split(",")[1],data.split(",")[2],data.split(",")[3]);
        actionData=action.getData();

        String state=actionData.get(0).split("&")[2].toLowerCase();
        //String status=actionData.get(0).split("&")[3].toLowerCase();
        btnActionStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state.toLowerCase().contains("stopped") || state.toLowerCase().contains("expired")){
                    Toast.makeText(ActionPropertiesOfSingleAction.this,"You can not stop "+state+" action",Toast.LENGTH_LONG).show();
                }else{
                    actionStop=new ActionStopCall(data.split(",")[0],data.split(",")[1],data.split(",")[2],data.split(",")[3]);
                    String response=actionStop.getData();
                    if (response.toLowerCase().contains("ok")){
                        Toast.makeText(ActionPropertiesOfSingleAction.this,"Action with ID "+actionID.getText().toString()+" has been stopped" +
                                "\nTo see the current state please go back to second page and refresh",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ActionPropertiesOfSingleAction.this,"Some error occurred",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        if (actionData.get(0).contains("&")){
            String id=actionData.get(0).split("&")[0];
            String name=actionData.get(0).split("&")[1];
            //String status=actionData.get(0).split("&")[3];
            String detailedStatus=actionData.get(0).split("&")[4];
            String computers="";
            for (String y:actionData.get(0).split("&")[5].split("6")){
                computers=y+"\n"+computers;
            }
            String restartFlag=actionData.get(0).split("&")[6];
            String startDate=actionData.get(0).split("&")[7];
            String issuer=actionData.get(0).split("&")[8];
            String endDate=actionData.get(0).split("&")[10];

            actionName.setText(name);
            actionID.setText(id);
            actionState.setText(state);
            actionDetailedStatus.setText(detailedStatus);
            actionTargetedComputers.setText(computers);
            actionRestartFlag.setText(restartFlag);
            actionIssuedDate.setText(startDate);
            actionIssuerName.setText(issuer);
            actionEndDate.setText(endDate);

        }else{
            String txt=actionData.get(0).toString();
            actionName.setText("Hello");
            actionID.setText(txt);
            actionState.setText(txt);
            actionDetailedStatus.setText(txt);
            actionTargetedComputers.setText(txt);
            actionRestartFlag.setText(txt);
            actionIssuedDate.setText(txt);
            actionIssuerName.setText(txt);
            actionEndDate.setText(txt);
        }
        }

    }