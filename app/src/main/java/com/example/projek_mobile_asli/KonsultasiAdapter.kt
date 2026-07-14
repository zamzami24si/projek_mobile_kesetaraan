package com.example.projek_mobile_asli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.Konsultasi

class KonsultasiAdapter(
    private var listKonsultasi: List<Konsultasi>,
    private val onItemClick: (Konsultasi) -> Unit
) : RecyclerView.Adapter<KonsultasiAdapter.KonsultasiViewHolder>() {

    class KonsultasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAvatar: TextView = itemView.findViewById(R.id.tvAvatarKonsultasi)
        val dotOnline: View = itemView.findViewById(R.id.dotOnline)
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaKonsultasi)
        val tvPesanTerakhir: TextView = itemView.findViewById(R.id.tvPesanTerakhir)
        val tvWaktu: TextView = itemView.findViewById(R.id.tvWaktuKonsultasi)
        val tvBadge: TextView = itemView.findViewById(R.id.tvBadgeUnread)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonsultasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_konsultasi, parent, false)
        return KonsultasiViewHolder(view)
    }

    override fun getItemCount(): Int = listKonsultasi.size

    override fun onBindViewHolder(holder: KonsultasiViewHolder, position: Int) {
        val data = listKonsultasi[position]
        holder.tvAvatar.text = if (data.namaPengguna.isNotEmpty()) data.namaPengguna.take(1).uppercase() else "?"
        holder.tvNama.text = data.namaPengguna
        holder.tvPesanTerakhir.text = data.pesanTerakhir
        holder.tvWaktu.text = data.waktuTerakhir
        holder.dotOnline.visibility = if (data.online) View.VISIBLE else View.GONE

        if (data.belumDibaca > 0) {
            holder.tvBadge.visibility = View.VISIBLE
            holder.tvBadge.text = data.belumDibaca.toString()
        } else {
            holder.tvBadge.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { onItemClick(data) }
    }

    fun updateData(newList: List<Konsultasi>) {
        listKonsultasi = newList
        notifyDataSetChanged()
    }
}