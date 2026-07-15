package com.example.projek_mobile_asli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.PesanChat

class PesanChatAdapter(private var listPesan: List<PesanChat>) : RecyclerView.Adapter<PesanChatAdapter.PesanViewHolder>() {

    class PesanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPengirim: TextView = itemView.findViewById(R.id.tvPengirim)
        val tvIsiPesan: TextView = itemView.findViewById(R.id.tvIsiPesan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesan, parent, false)
        return PesanViewHolder(view)
    }

    override fun getItemCount(): Int = listPesan.size

    override fun onBindViewHolder(holder: PesanViewHolder, position: Int) {
        val pesan = listPesan[position]
        holder.tvPengirim.text = pesan.pengirim.uppercase()
        holder.tvIsiPesan.text = pesan.isiPesan
    }

    fun updateData(newList: List<PesanChat>) {
        listPesan = newList
        notifyDataSetChanged()
    }
}