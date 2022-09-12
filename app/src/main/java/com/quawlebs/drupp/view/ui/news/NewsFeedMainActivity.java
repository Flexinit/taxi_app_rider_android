package com.quawlebs.drupp.view.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.widget.RxAdapterView;
import com.quawlebs.drupp.R;
import com.quawlebs.drupp.connectivity.DruppRequestHandler;
import com.quawlebs.drupp.connectivity.http.INetworkList;
import com.quawlebs.drupp.connectivity.http.QualStandardResponseList;
import com.quawlebs.drupp.helpers.AppConstants;
import com.quawlebs.drupp.helpers.SessionManager;
import com.quawlebs.drupp.models.NewsCategoryModel;
import com.quawlebs.drupp.models.NewsFeeds;
import com.quawlebs.drupp.view.adapters.NewsFeedAdapter;
import com.quawlebs.drupp.view.ui.base.MainBaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class NewsFeedMainActivity extends MainBaseActivity {
    @BindView(R.id.tv_no_results)
    TextView mNoResultsMessage;
    @BindView(R.id.news_feeds_Recycle)
    RecyclerView newsRecyclerView;
    @BindView(R.id.spinner_news_categories)
    Spinner newsCategories;
    @BindView(R.id.spinner_news_sub_categories)
    Spinner newsSubCategories;
    @BindView(R.id.container_news_sub_category)
    LinearLayout containerSubCategory;
    private NewsFeedAdapter newsFeedAdapter;

    private LinkedList<NewsCategoryModel> newsCategoryModels = new LinkedList<>();
    private LinkedList<NewsCategoryModel> newsSubCategoryModels = new LinkedList<>();

    private ArrayList<NewsFeeds> newsFeedList = new ArrayList<>();

    private ArrayList<String> newsNames = new ArrayList<>();
    private ArrayList<String> newsSubNames = new ArrayList<>();

    private ArrayAdapter<String> newsCategoryAdapter, newsSubCategoryAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static final String LIMIT = AppConstants.NEWS_FILTER_SIZE;
    private Disposable newsSpinnerDisposable;

    private int categorySelection = 0;
    private boolean isFresh = true;
    private int subCategorySelection = 0;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_main_activity);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            categorySelection = savedInstanceState.getInt(AppConstants.K_SELECTION, 0);
        }

        newsFeedAdapter = new NewsFeedAdapter(this, newsFeedList);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(newsFeedAdapter);

        newsCategoryAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item_drop, newsNames);
        newsCategories.setAdapter(newsCategoryAdapter);

        newsSubCategoryAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item_drop, newsSubNames);
        newsSubCategories.setAdapter(newsSubCategoryAdapter);
        //Add First Category

        newsNames.add(getString(R.string.all_categories));
//        newsSubNames.add("")


        getNewsCategories();
        getNewsFeeds(null);

        //Listener
        newsCategories.setSelection(categorySelection);
        newsCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0) {
                    categorySelection = position;
                    // getNewsFeeds(newsCategoryModels.get(position - 1).getId());
                    getNewsSubCategories(newsCategoryModels.get(position - 1).getId());
                    containerSubCategory.setVisibility(View.VISIBLE);
                } else {
                    containerSubCategory.setVisibility(View.GONE);
                    newsSubCategoryModels.clear();
                    newsSubNames.clear();
                    newsSubCategoryAdapter.notifyDataSetChanged();
                    getNewsFeeds(null);
                    //  getNewsFeeds(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        compositeDisposable.add(RxAdapterView.itemSelections(newsSubCategories).skipInitialValue().subscribe(position -> {
            if (isFresh) {
                subCategorySelection = position;
                getNewsFeeds(newsSubCategoryModels.get(position).getId());
            }


        }));

        showEmergency();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.K_SELECTION, categorySelection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.image_back)
    public void onBackPress() {
        onBackPressed();
    }

    public void getNewsCategories() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<NewsCategoryModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<NewsCategoryModel>> response) {
                hideLoading();
                newsNames.clear();
                newsCategoryModels.clear();
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        newsNames.add(getString(R.string.all_categories));
                        for (NewsCategoryModel newsCategoryModel : response.body().getResponse()) {
                            newsCategoryModels.add(newsCategoryModel);
                            newsNames.add(newsCategoryModel.getCategoryName());
                        }
                    } catch (Exception e) {

                    }

                }
                newsCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<NewsCategoryModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getNewsCategories();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getNewsCategories();
    }

    private void getNewsSubCategories(int categoryId) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<NewsCategoryModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<NewsCategoryModel>> response) {
                hideLoading();
                newsSubNames.clear();
                newsSubCategoryModels.clear();
                newsFeedList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        for (NewsCategoryModel newsCategoryModel : response.body().getResponse()) {
                            newsSubCategoryModels.add(newsCategoryModel);
                            newsSubNames.add(newsCategoryModel.getSubCategoryNames());
                        }

                        if (!newsSubCategoryModels.isEmpty()) {
                            isFresh = !isFresh;
                            subCategorySelection = 0;
                            getNewsFeeds(newsSubCategoryModels.get(0).getId());

//                            newsSubCategories.setSelection(1);
                            //  getNewsFeeds(newsSubCategoryModels.get(0).getId());
                        } else {
                            getNewsFeeds(newsCategoryModels.get(categorySelection - 1).getId());
                        }

                    } catch (Exception e) {

                    }

                }
                newsFeedAdapter.notifyDataSetChanged();
                newsSubCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<NewsCategoryModel>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getNewsCategories();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getNewsSubCategories(categoryId);
    }

    public void getNewsFeeds(Integer categoryId) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<NewsFeeds>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<NewsFeeds>> response) {
                hideLoading();
                newsFeedList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    mNoResultsMessage.setVisibility(response.body().getResponse().isEmpty() ? View.VISIBLE : View.GONE);
                    try {
                        newsFeedList.addAll(response.body().getResponse());
                        newsFeedAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<NewsFeeds>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getNewsFeeds(categoryId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).newsFeeds(categoryId);
    }

    private void showEmergency() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        findViewById(R.id.emergency).setOnClickListener(v -> {
            alertDialog.show();
            alertDialog.findViewById(R.id.btOk).setOnClickListener(v1 -> alertDialog.dismiss());
        });
    }
}
