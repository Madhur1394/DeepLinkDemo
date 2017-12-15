package com.example.ongraph.deeplinkdemo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by ongraph on 12/14/2017.
 */

public class DynamicLinkHandler {

    //Build dynamic link
    public static Uri buildDeepLink(int product_id,String product_name, int minVersion) {

        String DEEP_LINK_URL = null;
        String domain = "wxpx5.app.goo.gl";

        JSONObject json_data = new JSONObject();
        JSONObject json_parent = new JSONObject();

        try {
            json_data.put("product_name",product_name);
            json_data.put("product_id",product_id);
            json_parent.put("data",json_data);
            DEEP_LINK_URL = "https://example.com/"+json_parent.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(DEEP_LINK_URL))
                .setDynamicLinkDomain(domain)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .setMinimumVersion(minVersion)
                        .build())
                .buildDynamicLink();
        return dynamicLink.getUri();
    }

    //Decode the Uri
    public static String decode(String url)
    {
        try {
            String prevURL="";
            String decodeURL=url;
            while(!prevURL.equals(decodeURL)) {
                prevURL=decodeURL;
                decodeURL= URLDecoder.decode( decodeURL, "UTF-8" );
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" +e.getMessage();
        }
    }
}
