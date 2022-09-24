package com.dmtech.iw.http;
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



    /**
     * 为指定位置生成获取天气数据的URL
     * @param locationId 位置的ID
     * @return 返回locationId对应位置的URL
     */
    public static String getUrl(String locationId) {
        String url = String.format(URL_FORMAT, locationId, API_KEY);
        return url;
    }

}
