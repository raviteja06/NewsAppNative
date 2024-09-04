package com.titan.newsappnative

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDao
import com.titan.newsappnative.feature_news.data.data_source.BookmarkDatabase
import com.titan.newsappnative.feature_news.domain.model.Bookmark
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
class BookmarkDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: BookmarkDatabase
    private lateinit var bookmarkDao: BookmarkDao

    @Before
    fun setup() {
        hiltRule.inject()
        bookmarkDao = database.bookmarkDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertBookmark() = runTest {
        val bookmark = Bookmark("a", "b", "c")
        bookmarkDao.insert(bookmark)
        val retrievedBookmark = bookmarkDao.get(bookmark.url)
        assertEquals(bookmark, retrievedBookmark)
    }

    @Test
    fun removeBookmark() = runTest {
        val bookmark = Bookmark("a", "b", "c")
        bookmarkDao.insert(bookmark)
        bookmarkDao.delete(bookmark)
        val retrievedBookmark = bookmarkDao.get(bookmark.url)
        assertNull(retrievedBookmark)
    }
}