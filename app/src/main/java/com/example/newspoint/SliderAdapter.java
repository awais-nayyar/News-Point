package com.example.newspoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderHolder> {

    private ArrayList<SliderItems> dataset;
    private AdapterView.OnItemClickListener clickListener;

    public SliderAdapter(ArrayList<SliderItems> dataset, AdapterView.OnItemClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @Override
    public SliderHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_slider, parent, false);
        return new SliderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SliderHolder viewHolder, int position) {

        SliderItems selectedItem = dataset.get(position);
        Picasso.get().load(selectedItem.getImage()).placeholder(R.drawable.placeholder_vertical)
                .error(R.drawable.error_vertical)
                .into(viewHolder.ivSlider);
        viewHolder.tvSlider.setText(selectedItem.getTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(null, viewHolder.itemView, position, 0);
            }
        });
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    public class SliderHolder extends SliderViewAdapter.ViewHolder{

        ImageView ivSlider;
        TextView tvSlider;

        public SliderHolder(View itemView) {
            super(itemView);

            ivSlider = itemView.findViewById(R.id.ivSlider);
            tvSlider = itemView.findViewById(R.id.tvSlider);
        }
    }

}
