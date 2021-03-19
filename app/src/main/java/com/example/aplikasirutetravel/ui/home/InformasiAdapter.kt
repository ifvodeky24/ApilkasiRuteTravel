package com.example.aplikasirutetravel.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasirutetravel.databinding.ItemInformasiBinding

class InformasiAdapter(private val listInformasi: ArrayList<Informasi>) :
    RecyclerView.Adapter<InformasiAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemInformasiBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listInformasi[position])
    }

    override fun getItemCount(): Int = listInformasi.size

    inner class ListViewHolder(private val binding: ItemInformasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(informasi: Informasi) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(informasi.photo)
                    .into(imageIklan)
            }
        }
    }
}