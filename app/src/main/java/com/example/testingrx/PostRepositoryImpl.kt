package com.example.testingrx

import io.reactivex.rxjava3.core.Single

class PostRepositoryImpl(
    private val networkDataSource: PostDataSource
) : PostRepository {

    override fun getPost(): Single<Post> {
        return networkDataSource.getPost()
    }

    override fun getAllPosts(): Single<List<Post>> {
        return networkDataSource.getAllPosts()
    }
}