package com.example.gitusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gitusers.domain.response.Items
import com.example.gitusers.domain.usecase.FetchUsersUseCase
import com.example.gitusers.utils.LoadingState
import com.example.gitusers.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.UnknownHostException

class UsersViewModel(private var mGetUsersUseCase: FetchUsersUseCase): ViewModel() {

    private var mUsersLiveData = MutableLiveData<Resource<Flow<PagingData<Items>>>>()

    fun observeUsersLiveData() = mUsersLiveData
    private var mApiRequest: Job = Job()
    private var searchQuery = ""


    fun getUsersList(searchQuery: String) {
        mGetUsersUseCase.setParams(searchQuery)
        mApiRequest = viewModelScope.launch {
            mUsersLiveData.postValue(Resource(LoadingState.LOADING, null, null))

            delay(2000)
            try {

                var pagingResponse = Pager(PagingConfig(pageSize = 10)) {
                    mGetUsersUseCase
                }.flow.cachedIn(viewModelScope)

                mUsersLiveData.postValue(Resource(LoadingState.LOADED, pagingResponse, null))
            } catch (e: UnknownHostException) {
                mUsersLiveData.postValue(Resource(LoadingState.ERROR, null, e.message))
            } catch (e: Exception) {
                mUsersLiveData.postValue(Resource(LoadingState.ERROR, null, e.message))
            }
        }
    }

    fun cancelPreviousRequest() {
        if (mApiRequest.isActive) {
            mApiRequest.cancel()
        }
    }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery = searchQuery

    }

}


