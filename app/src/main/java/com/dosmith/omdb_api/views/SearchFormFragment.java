package com.dosmith.omdb_api.views;

import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dosmith.omdb_api.R;
import com.dosmith.omdb_api.databinding.FragmentSearchFormBinding;
import com.dosmith.omdb_api.models.SearchResult;
import com.dosmith.omdb_api.repository.Repository;
import com.dosmith.omdb_api.viewmodels.SearchActivityViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFormFragment extends Fragment {
    FragmentSearchFormBinding binding;
    SearchActivityViewModel viewModel;
    boolean maximized = true;
    private SearchShrinkListener shrinkListener;

    public interface SearchShrinkListener {
        void shrinkSearch(boolean shrink);
    }

    public SearchFormFragment() {
    }

    public static SearchFormFragment newInstance(String param1, String param2) {
        SearchFormFragment fragment = new SearchFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchShrinkListener) {
            shrinkListener = (SearchShrinkListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SearchShrinkListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchFormBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SearchActivityViewModel.class);

        ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Customize how the selected item looks in the spinner
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(getItem(position));
                // Modify your view (e.g., set an image or change text color)
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setText(getItem(position));
                view.setPadding(5, 10, 10, 10);
                return view;
            }
        };
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnType.setAdapter(spnAdapter);

        spnAdapter.add("Any");
        spnAdapter.add("Movies");
        spnAdapter.add("Series");
        spnAdapter.add("Episodes");

        binding.btnSearch.setOnClickListener(v->{
            Map<String, String> params = new HashMap<>();
            if(!binding.etTitle.getText().toString().isEmpty()){
                params.put("s", binding.etTitle.getText().toString());
            }
            if (!binding.etYear.getText().toString().isEmpty()){
                params.put("y", binding.etYear.getText().toString());
            }
            switch (binding.spnType.getSelectedItem().toString()){
                case "Movies":
                    params.put("type", "movie");
                    break;
                case "Series":
                    params.put("type", "series");
                    break;
                case "Episodes":
                    params.put("type", "episode");
                    break;
                default:
                    break;
            }
            viewModel.reset();
            viewModel.storeParams(params);
            viewModel.queryResultsPage();

            Observer<ArrayList<SearchResult>> observer = new Observer<ArrayList<SearchResult>>() {
                @Override
                public void onChanged(ArrayList<SearchResult> items) {
                    if (!items.isEmpty()){
                        binding.btnBack.setEnabled(true);
                        maximized = false;
                        if (shrinkListener != null) {
                            shrinkListener.shrinkSearch(!maximized);
                        }
                        viewModel.getSearchResults().removeObserver(this);
                    }
                }
            };
            viewModel.getSearchResults().observe(getViewLifecycleOwner(), observer);
        });

        binding.btnBack.setEnabled(false);
        binding.btnBack.setOnClickListener(v->{
            maximized = false;
            if (shrinkListener != null) {
                shrinkListener.shrinkSearch(!maximized);
            }
        });

        binding.getRoot().setOnClickListener(v->{
            if (!maximized){
                maximized = true;
                if (shrinkListener != null) {
                    shrinkListener.shrinkSearch(!maximized);
                }
            }
        });

        return binding.getRoot();
    }
}