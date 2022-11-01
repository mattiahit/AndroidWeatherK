package pl.mattiahit.androidweatherk.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.mattiahit.androidweatherk.home.data.repository.LocationRepository
import pl.mattiahit.androidweatherk.home.data.repository.WeatherRepository
import pl.mattiahit.androidweatherk.home.ui.viewmodel.HomeViewModel
import pl.mattiahit.androidweatherk.rest.model.*
import pl.mattiahit.androidweatherk.utils.SchedulerProvider
import pl.mattiahit.androidweatherk.utils.TimeProvider
import java.util.concurrent.TimeUnit

class HomeViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private var locationRepository = mockk<LocationRepository>()
    private var weatherRepository = mockk<WeatherRepository>()
    private var timeProvider = mockk<TimeProvider>()
    private var context = mockk<Context>()

    private var SUT: HomeViewModel = HomeViewModel(locationRepository,
        weatherRepository,
        SchedulerProvider(Schedulers.trampoline(), Schedulers.trampoline()),
        timeProvider)

    private lateinit var testWeatherObserver: TestObserver<WeatherResponse>
    private lateinit var testForecastObserver: TestObserver<ForecastResponse>

    @Before
    fun setup() {
        this.testWeatherObserver = TestObserver()
        this.testForecastObserver = TestObserver()
        every { timeProvider.getApiResponseTimeoutSeconds() } answers {
            2
        }
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
            Single.just(successWeatherResponse()).delay(3, TimeUnit.SECONDS)
        }
        this.SUT.getWeatherForCity("TestCity", this.testWeatherObserver)
        this.testWeatherObserver.await()
        this.testWeatherObserver.assertValue {
            it.cod == -1
        }
        this.testWeatherObserver.assertValue {
            it.message!!.isNotEmpty()
        }
    }

    @Test
    fun getWeatherForCityTest_cityNotEmptyErrorCityNotFound_errorResponse() {
        every { weatherRepository.getWeatherForCity(any()) } answers {
            Single.just(errorWeatherResponse())
        }
        this.SUT.getWeatherForCity("Testy", this.testWeatherObserver)
        this.testWeatherObserver.assertValue {
            it.cod > 0 && it.cod != 200
        }
        this.testWeatherObserver.assertValue {
            it.message!!.isNotEmpty()
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

    @Test
    fun getForecastForCityTest_notEmptyCityName_successResponseForecastData() {
        every { weatherRepository.getForecastForCity(any()) } answers {
            Single.just(successForecastResponse())
        }
        this.SUT.getForecastForCity("Test", this.testForecastObserver)
        this.testForecastObserver.assertValue {
            it.cod == 200
        }
        this.testForecastObserver.assertValue {
            it.message != null && it.message!!.isEmpty()
        }
    }

    @Test
    fun getForecastForCityTest_timeout_timeoutResponse() {
        every { weatherRepository.getForecastForCity(any()) } answers {
            Single.just(successForecastResponse()).delay(3, TimeUnit.SECONDS)
        }
        this.SUT.getForecastForCity("Test", this.testForecastObserver)
        this.testForecastObserver.await()
        this.testForecastObserver.assertValue {
            it.cod == -1
        }
        this.testForecastObserver.assertValue {
            it.message!!.isNotEmpty()
        }
    }

    @Test
    fun getForecastForCityTest_cityNotEmptyErrorCityNotFound_errorResponse() {
        every { weatherRepository.getForecastForCity(any()) } answers {
            Single.just(errorForecastResponse())
        }
        this.SUT.getForecastForCity("Testy", this.testForecastObserver)
        this.testForecastObserver.assertValue {
            it.cod > 0 && it.cod != 200
        }
        this.testForecastObserver.assertValue {
            it.message!!.isNotEmpty()
        }
    }

    @Test
    fun getForecastDataLocalFromForecastResponseTest_errorForecastResponse_emptyList() {
        val result = this.SUT.getForecastDataLocalFromForecastResponse(errorForecastResponse(), context)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun getForecastDataLocalFromForecastResponseTest_successForecastResponse_notEmptyList() {
        val result = this.SUT.getForecastDataLocalFromForecastResponse(successForecastResponse(), context)
        Assert.assertTrue(result.isNotEmpty())
    }

    // ---- Helpers ----
    private fun errorWeatherResponse(): WeatherResponse =
        WeatherResponse(
            null,
            null,
            404,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            "city not found"
        )

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

    private fun errorForecastResponse(): ForecastResponse =
        ForecastResponse(
            null,
            null,
            404,
            null,
            "city not found"
        )

    private fun successForecastResponse(): ForecastResponse =
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
            200,
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
            ""
        )
}