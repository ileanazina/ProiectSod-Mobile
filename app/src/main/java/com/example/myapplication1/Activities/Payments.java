package com.example.myapplication1.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Pair;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication1.Model.InvoiceModel;
import com.example.myapplication1.R;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.text.*;

public class Payments extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ProgressDialog dialog= ProgressDialog.show(this, null, getResources().getString(R.string.payment_please_wait));
        setContentView(R.layout.activity_payments);
        Intent intent = getIntent();
        InvoiceModel invoiceModel = (InvoiceModel)intent.getSerializableExtra("extra");
        payInvoice(invoiceModel.getInvoiceId(),invoiceModel.getValueWithVat(), dialog);
    }

    public void payInvoice(int invoiceID, float toBePaid, ProgressDialog dialog){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String mid="44840993126";
        String key="5A1018BDB17A05C676550B89482C3CA15505BB28";

        Date date = new Date();
        String dateString = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        List<Pair> s=new ArrayList();

        //acest rand este comentat decoarece euPlatesc accepta teste doar de 0-1 lei
        //s.add(new Pair<>("amount", String.valueOf(toBePaid)));

        s.add(new Pair<>("amount", "1"));
        s.add(new Pair<>("curr", "RON"));
        s.add(new Pair<>("invoice_id", String.valueOf(invoiceID)));
        s.add(new Pair<>("order_desc", getResources().getString(R.string.payment_pay_order)));
        s.add(new Pair<>("merch_id", mid));
        s.add(new Pair<>("timestamp", dateString));
        s.add(new Pair<>("nonce", nonceGen(32)));
        s.add(new Pair<>("fp_hash", fp_hash(s,key)));

        String url = buildURL("https://secure.euplatesc.ro/tdsprocess/tranzactd.php?", s);
        WebView browser = findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        browser.loadUrl(url);
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        dialog.dismiss();
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
        url += list.get(list.size()-1).first + "=" + list.get(list.size()-1).second;
        return url;
    }
}