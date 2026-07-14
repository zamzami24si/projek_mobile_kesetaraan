package com.example.projek_mobile_asli

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.LembagaBantuan

class LembagaAdapter(
    private var listLembaga: List<LembagaBantuan>,
    private val rolePengguna: String,
    private val onUbahStatus: (LembagaBantuan, String) -> Unit,
    private val onHapus: (LembagaBantuan) -> Unit
) : RecyclerView.Adapter<LembagaAdapter.LembagaViewHolder>() {

    class LembagaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGambar: ImageView = itemView.findViewById(R.id.ivGambarLembaga)
        val tvNama: TextView = itemView.findViewById(R.id.tvNamaLembaga)
        val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeteranganLembaga)
        val tvNomor: TextView = itemView.findViewById(R.id.tvNomorLembaga)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatusLembaga)
        val layoutAksi: LinearLayout = itemView.findViewById(R.id.layoutAksi)
        val btnClose: Button = itemView.findViewById(R.id.btnSetClose)
        val btnOpen: Button = itemView.findViewById(R.id.btnSetOpen)
        val btnHapus: ImageView = itemView.findViewById(R.id.btnHapusLembaga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LembagaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lembaga, parent, false)
        return LembagaViewHolder(view)
    }

    override fun getItemCount(): Int = listLembaga.size

    override fun onBindViewHolder(holder: LembagaViewHolder, position: Int) {
        val data = listLembaga[position]

        holder.tvNama.text = data.namaLembaga
        holder.tvKeterangan.text = data.keterangan
        holder.tvNomor.text = "Nomor : ${data.nomorTelepon}"
        holder.tvStatus.text = "Status : ${data.status}"

        // Tampilkan Gambar jika ada
        if (data.gambarUri.isNotEmpty()) {
            try {
                holder.ivGambar.setImageURI(Uri.parse(data.gambarUri))
            } catch (e: Exception) {
                // Jika gambar terhapus di galeri, biarkan default
            }
        }

        // Warna Teks Status
        if (data.status == LembagaBantuan.STATUS_OPEN) {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"))
        }

        // Hak Akses Jabatan (Sama seperti sebelumnya)
        val role = rolePengguna.lowercase()
        if (role == "admin") {
            holder.layoutAksi.visibility = View.VISIBLE
            holder.btnHapus.visibility = View.VISIBLE
        } else if (role == "konselor") {
            holder.layoutAksi.visibility = View.VISIBLE
            holder.btnHapus.visibility = View.GONE
        } else {
            holder.layoutAksi.visibility = View.GONE
            holder.btnHapus.visibility = View.GONE
        }

        holder.btnClose.setOnClickListener { onUbahStatus(data, LembagaBantuan.STATUS_CLOSE) }
        holder.btnOpen.setOnClickListener { onUbahStatus(data, LembagaBantuan.STATUS_OPEN) }
        holder.btnHapus.setOnClickListener { onHapus(data) }
    }

    fun updateData(newList: List<LembagaBantuan>) {
        listLembaga = newList
        notifyDataSetChanged()
    }
}