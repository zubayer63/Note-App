package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class EditNote extends AppCompatActivity implements TextWatcher {

    EditText editText;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); ////create back arrow symbol in this activity and also add  android:parentActivityName=".MainActivity" in androidManifest.xlm;;

        editText= findViewById(R.id.editText);
        Intent intent =getIntent();
         id = intent.getIntExtra("noteId",-1);
        if(id!= -1){
            editText.setText(MainActivity.arrayList.get(id));
        }
        editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        MainActivity.arrayList.set(id, String.valueOf(s));
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences =this.getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE);

        if(MainActivity.set == null){
            MainActivity.set = new HashSet<String>();  //initialized the set
        }
        else {
            MainActivity.set.clear();
        }

        MainActivity.set.addAll(MainActivity.arrayList);
        sharedPreferences.edit().remove("note").apply();
        sharedPreferences.edit().putStringSet("note",MainActivity.set).apply();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
   */



}