package com.dmtech.iw.http;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

// 网络访问助手
public class HttpHelper {
    //用于测试的样例位置ID列表
    public static final String[] LOCATION_IDS = {
            "CN101010800",  // 北京
            "CN101131012",  // 新疆伊犁
            "CN101310304",  // 南沙
            "US3290097",    // 洛杉矶
            "AU2147714"     // 悉尼
    };

    // 获取天气数据URL的格式串
    private static final String URL_FORMAT =
            "https://free-api.heweather.net/s6/weather?location=%s&key=%s&lang=zh";
    // 和风天气KEY
    private static final String API_KEY = "8f33976d35974b88866e05b83993bc04";
    // 天气图标URL模板
    private static final String ICON_URL_FORMAT = "http://182.92.0.47/iweather/icons/%s.png";

    //定义天气名字
    private static final String SUNNY = "sunny";    //晴
    private static final String CLOUDY = "cloudy";  //多云
    private static final String OVERCAST = "overcast";  //阴
    private static final String RAINY = "rainy";    //雨
    private static final String SNOWY = "snowy";    //雪
    //定义天气背景图URL模板
    private static final String BG_URL_FORMAT =
            "http://182.92.0.47/iweather/background/%s.jpg";

    // 位置搜索URL模板
    private static final String SEARCH_LOCATION_URL_FORMAT =
            "https://search.heweather.net/find?location=%s&key=%s&lang=zh";

    // 生成特定关键词搜索URL
    public static String getSearchLocationUrl(String keyword) {
        return String.format(SEARCH_LOCATION_URL_FORMAT, keyword, API_KEY);
    }

    /**
     * 为指定位置生成获取天气数据的URL
     * @param locationId 位置的ID
     * @return 返回locationId对应位置的URL
     */
    public static String getUrl(String locationId) {
        String url = String.format(URL_FORMAT, locationId, API_KEY);
        return url;
    }

    /**
     * 组成天气图标完成URL
     * @param conditionCode 天气代码
     * @return 天气图标URL
     */
    public static String getIconUrl(String conditionCode) {
        return String.format(ICON_URL_FORMAT, conditionCode);
    }

    /**
     * 根据天气代码获取对应的背景图
     * @param conditionCode 天气代码
     * @return 背景图URL
     */
    public static String getBackgroundUrl(String conditionCode) {
        String name = "";
        //TODO:将天气码映射到天气名字
        char head = conditionCode.charAt(0);
        if (head == '1') {  //天气码以1开头，分情况映射
            switch (conditionCode) {
                case "100":
                    name = SUNNY;
                    break;
                case "101":
                case "102":
                case "103":
                    name = CLOUDY;
                    break;
                case "104":
                    name = OVERCAST;
            }
        } else if (head == '3') {
            name = RAINY;
        } else if (head == '4') {
            name = SNOWY;
        } else {    //未知情况，简单归为晴天
            name = SUNNY;
        }
        return String.format(BG_URL_FORMAT, name);
    }

    /**
     * 通过OkHttp3访问网络服务
     * @param url: 被访问网络服务的地址
     * @param callback: 处理网络响应的回调对象
     */
    public static void requestByOkHttp3(String url, Callback callback) {
        // 创建OkHttp客户端对象
        OkHttpClient client = new OkHttpClient();
        // 构建访问url的请求对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        //客户端发起请求，并将任务放入异步队列
        client.newCall(request).enqueue(callback);
    }


}
