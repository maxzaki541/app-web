package com.example.projectqueue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HistoryActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ListView listView;
    MyAdapter adapter;
    public static ArrayList<history> historyArrayList = new ArrayList<>();
    String idglobal;
    history history;
    //String queue_id;
    //String url =
    String position_delete = "";

    String date,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        drawerLayout = findViewById(R.id.drawer_layout);
        //final TextView textView = (TextView) findViewById(R.id.textview);
        listView = findViewById(R.id.listView);
        adapter = new MyAdapter(this,historyArrayList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                CharSequence[] dialogItem = {"ยกเลิกคิว"};
                builder.setTitle("ต้องการยกเลิกหรือไม่?   "+historyArrayList.get(position).getQueue_id());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        switch (i){
                            case 0 :

                                //deleteData(historyArrayList.get(position).getQueue_id());
                                position_delete = historyArrayList.get(position).getID();
                                time = ((historyArrayList.get(position).getTime()).substring(0,5))+":00";
                                date = historyArrayList.get(position).getDate();

                                //Toast.makeText(getApplication(),date+" "+time,Toast.LENGTH_SHORT).show();
                                new PostMethodDemo().execute();

                                break;

                        }
                    }
                });

                AlertDialog alert3 = builder.create();
                alert3.show();
            }
        });

        Intent intent = getIntent();
        idglobal = intent.getStringExtra("idglobal");
        showdata();
        //new ShowDataTask().execute(); //Execute Data

    }


   /*private void deleteData(final String queue_id) {
       //Toast.makeText(getApplicationContext(), ""+queue_id ,Toast.LENGTH_LONG).show();

        StringRequest request = new StringRequest(Request.Method.POST,"https://172.20.10.8/projectq/deleteQ.php",
               new Response.Listener<String>() {
                   @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Data Deleted")){
                            Toast.makeText(getApplicationContext(),"Delete success", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), ""+queue_id ,Toast.LENGTH_LONG).show();

                       }
                        else{
                            Toast.makeText(getApplicationContext(), "not delete", Toast.LENGTH_LONG).show();
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){

            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String,String> param = new HashMap<String, String>();
               param.put("queue_id", queue_id);
                return param;
            }
        };
       RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }*/

    public class PostMethodDemo extends AsyncTask<String, Void, String> {
        String server_reponse;
        private Dialog loadingDialog;
        protected void onPreExecute() {

            super.onPreExecute();


        }
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://172.20.10.8/projectq/deleteQ.php");
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
                    //obj.put("name", name);
                    obj.put("queue_id", position_delete);

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

        protected void onPostExecute(String s){
            super.onPostExecute(s);

//            Intent intent = new Intent(HistoryActivity.this, HistoryActivity.class);
//            finish();
//            startActivity(intent);
            new PostMethodUpdate().execute();



        }
    }
    public class PostMethodUpdate extends AsyncTask<String, Void, String> {
        String server_reponse;
        private Dialog loadingDialog;
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL("http://172.20.10.8/projectq/update_date.php");
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
                    //obj.put("name", name);

                    obj.put("date1", date);
                    obj.put("ontime", time);
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

        protected void onPostExecute(String s){
            super.onPostExecute(s);
            //loadingDialog.dismiss();

            finish();
        }
    }
    public void showdata(){
        StringRequest request = new StringRequest(Request.Method.POST,"http://172.20.10.8/projectq/showHis.php?id="+idglobal.toString(),
                new Response.Listener<String>(){
                    public void onResponse(String response){

                        historyArrayList.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            if(success.equals("1")){
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String queue_id = "รหัสคิว : ( " + object.getString("queue_id")+" )";
                                    String queue_date = "วันที่ : ( " + object.getString("queue_date")+" )";
                                    String queue_time = "เวลา : ( " + object.getString("queue_time")+" )";
                                    String type_name = "ประเภท : ( " + object.getString("type_name")+" )";

                                    history = new history(queue_id,queue_date,queue_time,type_name,
                                            object.getString("queue_id"),object.getString("queue_time"),object.getString("queue_date"));
                                    historyArrayList.add(history);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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

//    public class ShowDataTask extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            try {
//                HttpURLConnection urlConnection = null;
//                String line = "";
//                StringBuilder sb = new StringBuilder();
//
//                try
//                {
//                    URL myUrl = new URL("http://172.20.10.8/projectq/showHis.php?id="+idglobal.toString());
//                    urlConnection = (HttpURLConnection)myUrl.openConnection();
//                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//
//                    while ((line = in.readLine()) != null)
//                    {
//                        sb.append(line).append('\n');
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//
//                }finally {
//                    if (urlConnection != null){
//                        urlConnection.disconnect();
//                    }
//                }
//                return sb.toString();
//            }
//            catch (Exception e){
//                return e.toString();
//            }
//        }
//
//        protected void onPostExecute(String Result){
//
//        }
//    }





    public void ClickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }
}
