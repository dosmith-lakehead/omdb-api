package com.dosmith.omdb_api.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.utilities.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Repository {

    private static int activeReqCount = 0;
    private static boolean allResultsLoaded = false;
    private static Context context;


    private static String searchMessage = "";
    private static ArrayList<SearchResult> searchResults;
    private static ArrayList<Bitmap> posters;


    public static void setContext(Context ctx){
        context = ctx;
    }

    public static int getActiveReqCount(){
        return activeReqCount;
    }
    public static boolean getAllResultsLoaded(){
        return allResultsLoaded;
    }
    public static ArrayList<SearchResult> getSearchResults(){
        return searchResults;
    }
    public static ArrayList<Bitmap> getPosters(){
        return posters;
    }


    public static void addResultsPage(Map<String, String> params, int page){
        activeReqCount += 1;
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://www.omdbapi.com/?apikey=2904fe35");
        for (String paramKey : params.keySet()){
            urlBuilder.append("&");
            urlBuilder.append(paramKey);
            urlBuilder.append("=");
            urlBuilder.append(params.get(paramKey));
        }
        urlBuilder.append("&page=");
        urlBuilder.append(page);
        String url = urlBuilder.toString();

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                try {
                    if (!response.getBoolean("Response")){
                        allResultsLoaded = true;
                        if (searchResults.isEmpty()){
                            searchMessage = "No results found.";
                        }
                    }
                    else {
                        searchResults = new ArrayList<>(10);
                        JSONArray resultsArray = response.getJSONArray("Search");
                        for (int i = 0; i < resultsArray.length(); i++){
                            searchResults.add(null);
                            JSONObject resultItem = resultsArray.getJSONObject(i);
                            SearchResult searchResult = gson.fromJson(resultItem.toString(), SearchResult.class);
                            getImage(searchResult, i);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                finally {
                    activeReqCount -= 1;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                searchMessage = "An error occurred.";
                activeReqCount -= 1;
            }
        });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(context).addToRequestQueue(getRequest);
    }

    public static void getImage(SearchResult searchResult, int i){
        if (!searchResult.getPosterURL().equals("N/A")) {
            activeReqCount += 1;
            ImageRequest imgRequest = new ImageRequest(searchResult.getPosterURL(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    searchResult.setPosterImg(response);
                    searchResults.set(i, searchResult);
                    activeReqCount -= 1;
                }
            }, 300, 448, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    searchResults.set(i, searchResult);
                    activeReqCount -= 1;
                }
            });
            imgRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleySingleton.getInstance(context).addToRequestQueue(imgRequest);
        }
        else {
            searchResults.set(i, searchResult);
        }
    }

    public static void reset(){
        allResultsLoaded = false;
        searchMessage = "";
        searchResults = new ArrayList<>();
    }
}
