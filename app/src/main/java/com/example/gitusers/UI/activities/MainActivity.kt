package com.example.gitusers.UI.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gitusers.R
import com.example.gitusers.UI.adapters.UsersListRecyclerAdapter
import com.example.gitusers.utils.LoadingState
import com.example.gitusers.utils.showToast
import com.example.gitusers.viewmodels.UsersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{


    private lateinit var mMainViewModel: UsersViewModel
    private lateinit var mUsersListAdapter: UsersListRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeUi()
        mMainViewModel = getViewModel()

        observeUsersListResponse()
    }

    private fun initializeUi() {
        mUsersListAdapter = UsersListRecyclerAdapter()
        users_list_rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        users_list_rv.setHasFixedSize(true)
        users_list_rv.adapter = mUsersListAdapter
        submit_btn.setOnClickListener(this)
        main_swipe_refresh_view.setOnRefreshListener(this)




    }

    private fun observeUsersListResponse(){
        mMainViewModel.observeUsersLiveData().observe(this, { response ->
            when (response.status) {
                LoadingState.LOADING -> {
                    empty_list_tv.visibility = View.GONE
                    if (!main_swipe_refresh_view.isRefreshing){
                        loading_shimmer_layout.visibility = View.VISIBLE
                        loading_shimmer_layout.startShimmer()
                    }
                }

                LoadingState.LOADED -> {
                    empty_list_tv.visibility = View.GONE
                    if (main_swipe_refresh_view.isRefreshing){
                        main_swipe_refresh_view.isRefreshing = false
                    }
                    loading_shimmer_layout.stopShimmer()
                    loading_shimmer_layout.visibility = View.GONE

                    lifecycleScope.launch {

                        response.data?.collect {
                            mUsersListAdapter.submitData(it)

                        }
                    }
                    //https://api.github.com/search/users?q=foo

                }

                LoadingState.ERROR -> {
                    empty_list_tv.visibility = View.VISIBLE
                    if (!main_swipe_refresh_view.isRefreshing){
                        main_swipe_refresh_view.isRefreshing = false
                    }

                    loading_shimmer_layout.stopShimmer()
                    loading_shimmer_layout.visibility = View.GONE
                    showToast("Something went wrong", Toast.LENGTH_LONG)
                }

            }


        })
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.submit_btn -> {

                var searchQuery = search_users_et.text.toString()
                if (searchQuery.isNotEmpty()){

                    mMainViewModel.cancelPreviousRequest()
                    mUsersListAdapter.submitData(lifecycle, PagingData.empty())
                    mMainViewModel.getUsersList(searchQuery)

                } else {
                    showToast("Please enter something", Toast.LENGTH_LONG)
                }

            }
        }
    }

    override fun onRefresh() {
        var searchQuery = search_users_et.text.toString()
        mMainViewModel.getUsersList(searchQuery)
    }
}