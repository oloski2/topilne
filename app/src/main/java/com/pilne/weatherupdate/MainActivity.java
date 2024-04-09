package com.pilne.weatherupdate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pilne.weatherupdate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText miasto;
    TextView tvResult, textView2, textView3, textView4, textView5, textView6, textView7;
    ImageView imageView2;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat tm = new DecimalFormat("#.#");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miasto = findViewById(R.id.miasto);
        tvResult = findViewById(R.id.tvResult);
        imageView2 = findViewById(R.id.imageView2);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = miasto.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("wypelnij to pole to pilne okokookokkokokokok???");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {

                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if (!jsonResponse.has("main")) {
                            throw new JSONException("nie ma miasta");
                        }

                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");
                        tvResult.setTextColor(Color.rgb(68, 134, 199));

                        if (temp < 10) {
                            imageView2.setImageResource(R.drawable.chmura);
                        } else if (temp >= 10 && temp <= 20) {
                            imageView2.setImageResource(R.drawable.deszcz);
                        } else {
                            imageView2.setImageResource(R.drawable.slonce);
                        }

                        textView2.setText(tm.format(temp) + " °C");
                        textView3.setText("Wilgotność: " + humidity + "%");
                        textView4.setText("Chmury: " + description);
                        textView5.setText("Wiatr: " + wind + "m/s (metry na sekunde)");
                        textView6.setText("% Chmurek: " + clouds + "%");
                        textView7.setText("Ciśnienie: " + pressure + " hPa");
                        tvResult.setText("Miasto: " + cityName + " (" + countryName + ")");



                    } catch (JSONException e) {
                        tvResult.setText("nyma");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {


                public void onErrorResponse(VolleyError error) {
                    tvResult.setText("nyma");
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
