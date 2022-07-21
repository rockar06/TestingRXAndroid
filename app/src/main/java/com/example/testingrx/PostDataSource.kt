package com.example.testingrx

import io.reactivex.rxjava3.core.Single

interface PostDataSource {

    fun getPost(): Single<Post>

    fun getAllPosts(): Single<List<Post>>
}
