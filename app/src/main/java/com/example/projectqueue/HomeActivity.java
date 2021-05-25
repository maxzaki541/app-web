package com.example.projectqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

public class HomeActivity extends AppCompatActivity {
    Button btnq,btnhistory,btnaddress;
    DrawerLayout drawerLayout;
    public static final String USER_CARD = "CARDNUMBER";
    TextView tvcard;
    protected  static String data;
    protected  static String idglobal;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /*tvcard = findViewById(R.id.tvcard);
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("card"))
            tvcard.setText(sp.getString("card",""));*/
        /*SharedPreferences sp1=this.getSharedPreferences("credentials", MODE_PRIVATE);

        String unm  = sp1.getString("name", null);
        String pass = sp1.getString("password", null);*/

        btnq = findViewById(R.id.btnQ);
        btnhistory = findViewById(R.id.btnhistory);
        btnaddress = findViewById(R.id.btnAddress);
        btnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QueueActivity.class);

                finish();
                startActivity(intent);            }
        });
        btnhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                intent.putExtra("idglobal",idglobal.toString());
                intent.putExtra("data",data.toString());
               // finish();

                startActivity(intent);
            }
        });
        btnaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(HomeActivity.this, AddressActivity.class);
                finish();
                startActivity(intent);*/

                Uri gmmIntentUri = Uri.parse("geo:14.0454916,101.3686154?q=สาธารณสุข+ปราจีนบุรี");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);




            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        Intent intent = getIntent();
        if (TextUtils.isEmpty(data) && intent.hasExtra("data")) {
            GetData(intent.getStringExtra("data"));
        }
    }

    public void GetData(String datalist){
        data = datalist;
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0 ; i < jsonArray.length(); i++){
                JSONObject contactObject = jsonArray.optJSONObject(i);
                idglobal = contactObject.optString("mem_id");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void ClickMenu(View view){
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view){
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view){
        recreate();
    }
    public void ClickAbout(View view) {
        redirectActivity(this,AboutActivity.class);
    }
    /*public void ClickAddress(View view) {
        redirectActivity(this,AddressActivity.class);
    }*/
    public  void ClickLogout(View view){
        //SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        //if(sp.contains("card")) {
            //SharedPreferences.Editor editor=sp.edit();
            //editor.remove("card");
            //editor.putString("msg","ออกจากระบบแล้ว");
            //editor.commit();

        /*Intent in = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(in);*/
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("คุณต้องการออกจากระบบ");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    /*SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
                    if(sp.contains("name")) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.remove("name");
                        editor.putString("msg", "ออกจากระบบแล้ว");
                        editor.commit();*/

                        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("remember","false");
                        editor.apply();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                   // }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    //}

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }

}
