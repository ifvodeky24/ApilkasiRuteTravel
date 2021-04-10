package com.example.aplikasirutetravel.ui.angkutan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.databinding.ItemAngkutanBinding

class AngkutanAdapter(private val callback: AngkutanCallback) :
    RecyclerView.Adapter<AngkutanAdapter.ListViewHolder>() {

    private var listAngkutan = ArrayList<TrayekEntity>()

    fun setAngkutan(angkutan: List<TrayekEntity>?) {
        if (angkutan == null) return
        this.listAngkutan.clear()
        this.listAngkutan.addAll(angkutan)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemAngkutanBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listAngkutan[position])
    }

    override fun getItemCount(): Int = listAngkutan.size

    inner class ListViewHolder(private val binding: ItemAngkutanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(angkutan: TrayekEntity) {
            with(binding) {

                tvNamaAngkutan.text = angkutan.nama_trayek
                tvAsal.text = angkutan.asal
                tvTujuan.text = angkutan.tujuan
                tvHari.text = angkutan.hari
                tvJam.text = angkutan.jam

                itemView.setOnClickListener {
                    callback.onItemClick(angkutan)
                }
            }
        }
    }
}