package pl.mattiahit.androidweatherk.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.*
import pl.mattiahit.androidweatherk.utils.SchedulerProvider

class HomeViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var locationRepository = mockk<LocationRepository>()
    private var weatherRepository = mockk<WeatherRepository>()

    @InjectMockKs
    private var SUT: HomeViewModel = HomeViewModel(locationRepository,
        weatherRepository,
        SchedulerProvider(Schedulers.io(), TestScheduler()))

    private lateinit var testObserver: TestObserver<WeatherResponse>

    @Before
    fun setup() {
        this.testObserver = TestObserver()
    }

    @Test
    fun getWeatherForCityTest_emptyCityName_responseWithMessage() {
        this.SUT.getWeatherForCity("", this.testObserver)
        this.testObserver.assertComplete().assertValue {
            it.cod == -1
        }
        this.testObserver.assertComplete().assertValue {
            it.message != null && it.message!!.isNotEmpty()
        }
    }

    @Test
    fun getWeatherForCityTest_notEmptyCityName_successResponseWeatherData() {
        every { weatherRepository.getWeatherForCity(any()) } answers {
            Single.just(successWeatherResponse())
        }
        this.SUT.getWeatherForCity("TestCity", this.testObserver)
    }

    // ---- Helpers ----
    private fun successWeatherResponse(): WeatherResponse =
        WeatherResponse(
            "base",
            Clouds(20),
            0,
            Coord(10.99, 10.00),
            1,
            2,
            Main(1.0, 1, 1, 1.0, 1.0, 1.0),
            "name",
            Sys("Test", 1, 1, 1, 1),
            1,
            1,
            null,
            null,
            false,
            ""
        )
}