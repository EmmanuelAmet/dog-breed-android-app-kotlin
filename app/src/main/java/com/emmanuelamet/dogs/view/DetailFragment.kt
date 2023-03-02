package com.emmanuelamet.dogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.databinding.FragmentDetailBinding
import com.emmanuelamet.dogs.util.getProgressDrawable
import com.emmanuelamet.dogs.util.loadImage
import com.emmanuelamet.dogs.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var dataBinding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)

        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.dogLiveData.observe(viewLifecycleOwner, Observer {dog ->
            dog?.let {
                dataBinding.dog = dog
//                dogName.text = dog.dogBreed
//                lifeSpan.text = dog.lifeSpan
//                dogPurpose.text = dog.breedFor
//                dogTemperament.text = dog.temperament
                context?.let {
                    dog.imageUrl?.let { it1 -> imageView.loadImage(it1, getProgressDrawable(it)) }
                }
            }

        })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        arguments.let {

        }
    }

}