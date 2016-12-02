package com.telstra.androidjsonparsing.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.telstra.androidjsonparsing.AppConfig;
import com.telstra.androidjsonparsing.R;
import com.telstra.androidjsonparsing.model.Country;
import com.telstra.androidjsonparsing.model.CountryDetails;
import com.telstra.androidjsonparsing.utils.NetworkUtils;
import com.telstra.androidjsonparsing.utils.UiUtils;
import com.telstra.androidjsonparsing.webservice.RequestInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsFeedFragment extends Fragment {

    /* UI variables */
    private RecyclerView mRecyclerView;
    private LinearLayout mProgressbarLayout;
    private LinearLayout mNoConnectionLayout;

    /* Reference variables */
    private ArrayList<CountryDetails> mCountryList;
    private CountryAdapter mCountryAdapter;

    private OnFragmentInteractionListener mListener;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFeedFragment.
     */
    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Initialize views
        initViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews() {

        mProgressbarLayout = (LinearLayout) getActivity().findViewById(R.id.progressBarLayout);
        mNoConnectionLayout = (LinearLayout) getActivity().findViewById(R.id.noConnectionLayout);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        if (NetworkUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            executeCountryDetailsRequest(AppConfig.WEB_SERVICE_BASE_URL);
        } else {
            showConnectionLayout();
        }
    }

    public  void showConnectionLayout() {
        mNoConnectionLayout.setVisibility(View.VISIBLE);
    }

    public  void hideConnectionLayout() {
        mNoConnectionLayout.setVisibility(View.GONE);
    }

    /**
     * Execute country detail request
     */
    private void executeCountryDetailsRequest(String url) {

        // Show progress bar while loading list
        mProgressbarLayout.setVisibility(View.VISIBLE);

        Retrofit webReqObj = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestCountryDetails = webReqObj.create(RequestInterface.class);
        Call<Country> call = requestCountryDetails.getCountryDetails();
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {

                // Hide the progress bar
                mProgressbarLayout.setVisibility(View.GONE);

                Country jsonResponse = response.body();

                if (jsonResponse != null) {

                    // Set title of the action bar
                    /*mActionbar.setTitle(jsonResponse.getTitle());*/

                    mCountryList = new ArrayList<>(Arrays.asList(jsonResponse.getRows()));
                    mCountryAdapter = new CountryAdapter(mCountryList, getActivity());
                    mRecyclerView.setAdapter(mCountryAdapter);
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.d("Error", t.getMessage());

                // Hide the progress bar
                mProgressbarLayout.setVisibility(View.GONE);

                // Show error message if we fail to load/fetch data
                UiUtils.showToast(getActivity().getApplicationContext(), R.string.error_msg);
            }
        });
    }

    public void refreshCountryDetails() {

        // Clear current data & update the list view
        if (mCountryList != null && mCountryAdapter !=null) {
            mCountryList.clear();
            mCountryAdapter.notifyDataSetChanged();
        }

        // Reload the data
        executeCountryDetailsRequest(AppConfig.WEB_SERVICE_BASE_URL);
    }

    /**
     * Communication interface
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction();
    }
}
