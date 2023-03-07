package com.emmanuelamet.dogs.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.databinding.FragmentDetailBinding
import com.emmanuelamet.dogs.databinding.SendSmsDialogBinding
import com.emmanuelamet.dogs.model.DogBreed
import com.emmanuelamet.dogs.model.DogPalette
import com.emmanuelamet.dogs.model.SmsInfo
import com.emmanuelamet.dogs.util.getProgressDrawable
import com.emmanuelamet.dogs.util.loadImage
import com.emmanuelamet.dogs.viewmodel.DetailViewModel
import com.google.android.material.color.utilities.CorePalette
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment() {
    private lateinit var dataBinding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var dogUuid = 0

    private var currentDog: DogBreed? = null

    private var sendSmsStarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
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
            currentDog = dog
            dog?.let {
                dataBinding.dog = dog
//                dogName.text = dog.dogBreed
//                lifeSpan.text = dog.lifeSpan
//                dogPurpose.text = dog.breedFor
//                dogTemperament.text = dog.temperament
                context?.let {
                    dog.imageUrl?.let { it1 -> imageView.loadImage(it1, getProgressDrawable(it)) }
                    dog.imageUrl?.let { it1 -> setBackgroundColor(it1) }
                }
            }

        })
    }

    private fun setBackgroundColor(urL: String){
        Glide.with(this)
            .asBitmap()
            .load(urL)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate{
                            val inColor = it?.vibrantSwatch?.rgb ?: 0
                            val myPalette = DogPalette(inColor)
                            dataBinding.palette = myPalette
                        }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_send_sms -> {
                sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Dog breed")
                intent.putExtra(Intent.EXTRA_TEXT, "Dog name: ${currentDog?.dogBreed},\nBreed for: ${currentDog?.breedFor}\nLifespan: ${currentDog?.lifeSpan}\norigin: ${currentDog?.origin},\nimage: ${currentDog?.imageUrl}")
                intent.putExtra(Intent.EXTRA_STREAM, "${currentDog?.imageUrl}")
                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean){
        if(sendSmsStarted && permissionGranted){
            context?.let {
                val smsInfo = SmsInfo("", "Dog name: ${currentDog?.dogBreed},\nBreed for: ${currentDog?.breedFor}\nLifespan: ${currentDog?.lifeSpan}\norigin: ${currentDog?.origin}", currentDog?.imageUrl.toString())
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it), R.layout.send_sms_dialog, null, false)
                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS"){dialog, which ->
                        if(!dialogBinding.smsDestination.text.isNullOrEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel"){dialog, which ->}
                    .show()
                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val ip = PendingIntent.getActivity(context, 0, intent, 0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, ip, null)
    }

}