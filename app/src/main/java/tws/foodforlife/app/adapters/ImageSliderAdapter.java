package tws.foodforlife.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import tws.foodforlife.app.R;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterViewHolder> {
    int[] images;

    public ImageSliderAdapter(int[] images) {
        this.images = images;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_slider_image, null);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
        Picasso.get().load(images[position]).into(viewHolder.ivSlider);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView ivSlider;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            ivSlider = itemView.findViewById(R.id.iv_slider);
            this.itemView = itemView;
        }
    }
}
