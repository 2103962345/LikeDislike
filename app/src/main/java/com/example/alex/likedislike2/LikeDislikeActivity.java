package com.example.alex.likedislike2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alex.likedislike2.application.AppController;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LikeDislikeActivity extends AppCompatActivity{

    private SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private static final String getURL = "http://cashadvanceapp.pp.ua/json/imgs.json";
    List<Profile> imageRecords;
    SharedPreferences sPref;
    public final String save_value_like = "LikeSave";
    Link link;
    List<Text> text;
    static LikeVariable likeVariable;
    private WebView wv;

    public static LikeVariable getLikeVariable() {
        return likeVariable;
    }

    public static void setLikeVariable(LikeVariable likeVariable) {
        LikeDislikeActivity.likeVariable = likeVariable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_dislike2);
        mSwipeView = (SwipePlaceHolderView) findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        likeVariable = new LikeVariable();
       sPref = getPreferences(MODE_PRIVATE);

            String saveLike = sPref.getString(save_value_like,"0");
            likeVariable.setLike(Integer.parseInt(saveLike));
            link= new Link("");

    int bottomMargin = Utils.dpToPx(160);
    Point windowSize = Utils.getDisplaySize(getWindowManager());
    mSwipeView.getBuilder()
            .setDisplayViewCount(1)
            .setIsUndoEnabled(true)
            .setHeightSwipeDistFactor(10)
            .setWidthSwipeDistFactor(5)
            .setSwipeDecor(new SwipeDecor()
                    .setViewWidth(windowSize.x)
                    .setViewHeight(windowSize.y - bottomMargin)
                    .setViewGravity(Gravity.TOP)
                    .setPaddingTop(20)
                    .setRelativeScale(0.01f)
                    .setSwipeMaxChangeAngle(2f)
                    .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                    .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

    JsonObjectRequest billionaireReq = new JsonObjectRequest(
            getURL,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        text=parseText(response);
                        imageRecords = parseProfile(response);
                        link.setLink(parseLink(response));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setTitle(setLanguage(getCurrentLocaleLanguage(mContext)));
                    for (Profile profile : imageRecords) {
                        mSwipeView.addView(new TinderCard(mContext, profile, mSwipeView));
                    //    Log.e("LikeDislikeActivity", profile.getImageId());

                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
             Toast.makeText(getApplicationContext(), "network issue: please enable wifi/mobile data + " + error, Toast.LENGTH_SHORT).show();
     //       Log.e("LikeDislikeActivity", error.toString());
        }
    });

        // Adding request to request queue
    AppController.getInstance().addToRequestQueue(billionaireReq);
    findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSwipeView.doSwipe(false);
        }
    });

    findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSwipeView.doSwipe(true);
        }
    });

    likeVariable.setListener(new LikeVariable.ChangeListener() {
        @Override
        public void onChange() {
            //Log.e("Tinder", likeVariable.getLike() + "");
            setWebView();
        }
    });
    link.setListener(new Link.ChangeListener() {
        @Override
        public void onChange() {
            //Log.e("Tinder", likeVariable.getLike() + "");
            setWebView();
        }
    });
}


    public void setWebView(){
        if (likeVariable.getLike() >= 3) {
            wv = new WebView(mContext);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());
              wv.loadUrl(link.getLink());
                setContentView(wv);
        }
    }

    private List<Profile> parseProfile(JSONObject json) throws JSONException {
        List<Profile> records = new ArrayList<Profile>();
        JSONArray jsonImages = json.getJSONArray("images");

        for (int i = 0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String src = jsonImage.getString("src");
            String id = jsonImage.getString("id");

            Profile record = new Profile(src, id);

            records.add(record);
        }
        Collections.shuffle(records);
        return records;
    }

    private String parseLink(JSONObject json) throws JSONException {

        JSONArray jsonLinks = json.getJSONArray("link");
        JSONObject jsonLink = jsonLinks.getJSONObject(0);
        String link = jsonLink.getString("link");
        return link;
    }

    private List<Text> parseText(JSONObject json) throws JSONException {
        List<Text> textes = new ArrayList<>();
        JSONArray jsonImages = json.getJSONArray("text");
        for (int i = 0; i < jsonImages.length(); i++) {
            JSONObject jsonImage = jsonImages.getJSONObject(i);
            String id = jsonImage.getString("id");
            String text = jsonImage.getString("text");
            textes.add( new Text(id,text));
        }
        return textes;
    }

@Override
protected  void onResume(){
    sPref = getPreferences(MODE_PRIVATE);
    String saveLike = sPref.getString(save_value_like,"0");
    likeVariable.setLike(Integer.parseInt(saveLike));
    setWebView();
    super.onResume();
}
@Override
protected void onStop(){
    sPref = getPreferences(MODE_PRIVATE);
    SharedPreferences.Editor e =sPref.edit();
    if (likeVariable.getLike() < 3) {
        e.putString(save_value_like, "0");
   //     Log.e("Tinder", likeVariable.getLike() + "");
    }
    else {
        e.putString(save_value_like, "3");
   //     Log.e("Tinder", likeVariable.getLike() + "");
    }
    e.commit();
        super.onStop();
}

public String setLanguage(String currentLanguage){
    String titleLanguage="";
    if(currentLanguage.equals("English")){
            titleLanguage=text.get(0).getText();
    }else
    if(currentLanguage.equals("Русский")) {
        titleLanguage=text.get(1).getText();
    }
    return titleLanguage;
}
public String getCurrentLocaleLanguage(Context context){
    if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
        return context.getResources().getConfiguration().getLocales().get(0).getDisplayLanguage();
    }else{
        return context.getResources().getConfiguration().locale.getDisplayLanguage();
    }
}

}