package com.example.klinikcaremobile.features.pasien.records.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.klinikcaremobile.R

class RecordsAdaptor (private val mList: List<RecordListViewModel>) : RecyclerView.Adapter<RecordsAdaptor.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_records_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recordListViewModel = mList[position]

        holder.diagnosisValueView.text = recordListViewModel.hasilDiagnosa
        holder.alergiValueView.text = recordListViewModel.hasilAlergi
        holder.obatValueView.text = recordListViewModel.hasilObat
        holder.timeValueView.text = recordListViewModel.hasilTime
        holder.pemeriksaValueView.text = recordListViewModel.hasilPemeriksa
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View):RecyclerView.ViewHolder(ItemView)  {
        val diagnosisValueView: TextView = itemView.findViewById(R.id.value_hasil_diagnosa)
        val alergiValueView: TextView = itemView.findViewById(R.id.value_hasil_alergi)
        val obatValueView: TextView = itemView.findViewById(R.id.value_hasil_obat)
        val timeValueView: TextView = itemView.findViewById(R.id.value_hasil_time)
        val pemeriksaValueView: TextView = itemView.findViewById(R.id.value_pemeriksa_diagnosa)
    }
}

