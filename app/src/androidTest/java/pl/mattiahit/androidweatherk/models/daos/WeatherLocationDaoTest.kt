package pl.mattiahit.androidweatherk.models.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.mattiahit.androidweatherk.database.AppDatabase
import pl.mattiahit.androidweatherk.models.WeatherLocation

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherLocationDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: WeatherLocationDao
    private lateinit var testingLocation: WeatherLocation

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.locationDao()
        testingLocation = WeatherLocation(1, " test", 0.0, 0.0, true)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNewLocation_shouldBeInAllLocations() {
        dao.insertLocation(testingLocation).blockingAwait()
        dao.getAllLocations().test().assertValue { list ->
            testingLocation in list
        }
    }

    @Test
    fun deleteLocation_shouldNotBeInLocations() {
        dao.deleteLocation(testingLocation).blockingGet()
        dao.getAllLocations().test().assertValue { list ->
            testingLocation !in list
        }
    }
}