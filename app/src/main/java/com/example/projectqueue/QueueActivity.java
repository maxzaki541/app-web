package com.example.projectqueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class QueueActivity extends AppCompatActivity implements View.OnClickListener /*,DatePickerDialog.OnDateSetListener*/{
    DrawerLayout drawerLayout;
    Button btndate ,btntime,btnok,txtdate1;
    EditText txtdate,txttime;

    private int day,month,year;

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner sp;
    ArrayList<String> data;
    RecyclerView recyclerView;

    DatePickerDialog datePickerDialog  ;
    int Year, Month, Day;
    Calendar calendar =  Calendar.getInstance();
    String time="";


    private Calendar date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        sp=(Spinner)findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(this,R.layout.spinner_layout,R.id.txtspin,listItems);
        sp.setAdapter(adapter);


        drawerLayout = findViewById(R.id.drawer_layout);

        //btndate = findViewById(R.id.btndate);
        //btntime = findViewById(R.id.btntime);
        btnok = findViewById(R.id.btnok);
        txtdate1 = findViewById(R.id.txtdate);
       //txttime = findViewById(R.id.txttime);


        txtdate1.setOnClickListener(this);
       //btndate.setOnClickListener(this);
       //btntime.setOnClickListener(this);
       btnok.setOnClickListener(this);

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);


        CreateSelectTime(Day,Month,Year);

    }

    public void onStart(){
        super.onStart();
        BackTask bt=new BackTask();
        bt.execute();

    }




    private class BackTask extends AsyncTask<Void,Void,Void> {
        ArrayList<String> list;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is=null;
            String result="";
            try{
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost= new HttpPost("http://172.20.10.8/projectq/spinner.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                }
                is.close();
                //result=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray =new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("type_name"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }


    public void onClick(View view) {

        if(view == txtdate1){

            //final Calendar currentDate = Calendar.getInstance();
            /*long now = System.currentTimeMillis()-1000;long next7 = now+(1000*60*60*24*7);
            datePickerDialog = DatePickerDialog.newInstance(QueueActivity.this, Year, Month, Day);
            datePickerDialog.setThemeDark(false);
            datePickerDialog.showYearPickerFirst(false);
            //datePickerDialog.setTitle("Date Picker");

            datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialogInterface) {

                    Toast.makeText(getApplication(), "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                }
            });

            Calendar min_date_c = Calendar.getInstance();
            datePickerDialog.setMinDate(min_date_c);

            Calendar max_date_c = Calendar.getInstance();
            max_date_c.set(Calendar.DATE,Day+12);
            datePickerDialog.setMaxDate(max_date_c);

            for (Calendar loopdate = min_date_c; loopdate.before(max_date_c);  loopdate.add(Calendar.DATE,1)) {

                int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);

                Log.d("bbbbb",""+loopdate.get(Calendar.DAY_OF_MONTH));
                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {

                    Calendar[] disabledDays =  new Calendar[1];
                    disabledDays[0] = loopdate;
                    Log.d("aaaaaaaaaaaaaaaaaaaaaa",""+loopdate.get(Calendar.DAY_OF_MONTH));

                    datePickerDialog.setDisabledDays(new Calendar[]{loopdate});
                    //datePickerDialog.setHighlightedDays(new Calendar[]{loopdate});


                }
            }
            datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
*/
            final Calendar currentDate = Calendar.getInstance();
            date = Calendar.getInstance();

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {



                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                    Calendar a = Calendar.getInstance();
                    a.set(year, monthOfYear, dayOfMonth);
                    CreateSelectTime(dayOfMonth,monthOfYear,year);
                    String da = "";
                    switch(a.get(Calendar.DAY_OF_WEEK)){
                        case 2: da = "จันทร์";
                            break;
                        case 3:da = "อังคาร";
                            break;
                        case 4:da = "พุธ";
                            break;
                        case 5:da = "พฤหัสบดี";
                            break;
                        case 6:da = "ศุกร์";
                            break;

                    }
                    txtdate1.setText(da+" "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    if (a.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_13)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_17)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.GONE);
                    }
                    else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(QueueActivity.this);
                        builder.setTitle("กรุณาเลือกวันอื่น");
                        builder.setMessage("หยุดทำการวันเสาร์และอาทิตย์");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                        Intent i = new Intent(QueueActivity.this,QueueActivity.class);
                                        startActivity(i);

                                    }
                                });

                        AlertDialog alert3 = builder.create();
                        alert3.show();
                    }
                    else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(QueueActivity.this);
                        builder.setTitle("กรุณาเลือกวันอื่น");
                        builder.setMessage("หยุดทำการวันเสาร์และอาทิตย์");
                        builder.setCancelable(true);

                        builder.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        finish();
                                        Intent i = new Intent(QueueActivity.this,QueueActivity.class);
                                        startActivity(i);

                                    }
                                });

                        AlertDialog alert3 = builder.create();
                        alert3.show();
                    }
                   /* else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.GONE);

                    } else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.GONE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.GONE);
                    }*/ else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_17)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_13)).setVisibility(View.VISIBLE);

                    } else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_17)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_13)).setVisibility(View.VISIBLE);
                    } else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_17)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_13)).setVisibility(View.VISIBLE);
                    } else if (a.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                        ((RadioButton) findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_17)).setVisibility(View.VISIBLE);
                        ((RadioButton) findViewById(R.id.rdo_13)).setVisibility(View.VISIBLE);
                    }
                }

            };
            long now = System.currentTimeMillis()-1000;
            long next7 = now+(1000*60*60*24*8);
            DatePickerDialog datePickerDialog = new  DatePickerDialog(this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),   currentDate.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(now);
            datePickerDialog.getDatePicker().setMaxDate(next7);
            datePickerDialog.show();



        }
        /*if(view==btntime){

        }*/
        if(view==btnok){
            String date = (txtdate1.getText().toString());
            String itemStr = sp.getSelectedItem().toString();

            if (TextUtils.isEmpty(date)) {
                txtdate1.setError("Please select Date");
                txtdate1.requestFocus();
                return;
            }

            Intent intent = new Intent(QueueActivity.this, ConfirmQueueActivity.class);
            intent.putExtra("Spinner", itemStr);
            intent.putExtra("Date", date);
            intent.putExtra("Time", time);

            finish();
            startActivity(intent);

//            Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplication(),""+itemStr,Toast.LENGTH_SHORT).show();
        }
    }



    /*public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        txtdate.setText(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Calendar a = Calendar.getInstance();
        a.set(year,monthOfYear,dayOfMonth);

        if(a.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
            ((RadioButton) findViewById(R.id.rdo_18)).setVisibility(View.GONE);
        }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.GONE);
        }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.GONE);
                ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.GONE);
            }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
        }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY){
            ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
        }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY){
            ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
        }
        else if(a.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY){
            ((RadioButton)findViewById(R.id.rdo_8)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_10)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_14)).setVisibility(View.VISIBLE);
            ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
        }
        /*else {
            ((RadioButton)findViewById(R.id.rdo_18)).setVisibility(View.VISIBLE);
        }
    }*/

    private void CreateSelectTime(int d,int m,int y){
        RadioButton rdo_8 = findViewById(R.id.rdo_8);
        RadioButton rdo_10 = findViewById(R.id.rdo_10);
        RadioButton rdo_14 = findViewById(R.id.rdo_14);
        RadioButton rdo_18 = findViewById(R.id.rdo_18);
        RadioButton rdo_13 = findViewById(R.id.rdo_13);
        RadioButton rdo_17 = findViewById(R.id.rdo_17);

        Calendar dd = Calendar.getInstance();

        RadioButton[] radio = new RadioButton[]{rdo_8,rdo_10,rdo_13,rdo_14,rdo_17,rdo_18};
        int[] hh = new int[]{9,10,13,14,17,18};
        int[] mm = new int[]{0,30,0,30,0,30};
        for(int i=0;i<6;i++){
            dd.set(y,m,d,hh[i],mm[i]);
            if(Calendar.getInstance().getTime().after(dd.getTime()))
                radio[i].setEnabled(false);
            else
                radio[i].setEnabled(true);
        }






        rdo_17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "17:00-18:30";
            }
        });

        rdo_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "09:00-10:30";
            }
        });
        rdo_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "10:30-12:00";

            }
        });
        rdo_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "13:00-14:30";
            }
        });
        rdo_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "14:30-16:00";

            }
        });
        rdo_18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = "18:30-20:00";
            }
        });
    }







    public void ClickBack(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }



}
