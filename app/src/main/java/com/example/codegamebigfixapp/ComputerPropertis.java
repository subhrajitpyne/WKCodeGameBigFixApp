package com.example.codegamebigfixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ComputerPropertis extends AppCompatActivity {
    TextView hostName,hostID,hostAgentType,hostCPU,hostRAM,
    hostOS,hostDNS,hostIP,hostIPV6,hostLastReportTime,hostDeviceType,
    hostRelay;
    ArrayList<String>computerData;
    String data="";
    ComputerDetails computerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_propertis);
        //Suppressing Network Force Policy
        if (Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }
        Intent intent=getIntent();
        data=intent.getStringExtra("Details");

        //Calling ComputerDetails Class
        computerDetails=new ComputerDetails(data.split(",")[0],data.split(",")[1],data.split(",")[2],data.split(",")[3]);
        computerData=computerDetails.getData();


        //All TextViews
        hostName=findViewById(R.id.viewHostName);
        hostID=findViewById(R.id.viewID);
        hostAgentType=findViewById(R.id.viewAgentType);
        hostCPU=findViewById(R.id.viewCPU);
        hostRAM=findViewById(R.id.viewRAM);
        hostOS=findViewById(R.id.viewOS);
        hostDNS=findViewById(R.id.viewDNS);
        hostIP=findViewById(R.id.viewIP);
        hostIPV6=findViewById(R.id.viewIPV6);
        hostLastReportTime=findViewById(R.id.viewLastReportTime);
        hostDeviceType=findViewById(R.id.viewDeviceType);
        hostRelay=findViewById(R.id.viewRelay);

        if (computerData.get(0).contains("&")){
            for (String x:computerData){
                String arr[]=x.split("&");
                hostName.setText(arr[0]);
                hostID.setText(arr[1]);
                hostAgentType.setText(arr[2]);
                hostCPU.setText(arr[3]);
                hostRAM.setText(arr[4]);
                hostOS.setText(arr[5]);
                hostDNS.setText(arr[6]);
                hostIP.setText(arr[7]);
                hostIPV6.setText(arr[8]);
                hostLastReportTime.setText(arr[10]);
                hostDeviceType.setText(arr[11]);
                hostRelay.setText(arr[12]);
        }
        }else{
            hostName.setText(computerData.get(0).toString());
        }
    }
}