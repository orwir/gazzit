package orwir.gazzit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import orwir.gazzit.R
import orwir.gazzit.databinding.FragmentSplashBinding
import orwir.gazzit.util.provide

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by provide()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DataBindingUtil
        .inflate<FragmentSplashBinding>(inflater, R.layout.fragment_splash, container, false)
        .root
}

class SplashViewModel : ViewModel()