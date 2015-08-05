package com.codi.frame.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    public static String encrypt(String input, String key) {

        byte[] crypted = null;

        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, skey);

            crypted = cipher.doFinal(input.getBytes());

        } catch (Exception e) {

            System.out.println(e.toString());

        }

//        return new String(Base64.encodeBase64(crypted));
        return new String(Base64.encodeToString(crypted, Base64.DEFAULT));

    }

    public static String decrypt(String input, String key) {

        byte[] output = null;

        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, skey);

//            output = cipher.doFinal(Base64.decodeBase64(input));
            output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));

        } catch (Exception e) {

            System.out.println(e.toString());

        }

        return new String(output);

    }

    public static void main(String[] args) {

        String key = "1234567890123456";

        String data = "example";

        System.out.println(AESUtil.encrypt(data, key));

        System.out.println(AESUtil.decrypt(AESUtil.encrypt(data, key), key));

    }

}