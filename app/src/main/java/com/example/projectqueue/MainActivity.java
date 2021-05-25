package com.example.projectqueue;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {
    EditText txtpass, txtcard;
    Button btnlogin;
    TextView tvregis,tvfgot;
    String username, password;
    SharedPreferences sharedPreferences;
    CheckBox CheckBoxRem;
    Boolean savelogin;
    protected static int points;
    //public static final String USER_CARD = "CARDNUMBER";
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);


        txtcard = findViewById(R.id.txtcard);
        txtpass = findViewById(R.id.txtpass);
        tvregis = findViewById(R.id.tvregis);
        tvfgot = findViewById(R.id.tvfgot);

        btnlogin = findViewById(R.id.btnlogin);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.commit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtcard.setText(loginPreferences.getString("username", ""));
            txtpass.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(false);

        }
        /*SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if(checkbox.equals("true")){

            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);

        }
        else if(checkbox.equals("false")){
            //Toast.makeText(MainActivity.this,"please sign in",Toast.LENGTH_SHORT).show();

        }*/
        /*saveLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(MainActivity.this,"checked",Toast.LENGTH_SHORT).show();
                }
                else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(MainActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        tvfgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), forgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                username = (txtcard.getText().toString());
                password = (txtpass.getText().toString());



                if (TextUtils.isEmpty(username)) {
                    txtcard.setError("Please enter ID Card");
                    txtcard.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtpass.setError("Please enter Password");
                    txtpass.requestFocus();
                    return;
                }
               if (view == btnlogin) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtcard.getWindowToken(), 0);



                    if (saveLoginCheckBox.isChecked()) {
                        loginPrefsEditor.putBoolean("saveLogin", true);
                        loginPrefsEditor.putString("username", username);
                        loginPrefsEditor.putString("password", password);
                        loginPrefsEditor.commit();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }


                }
                UserLogin(username,password);


            }

        });
    }

    private void UserLogin(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show
                        (MainActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String username = params[0];
                String password = params[1];


                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://172.20.10.8/projectq/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if(result==null){
                    return ;
                }
                String s = result.trim();
                loadingDialog.dismiss();
                //if (s.equalsIgnoreCase("login success")) {
                if(!(s.equals("login not success"))) {



                    SharedPreferences sp = getSharedPreferences("credentials",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("name", txtcard.getText().toString());
                    editor.putString("password", txtpass.getText().toString());
                    editor.commit();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        //intent.putExtra(USER_CARD, cardnumber);
                        intent.putExtra("data",s.toString()); //Call to Home ACtivity put json
                        finish();
                        startActivity(intent);

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Thai Traditional Queue");
                    builder.setMessage("ชื่อผู้ใช้ หรือ รหัสผ่านไม่ถูกต้อง");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(i);
                                }
                            });

                    AlertDialog alert3 = builder.create();
                    alert3.show();


                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }

}






































    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvregis:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
                break;

            case R.id.btnlogin:
                cardnumber = (txtcard.getText().toString());
                password = (txtpass.getText().toString());
                if (TextUtils.isEmpty(cardnumber)) {
                    txtcard.setError("Please enter ID Card");
                    txtcard.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    txtpass.setError("Please enter Password");
                    txtpass.requestFocus();
                    return;
                }
                break;
        }
    }*/

