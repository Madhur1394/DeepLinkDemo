# DeepLinkDemo

Firebase DeepLinking demo. Here use generate dynamic link for a specific view and share with other users.

Firebase dynamic link :

Invites and Dynamic Linksdependency :

com.google.firebase:firebase-invites:11.6.2

**To genetrate dynamic link:

public static Uri buildDeepLink(int product_id,String product_name, int minVersion) {

        String DEEP_LINK_URL = null;
        String domain = "wxpx5.app.goo.gl";
        
        //First create JSON objects if you send data with dynamic link
        //Here i use product id and product name for ease
        
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
    
    
**To generate Short dynamic link:

 public void ShortBuildDynamicLink(Uri longDynamicLink) {

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(longDynamicLink)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowChartLink = task.getResult().getPreviewLink();
                    }
                });
    }
   
   
**To handel received dynamic link:

 
 
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
                        
                        //Here we fetch data from dynamic link url
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
