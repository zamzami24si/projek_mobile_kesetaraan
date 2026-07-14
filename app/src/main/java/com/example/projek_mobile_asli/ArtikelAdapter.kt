package com.example.projek_mobile_asli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.Artikel

class ArtikelAdapter(
    private var listArtikel: List<Artikel>,
    private val rolePengguna: String, // Tambahan: Menerima role
    private val onHapus: (Artikel) -> Unit
) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tvJudulArtikel)
        val tvIsi: TextView = itemView.findViewById(R.id.tvIsiArtikel)
        val tvWaktu: TextView = itemView.findViewById(R.id.tvWaktuArtikel)
        val btnHapus: ImageView = itemView.findViewById(R.id.btnHapusArtikel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artikel, parent, false)
        return ArtikelViewHolder(view)
    }

    override fun getItemCount(): Int = listArtikel.size

    override fun onBindViewHolder(holder: ArtikelViewHolder, position: Int) {
        val data = listArtikel[position]
        holder.tvJudul.text = data.judul
        holder.tvIsi.text = data.isi
        holder.tvWaktu.text = data.waktu

        // LOGIKA HAK AKSES
        val role = rolePengguna.lowercase()
        if (role == "admin") {
            // Jika admin, munculkan tombol hapus
            holder.btnHapus.visibility = View.VISIBLE
            holder.btnHapus.setOnClickListener { onHapus(data) }
        } else {
            // Jika user/konselor, sembunyikan tombol hapus
            holder.btnHapus.visibility = View.GONE
            holder.btnHapus.setOnClickListener(null)
        }
    }

    fun updateData(newList: List<Artikel>) {
        listArtikel = newList
        notifyDataSetChanged()
    }
}