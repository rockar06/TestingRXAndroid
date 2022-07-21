package com.example.testingrx

import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Single
import org.junit.Test

class PostRepositoryImplTest {

    private val mockDataSource = MockSinglePostDataSource()
    private val systemUnderTest = PostRepositoryImpl(mockDataSource)

    @Test
    fun `test getPost using Truth to validate behavior`() {
        val singleResult = systemUnderTest.getPost()
        var result: Post? = null
        singleResult.subscribe { post ->
            result = post
        }
        assertThat(result).isNotNull()
        assertThat(result!!.title).isEqualTo("Title")
    }

    @Test
    fun `test getAllPosts using Truth to validate behavior`() {
        val singleResult = systemUnderTest.getAllPosts()
        val result = mutableListOf<Post>()
        singleResult.subscribe(result::addAll)
        assertThat(result).isNotEmpty()
        assertThat(result).hasSize(10)
        assertThat(result.first().title).isEqualTo("Title: 1")
        assertThat(result.last().title).isEqualTo("Title: 10")
    }

    @Test(expected = RuntimeException::class)
    fun `test getPost and receive an exception`() {
        mockDataSource.thrownAnException = true
        systemUnderTest.getPost()
    }

    @Test
    fun `test getPost using TestObserver to validate behaviour`() {
        with(systemUnderTest.getPost().test()) {
            assertComplete()
            assertNoErrors()
            assertValue { post ->
                post.title == "Title"
            }
        }
    }

    @Test
    fun `test getAllPosts using TestObserver to validate behaviour`() {
        with(systemUnderTest.getAllPosts().test()) {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertValue { list ->
                list.size == 10
            }
            assertValue { list ->
                list.first().title == "Title: 1"
            }
            assertValue { list ->
                list.last().title == "Title: 10"
            }
        }
    }

    @Test
    fun `test getPost using TestObserver and receive an exception`() {
        val testObserver = systemUnderTest.getPost()
            .concatWith(Single.error(RuntimeException())).test()
        with(testObserver) {
            assertError(RuntimeException::class.java)
            assertNotComplete()
        }
    }
}

private class MockSinglePostDataSource : PostDataSource {

    var thrownAnException = false
    private val testList = (1..10).map {
        Post("Title: $it")
    }

    override fun getPost(): Single<Post> {
        return if (thrownAnException) {
            thrownAnException = false
            throw RuntimeException("Hello!")
        } else {
            Single.just(Post("Title"))
        }
    }

    override fun getAllPosts(): Single<List<Post>> {
        return Single.just(testList)
    }
}