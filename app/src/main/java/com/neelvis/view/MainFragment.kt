package com.neelvis.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.neelvis.R
import com.neelvis.databinding.FragmentMainBinding
import com.neelvis.model.Model
import com.neelvis.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
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
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel

        viewModel.weatherData.observe(viewLifecycleOwner, { weather ->
            viewBinder.temperatureTextView.text = (weather?.temperature?.toInt() ?: -100).toString()
        })

    }

}