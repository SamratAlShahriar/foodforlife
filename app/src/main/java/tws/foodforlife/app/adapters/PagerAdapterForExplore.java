package tws.foodforlife.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

import tws.foodforlife.app.view.fragments.post.RequestPostFragment;
import tws.foodforlife.app.view.fragments.post.SharePostFragment;

public class PagerAdapterForExplore extends FragmentStateAdapter {

    public PagerAdapterForExplore(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new RequestPostFragment();
        }
        return new SharePostFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
