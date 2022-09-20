package com.example.newspoint;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PullRefreshLayout pullRefreshLayout;
    private ProgressBar progressBar;
    private LinearLayout errorLayout;
    private ImageView ivError;
    private TextView tvError;
    private Button btnError;

    private SliderView sliderView;
    private ArrayList<SliderItems> sliderItemsList;
    private SliderAdapter sliderAdapter;

    private RecyclerView rvMainNews;
    private ArrayList<News> mainNewsList;
    private NewsAdapter adapter;

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    private ArrayList<Adds> adsList;

    private static int ADD_NUMBER = 0;
    //private static final int ADD_INTERVAL = 3;
    //private static final boolean BANNER_ADD = true;
   // private static  final boolean INTERSTITIAL_ADD = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);


        sliderView = findViewById(R.id.imageSlider);
        rvMainNews = findViewById(R.id.rvMainNews);
        pullRefreshLayout = findViewById(R.id.pullRefresh);
        tvError = findViewById(R.id.tvError);
        btnError = findViewById(R.id.btnError);
        errorLayout = findViewById(R.id.erroLayout);
        ivError = findViewById(R.id.ivError);
        progressBar = findViewById(R.id.progressBar);

        rvMainNews.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adsList = new ArrayList<>();
        mainNewsList = new ArrayList<>();

        sliderItemsList = new ArrayList<>();

        SliderItems slidePak = new SliderItems();
        slidePak.setImage("https://www.mul.edu.pk/images/programs/mphil-pakistan-studies_106.jpg");
        slidePak.setTitle("پاکستانی");
        sliderItemsList.add(slidePak);

        SliderItems slideInternational = new SliderItems();
        slideInternational.setImage("https://www.timeshighereducation.com/student/sites/default/files/styles/default/public/international_students_1.jpg?itok=ay7gWOwH");
        slideInternational.setTitle("بین اقوامی");
        sliderItemsList.add(slideInternational);

        SliderItems slideSports = new SliderItems();
        slideSports.setImage("https://static01.nyt.com/images/2020/07/21/autossell/sports-reboot-promo-still/sports-reboot-promo-still-facebookJumbo.jpg");
        slideSports.setTitle("کھیل");
        sliderItemsList.add(slideSports);

        SliderItems slideInteresting = new SliderItems();
        slideInteresting.setImage("https://cdn.mos.cms.futurecdn.net/8bEfxy8skS6fUVZnfaLWxj.jpg");
        slideInteresting.setTitle("حیرت انگیز");
        sliderItemsList.add(slideInteresting);

        sliderAdapter = new SliderAdapter(sliderItemsList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SliderItems currentItem = sliderItemsList.get(position);
                if (currentItem.getTitle().trim().equals("پاکستانی")) {

                    Intent pakIntent = new Intent(MainActivity.this, PakistaniActivity.class);
                    startActivity(pakIntent);

                } else if (currentItem.getTitle().trim().equals("بین اقوامی")) {

                    Intent internationalIntent = new Intent(MainActivity.this, InternationalActivity.class);
                    startActivity(internationalIntent);

                } else if (currentItem.getTitle().trim().equals("کھیل")) {

                    Intent sportsIntent = new Intent(MainActivity.this, SportsActivity.class);
                    startActivity(sportsIntent);
                } else if (currentItem.getTitle().trim().equals("حیرت انگیز")) {

                    Intent sportsIntent = new Intent(MainActivity.this, TechActivity.class);
                    startActivity(sportsIntent);
                } else {

                    Toast.makeText(MainActivity.this, "Unable to open this page", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.setAutoCycle(true);

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

    public void fetchDataFromServer() {
        progressBar.setVisibility(View.VISIBLE);
        loadInterstitial();

        String url = "http://smashdevelopers.tk/ary_urdu_news/health.php";
       // String url = "http://muhammadsuleman.androidstudent.net/ary_urdu_news/urdumainpage.php";

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                pullRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    mainNewsList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<News>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new NewsAdapter(mainNewsList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                News currentNews = mainNewsList.get(position);

                                ADD_NUMBER++;

                                fetchAddStatus();

                                Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                mainIntent.putExtra("newslink", currentNews.getLink());
                                startActivity(mainIntent);

                                // yeh jo comments hen yeh bhi code hy jis se hum different action perfoerm karty hen ads par

                                /*mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            // Called when fullscreen content is dismissed.
                                            Log.d("TAG", "The ad was dismissed.");
                                            Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                            mainIntent.putExtra("newslink", currentNews.getLink());
                                            startActivity(mainIntent);

                                        }

                                        @Override
                                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                                            // Called when fullscreen content failed to show.
                                            Log.d("TAG", "The ad failed to show.");
                                            loadInterstitial();
                                            Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                            mainIntent.putExtra("newslink", currentNews.getLink());
                                            startActivity(mainIntent);

                                        }

                                        @Override
                                        public void onAdShowedFullScreenContent() {
                                            // Called when fullscreen content is shown.
                                            // Make sure to set your reference to null so you don't
                                            // show it a second time.
                                            mInterstitialAd = null;
                                            Log.d("TAG", "The ad was shown.");
                                        }
                                    });*/


                                /*if (INTERSTITIAL_ADD) {


                                    ADD_NUMBER++;
                                    if (ADD_NUMBER >= ADD_INTERVAL) {

                                        ADD_NUMBER = 0;
                                        if (mInterstitialAd != null) {
                                            mInterstitialAd.show( MainActivity.this);
                                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                                                @Override
                                                public void onAdDismissedFullScreenContent() {
                                                    // Called when fullscreen content is dismissed.
                                                    Log.d("TAG", "The ad was dismissed.");
                                                    Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                                    mainIntent.putExtra("newslink", currentNews.getLink());
                                                    startActivity(mainIntent);

                                                }

                                                @Override
                                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                                    // Called when fullscreen content failed to show.
                                                    Log.d("TAG", "The ad failed to show.");
                                                    loadInterstitial();
                                                }

                                                @Override
                                                public void onAdShowedFullScreenContent() {
                                                    // Called when fullscreen content is shown.
                                                    // Make sure to set your reference to null so you don't
                                                    // show it a second time.
                                                    mInterstitialAd = null;
                                                    Log.d("TAG", "The ad was shown.");
                                                }
                                            });

                                        } else {
                                            Log.i("TAG", "ad not available");

                                            Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                            mainIntent.putExtra("newslink", currentNews.getLink());
                                            startActivity(mainIntent);
                                        }
                                    } else {

                                        Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                        mainIntent.putExtra("newslink", currentNews.getLink());
                                        startActivity(mainIntent);

                                    }

                                } else {

                                    Intent mainIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                                    mainIntent.putExtra("newslink", currentNews.getLink());
                                    startActivity(mainIntent);

                                }*/

                            }
                        });
                        rvMainNews.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error while parsing data from server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (mainNewsList.isEmpty()) {
                    errorLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                Toast.makeText(MainActivity.this, "Unable to fetch data from server", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MainActivity.this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionPak) {

            Intent pakIntent = new Intent(MainActivity.this, PakistaniActivity.class);
            startActivity(pakIntent);

        } else if (id == R.id.actionInternational) {

            Intent internationalIntent = new Intent(MainActivity.this, InternationalActivity.class);
            startActivity(internationalIntent);

        } else if (id == R.id.actionSports) {

            Intent sportsIntent = new Intent(MainActivity.this, SportsActivity.class);
            startActivity(sportsIntent);

        } else if (id == R.id.actionTech) {

            Intent techIntent = new Intent(MainActivity.this, TechActivity.class);
            startActivity(techIntent);

        } else if (id == R.id.actionPrivacy) {

            Intent privacyIntent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            startActivity(privacyIntent);

        } else {
            Toast.makeText(MainActivity.this, "Noting selected", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadInterstitial() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadInterstitial();
    }

    public  void fetchAddStatus() {

        String adUrl = "http://muhammadsuleman.androidstudent.net/ary_urdu_news/addsstatus.php";
        StringRequest request = new StringRequest(adUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray responseArray = new JSONArray(response);
                    adsList = new Gson().fromJson(responseArray.toString(), new TypeToken<ArrayList<Adds>>(){}.getType());
                    // is tarah se pehly ap is list men se object get karen gy phr ap us men se apna 1 1 item nikalen gy.
                    Adds adds = adsList.get(0);

                    String banner = adds.getBannerAdd();
                    String interstitial = adds.getInterstitialAdd();
                    int addInterval = adds.getAddInterval();

                    if (banner.equals("true")) {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        adView.loadAd(adRequest);
                    }
                    // after checking from server then showing the add
                    if (interstitial.equals("true")) {
                            if (ADD_NUMBER >= addInterval) {

                                ADD_NUMBER = 0;

                                if (mInterstitialAd != null) {
                                    mInterstitialAd.show( MainActivity.this);
                                }
                            }

                        }

                    Log.i("mytag", "Ad status successful");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("mytag", "Ad status not succesful");
                    Toast.makeText(MainActivity.this, "Error while parsing data from server", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(MainActivity.this, "Unable to fetch add status", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(MainActivity.this).add(request);

        /*if (!adsList.isEmpty()) {
            Log.i("mytag", "list is not empty");
        }*/
    }


}