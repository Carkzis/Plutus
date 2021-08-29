package com.carkzis.android.plutus.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.android.plutus.data.DatabaseRpiItem
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
class RpiItemDaoTest {

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
    fun insertAllAndGetRpiRates() = runBlockingTest {
        // Insert a task
        val rpiList = MutableList(1) {
            DatabaseRpiItem(
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
        database.rpiItemDao().insertAll(rpiList)

        // Get items, using just the list, rather than the LiveData for simplicity.
        val retrievedRpiList = database.rpiItemDao().getRpiItemList()

        MatcherAssert.assertThat(retrievedRpiList.first().value, CoreMatchers.`is`("100.0"))
        MatcherAssert.assertThat(retrievedRpiList.size, CoreMatchers.`is`(1))
    }

    @Test
    fun insertAllAddsNewRpiRecord() = runBlockingTest {
        // Insert a task
        val rpiList = MutableList(1) {
            DatabaseRpiItem(
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
        database.rpiItemDao().insertAll(rpiList)

        // Insert a task with the same primary key, but a separate value
        val newRpiList = MutableList(1) {
            DatabaseRpiItem(
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
        database.rpiItemDao().insertAll(newRpiList)

        val retrievedRpiList = database.rpiItemDao().getRpiItemList()

        MatcherAssert.assertThat(retrievedRpiList.first().value, CoreMatchers.`is`("100.0"))
        MatcherAssert.assertThat(retrievedRpiList.last().value, CoreMatchers.`is`("102.3"))
        MatcherAssert.assertThat(retrievedRpiList.size, CoreMatchers.`is`(2))
    }

    @Test
    fun insertAllOverwritesOnConflict() = runBlockingTest {
        // Insert a task
        val rpiList = MutableList(1) {
            DatabaseRpiItem(
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
        database.rpiItemDao().insertAll(rpiList)

        // Insert a task with the same primary key, but a separate value
        val newRpiList = MutableList(1) {
            DatabaseRpiItem(
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
        database.rpiItemDao().insertAll(newRpiList)

        val retrievedRpiList = database.rpiItemDao().getRpiItemList()

        MatcherAssert.assertThat(retrievedRpiList.first().value, CoreMatchers.`is`("105.5"))
        MatcherAssert.assertThat(retrievedRpiList.size, CoreMatchers.`is`(1))
    }

}