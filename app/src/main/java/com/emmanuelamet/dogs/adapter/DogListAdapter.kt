package com.emmanuelamet.dogs.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emmanuelamet.dogs.R
import com.emmanuelamet.dogs.databinding.ItemDogBinding
import com.emmanuelamet.dogs.model.DogBreed
import com.emmanuelamet.dogs.util.getProgressDrawable
import com.emmanuelamet.dogs.util.loadImage
import com.emmanuelamet.dogs.view.DetailFragmentDirections
import com.emmanuelamet.dogs.view.DogClickListener
import com.emmanuelamet.dogs.view.ListFragmentDirections
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(val dogList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogListAdapter.DogViewHolder>(), DogClickListener{

    @SuppressLint("NotifyDataSetChanged")
    fun updateDogList(newDogList: List<DogBreed>){
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    class DogViewHolder(val view: ItemDogBinding) : RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        //val view = inflator.inflate(R.layout.item_dog, parent, false)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflator, R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog =  dogList[position]
        holder.view.listener = this
        /*
        holder.view.name.text = dogList[position].dogBreed
        holder.view.lifeSpan.text = dogList[position].lifeSpan
        dogList[position].imageUrl?.let {
            holder.view.imageUrl.loadImage(
                it,
                getProgressDrawable(holder.view.context))
        }
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            action.dogUuid = dogList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }
        */
    }

    override fun onDogClicked(v: View) {
        val uuid = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}