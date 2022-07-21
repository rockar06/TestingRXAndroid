package com.example.testingrx

import io.reactivex.rxjava3.core.Single

interface PostRepository {

    fun getPost(): Single<Post>

    fun getAllPosts(): Single<List<Post>>
}
