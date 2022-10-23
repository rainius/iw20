package com.dmtech.iw.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//表示搜索关键字得到的JSON信息
public class SearchResult {
    @SerializedName("HeWeather6")
    private List<SearchInfos> infos;

    public List<SearchInfos> getInfos() {
        return infos;
    }

    public void setInfos(List<SearchInfos> infos) {
        this.infos = infos;
    }
}
