package com.antanas.homeworkapp2.REST;

import com.antanas.homeworkapp2.RestResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by antas on 2017-06-25.
 */

public interface ApiInterface {
    @GET("articles?source=techcrunch")
    Call<RestResponse> getStatusAndSource(@Query("ApiKey") String apiKey);
}
