package com.example.xicomtask.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xicomtask.R
import com.example.xicomtask.databinding.ImageItemLayoutBinding

class ImageRecyAdapter(private val mlist: MutableList<String>,val context:Context) :
    RecyclerView.Adapter<ImageRecyAdapter.ImageRecyViewHolder>() {

    class ImageRecyViewHolder(var binding: ImageItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyViewHolder {
        val binding: ImageItemLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_item_layout,
            parent,
            false
        )
        return ImageRecyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageRecyViewHolder, position: Int) {
        val current = mlist[position]
        Glide.with(context).load(current).into(holder.binding.image)


        holder.itemView.setOnClickListener {
            val bundle= Bundle()
            bundle.putString("URL",current)
            it.findNavController().navigate(R.id.action_homeFragment_to_detailFragment,bundle)

        }

    }

    override fun getItemCount(): Int {
        return mlist.size
    }

}


