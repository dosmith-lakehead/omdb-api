package com.dosmith.omdb_api.viewmodels;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.repository.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SearchActivityViewModel extends ViewModel {
    private MutableLiveData<ArrayList<SearchResult>> searchResults = new MutableLiveData<>(new ArrayList<>());
    private int resultsPage = 0;
    private boolean allResultsLoaded = false;
    private MutableLiveData<Boolean> addingResults = new MutableLiveData<>(false);
    private MutableLiveData<String> searchMessage = new MutableLiveData<>();

    private Map<String,String> params = new HashMap<>();

    public LiveData<Boolean> getAddingResults(){
        return addingResults;
    }
    public LiveData<ArrayList<SearchResult>> getSearchResults(){
        return searchResults;
    }
    public LiveData<String> getSearchMessage(){
        return searchMessage;
    }
    public void queryResultsPage(){
        if (!(addingResults.getValue() || allResultsLoaded)){
            addingResults.setValue(true);
            ArrayList<SearchResult> tempResults = searchResults.getValue();
            Thread backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    Handler handler = new Handler(Looper.myLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Repository.addResultsPage(params, ++resultsPage);
                            while (Repository.getActiveReqCount() > 0){
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            allResultsLoaded = Repository.getAllResultsLoaded();
                            if (!allResultsLoaded) {
                                tempResults.addAll(Repository.getSearchResults());
                                searchResults.postValue(tempResults);
                            }
                            addingResults.postValue(false);
                            searchMessage.postValue(Repository.getSearchMessage());
                        }
                    });
                    Looper.loop();
                }
            });
            backgroundThread.start();
        }
    }

    public void storeParams(Map<String, String> params){
        this.params = params;
    }

    public void reset(){
        resultsPage = 0;
        allResultsLoaded = false;
        addingResults.setValue(false);
        searchResults.setValue(new ArrayList<>());
        Repository.reset();
    }
}
