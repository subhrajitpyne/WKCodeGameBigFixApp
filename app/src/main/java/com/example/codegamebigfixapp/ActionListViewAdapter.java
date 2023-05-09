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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionListViewAdapter extends RecyclerView.Adapter<ActionListViewAdapter.ActionHolder> {
    ArrayList<String> actionIDAndName;
    public Context context;
    public int size;

    public ActionListViewAdapter(ArrayList<String>actionIdAndName,int s,Context c){
        this.actionIDAndName=actionIdAndName;
        this.size=s;
        this.context=c;
    }

    @NonNull
    @Override
    public ActionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.action_recycler_view,parent,false);
        return new ActionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionHolder holder, int position) {
            String id= actionIDAndName.get(position).split("&")[0];
            String name=actionIDAndName.get(position).split("&")[1];
            String state=actionIDAndName.get(position).split("&")[2];
            String status=actionIDAndName.get(position).split("&")[3];

            holder.actionID.setText(id);
            holder.actionName.setText(name);
            holder.actionState.setText(state);
            holder.actionStatus.setText(status);

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
                String data=readFromFile()+","+id;
                //Toast.makeText(context,data,Toast.LENGTH_LONG).show();
                 //Calling Context where Computer Details of a specific computer will be called
                 Intent intent=new Intent(context,ActionPropertiesOfSingleAction.class);
                 intent.putExtra("Action",data);
                 //Toast.makeText(context,data,Toast.LENGTH_LONG).show();
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
        holder.actionScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.size;
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

    public class ActionHolder extends RecyclerView.ViewHolder{
        TextView actionID,actionName,actionStatus,actionState;
        HorizontalScrollView actionScroll;
        public ActionHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            actionID=itemView.findViewById(R.id.actionID);
            actionName=itemView.findViewById(R.id.actionName);
            actionState=itemView.findViewById(R.id.actionCurrentState);
            actionStatus=itemView.findViewById(R.id.actionStatus);
            actionScroll=itemView.findViewById(R.id.actionScroll);
        }
    }
}
