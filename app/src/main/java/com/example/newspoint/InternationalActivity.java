package com.example.newspoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class InternationalActivity extends AppCompatActivity {

    private RecyclerView rvInternationalNews;
    private PullRefreshLayout pullRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private ImageView ivError;
    private TextView tvError;
    private Button btnError;

    private ArrayList<News> internationalNewsList;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international);

        rvInternationalNews = findViewById(R.id.rvInternationalNews);
        pullRefreshLayout = findViewById(R.id.pullRefresh);
        tvError = findViewById(R.id.tvError);
        btnError = findViewById(R.id.btnError);
        errorLayout = findViewById(R.id.erroLayout);
        ivError = findViewById(R.id.ivError);
        progressBar = findViewById(R.id.progressBar);

        rvInternationalNews.setLayoutManager(new LinearLayoutManager(InternationalActivity.this));

        internationalNewsList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("بین اقوامی");
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                fetchDataFromServer();
            }
        });

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                fetchDataFromServer();
            }
        });
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        pullRefreshLayout.setColorSchemeColors(Color.parseColor("#E53935"), Color.parseColor("#FDD835"), Color.parseColor("#43A047"));
        pullRefreshLayout.setRefreshing(false);
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        if (progressBar.isEnabled()) {
            pullRefreshLayout.setRefreshing(false);
        }
        if (internationalNewsList.isEmpty()) {

            progressBar.setVisibility(View.VISIBLE);
        }
        errorLayout.setVisibility(View.GONE);

        String url = "http://smashdevelopers.tk/ary_urdu_news/international.php";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    internationalNewsList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<News>>(){}.getType());

                    if (adapter == null) {
                        adapter = new NewsAdapter(internationalNewsList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                News currentNews = internationalNewsList.get(position);
                                Intent intIntent = new Intent(InternationalActivity.this, NewsDetailActivity.class);
                                intIntent.putExtra("newslink", currentNews.getLink());
                                startActivity(intIntent);
                            }
                        });
                        rvInternationalNews.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (internationalNewsList.isEmpty()) {
                        errorLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(InternationalActivity.this, "Error while parsing data from server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (internationalNewsList.isEmpty()) {
                    errorLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                }
                Toast.makeText(InternationalActivity.this, "Unable to fetch data from server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(InternationalActivity.this).add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}