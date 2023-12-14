package com.example.mangaapp_finalproject.api.type.Statistic;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Statistic {
    @SerializedName("statistics")
    public Map<String, StatisticDetail> statistics;
}

