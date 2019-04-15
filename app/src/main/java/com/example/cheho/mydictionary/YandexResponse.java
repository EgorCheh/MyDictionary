package com.example.cheho.mydictionary;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YandexResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("lang")
    @Expose
    private String lang;
    @SerializedName("text")
    @Expose
    private List<String> text = null;

    public YandexResponse(Integer code, String lang, List<String> text) {
        this.code = code;
        this.lang = lang;
        this.text = text;
    }



    public Integer getCode() {
        return code;
    }

        public String getLang() {
        return lang;
    }

    public List<String> getText() {
        return text;
    }



}
