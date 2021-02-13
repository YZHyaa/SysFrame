package com.mdt.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class InterfaceTestUtil {

    /***
     *
     * @param pd(包含requestMethod和servelUrl属性)
     * @return 包含errInfo状态信息 、result返回结果  、rTime服务器请求时间毫秒三种属性的Map
     */
    public static Map<String, String> testInterface(PageData pd) {
        String errInfo = "success", str = "", rTime = "";
        Map<String, String> map = new HashMap<String, String>();
        long startTime = System.currentTimeMillis(); // 请求起始时间_毫秒
        try {

            CloseableHttpResponse response = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            if ("POST".equals(pd.get("requestMethod"))) {
                HttpPost httpPost = new HttpPost(pd.getString("serverUrl"));
                response = httpClient.execute(httpPost);
            } else {
                HttpGet httpGet = new HttpGet(pd.getString("serverUrl"));
                response = httpClient.execute(httpGet);
            }
            int code = response.getStatusLine().getStatusCode();
            String conResult = EntityUtils.toString(response.getEntity(), "utf-8");
            if (code == 200) {
                str += "********************************\n";
                str += "result:200(成功) \n";
                str += "********************************\n";
                str += conResult;
            } else {
                str += "********************************\n";
                str += "错误码:" + code + "\n";
                str += "********************************\n";
                errInfo = "error";
            }
        } catch (Exception e) {
            str += "********************************\n";
            str += "出错" + "\n";
            str += "********************************\n";
            errInfo = "error";

        } finally {
            long endTime = System.currentTimeMillis();
            rTime = String.valueOf(endTime - startTime);
        }
        map.put("errInfo", errInfo); // 状态信息
        map.put("result", str); // 返回结果
        map.put("rTime", rTime); // 服务器请求时间 毫秒
        return map;
    }


    /***
     *
     * @param requestMethod请求方法和servelUrl地址参数)
     * @return 包含errInfo状态信息 、result返回结果  、rTime服务器请求时间毫秒三种属性的Map
     */
    public static Map<String, String> testInterface(String method, String url) {
        String errInfo = "success", str = "", rTime = "";
        Map<String, String> map = new HashMap<String, String>();
        long startTime = System.currentTimeMillis(); // 请求起始时间_毫秒
        try {

            CloseableHttpResponse response = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            if ("POST".equals(method)) {
                HttpPost httpPost = new HttpPost(url);
                response = httpClient.execute(httpPost);
            } else {
                HttpGet httpGet = new HttpGet(url);
                response = httpClient.execute(httpGet);
            }
            int code = response.getStatusLine().getStatusCode();
            String conResult = EntityUtils.toString(response.getEntity(), "utf-8");
            if (code == 200) {
                str += "********************************\n";
                str += "result:200(成功) \n";
                str += "********************************\n";
                str += conResult;
            } else {
                str += "********************************\n";
                str += "错误码:" + code + "\n";
                str += "********************************\n";
                errInfo = "error";
            }
        } catch (Exception e) {
            str += "********************************\n";
            str += "出错" + "\n";
            str += "********************************\n";
            errInfo = "error";

        } finally {
            long endTime = System.currentTimeMillis();
            rTime = String.valueOf(endTime - startTime);
        }
        map.put("errInfo", errInfo); // 状态信息
        map.put("result", str); // 返回结果
        map.put("rTime", rTime); // 服务器请求时间 毫秒
        return map;
    }

}
