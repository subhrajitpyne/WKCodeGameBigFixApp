package com.example.codegamebigfixapp;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RViewAdapter extends RecyclerView.Adapter<RViewAdapter.HolderAdapter> {
    ArrayList<String> bigfixData=new ArrayList<>();
    public Context context;

    public RViewAdapter(ArrayList<String> bigfixData,Context context) {
        this.bigfixData = bigfixData;
        this.context=context;
    }

    @NonNull
    @Override
    public HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout,parent,false);
        return new HolderAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HolderAdapter holder, int position) {
        //Data
        String host=bigfixData.get(position).split("&")[0];
        String os=bigfixData.get(position).split("&")[1];
        String lastReportTime=bigfixData.get(position).split("&")[3];
        String relayName=bigfixData.get(position).split("&")[4];
        String relaySelectionMethod=bigfixData.get(position).split("&")[5];

        //Data set
        holder.hostName.setText(host);
        holder.os.setText(os);
        holder.lTime.setText(lastReportTime);
        holder.relay.setText(relayName);
        holder.relaySelectionMethod.setText(relaySelectionMethod);

        //HorizontalScrollView/ScrollView is not Clickable by default and we need to
        //set gesture to achieve it
        /*
        GestureDetector to detect Gesture and Apply on Horizontal Scroll
         */
         final GestureDetector detector = new GestureDetector(context, new GestureDetector.OnGestureListener() {

             @Override
             public boolean onDown(MotionEvent motionEvent) {
                 return false;
             }

             @Override
             public void onShowPress(MotionEvent motionEvent) {

             }
             //onSingleTapUp is for click

             @Override
            public boolean onSingleTapUp(MotionEvent e) {
                 //OnClick Reading data from file
                String data=readFromFile()+","+host;
                //Toast.makeText(context,data,Toast.LENGTH_LONG).show();
                 //Calling Context where Computer Details of a specific computer will be called
                 Intent intent=new Intent(context,ComputerPropertis.class);
                 intent.putExtra("Details",data);
                 context.startActivity(intent);
                 return false;
            }
             @Override
             public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                 return false;
             }
             @Override
             public void onLongPress(MotionEvent motionEvent) {
             }
             @Override
             public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                 return false;
             }
             // Note that there are more methods which will appear here
        // (which you probably don't need).
    });
        holder.horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }
    @Override
    public int getItemCount() {
        return (bigfixData.size());
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


    public class HolderAdapter extends RecyclerView.ViewHolder{
        TextView hostName,os,lTime,relay,relaySelectionMethod;
        HorizontalScrollView horizontalScrollView;
        public HolderAdapter(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            hostName=itemView.findViewById(R.id.host);
            os=itemView.findViewById(R.id.os);
            lTime=itemView.findViewById(R.id.lTime);
            relay=itemView.findViewById(R.id.relayName);
            relaySelectionMethod=itemView.findViewById(R.id.relaySelectionMethod);
            horizontalScrollView=itemView.findViewById(R.id.hScroll);
        }

    }

}
