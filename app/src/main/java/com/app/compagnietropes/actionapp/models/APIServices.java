package com.app.compagnietropes.actionapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIServices {





    public  List<String> getMeteoFromCity(String city){
        OkHttpClient client = new OkHttpClient();
        final List<String> listExample = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();


                    JSONObject object = null;

                    JSONArray json = null;
                    try {
                        object = new JSONObject(myResponse);



                        json =  object.getJSONArray("list");
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    listExample.clear();
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            JSONObject row = json.getJSONObject(i);
                            Integer id = row.getJSONObject("main").getInt("temp");
                            String dateS = row.getString("dt_txt");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM HH");
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateS);
                            dateS=simpleDateFormat.format(date);

                            String weather = row.getJSONArray("weather").getJSONObject(0).getString("description");
                            switch (weather){
                                case "clear sky":
                                    listExample.add(id.toString() + "°C" + "     " + dateS + "h  " + weather + "  ☀️");
                                    break;
                                case "few clouds":
                                    listExample.add(id.toString() + "°C" + "     " + dateS + "h  " + weather + "  \uD83C\uDF24️");
                                    break;
                                case "scattered clouds":
                                    listExample.add(id.toString() + "°C" + "     " + dateS + "h  " + weather + "  ☁️");
                                    break;
                                case "broken clouds":
                                    listExample.add(id.toString() + "°C" + "     " + dateS + "h  " + weather + "  \uD83C\uDF29️");
                                    break;
                                case "shower rain":
                                    listExample.add(id.toString() + "°C" + "     " + dateS + "h  " + weather + "  \uD83C\uDF26️");
                                    break;
                                case "rain":
                                    listExample.add(id.toString() + "°C" + "     " + dateS+ "h  " + weather + "  \uD83C\uDF27️");
                                    break;
                                case "thunderstorm":
                                    listExample.add(id.toString() + "°C" + "     " + dateS+ "h  " + weather + "  ⛈️");
                                    break;
                                case "snow":
                                    listExample.add(id.toString() + "°C" + "     " + dateS+ "h  " +  weather + "  \uD83C\uDF28️");
                                    break;
                                case "mist":
                                    listExample.add(id.toString() + "°C" + "     " + dateS+ "h  " +  weather +"  \uD83C\uDF2B️");
                                    break;
                            }
                            //String name = row.getString("name");
                            //listExample.add(id.toString() + "°C" + "     " + date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    countDownLatch.countDown();



                }else{

                    countDownLatch.countDown();


                }

            }
        };



        // Details url //String url = "http://api.openweathermap.org/data/2.5/weather?q="+ city+",fr&APPID=9a315941e5a343c41de9da5235de1ab0";
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+city+"&units=metric&appid=9a315941e5a343c41de9da5235de1ab0";
        final Boolean[] lock = {false};



        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback
                );



        try {
            countDownLatch.await();

            return listExample;
        } catch (InterruptedException e) {

            e.printStackTrace();
            return listExample;
        }



    }

    public Weather getCityDescription(String city){
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&APPID=9a315941e5a343c41de9da5235de1ab0";
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final Weather[] weatherObject = {null};
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    JSONObject object = null;
                    JSONArray json = null;
                    try{
                        object = new JSONObject(myResponse);
                        json =  object.getJSONArray("weather");
                        JSONObject weather = json.getJSONObject(0);
                        JSONObject main = object.getJSONObject("main");
                        JSONObject wind = object.getJSONObject("wind");
                        JSONObject clouds = object.getJSONObject("clouds");
                        String icon = weather.getString("icon");


                        final String description = weather.getString("description");
                        final String temperature = main.getString("temp");
                        final String pression = main.getString("pressure");
                        final String humidite = main.getString("humidity");
                        final String tempMin = main.getString("temp_min");
                        final String tempMax = main.getString("temp_max");
                        final String ventVit = wind.getString("speed");

                        final String cloud = clouds.getString("all");

                        weatherObject[0] = new Weather(description,icon,temperature,pression,humidite,tempMin,tempMax,ventVit,cloud);
                        countDownLatch.countDown();




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await();
            return weatherObject[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return weatherObject[0];
        }


    }

    public HashMap<Date, Integer> getMeteoGraphFromCity(String city){
        OkHttpClient client = new OkHttpClient();
        final HashMap<Date, Integer> listExample = new HashMap<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();


                    JSONObject object = null;

                    JSONArray json = null;
                    try {
                        object = new JSONObject(myResponse);



                        json =  object.getJSONArray("list");
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    listExample.clear();
                    for (int i = 0; i < 7; i++) {
                        try {
                            JSONObject row = json.getJSONObject(i);
                            Integer id = row.getJSONObject("main").getInt("temp");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
                            String dateS = row.getString("dt_txt");
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateS);

                            listExample.put(date,id);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    countDownLatch.countDown();



                }else{

                    countDownLatch.countDown();


                }

            }
        };



        // Details url //String url = "http://api.openweathermap.org/data/2.5/weather?q="+ city+",fr&APPID=9a315941e5a343c41de9da5235de1ab0";
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+city+"&units=metric&appid=9a315941e5a343c41de9da5235de1ab0";
        final Boolean[] lock = {false};



        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback
        );



        try {
            countDownLatch.await();

            return listExample;
        } catch (InterruptedException e) {

            e.printStackTrace();
            return listExample;
        }



    }




}
