package com.dosmith.omdb_api.views;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;

import com.dosmith.omdb_api.R;
import com.dosmith.omdb_api.databinding.ActivitySearchBinding;
import com.dosmith.omdb_api.repository.Repository;
import com.dosmith.omdb_api.utilities.VolleySingleton;
import com.dosmith.omdb_api.viewmodels.SearchActivityViewModel;

public class SearchActivity extends AppCompatActivity implements SearchFormFragment.SearchShrinkListener {

    SearchActivityViewModel viewModel;
    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Repository.setContext(this.getApplicationContext());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(SearchActivityViewModel.class);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void shrinkSearch(boolean shrink) {
        LinearLayout.LayoutParams searchContainerParams = (LinearLayout.LayoutParams) binding.searchFormContainer.getLayoutParams();
        LinearLayout.LayoutParams spaceParams = (LinearLayout.LayoutParams) binding.filler.getLayoutParams();

        ValueAnimator animator;
        if (shrink){
            animator = ValueAnimator.ofFloat(1.0f, 0.0f);
        }
        else {
            animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        }
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator(2f));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                // Get the animated value
                float animatedValue = (float) animator.getAnimatedValue();
                searchContainerParams.weight = animatedValue;
                spaceParams.weight = 1.0f - animatedValue;
                binding.filler.setLayoutParams(spaceParams);
                binding.searchFormContainer.setLayoutParams(searchContainerParams);
            }
        });
        animator.start();
    }
}