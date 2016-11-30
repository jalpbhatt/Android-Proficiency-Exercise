package com.telstra.androidjsonparsing.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.telstra.androidjsonparsing.AppConfig;
import com.telstra.androidjsonparsing.R;
import com.telstra.androidjsonparsing.webservice.RequestInterface;
import com.telstra.androidjsonparsing.model.Country;
import com.telstra.androidjsonparsing.model.CountryDetails;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.app.ActionBar mActionbar;
    private RecyclerView mRecyclerView;
    private ArrayList<CountryDetails> mCountryList;
    private CountryAdapter mCountryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionbar = getSupportActionBar();

        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        executeCountryDetailsRequest(AppConfig.WEB_SERVICE_BASE_URL);
    }

    private void executeCountryDetailsRequest(String url){
        Retrofit webReqObj = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestCountryDetails = webReqObj.create(RequestInterface.class);
        Call<Country> call = requestCountryDetails.getCountryDetails();
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {

                Country jsonResponse = response.body();

                if (jsonResponse != null) {

                    // Set title of the action bar
                    // NOTE: Should we check for null?
                    mActionbar.setTitle(jsonResponse.getTitle());

                    mCountryList = new ArrayList<>(Arrays.asList(jsonResponse.getRows()));
                    mCountryAdapter = new CountryAdapter(mCountryList);
                    mRecyclerView.setAdapter(mCountryAdapter);
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}
