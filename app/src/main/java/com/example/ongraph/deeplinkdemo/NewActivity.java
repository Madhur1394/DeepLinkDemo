package com.example.ongraph.deeplinkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * Created by ongraph on 12/13/2017.
 */

public class NewActivity extends AppCompatActivity {

    private static final String TAG ="@Result" ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        Button btn = findViewById(R.id.clickbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewActivity.this,MainActivity.class));
                finish();
            }
        });

        //Handel received deep link

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        //Get dynamic link from result (may be null if no link is found)

                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        //Here, Handle deep link. For example open the linked content.

                        //Display deep link in UI
                        if (deepLink != null) {
                            String path = deepLink.getPath();
                            String[] parts = path.split("/");
                            Intent intent = new Intent(NewActivity.this,MainActivity.class);
                            intent.putExtra("path",parts[1]);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, getString(R.string.no_link));
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "getDynamicLink : onFailure", e);
                    }
                });
    }
}
