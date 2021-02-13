package com.mdt.util;

import org.apache.shiro.crypto.hash.SimpleHash;

public class TestUtil {

    public static void main(String[] args) {
        System.out.println(new SimpleHash("SHA-1", "panglin", "1").toString());
    }

}
