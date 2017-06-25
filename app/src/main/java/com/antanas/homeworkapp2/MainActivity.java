package com.antanas.homeworkapp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antanas.homeworkapp2.REST.ApiClient;
import com.antanas.homeworkapp2.REST.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "a87e23b864a045e0978012524e2d8eef";
    TextView infoText;
    Button readButton;
    Button downloadButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoText = (TextView) findViewById(R.id.info_text);
        readButton = (Button) findViewById(R.id.read_button);
        downloadButton = (Button) findViewById(R.id.download_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        downloadButtonOnClick(downloadButton);
        readButtonOnClick(readButton);
    }

    private void readButtonOnClick(Button readButton) {
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                String status, source;
                status = preferences.getString("status", getString(R.string.not_set_string));
                source = preferences.getString("source", getString(R.string.not_set_string));
                infoText.setText("Status: " + status + " Source: " + source);
            }
        });
    }

    private void downloadButtonOnClick(Button downloadButton) {
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    progressBar.setVisibility(View.VISIBLE);
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<RestResponse> call = apiInterface.getStatusAndSource(API_KEY);
                    call.enqueue(new Callback<RestResponse>() {
                        @Override
                        public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                            SharedPreferences preferences = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("status", response.body().getStatus());
                            editor.putString("source", response.body().getSource());
                            editor.apply();
                            progressBar.setVisibility(View.GONE);
                            infoText.setText(R.string.download_finished_text);
                        }

                        @Override
                        public void onFailure(Call<RestResponse> call, Throwable t) {
                            infoText.setText(R.string.network_error_text);
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    infoText.setText(R.string.network_error_text);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
