package com.neelvis.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.icu.text.DateFormat
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.neelvis.R
import com.neelvis.databinding.FragmentMainBinding
import com.neelvis.model.Weather
import com.neelvis.model.livedata.ApiEmptyResponse
import com.neelvis.model.livedata.ApiErrorResponse
import com.neelvis.model.livedata.ApiSuccessResponse
import com.neelvis.viewmodel.MainFragmentViewModel
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

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

        val imageHolder = viewBinder.fragmentMain
        val image = ImageView(this.context)
        image.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)//imageHolder.layoutParams
        image.setColorFilter(Color.RED)
        image.setImageDrawable(getDrawable(this.requireContext(), R.drawable.loader))
        //TODO: Add image instead of this creepy one
        image.scaleType = ImageView.ScaleType.FIT_XY
        imageHolder.addView(image)

        viewBinder.dateTextView.text = getDateInstance(DateFormat.MEDIUM, Locale.UK).format(Date())
        // SimpleDateFormat("yyyy/MM/dd")
        fragmentViewModel.weatherLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is ApiSuccessResponse<Weather> -> {
                    val currentWeather = response.body
                    viewBinder.temperatureTextView.text = resources
                        .getString(
                            R.string.temperature,
                            (currentWeather.main.currentTemperature - 273.15).toInt()
                        )
                    viewBinder.descriptionTextView.text = currentWeather.info[0].description
                    val highestTemp = (currentWeather.main.temperatureTodayMax - 273.15).toInt()
                    val lowestTemp = (currentWeather.main.temperatureTodayMin - 273.15).toInt()
                    viewBinder.tLimitsTextView.text = resources
                        .getString(R.string.temperature_limits, highestTemp, lowestTemp)
                    viewBinder.ortTextView.text = response.body.name

                    imageHolder.removeView(image)
                }
                is ApiEmptyResponse<Weather> -> Log.d("MAIN FRG", "Empty response")
                is ApiErrorResponse<Weather> -> Log.d("MAIN FRG", "Error: ${response.errorMessage}")
            }
        })

    }
}