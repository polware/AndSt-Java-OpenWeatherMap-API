package com.example.openweathermapapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.openweathermapapi.models.WeatherData;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EditTextsearch;
    private TextView textViewcity;
    private TextView textViewcoords;
    private TextView textViewDescription;
    private TextView textViewTemp;
    private TextView textViewHumidity;
    private ImageView image;
    private Button btn;
    private WeatherInterface service;
    private WeatherData weatherData;
    Retrofit retrofit;
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String BASE_ICONS = "https://openweathermap.org/img/w/";
    public static final String EXTENSION_ICONS = ".png";
    String API_KEY="colocar_aqui_la_llave_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(WeatherInterface.class);
        btn.setOnClickListener(this);
    }

    private void setUI() {
        EditTextsearch = findViewById(R.id.editTextSearch);
        textViewcity = findViewById(R.id.textViewCity);
        textViewcoords = findViewById(R.id.textViewCoords);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewTemp = findViewById(R.id.textViewTemperature);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        image = findViewById(R.id.imageViewIcon);
        btn = findViewById(R.id.buttonSearch);
    }

    @Override
    public void onClick(View view) {
        String textCity = EditTextsearch.getText().toString();
        if (textCity != "") {
            service.getWeatherInfo(textCity, API_KEY, "metric", "es").enqueue(new Callback<WeatherData>() {
                @Override
                public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                    weatherData = response.body();

                    textViewcity.setText(new StringBuilder().append(weatherData.getName()).append(", ").append(weatherData.getSys().getCountry()).toString());
                    String latitud = String.valueOf(weatherData.getCoord().getLat());
                    String longitud = String.valueOf(weatherData.getCoord().getLon());
                    textViewcoords.setText(new StringBuilder().append("Latitud: ").append(latitud).append(" | Longitud: ").append(longitud).toString());
                    String descripcion = weatherData.getWeather().get(0).getDescription();
                    textViewDescription.setText(new StringBuilder().append("Descripción: ").append(descripcion).toString());
                    String temperatura = String.valueOf(weatherData.getMain().getTemp());
                    textViewTemp.setText(new StringBuilder().append("Temp: ").append(temperatura).append(" ºC").toString());
                    String humedad = String.valueOf(weatherData.getMain().getHumidity());
                    textViewHumidity.setText(new StringBuilder().append("Humedad: ").append(humedad).append(" %").toString());
                    Picasso.get().load(BASE_ICONS + weatherData.getWeather().get(0).getIcon() + EXTENSION_ICONS).into(image);
                }
                @Override
                public void onFailure(Call<WeatherData> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}