package com.dosmith.omdb_api.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dosmith.omdb_api.R;
import com.dosmith.omdb_api.databinding.FragmentSearchFormBinding;
import com.dosmith.omdb_api.databinding.FragmentSearchResultsBinding;
import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.utilities.SearchResultsAdapter;
import com.dosmith.omdb_api.viewmodels.SearchActivityViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment implements SearchResultsAdapter.SearchResultViewHolder.OnItemClickListener {

    FragmentSearchResultsBinding binding;
    SearchActivityViewModel viewModel;

    SearchResultsAdapter adapter;

    public SearchResultsFragment() {
    }

    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchActivityViewModel.class);
        adapter = new SearchResultsAdapter(viewModel.getSearchResults().getValue(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);

        binding.rvResults.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        binding.rvResults.setAdapter(adapter);

        viewModel.getSearchResults().observe(getViewLifecycleOwner(), items -> {;
            adapter.updateData(viewModel.getSearchResults().getValue());
        });

        binding.rvResults.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        viewModel.queryResultsPage();
                    }
                }
            }
        });

        binding.main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.reset();
                viewModel.queryResultsPage();
                Observer<Boolean> observer = new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean b) {
                        if (!b){
                            binding.main.setRefreshing(false);
                            viewModel.getAddingResults().removeObserver(this);
                        }
                    }
                };
                viewModel.getAddingResults().observe(getViewLifecycleOwner(), observer);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onItemClick(SearchResult searchResult) {
        Intent intent = new Intent(this.getContext().getApplicationContext(), DetailsActivity.class);
        intent.putExtra("imdbId", searchResult.getImdbID());
        startActivity(intent);
    }
}