package com.example.codegamebigfixapp;;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class SecondPage extends AppCompatActivity {
    ProgressDialog progressDialog;
    ActionQuery action;
    ConnectAndGetData bigFixConnection;
    Button btnAction,btnComputer;
    Context context;
    ArrayList<String> computerDetails;
    ArrayList<String> actionDetails;
    TextView textMarque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        //Suppressing Network Force Policy
        if (Build.VERSION.SDK_INT>9){
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            btnAction=findViewById(R.id.btnActionDetails);
            btnComputer=findViewById(R.id.btnComputerDetails);
            textMarque=findViewById(R.id.textMarque);
            textMarque.setSelected(true);
            context=getApplicationContext();

            progressDialog=new ProgressDialog(SecondPage.this);
            Timer timer = new Timer();

            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fqdnOrIP=readFromFile().split(",")[0];
                    String userName=readFromFile().split(",")[1];
                    String password=readFromFile().split(",")[2];

                    action=new ActionQuery(fqdnOrIP,userName,password);
                    actionDetails=action.getData();
                    if (actionDetails !=null && actionDetails.get(0).contains("&")){
                        //Timer timer = new Timer();
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progressbar_layout);

                        Intent intent=new Intent(SecondPage.this,ActionList.class);
                        intent.putExtra("Action",actionDetails);
                        startActivity(intent);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                        public void run() {progressDialog.dismiss();
                        }
                    }, 5000);

                    }else if (action !=null && !action.getData().get(0).contains("&")){
                            String msg=action.getData().get(0).toString();
                            Toast.makeText(SecondPage.this,msg,Toast.LENGTH_LONG).show();
                    }else{
                            Toast.makeText(SecondPage.this,"Null Data. Check Again",Toast.LENGTH_LONG).show();
                    }
                }
            });

            btnComputer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fqdnOrIP=readFromFile().split(",")[0];
                    String userName=readFromFile().split(",")[1];
                    String password=readFromFile().split(",")[2];

                    bigFixConnection=new ConnectAndGetData(fqdnOrIP,userName,password);
                    computerDetails=bigFixConnection.getData();
                    if (computerDetails !=null && computerDetails.get(0).contains("&")){
                        long delayInMillis = 5000;
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progressbar_layout);

                        Intent intent=new Intent(SecondPage.this,MainActivity.class);
                        intent.putExtra("Return",computerDetails);
                        startActivity(intent);
                        timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                     }, delayInMillis);
                    }else if (bigFixConnection.getData()!=null && !bigFixConnection.getData().get(0).contains("&")){
                            String msg=bigFixConnection.getData().get(0).toString();
                            Toast.makeText(SecondPage.this,msg,Toast.LENGTH_LONG).show();
                    }else{
                            Toast.makeText(SecondPage.this,"Null Data. Check Again",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Reading data
    private String readFromFile() {
        String data="";
        try{
            File pathOfConfig=context.getFilesDir();
            File fileToRead=new File(pathOfConfig,"Config.txt");
            FileInputStream fIn=new FileInputStream(fileToRead);
            byte content[]=new byte [(int)fileToRead.length()];
            fIn.read(content);
            data=new String(content);
        }catch (Exception ex){
            Log.d("Error","Issue Occurred");
        }
        finally{
            return data;
        }
    }
    //So that Not Attached WindowsManager not occur
    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog.isShowing()){
            progressDialog.cancel();
        }

    }
}