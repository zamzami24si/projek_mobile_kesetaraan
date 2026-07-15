package com.example.projek_mobile_asli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.NotifikasiItem

class NotifikasiAdapter(private var listNotif: List<NotifikasiItem>) : RecyclerView.Adapter<NotifikasiAdapter.NotifViewHolder>() {

    class NotifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudulNotif)
        val tvIsi: TextView = itemView.findViewById(R.id.tvIsiNotif)
        val tvWaktu: TextView = itemView.findViewById(R.id.tvWaktuNotif)
        val dotUnread: View = itemView.findViewById(R.id.dotUnread)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi, parent, false)
        return NotifViewHolder(view)
    }

    override fun getItemCount(): Int = listNotif.size

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val data = listNotif[position]
        holder.tvJudul.text = data.judul
        holder.tvIsi.text = data.isi
        holder.tvWaktu.text = data.waktu

        // Sembunyikan titik merah jika sudah dibaca
        holder.dotUnread.visibility = if (data.sudahDibaca) View.INVISIBLE else View.VISIBLE
    }
}