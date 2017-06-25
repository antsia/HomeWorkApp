package com.antanas.homeworkapp2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by antas on 2017-06-25.
 */

public class RestResponse {
    @SerializedName("status")
    private String status = "";
    @SerializedName("source")
    private String source = "";

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }
}
