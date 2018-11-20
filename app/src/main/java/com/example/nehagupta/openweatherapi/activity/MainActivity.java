package com.example.nehagupta.openweatherapi.activity;

import android.content.Context;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nehagupta.openweatherapi.R;
import com.example.nehagupta.openweatherapi.model.Weather;
import com.example.nehagupta.openweatherapi.model.WeatherContributor;
import com.example.nehagupta.openweatherapi.rest.ApiClient;
import com.example.nehagupta.openweatherapi.rest.ApiInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tvInternet, tvTemp, tvHum, tvDesc;
    private final static String API_KEY = "3475d70695cba4c42abeed83d14d253b";
    //private String cityname = "London";
    private LinearLayout llData;
    private Button btShow;
    //private Context context;
    private EditText etCity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInternet = (TextView) findViewById(R.id.tvInternet);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        tvHum= (TextView) findViewById(R.id.tvHum);
        tvDesc= (TextView) findViewById(R.id.tvDescrip);
        llData = (LinearLayout) findViewById(R.id.llData);
        btShow = (Button) findViewById(R.id.btDisplay);
        etCity = (EditText)findViewById(R.id.etCity);
        //context = getApplicationContext();


        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etCity.getText() == null || etCity.getText().toString().equals("")) {
                    llData.setVisibility(View.GONE);
                    tvInternet.setVisibility(View.VISIBLE);
                    tvInternet.setText("Enter City name");
                    }else{

                        doConnection(etCity.getText().toString());
                        }

            }
        });



    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    private void doConnection(String cityname){
        if(isOnline(this)) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            Call<WeatherContributor> call = apiService.getWeatherDetails(cityname, API_KEY);
            call.enqueue(new Callback<WeatherContributor>() {
                @Override
                public void onResponse(Call<WeatherContributor> call, Response<WeatherContributor> response) {
                    Log.d("MainActivity", "Hello "+String.valueOf(response.body()));
                    if(response.body() == null){
                       llData.setVisibility(View.GONE);
                       tvInternet.setVisibility(View.VISIBLE);
                       tvInternet.setText("City Not Found");
                   }else {
                        if(llData.getVisibility()==View.GONE) {
                            tvInternet.setVisibility(View.GONE);
                            llData.setVisibility(View.VISIBLE);
                        }
                        Float temp = (response.body().getMain().getTemp() - 273.15f);
                        tvTemp.setText(String.valueOf(temp +"C"));
                       tvHum.setText(String.valueOf(response.body().getMain().getHumidity()));
                       tvDesc.setText(String.valueOf(response.body().getWeather().get(0).getDescription()));

                   }
                }

                @Override
                public void onFailure(Call<WeatherContributor> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Response issue", Toast.LENGTH_LONG).show();
                }
            });

        }
        else {
            llData.setVisibility(View.GONE);
            tvInternet.setVisibility(View.VISIBLE);
            tvInternet.setText("Check Internet Connection");
        }

    }
}


