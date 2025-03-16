package com.dosmith.omdb_api.repository;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dosmith.omdb_api.models.SearchResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Repository {

    private static int activeReqCount = 0;

    private static boolean allResultsLoaded = false;

    private static ArrayList<SearchResult> searchResults;


    public static int getActiveReqCount(){
        return activeReqCount;
    }
    public static boolean getAllResultsLoaded(){
        return allResultsLoaded;
    }


    public static ArrayList<SearchResult> getSearchResults(){
        return searchResults;
    }


    public static void addResultsPage(Map<String, String> params, int page){
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
                    }
                    else {
                        searchResults = new ArrayList<>();
                        JSONArray resultsArray = response.getJSONArray("Search");
                        for (int i = 0; i < resultsArray.length(); i++){
                            JSONObject resultItem = resultsArray.getJSONObject(i);
                            SearchResult searchResult = gson.fromJson(resultItem.toString(), SearchResult.class);
                            searchResults.add(searchResult);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

}
