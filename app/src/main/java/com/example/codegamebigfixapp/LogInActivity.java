package com.example.codegamebigfixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LogInActivity extends AppCompatActivity {
    ImageView btnImage;
    EditText fqdnOrIp,userName,password;
    Context context;
    ArrayList<String> actionDetails;
    ActionQuery action;

    static {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);

            final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce_btn);

            fqdnOrIp = this.findViewById(R.id.fqdnOrIP);
            userName = this.findViewById(R.id.userName);
            password = this.findViewById(R.id.password);
            context = getApplicationContext();

            btnImage = this.findViewById(R.id.btnImg);
            btnImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnImage.startAnimation(animation);

                    action=new ActionQuery(fqdnOrIp.getText().toString(),userName.getText().toString(),password.getText().toString());
                    actionDetails=action.getData();
                    if (fqdnOrIp.getText().toString().equals("") || userName.getText().toString().equals("") || password.getText().toString().equals("")) {
                        Toast.makeText(LogInActivity.this, "Fields are empty.", Toast.LENGTH_LONG).show();

                    } else if(action !=null && !action.getData().get(0).contains("&")){
                        String msg=action.getData().get(0).toString();
                        Toast.makeText(LogInActivity.this,msg,Toast.LENGTH_LONG).show();
                    }

                    else{
                        //File writing
                        writeToFile(fqdnOrIp.getText().toString(), userName.getText().toString(), password.getText().toString(), context);
                        Intent intent = new Intent(LogInActivity.this, SecondPage.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
    //FileWriting on default location /data/data/<app-name(com.subhrajit.bigfixapp)/files/Config.txt>
    private void writeToFile (String url,String userName,String password,Context context) {
            try {
                File path= getApplicationContext().getFilesDir();
                String data=url+","+userName+","+password;
                FileOutputStream writer=new FileOutputStream(new File(path,"Config.txt"));
                writer.write(data.getBytes());
                writer.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
    }
}