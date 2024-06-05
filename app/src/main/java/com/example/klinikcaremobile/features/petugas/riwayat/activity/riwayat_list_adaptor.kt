package com.example.klinikcaremobile.features.petugas.riwayat.activity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R

class RiwayatListAdaptor (private val mList: List<RiwayatListViewModel>) : RecyclerView.Adapter<RiwayatListAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_riwayat_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatListAdaptor.ViewHolder, position: Int) {
        val riwayatListViewModel = mList[position]
        Log.d("CheckUpListAdaptor", "Binding data at position $position: $riwayatListViewModel")

        holder.NoRiwayatView.text = riwayatListViewModel.id
        holder.NameRiwayatView.text = riwayatListViewModel.name
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val NoRiwayatView: TextView = itemView.findViewById(R.id.NoAntrian_recycler)
        val NameRiwayatView: TextView = itemView.findViewById(R.id.NamaAntrian_recycler)
    }
}