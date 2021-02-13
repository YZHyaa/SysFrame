package com.mdt.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EncryptUtil {
    private static Logger logger = Logger.getLogger(EncryptUtil.class);
    private static String keyPassword = "25d55ada25d55ada";
    private static Gson gson = new Gson();

    public static void main(String[] args) {
    /*	Map queryParams=new HashMap();
		queryParams.put("USERNAME", "admin");
		queryParams.put("PASSWORD", "1");
		String ret=EncryptUtil.encryptMsg(queryParams);
		System.out.println(JsonUtil.getJsonStr(ret));*/


        try {
            String msgStr = "{'data':'eyJQQVNTV09SRCI6ImM0Y2E0MjM4YTBiOTIzODIwZGNjNTA5YTZmNzU4NDli+IiwiVVNFUk5BTUUiOiJhZG1pbiJ9','token':'%2FOjqBsDY%2BCCRWAHVeNTrSRUeSzb5hutOUcfqDsZMiGTtsBqFCJbSUaRoMyO7+rOMk'}";
            Gson gson = new Gson();
            Map<String, String> bodyMap = gson.fromJson(msgStr, new TypeToken<Map<String, String>>() {
            }.getType());
             
            /* Gson gson = new Gson();
             JsonReader reader = new JsonReader(new StringReader(msgStr.trim()));
             reader.setLenient(true);
             Map bodyMap = gson.fromJson(reader, Map.class);*/

            System.out.println(bodyMap);

            System.out.println("{\"token\":\"xR08pSJAefXuqx2+Tx6Ohhl92BP8HSDX8JM+WdSc8t/RaNJwLI4kVwdFwFzS 66ls\",\"data\":\"eyJQSE9ORSI6IjEzMTczMjI2Mjk5IiwiTE9HSU5TVEFURSI6IlNVQ0NFU1Mi LCJOQU1FIjoi57O757uf566h55CG5ZGYIiwiVVNFUk5BTUUiOiJhZG1pbiJ9 \"}".length());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * @param request
     * @return Map<String,String>
     * @throws
     * @Description: 解密信息
     */
    public static Map<String, String> decrypt(HttpServletRequest request) {
        Map<String, String> msgMap = null;
        int len = request.getContentLength();
        if (len > 0) {
            InputStream is = null;
            BufferedReader br = null;
            StringBuffer bodyBf = new StringBuffer();
            try {
                request.setCharacterEncoding("UTF-8");
                is = request.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String buffer = null;
                while ((buffer = br.readLine()) != null) {
                    bodyBf.append(buffer);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.info("接收数据：" + bodyBf.toString());
            try {

                if (bodyBf.length() > 0) {
                    //消息转换
                    msgMap = decryptMsg(bodyBf.toString());
                    if (msgMap != null) {
                        msgMap.put("sendIp", RequestIpUtil.getIpAddress(request));
                    }
                }
            } catch (Exception e) {
                String message = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
                logger.error("EncryptUtil.decrypt异常错误：" + message);
            }
        }
        return msgMap;
    }

    /**
     * @param key
     * @param msg
     * @return String
     * @throws Exception
     * @throws
     * @Description: 获取数据
     */
    public static Map<String, String> decryptMsg(String bodyStr) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> bodyMap = gson.fromJson(bodyStr, new TypeToken<Map<String, String>>() {
            }.getType());
            String key = bodyMap.get("token");
            String msg = bodyMap.get("data");
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(msg)) {
                String msgMd5 = MD5.md5(msg);
                String aesStr = AESUtil.encrypt(keyPassword, msgMd5);
                if (aesStr.equals(key)) {
                    String msgStr = new String(Base64.decode(msg), "UTF-8");
                    Map<String, String> msgmap = gson.fromJson(msgStr, new TypeToken<Map<String, String>>() {
                    }.getType());
                    return msgmap;
                } else {
                    //不处理
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
            logger.error("EncryptUtil.decryptMsg异常错误：" + message);
            return null;
        }
        return null;


    }

    /**
     * @param dataMap
     * @return String
     * @throws
     * @Description: 加密MSG
     */
    public static Map encryptMsg(Object data) {
        try {
            Map jsonMap = new HashMap();
            //生成JSON格式
            Gson gson = new Gson();
            String jsonStr = gson.toJson(data);
            //String jsonStr = JsonUtil.getJsonStr(dataMap);
            //JSON格式转换成64位
            String base64Str = Base64.encode(jsonStr.getBytes("UTF-8"));
            //按MD5方式加密
            String md5Str = MD5.md5(base64Str);
            //按AES加密
            String aesStr = AESUtil.encrypt(keyPassword, md5Str);
            jsonMap.put("data", base64Str);
            jsonMap.put("token", aesStr);
            //生成最终JSON格式
            String resultJson = gson.toJson(jsonMap);
//	         String resultJson=JsonUtil.getJsonStr(jsonMap);
//	         System.out.println("return:"+resultJson);
            return jsonMap;
        } catch (Exception e) {
            String message = e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage();
            logger.error("EncryptUtil.encryptMsg异常错误：" + message);
            return null;
        }

    }

}
