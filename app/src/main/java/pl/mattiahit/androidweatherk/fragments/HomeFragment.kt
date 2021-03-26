package pl.mattiahit.androidweatherk.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.mattiahit.androidweatherk.viewmodels.HomeViewModel
import pl.mattiahit.androidweatherk.viewmodels.factories.HomeViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject lateinit var mHomeViewModelFactory: HomeViewModelFactory
    lateinit var mHomeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mHomeViewModel = ViewModelProvider(this, this.mHomeViewModelFactory).get(HomeViewModel::class.java)
        this.initializeUI()
    }

    fun initializeUI() {
        this.mHomeViewModel.getLocations().observe(this, Observer {

        })
    }
}