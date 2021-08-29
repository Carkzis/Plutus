package com.carkzis.android.plutus.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.android.plutus.data.DatabaseCpiItem
import com.carkzis.android.plutus.data.PlutusDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CpiItemDaoTest {

    private lateinit var database: PlutusDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initialiseDatabase() {
        // In MemoryDatabaseBuilder will remove data when tests are run.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PlutusDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAllAndGetCpiRates() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiItem(
                "01/01/1900",
                "100.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
        database.cpiItemDao().insertAll(cpiList)

        // Get items, using just the list, rather than the LiveData for simplicity.
        val retrievedCpiList = database.cpiItemDao().getCpiItemList()

        MatcherAssert.assertThat(retrievedCpiList.first().value, CoreMatchers.`is`("100.0"))
        MatcherAssert.assertThat(retrievedCpiList.size, CoreMatchers.`is`(1))
    }

    @Test
    fun insertAllAddsNewCpiRecord() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiItem(
                "01/01/1900",
                "100.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
        database.cpiItemDao().insertAll(cpiList)

        // Insert a task with the same primary key, but a separate value
        val newCpiList = MutableList(1) {
            DatabaseCpiItem(
                "01/01/1900",
                "102.3",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "2"
            )
        }
        database.cpiItemDao().insertAll(newCpiList)

        val retrievedCpiList = database.cpiItemDao().getCpiItemList()

        MatcherAssert.assertThat(retrievedCpiList.first().value, CoreMatchers.`is`("100.0"))
        MatcherAssert.assertThat(retrievedCpiList.last().value, CoreMatchers.`is`("102.3"))
        MatcherAssert.assertThat(retrievedCpiList.size, CoreMatchers.`is`(2))
    }

    @Test
    fun insertAllOverwritesOnConflict() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiItem(
                "01/01/1900",
                "5.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
        database.cpiItemDao().insertAll(cpiList)

        // Insert a task with the same primary key, but a separate value
        val newCpiList = MutableList(1) {
            DatabaseCpiItem(
                "01/01/1900",
                "105.5",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
        database.cpiItemDao().insertAll(newCpiList)

        val retrievedCpiList = database.cpiItemDao().getCpiItemList()

        MatcherAssert.assertThat(retrievedCpiList.first().value, CoreMatchers.`is`("105.5"))
        MatcherAssert.assertThat(retrievedCpiList.size, CoreMatchers.`is`(1))
    }

}