package com.mdt.util;

public class MercatorLatLngUtil {

    /// 经纬度转墨卡托
    public static double[] lonLat2Mercator(double lon, double lat) {
        double x = lon * 20037508.34 / 180;
        double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.34 / 180;
        return new double[]{x, y};
    }


    /// 墨卡托转经纬度
    public static double[] Mercator2lonLat(double x, double y) {
        double lon = x / 20037508.34 * 180;
        double lat = y / 20037508.34 * 180;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
        return new double[]{lon, lat};
    }


}
