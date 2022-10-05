package pl.mattiahit.androidweatherk.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import pl.mattiahit.androidweatherk.repositories.LocationRepository
import pl.mattiahit.androidweatherk.repositories.WeatherRepository
import pl.mattiahit.androidweatherk.rest.model.WeatherResponse
import pl.mattiahit.androidweatherk.v2.fragments.HomeFragment


@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @InjectMockKs
    private lateinit var SUT: HomeViewModel
    @MockK
    private lateinit var locationRepository: LocationRepository
    @MockK
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var testScheduler: TestScheduler
    private lateinit var testObserver: TestObserver<WeatherResponse>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        this.testScheduler = TestScheduler()
        this.testObserver = TestObserver()
        this.SUT = HomeViewModel(locationRepository, weatherRepository)
        this.SUT.setObserveOnThread(this.testScheduler)
    }

    @Test
    fun getWeatherForCityTest_emptyCityName_errorResponseWithMessage() {
        this.SUT.getWeatherForCity("", this.testObserver)
        this.testObserver.assertComplete().assertValue {
            it.cod == -1
        }
        this.testObserver.assertComplete().assertValue {
            it.message != null && it.message!!.isNotEmpty()
        }
    }
}