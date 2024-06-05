package com.example.klinikcaremobile.features.petugas.antrian.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R

class CheckUpListAdaptor (private val mList: List<CheckupListViewModel>) : RecyclerView.Adapter<CheckUpListAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_checkup_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val CheckupListViewModel = mList[position]

        holder.userNameView.text = CheckupListViewModel.userName
        holder.userNIKView.text = CheckupListViewModel.userNik
        holder.nomorAntrianView.text = CheckupListViewModel.nomorAntrian

    }
    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val userNameView: TextView = itemView.findViewById(R.id.userNIK_checkup_recycler)
        val userNIKView: TextView = itemView.findViewById(R.id.userNIK_checkup_recycler)
        val nomorAntrianView: TextView = itemView.findViewById(R.id.userTicket_checkup_recycler)
    }
}