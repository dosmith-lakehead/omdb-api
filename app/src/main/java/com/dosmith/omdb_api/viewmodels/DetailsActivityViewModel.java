package com.dosmith.omdb_api.viewmodels;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dosmith.omdb_api.models.MovieDetails;
import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.repository.Repository;

import java.util.ArrayList;

public class DetailsActivityViewModel extends ViewModel {
    private MutableLiveData<MovieDetails> movieDetails = new MutableLiveData<>();
    public LiveData<MovieDetails> getMovieDetails() {
        return movieDetails;
    }

    public void queryMovieDetails(String imdbId){
        movieDetails.setValue(null);
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler(Looper.myLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Repository.setMovieDetails(imdbId);
                        while (Repository.getActiveReqCount() > 0){
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        movieDetails.postValue(Repository.getMovieDetails());
                    }
                });
                Looper.loop();
            }
        });
        backgroundThread.start();
    }
}

