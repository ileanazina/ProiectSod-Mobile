package com.example.myapplication1.Activities;

import android.content.Context;
import android.os.StrictMode;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.text.*;

public class Payments {

    public static void payInvoice(int invoiceID, float toBePaid){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String mid="testaccount";
        String key="00112233445566778899AABBCCDDEEFF";

        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        List<Pair> s=new ArrayList();

        //acest rand este comentat decoarece euPlatesc accepta teste doar de 0-1 lei
        //s.add(new Pair<>("amount", String.valueOf(toBePaid)));

        s.add(new Pair<>("amount", "0.5"));
        s.add(new Pair<>("curr", "RON"));
        s.add(new Pair<>("invoice_id", String.valueOf(invoiceID)));
        s.add(new Pair<>("order_desc", "Ordin-de-plata"));
        s.add(new Pair<>("merch_id", mid));
        s.add(new Pair<>("timestamp", dateString));
        s.add(new Pair<>("nonce", nonceGen(32)));
        s.add(new Pair<>("fp_hash", fp_hash(s,key)));

        String paramList = buildURL("", s);
        paramList += s.get(s.size()-1).first + "=" + s.get(s.size()-1).second;
        String url = "https://secure.euplatesc.ro/tdsprocess/tranzactd.php?";
        String window = executePost(url, paramList);

        System.out.println(window);
    }

    public static String nonceGen(int len){
        String AlphaNumericString = "ABCDEF0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static byte[] hex2byte(String key){
        int len = key.length();
        byte[] bkey = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bkey[i / 2] = (byte) ((Character.digit(key.charAt(i), 16) << 4) + Character.digit(key.charAt(i+1), 16));
        }
        return bkey;
    }

    public static String fp_hash(List<Pair> s, String key){
        StringBuffer ret = new StringBuffer();
        Formatter formatter = new Formatter();
        String t;
        Integer l;
        for(int i = 0; i < s.size(); i++)
        {
            t = s.get(i).second.toString().trim();
            if(t.length() == 0)
                ret.append("-");
            else
            {
                l = t.length();
                ret.append(l.toString());
                ret.append(t);
            }
        }
        String data=ret.toString();
        try
        {
            SecretKeySpec secretKeySpec = new SecretKeySpec(hex2byte(key), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secretKeySpec);
            for (byte b : mac.doFinal(data.getBytes())) {
                formatter.format("%02x", b);
            }
        }
        catch(InvalidKeyException e){}
        catch (NoSuchAlgorithmException e) {}

        return formatter.toString().toUpperCase();
    }

    public static String buildURL(String url, List<Pair> list)
    {
        for(int i = 0; i < list.size()-1 ; i++)
        {
            if(list.get(i).second == "")
                url += '-';
            else url += list.get(i).first + "=" + list.get(i).second + '&';
        }
        return url;
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}