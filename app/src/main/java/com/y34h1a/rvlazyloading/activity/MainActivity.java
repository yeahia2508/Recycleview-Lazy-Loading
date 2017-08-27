package com.y34h1a.rvlazyloading.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.y34h1a.rvlazyloading.interfaces.OnDataLoadListener;
import com.y34h1a.rvlazyloading.R;
import com.y34h1a.rvlazyloading.adapter.UserAdapter;
import com.y34h1a.rvlazyloading.model.User;
import com.y34h1a.rvlazyloading.provider.DataProvider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataLoadListener{
    
    private RecyclerView mRecyclerView;
    private List<User> mUserSubList = new ArrayList<>();
    private List<User> mUserAllList = new ArrayList<>();
    private UserAdapter mUserAdapter;

    private Handler handler;
    private DataProvider dataProvider;

    private int onScrollIndex = 0;
    private int mPrevTotalItemCount = 0;
    private int mDifference = 0;
    final int UPDATE_ITEM_COUNT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListeners();
        dataProvider.getData();

    }

    //Initialize View Objects
    private void initView() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Lazy Loading");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter = new UserAdapter(getApplicationContext(), mUserSubList);
        mRecyclerView.setAdapter(mUserAdapter);

        handler = new Handler();
        dataProvider = new DataProvider();
    }

    //Initialize Listeners
    private void initListeners() {

        dataProvider.setOnDataLoadListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //current total item count in Reycycleview
                int totalItemCount = linearLayoutManager.getItemCount();
                //current upper list item position
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                //current bottom list item position
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //current visible item number in Reycleview
                int visibleItemCount = (lastVisibleItem - firstVisibleItem) + 1;


                if(((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount ){
                    mPrevTotalItemCount = totalItemCount;
                    mDifference = mUserAllList.size() - totalItemCount;
                    addMoreData();
                }
            }
        });
    }


    /**
    * When data will be provided form {@link DataProvider#getData()}  }
    * */
    
    @Override
    public void onDataLoaded(List<User> users) {
        mUserSubList.clear();
        mUserAllList.clear();
        mUserAllList.addAll(users);
        
        addMoreData();
    }
    
    /**
     * Add more data to {@link #mUserSubList}
     * At First a sub list will be created from {@link #mUserAllList}, based on {@link #onScrollIndex} variable
     * When  {@link #onScrollIndex} is 0 then subList {@link #mUserSubList} will be extract form {@link #mUserAllList} like this (0, 0 + 6)
     * When  {@link #onScrollIndex} is 1 then subList {@link #mUserSubList} will be extract form {@link #mUserAllList} like this (6, 6 + 6)
     * When  {@link #onScrollIndex} is 2 then subList {@link #mUserSubList} will be extract form {@link #mUserAllList} like this (12, 12 + 6)
    * */

    private void addMoreData() {

        try {

            List<User> users;
            if (mDifference !=0 && mDifference < UPDATE_ITEM_COUNT){
                users = mUserAllList.
                        subList(UPDATE_ITEM_COUNT * onScrollIndex,
                                onScrollIndex * UPDATE_ITEM_COUNT + mDifference);
            }else{
                users = mUserAllList.
                        subList(UPDATE_ITEM_COUNT * onScrollIndex,
                                onScrollIndex * UPDATE_ITEM_COUNT + UPDATE_ITEM_COUNT);
            }
            mUserSubList.addAll(users);

            final int currentUserSize = users.size();
            final int curAdapterSize = mUserAdapter.getItemCount();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(mUserAdapter != null){
                        mUserAdapter.notifyItemRangeInserted(curAdapterSize, currentUserSize);
                    }
                }
            });

            onScrollIndex++;

        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }

    
}
