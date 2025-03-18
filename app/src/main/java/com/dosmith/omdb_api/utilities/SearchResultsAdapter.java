package com.dosmith.omdb_api.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.dosmith.omdb_api.R;
import com.dosmith.omdb_api.databinding.SearchResultBinding;
import com.dosmith.omdb_api.models.SearchResult;

import java.util.ArrayList;
import java.util.List;
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder> {

    private List<SearchResult> searchResults;

    public static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private final SearchResultBinding binding;

        public SearchResultViewHolder(SearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SearchResult searchResult){
            binding.etTitle.setText(searchResult.getTitle());
            binding.etYear.setText("Release Year: " + searchResult.getYear());
            binding.imdbId.setText("IMDBid: " + searchResult.getImdbID());
            String type = Character.toUpperCase(searchResult.getType().charAt(0)) + searchResult.getType().substring(1);
            binding.type.setText(type);
            if (searchResult.getPosterImg() != null) {
                binding.imgPoster.setImageBitmap(searchResult.getPosterImg());
            }
        }
    }

    public SearchResultsAdapter(List<SearchResult> objects) {
        this.searchResults = objects;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the binding layout
        SearchResultBinding binding = SearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchResultViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        SearchResult currentItem = searchResults.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateData(ArrayList<SearchResult> searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults);
        notifyDataSetChanged();
    }
}
