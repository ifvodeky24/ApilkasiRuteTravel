package com.example.aplikasirutetravel.ui.perusahaan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.data.source.remote.service.ApiConfig
import com.example.aplikasirutetravel.databinding.ItemPerusahaanBinding

class PerusahaanAdapter(private val callback: PerusahaanCallback) :
    RecyclerView.Adapter<PerusahaanAdapter.ListViewHolder>() {

    private var listPerusahaan = ArrayList<PerusahaanEntity>()

    fun setPerusahaan(perusahaan: List<PerusahaanEntity>?) {
        if (perusahaan == null) return
        this.listPerusahaan.clear()
        this.listPerusahaan.addAll(perusahaan)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemPerusahaanBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPerusahaan[position])
    }

    override fun getItemCount(): Int = listPerusahaan.size

    inner class ListViewHolder(private val binding: ItemPerusahaanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(perusahaan: PerusahaanEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(ApiConfig.perusahaan_image + perusahaan.foto)
                    .into(ivGambarPerusahaan)

                tvNamaPerusahaan.text = perusahaan.nama_perusahaan
                tvAlamatPerusahaan.text = perusahaan.alamat_perusahaan

                itemView.setOnClickListener {
                    callback.onItemClick(perusahaan.id_perusahaan)
                }
            }
        }
    }
}