package com.carkzis.android.plutus.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.carkzis.android.plutus.data.DatabaseCpiPct
import com.carkzis.android.plutus.data.PlutusDatabase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CpiPctDaoTest {

    private lateinit var database: PlutusDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initialiseDatabase() {
        // In MemoryDatabaseBuilder will remove data when tests are run.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            PlutusDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertAllAndGetCpiRates() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiPct(
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
        database.cpiDao().insertAll(cpiList)

        // Get items, using just the list, rather than the LiveData for simplicity.
        val retrievedCpiList = database.cpiDao().getCpiRateList()

        assertThat(retrievedCpiList.first().value, `is`("5.0"))
        assertThat(retrievedCpiList.size, `is`(1))
    }

    @Test
    fun insertAllAddsNewCpiRecord() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiPct(
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
        database.cpiDao().insertAll(cpiList)

        // Insert a task with the same primary key, but a separate value
        val newCpiList = MutableList(1) {
            DatabaseCpiPct(
                "01/01/1900",
                "1.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "2"
            )
        }
        database.cpiDao().insertAll(newCpiList)

        val retrievedCpiList = database.cpiDao().getCpiRateList()

        assertThat(retrievedCpiList.first().value, `is`("5.0"))
        assertThat(retrievedCpiList.last().value, `is`("1.0"))
        assertThat(retrievedCpiList.size, `is`(2))
    }

    @Test
    fun insertAllOverwritesOnConflict() = runBlockingTest {
        // Insert a task
        val cpiList = MutableList(1) {
            DatabaseCpiPct(
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
        database.cpiDao().insertAll(cpiList)

        // Insert a task with the same primary key, but a separate value
        val newCpiList = MutableList(1) {
            DatabaseCpiPct(
                "01/01/1900",
                "1.0",
                "N/A",
                "1900",
                "January",
                "Q1",
                "N/A",
                "N/A",
                "1"
            )
        }
        database.cpiDao().insertAll(newCpiList)

        val retrievedCpiList = database.cpiDao().getCpiRateList()

        assertThat(retrievedCpiList.first().value, `is`("1.0"))
        assertThat(retrievedCpiList.size, `is`(1))
    }

}