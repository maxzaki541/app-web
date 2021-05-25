package com.example.projectqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class ConfirmQueueActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
TextView tvcard,tvdate,tvtime,tvtype;

    //String text2 = getIntent().getExtras().getString("Spinner");
    String name,date,time,type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_queue);

        //drawerLayout = findViewById(R.id.drawer_layout);


        tvcard = findViewById(R.id.tvcard);
        SharedPreferences sp=getSharedPreferences("credentials",MODE_PRIVATE);
        if(sp.contains("name"))
            tvcard.setText(sp.getString("name",""));


        tvdate =  findViewById(R.id.tvdate);
        tvtype =  findViewById(R.id.tvtype);
        tvtime =  findViewById(R.id.tvtime);

        String date1 = getIntent().getExtras().getString("Date");
        String time1 = getIntent().getExtras().getString("Time");
        String type1 = getIntent().getExtras().getString("Spinner");

        tvdate.setText(date1);
        tvtime.setText(time1);
        tvtype.setText(type1);


        Button btncon = findViewById(R.id.btnCon);
        btncon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = (tvcard.getText().toString());
                date = (tvdate.getText().toString());
                time = (tvtime.getText().toString());
                type = (tvtype.getText().toString());

                //Toast.makeText(getApplicationContext(), "SAVE", Toast.LENGTH_SHORT).show();
                new PostMethodDemo().execute();
            }
        });

    }

    public class PostMethodDemo extends AsyncTask<String, Void, String> {
        String server_reponse;
        private Dialog loadingDialog;
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show
                    (ConfirmQueueActivity.this, "Please wait", "Loading...");
        }
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;
            String result = null;

            try {
                url = new URL("http://172.20.10.8/projectq/insertQ.php");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                StringBuilder sb = new StringBuilder();

                try {
                    JSONObject obj = new JSONObject();
                    String[] da = date.split(" ");
                    obj.put("queue_date", da[1]);
                    obj.put("queue_time", time);
                    obj.put("type_name", type);
                    //obj.put("cardnumber", card);
                    obj.put("username", name);
                    writer.write(getPostDataString(obj));

                    Log.e("JSON Input", obj.toString());
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_reponse = readStream(urlConnection.getInputStream());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();

            }

            return server_reponse;
        }

        protected void onPostExecute(String s){
            String result = s.trim();
            super.onPostExecute(s);

           loadingDialog.dismiss();
           Log.e("1",result);
            if(!(result.equals("Maximum"))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmQueueActivity.this);
                builder.setTitle("Thai Traditional Queue");
                builder.setMessage("ยืนยันการจองคิว");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "ยืนยัน",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                Intent i = new Intent(ConfirmQueueActivity.this, HomeActivity.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alert3 = builder.create();
                alert3.show();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmQueueActivity.this);
                builder.setTitle("Thai Traditional Queue");
                builder.setMessage("เต็มแล้ว");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                Intent i = new Intent(ConfirmQueueActivity.this, QueueActivity.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alert3 = builder.create();
                alert3.show();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception{
        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();
        while (itr.hasNext()){
            String key = itr.next();
            Object value = params.get(key);

            if(first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key,"UTF-8"));
            result.append('=');
            result.append(URLEncoder.encode(value.toString(),"UTF-8"));
        }
        return  result.toString();
    }
    public String readStream(InputStream in){
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();;
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return  response.toString();
    }












    public void ClickBack(View view) {
        Intent intent = new Intent(this, QueueActivity.class);
        finish();
        startActivity(intent);
    }

}
