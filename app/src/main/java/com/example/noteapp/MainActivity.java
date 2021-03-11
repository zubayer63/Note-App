package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static   ArrayList<String> arrayList = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView =findViewById(R.id.listview);


        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EditNote.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(position);
                                Toast.makeText(getApplicationContext(),"Note is deleted",Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("com.example.noteapp",Context.MODE_PRIVATE);
                                if(set == null){
                                    set =new HashSet<String>();

                                }
                                else {
                                    set.clear();
                                }
                                set.addAll(arrayList);
                                sharedPreferences.edit().remove("note").apply();
                                sharedPreferences.edit().putStringSet("note",set).apply();
                                arrayAdapter.notifyDataSetChanged(); //update the array adapter after deleted item or somethings has been changed
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();


                return true;
            }
        });


        SharedPreferences sharedPreferences =this.getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE);

        //Set is similar to array.here use set because shared Preferences dont have/support array
        set = sharedPreferences.getStringSet("note",null);

        arrayList.clear(); //this used for clear all data in array list;

        if(set != null){
            arrayList.addAll(set);
        }
        else {
            arrayList.add("Add Notes");
            set = new HashSet<String>(); //initializr the set;
            set.addAll(arrayList);
            sharedPreferences.edit().putStringSet("note", set).apply();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.add){
            arrayList.add("");
           SharedPreferences sharedPreferences =this.getSharedPreferences("com.example.noteapp",Context.MODE_PRIVATE);
           if(set == null){
               set =new HashSet<String>();

           }
           else {
               set.clear();
           }
           set.addAll(arrayList);

            sharedPreferences.edit().remove("note").apply();
           sharedPreferences.edit().putStringSet("note",set).apply();

           arrayAdapter.notifyDataSetChanged(); //update the array adapter

           Intent intent = new Intent(getApplicationContext(),EditNote.class);
           intent.putExtra("noteId",arrayList.size()-1);
           startActivity(intent);
           return true;
        }
        return super.onOptionsItemSelected(item);
    }
}