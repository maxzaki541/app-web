package com.example.projectqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class forgotPasswordActivity extends AppCompatActivity {
    EditText txt_card,txt_passupdate;
    Button btnsaverup;

    String cardnumber,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        txt_card = findViewById(R.id.txt_card);
        txt_passupdate = findViewById(R.id.txt_passupdate);
        btnsaverup = findViewById(R.id.btn_saveupdate);
        btnsaverup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardnumber = (txt_card.getText().toString());
                password = (txt_passupdate.getText().toString());
                if (TextUtils.isEmpty(cardnumber)) {
                    txt_card.setError("Please enter ID Card");
                    txt_card.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txt_passupdate.setError("Please enter New Password");
                    txt_passupdate.requestFocus();
                    return;
                }

                Toast.makeText(getApplicationContext(), "SAVE", Toast.LENGTH_SHORT).show();
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
                    (forgotPasswordActivity.this, "Please wait", "Loading...");
        }
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://172.20.10.8/projectq/forgotpassword.php");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setReadTimeout(15000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                try {
                    JSONObject obj = new JSONObject();

                    obj.put("cardnumber", cardnumber);
                    obj.put("password", password);

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

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            loadingDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(forgotPasswordActivity.this);
                builder.setTitle("Thai Traditional Queue");
                builder.setMessage("บันทึกรหัสผ่านใหม่สำเร็จ");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "ยืนยัน",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                                Intent i = new Intent(forgotPasswordActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });

                AlertDialog alert3 = builder.create();
                alert3.show();


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
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
