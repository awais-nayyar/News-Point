package com.example.newspoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> dataset;
    private AdapterView.OnItemClickListener clickListener;

    public NewsAdapter(ArrayList<News> dataset, AdapterView.OnItemClickListener clickListener) {
        this.dataset = dataset;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

        News selectedNews = dataset.get(position);
        Picasso.get().load(selectedNews.getImage()).placeholder(R.drawable.placeholder_vertical)
                .error(R.drawable.error_vertical)
                .into(holder.ivNews);
        holder.tvNews.setText(selectedNews.getTitle());
        holder.tvNewsDesc.setText(selectedNews.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder{

        ImageView ivNews;
        TextView tvNews;
        TextView tvNewsDesc;
        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            ivNews = itemView.findViewById(R.id.ivNews);
            tvNews = itemView.findViewById(R.id.tvNewsTitle);
            tvNewsDesc = itemView.findViewById(R.id.tvNewsDesc);
        }
    }
}
