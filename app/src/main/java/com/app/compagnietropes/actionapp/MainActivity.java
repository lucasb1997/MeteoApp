package com.app.compagnietropes.actionapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.compagnietropes.actionapp.models.APIServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private Button button;
    private Button buttonDetails;
    private TextInputEditText textInput;
    private List<String> listExample = new ArrayList<String>();
    private ArrayAdapter<String> adapter = null;
    public static final String EXTRA_MESSAGE = "city";
    public static final String SHARED_PREFS="sharedPrefs";
    public static final String TEXT = "";
    public static final String SWITCH1 ="switch1";
    public String text ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
        textInput =(TextInputEditText) findViewById(R.id.textInput);
        buttonDetails =(Button) findViewById(R.id.details);

        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listExample);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = textInput.getText().toString();

                requestAPI(city);
            }
        });

        buttonDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = textInput.getText().toString();
                detailsActivity(city);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listExample);
        listView.setAdapter(adapter);

        loadData();


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setEmptyView(empty);
    }

    private void detailsActivity(String city){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE,city);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT,textInput.getText().toString());
        editor.apply();

        //Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT,"");

        if(!text.isEmpty()){
            requestAPI(text);
            textInput.setText(text);
        }

    }

    private void requestAPI(String city){
        APIServices apiServices = new APIServices();
        listExample.clear();
        listExample = apiServices.getMeteoFromCity(city);
        System.out.println("WTF !!!!!!!!!!!!!!!!!!" + listExample.size());
        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,listExample);
        if(listExample.size()>0){
            saveData();
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);


                }
            });


        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("alert");
            alertDialog.setMessage("City not found");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }




    }


}


