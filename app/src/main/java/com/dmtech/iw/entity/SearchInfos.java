package com.dmtech.iw.entity;

import java.util.List;

//描述搜索结果内容
public class SearchInfos {
    //查询到的城市的基本信息对象列表
    private List<Basic> basic;
    //查询状态
    private String status;

    public List<Basic> getBasic() {
        return basic;
    }

    public void setBasic(List<Basic> basic) {
        this.basic = basic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
