package com.app.compagnietropes.actionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.compagnietropes.actionapp.models.APIServices;
import com.app.compagnietropes.actionapp.models.Weather;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity  {

    private TextView textView;
    private static final String TAG = "DetailsActivity";
    private ImageView imageView;
    private GraphView graph;
    LineGraphSeries<DataPoint> series = null;
    private String urlImage ="http://openweathermap.org/img/w/";
    private TextView textView2;
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT = "";
    public static final String SWITCH1 ="switch1";
    public String text ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String city = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView.setText(city);

        graph = (GraphView) findViewById(R.id.graph);

        request2API(city);
        requestGraph2API(city);





    }


    private void request2API(String city){
        APIServices apiServices = new APIServices();
        Weather weather = apiServices.getCityDescription(city);

        if(weather!=null){
            textView2.setText(weather.getDescription() + "  \n" + "temperature : " + weather.getTemp() + "°C" +"\n" +
                    "pressure : " + weather.getPressure() + " hPa " + "\n"+"humidity : " + weather.getHumidity() + "%" +"\n" +
                    "minimal : " + weather.getTempMin() +"°C\n" + "maximale : " + weather.getTempMax() + "°C\n" + "wind : " + weather.getSpeed() +" m/s\n" + "cloud : " + weather.getCloud() +"%");
            urlImage += weather.getIcon() +".png";
            Picasso.with(DetailsActivity.this).load(urlImage).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(imageView);
        }

    }

    private void requestGraph2API(String city){
        final SimpleDateFormat sdf = new SimpleDateFormat("HH'h'");
        APIServices apiServices = new APIServices();
        HashMap<Date,Integer> mapDate = apiServices.getMeteoGraphFromCity(city);
        List<Date> listDate = new ArrayList<>();
        List<Integer> listTemp = new ArrayList<>();
        for(Date d : mapDate.keySet()){
            listDate.add(d);


        }
        Collections.sort(listDate);
        System.out.println("DATE :" + listDate.get(0));
        System.out.println("DATE :" + listDate.get(1));
        System.out.println("DATE :" + listDate.get(2));
        System.out.println("DATE :" + listDate.get(3));

        for(Date d : listDate){
            listTemp.add(mapDate.get(d));
        }
        series = new LineGraphSeries<>(new DataPoint[]{

                new DataPoint(listDate.get(0),listTemp.get(0)),
                new DataPoint(listDate.get(1),listTemp.get(1)),
                new DataPoint(listDate.get(2),listTemp.get(2)),
                new DataPoint(listDate.get(3),listTemp.get(3)),

        });


        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long)value));
                }else{
                    return super.formatLabel(value, isValueX)+ "°C";
                }

            }
        });
        graph.setTitle("next hours weather");
        //graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);




    }


}
