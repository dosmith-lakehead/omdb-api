package com.dosmith.omdb_api.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.repository.Repository;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivityViewModel extends ViewModel {
    private MutableLiveData<ArrayList<SearchResult>> searchResults;
    private int resultsPage = 0;
    private boolean allResultsLoaded = false;
    private boolean addingResults = false;

    public LiveData<ArrayList<SearchResult>> getSearchResults(){
        return searchResults;
    }

    public void queryResultsPage(Map<String, String> params){
        if (!(addingResults || allResultsLoaded)){
            addingResults = true;
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
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            allResultsLoaded = Repository.getAllResultsLoaded();
                            if (!allResultsLoaded) {
                                tempResults.addAll(Repository.getSearchResults());
                                searchResults.postValue(tempResults);
                            }
                        }
                    });
                }
            });
        }
    }
}
