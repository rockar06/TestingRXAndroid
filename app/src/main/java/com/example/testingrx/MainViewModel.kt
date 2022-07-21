package com.example.testingrx

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(
    private val repository: PostRepository,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : ViewModel() {

    private val _postLiveData = MutableLiveData<Post>()
    val postLiveData: LiveData<Post> = _postLiveData

    private val _listLiveData = MutableLiveData<List<Post>>()
    val listLiveData: LiveData<List<Post>> = _listLiveData

    fun getPost() {
        repository.getPost()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe { result ->
                _postLiveData.value = result
            }
    }

    fun getAllPosts() {
        repository.getAllPosts()
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe { result ->
                _listLiveData.value = result
            }
    }
}
