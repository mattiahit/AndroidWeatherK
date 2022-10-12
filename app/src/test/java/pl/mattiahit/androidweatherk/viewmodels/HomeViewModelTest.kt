package pl.mattiahit.androidweatherk.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.*
import pl.mattiahit.androidweatherk.utils.SchedulerProvider
import java.util.concurrent.TimeUnit

class HomeViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var locationRepository = mockk<LocationRepository>()
    private var weatherRepository = mockk<WeatherRepository>()

    private var SUT: HomeViewModel = HomeViewModel(locationRepository,
        weatherRepository,
        SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline()))

    private lateinit var testWeatherObserver: TestObserver<WeatherResponse>
    private lateinit var testForecastObserver: TestObserver<ForecastResponse>

    @Before
    fun setup() {
        this.testWeatherObserver = TestObserver()
        this.testForecastObserver = TestObserver()
    }

    @Test
    fun getWeatherForCityTest_emptyCityName_responseWithMessage() {
        this.SUT.getWeatherForCity("", this.testWeatherObserver)
        this.testWeatherObserver.assertComplete().assertValue {
            it.cod == -1
        }
        this.testWeatherObserver.assertComplete().assertValue {
            it.message != null && it.message!!.isNotEmpty()
        }
    }

    @Test
    fun getWeatherForCityTest_notEmptyCityName_successResponseWeatherData() {
        every { weatherRepository.getWeatherForCity(any()) } answers {
            Single.just(successWeatherResponse())
        }
        this.SUT.getWeatherForCity("TestCity", this.testWeatherObserver)
        this.testWeatherObserver.assertValue {
            it.cod == 200
        }
        this.testWeatherObserver.assertValue {
            it.message != null && it.message!!.isEmpty()
        }
    }

    @Test
    fun getWeatherForCityTest_timeout_timeoutResponse() {
        every { weatherRepository.getWeatherForCity(any()) } answers {
            Single.just(successWeatherResponse()).delay(20, TimeUnit.SECONDS)
        }
        this.SUT.getWeatherForCity("TestCity", this.testWeatherObserver)
        this.testWeatherObserver.await()
        this.testWeatherObserver.assertValue {
            it.cod == -1
        }
        this.testWeatherObserver.assertValue {
            it.message.equals("Something went wrong...")
        }
    }

    @Test
    fun getForecastForCityTest_emptyCityName_responseWithMessage() {
        this.SUT.getForecastForCity("", this.testForecastObserver)
        this.testForecastObserver.assertComplete().assertValue {
            it.cod == -1
        }
        this.testForecastObserver.assertComplete().assertValue {
            it.message != null && it.message!!.isNotEmpty()
        }
    }

    // ---- Helpers ----
    private fun successWeatherResponse(): WeatherResponse =
        WeatherResponse(
            "base",
            Clouds(20),
            200,
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

    private fun successForecastResponseWithMessage(message: String): ForecastResponse =
        ForecastResponse(
            City(
                Coord(10.99, 10.99),
                "PL",
                1,
                "Test",
                100,
                6,
                20,
                0
            ),
            0,
            -1,
            listOf(
                ForecastData(
                    Clouds(20),
                    200,
                    "200",
                    Main(
                        1.9,
                        1,
                        1,
                        1.9,
                        1.9,
                        1.9
                    ),
                    2.1,
                    Rain(1.9),
                    Sys("Test", 1, 1, 1, 1),
                    1,
                    listOf(),
                    Wind(1, 1.9, 1.9)
                )
            ),
            message
        )
}