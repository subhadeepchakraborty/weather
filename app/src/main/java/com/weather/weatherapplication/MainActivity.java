package com.weather.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.weather.weatherapplication.Adapter.Recycleview_adapter;
import com.weather.weatherapplication.recycleviewlist.recyclelist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
LocationManager locationManager;
TextView citytext,date,temperature,humidity,main;
int permissiocode=101;
ProgressDialog pg;
  Handler handler;
 Runnable r;
public String city ;
private RequestQueue mRequestQueue;
private StringRequest mStringRequest;
private List<recyclelist> list = new ArrayList<>();
private RecyclerView sheet_recycleview;
private Recycleview_adapter adapter;
public recyclelist list1;
String tom1,tom2,tom3,tom4,tom5,tom6,tom7,tom8,tom9,tom10,tom11,tom12,tom13,tom14;
float chart1,chart2,chart3,chart4,chart5,chart6,chart7;
float second1,second2,second3,second4,second5,second6;
private LineChart mChart;
ImageView im1,im2,im3,im4,im5,im6,im7;
String url1,url2,url3,url4,url5,url6;
BottomSheetBehavior bottomSheetBehavior;
LinearLayout linearLayout;
String[] day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citytext=findViewById(R.id.city);
        date=findViewById(R.id.date);
        pg=new ProgressDialog(this);
        temperature=findViewById(R.id.temperature);
        humidity=findViewById(R.id.humidity);
        im1=findViewById(R.id.im1);
        im2=findViewById(R.id.im2);
        im3=findViewById(R.id.im3);
        im4=findViewById(R.id.im4);
        im5=findViewById(R.id.im5);
        im6=findViewById(R.id.im6);
        im7=findViewById(R.id.im7);
        day=new String[10];
        main=findViewById(R.id.main);
        mChart = findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        pg.setCancelable(false);

        getday();
        grantpermission();
        getanimation();
        sheet_recycleview = (RecyclerView) findViewById(R.id.sheet_recycleview);

        adapter = new Recycleview_adapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        sheet_recycleview.setLayoutManager(mLayoutManager);
        sheet_recycleview.setItemAnimator(new DefaultItemAnimator());
        sheet_recycleview.setAdapter(adapter);
        getanimation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();

        r = new Runnable() {
            public void run() {
            init();
                handler.postDelayed(r, 1000);

            }
        };

        handler.postDelayed(r, 1000);
    }


    private void init() {
        this.linearLayout=findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior=BottomSheetBehavior.from(linearLayout);
        if (bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED) {
            AnimationSet set = new AnimationSet(true);
            Animation trAnimation = new TranslateAnimation(0, -100, 0, -20);
            trAnimation.setDuration(2000);
            set.addAnimation(trAnimation);
            citytext.startAnimation(set);
            date.startAnimation(set);
            temperature.startAnimation(set);
            main.startAnimation(set);
            humidity.startAnimation(set);
            trAnimation.setFillAfter(true);
            handler.removeCallbacks(r);
            handler.post(r);


        }
    }



    private void getanimation() {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(-40, 0, im1.getHeight(), 20);
        trAnimation.setDuration(5000);

        trAnimation.setRepeatMode(Animation.REVERSE); // This will make the view translate in the reverse direction

        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(5000);
        set.addAnimation(anim);

        im1.startAnimation(set);
        im2.startAnimation(set);
        im3.startAnimation(set);
        im4.startAnimation(set);
        im5.startAnimation(set);
        im6.startAnimation(set);
        im7.setAnimation(set);
        mChart.startAnimation(set);




    }

    private void getchart() {
        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(1 ,chart1));
        values.add(new Entry(2, chart2));
        values.add(new Entry(3, chart3));
        values.add(new Entry(4, chart4));
        values.add(new Entry(5, chart5));
        values.add(new Entry(6, chart6));
        values.add(new Entry(7, chart7));



        LineDataSet set1;

            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);
            set1.isDrawCubicEnabled();
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setValueTextColor(Color.WHITE);
            mChart.getXAxis().setDrawGridLines(false);

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            set1.setFillColor(Color.WHITE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
            mChart.getDescription().setEnabled(false);
            mChart.getLegend().setEnabled(false);

            mChart.getAxisLeft().setDrawGridLines(false);
            mChart.getAxisLeft().setDrawLabels(false);
            mChart.getAxisLeft().setDrawAxisLine(false);

            mChart.getXAxis().setDrawGridLines(false);
            mChart.getXAxis().setDrawLabels(false);
            mChart.getXAxis().setDrawAxisLine(false);

            mChart.getAxisRight().setDrawGridLines(false);
            mChart.getAxisRight().setDrawLabels(false);
            mChart.getAxisRight().setDrawAxisLine(false);

        mChart.animateX(4000, Easing.EasingOption.EaseInCirc);

    }

    private void getday() {
        SimpleDateFormat dateFormat= new SimpleDateFormat("EEEE ");
        Calendar currentCal = Calendar.getInstance();
        String currentdate=dateFormat.format(currentCal.getTime());
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
         int j=0;
        for(int i=1;i<=7;i++) {

            currentCal.add(Calendar.DATE,j+1);

            String toDate = dateFormat.format(currentCal.getTime());
            day[i]= toDate;
        }
        date.setText(currentdate+","+currentDateTimeString);

    }

    public void loaddata_in_recycleview()
    {
        list1 = new recyclelist(url1,"Tomorrow", String.valueOf(chart2).substring(0,4)+"°C",String.valueOf(second1).substring(0,4)+"°C");
        list.add(list1);
        list1 = new recyclelist( url2,day[2], String.valueOf(chart3).substring(0,4)+"°C", String.valueOf(second2).substring(0,4)+"°C");
        list.add(list1);
        list1 = new recyclelist( url3,day[3], String.valueOf(chart4).substring(0,4)+"°C", String.valueOf(second3).substring(0,4)+"°C");
        list.add(list1);
        list1 = new recyclelist(url4, day[4], String.valueOf(chart5).substring(0,4)+"°C", String.valueOf(second4).substring(0,4)+"°C");
        list.add(list1);
        list1 = new recyclelist( url5,day[5], String.valueOf(chart6).substring(0,4)+"°C", String.valueOf(second5).substring(0,4)+"°C");
        list.add(list1);
        list1 = new recyclelist( url6,day[6], String.valueOf(chart7).substring(0,4)+"°C", String.valueOf(second6).substring(0,4)+"°C");
        list.add(list1);
        adapter.notifyDataSetChanged();

    }

    public void getweather(String city, double latitude, double longitude) {
        mRequestQueue = Volley.newRequestQueue(this);
        citytext.setText(city);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&exclude=current,minutely,hourly&appid=4c7669a79ba91d3c1942718f6413f7e4";
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(0); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                   String humiditystr= locArrObj.getString("humidity");
                   humidity.setText(humiditystr+"%");
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String str_Name=temparr.getString("day");
                    chart1=Float.parseFloat(str_Name)-273;
                    temperature.setText(String.valueOf(chart1).substring(0,4)+"°C");
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String description,icon;
                        description=weatherobj.getString("description");
                        icon=weatherobj.getString("icon");
                        String url="https://openweathermap.org/img/w/" + icon + ".png";
                        main.setText(description);
                        Glide.with(MainActivity.this).load(url).into(im1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(1); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom1=min;
                    tom2=max;
                    chart2=Float.parseFloat(tom1)-273;
                    second1=Float.parseFloat(tom2)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url1="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url1).into(im2);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(2); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom3=min;
                    tom4=max;
                    chart3=Float.parseFloat(tom3)-273;
                    second2=Float.parseFloat(tom4)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url2="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url2).into(im3);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(3); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom5=min;
                    tom6=max;
                    chart4=Float.parseFloat(tom5)-273;
                    second3=Float.parseFloat(tom6)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url3="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url3).into(im4);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(4); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom7=min;
                    tom8=max;
                    chart5=Float.parseFloat(tom7)-273;
                    second4=Float.parseFloat(tom8)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url4="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url4).into(im5);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(5); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom9=min;
                    tom10=max;
                    chart6=Float.parseFloat(tom9)-273;
                    second5=Float.parseFloat(tom10)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url5="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url5).into(im6);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(6); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom11=min;
                    tom12=max;
                    chart7=Float.parseFloat(tom11)-273;
                    second6=Float.parseFloat(tom12)-273;
                    for (int i = 0; i < conferenceLocArr.length(); i++) {
                        JSONObject weatherobj=conferenceLocArr.getJSONObject(i);
                        String icon;
                        icon=weatherobj.getString("icon");
                        url6="https://openweathermap.org/img/w/" + icon + ".png";
                        Glide.with(MainActivity.this).load(url6).into(im7);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    JSONArray locArr = obj.getJSONArray("daily"); // contains one object
                    JSONObject locArrObj = locArr.getJSONObject(7); // cotains one "out" array
                    JSONArray conferenceLocArr = locArrObj.getJSONArray("weather");
                    JSONObject o = null;
                    JSONArray arr = null;
                    JSONObject temparr = locArrObj.getJSONObject("temp");
                    String min=temparr.getString("min");
                    String max=temparr.getString("max");
                    tom13=min;
                    tom14=max;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loaddata_in_recycleview();
                getchart();
                pg.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"false", Toast.LENGTH_LONG).show();//display the response on screen

            }


        });


        mRequestQueue.add(mStringRequest);

    }

    public void getlocation()
    { try {
        pg.setMessage("Fetching Data...please wait...");
        pg.show();
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,5,(LocationListener)this);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void checklocationstatus()
    {
        LocationManager lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsenable=false;
        boolean networkenable=false;

        try {
            gpsenable=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkenable=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!gpsenable && !networkenable)
        {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Give permission")
                    .setPositiveButton("enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();

        }

    }

    public void grantpermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
            else
            {
                getlocation();
                checklocationstatus();
            }
        }

    }


    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> address=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
          //city_textview.setText(address.get(0).getLocality());

            double latitude=address.get(0).getLatitude();
            double longitude=address.get(0).getLongitude();
            getweather(address.get(0).getLocality(),latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (100) {
            case 100: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //                    grantResult[0] means it will check for the first postion permission which is READ_EXTERNAL_STORAGE
                    //                    grantResult[1] means it will check for the Second postion permission which is CAMERA
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    getlocation();
                    checklocationstatus();
                }
                else {
                    grantpermission();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Want to exit");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
}