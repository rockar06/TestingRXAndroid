package com.example.testingrx

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    private val mockRepository = MockSingleRepository()
    private val testScheduler = TestScheduler()
    private val systemUnderTest = MainViewModel(mockRepository, testScheduler, testScheduler)

    // For LiveData
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @Test
    fun `test getPost and receive a single post`() {
        systemUnderTest.getPost()
        systemUnderTest.postLiveData.captureValues {
            testScheduler.triggerActions()
            val result = this.values.first()
            assertThat(result).isNotNull()
            assertThat(result!!.title).isEqualTo("Title")
        }
    }

    @Test
    fun `test getAllPost and receive ten post from a single`() {
        systemUnderTest.getAllPosts()
        systemUnderTest.listLiveData.captureValues {
            testScheduler.triggerActions()
            val result = values.first()!!
            assertThat(result).isNotEmpty()
            assertThat(result).hasSize(10)
            assertThat(result.first().title).isEqualTo("Title: 1")
            assertThat(result.last().title).isEqualTo("Title: 10")
        }
    }
}

private class MockSingleRepository : PostRepository {

    override fun getPost(): Single<Post> {
        return Single.just(Post("Title"))
    }

    override fun getAllPosts(): Single<List<Post>> {
        return Single.just(
            (1..10).map {
                Post("Title: $it")
            }
        )
    }
}