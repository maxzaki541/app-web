package com.example.projectqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;


public class AboutActivity extends AppCompatActivity {
  DrawerLayout drawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about);

    drawerLayout = findViewById(R.id.drawer_layout);


  }









  public void ClickBack(View view) {
    Intent intent = new Intent(this, HomeActivity.class);
    finish();
    startActivity(intent);
  }








}
