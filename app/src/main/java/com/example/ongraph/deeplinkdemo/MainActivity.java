package com.example.ongraph.deeplinkdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DEEP_LINK_URL = "https://example.com";
    private static final String TAG ="@Result";
    private static final int REQUEST_INVITE = 0;
    private TextView tv_send,tv_receive;
    private Button btnSendInvitation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_receive = findViewById(R.id.tv_received);
        tv_send = findViewById(R.id.tv_send);
        btnSendInvitation = findViewById(R.id.sendInvite);
        Intent intent = getIntent();

        final String path=intent.getStringExtra("path");
        tv_receive.setText(path);

        final Uri deepLinkUri = DynamicLinkHandler.buildDeepLink(123,"new_product",0);
        Uri longDynamicLink = deepLinkUri;
        ShortBuildDynamicLink(longDynamicLink,this);
        //tv_send.setText(decode(deepLinkUri.toString()));

        btnSendInvitation.setOnClickListener(this);
    }

    public void ShortBuildDynamicLink(Uri longDynamicLink, Context context) {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(longDynamicLink)
                .buildShortDynamicLink()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowChartLink = task.getResult().getPreviewLink();
                        tv_send.setText(shortLink.toString());
                    }
                });
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sendInvite){
            shareDeepLink(tv_send.getText().toString());
        }
    }

    private void shareDeepLink(String deepLink) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link");
        intent.putExtra(Intent.EXTRA_TEXT,deepLink);
        startActivity(intent);
    }

//    //To send invitation link to the other user
//    private void onInviteClicked() {
//        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                .setMessage(getString(R.string.invitation_msg))
//                .setDeepLink(Uri.parse(DEEP_LINK_URL))
//                .setCallToActionText("Install")
//                .build();
//        startActivityForResult(intent,REQUEST_INVITE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
//
//        if (requestCode == REQUEST_INVITE) {
//            if (resultCode == RESULT_OK) {
//                // Get the invitation IDs of all sent messages
//                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//                for (String id : ids) {
//                    Log.d(TAG, "onActivityResult: sent invitation " + id);
//                }
//            } else {
//                // Sending failed or it was canceled, show failure message to the user
//                showMessage(getString(R.string.send_failed));
//            }
//        }
//    }

        private void showMessage(String msg) {
            ViewGroup container = findViewById(R.id.snackbar_layout);
            Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
        }
}