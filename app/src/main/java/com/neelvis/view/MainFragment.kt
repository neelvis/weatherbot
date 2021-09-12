package com.neelvis.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neelvis.databinding.FragmentMainBinding
import com.neelvis.model.Weather
import com.neelvis.model.livedata.ApiEmptyResponse
import com.neelvis.model.livedata.ApiErrorResponse
import com.neelvis.model.livedata.ApiSuccessResponse
import com.neelvis.viewmodel.MainFragmentViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var fragmentViewModel: MainFragmentViewModel
    private lateinit var viewBinder: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = FragmentMainBinding.inflate(inflater, container, false)
        return viewBinder.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
        // TODO: Use the ViewModel

        fragmentViewModel.weatherLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiSuccessResponse<Weather> -> {
                    viewBinder.temperatureTextView.text = response.body.temperature
                    viewBinder.descriptionTextView.text = response.body.name
                }
                is ApiEmptyResponse<Weather> -> Log.d("MAIN FRG", "Empty response")
                is ApiErrorResponse<Weather> -> Log.d("MAIN FRG", "Error: ${response.errorMessage}")
            }
        })

    }

}