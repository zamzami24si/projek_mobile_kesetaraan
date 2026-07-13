package com.example.projek_mobile_asli

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.data.entity.Laporan

class LaporanAdapter(
    private var daftarLaporan: List<Laporan>,
    private val rolePengguna: String,
    private val onStatusClick: (Laporan) -> Unit, // BARU: Fungsi saat status diklik
    private val onDeleteClick: (Laporan) -> Unit
) : RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    // ... (LaporanViewHolder dan onCreateViewHolder tetap SAMA) ...
    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_item_judul)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tv_item_deskripsi)
        val tvPengirim: TextView = itemView.findViewById(R.id.tv_item_pengirim)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_item_status)
        val ivFoto: ImageView = itemView.findViewById(R.id.iv_item_foto)
        val btnHapus: ImageView = itemView.findViewById(R.id.btn_item_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = daftarLaporan[position]

        holder.tvJudul.text = laporan.judul
        holder.tvDeskripsi.text = laporan.deskripsi

        if (laporan.is_anonim) {
            holder.tvPengirim.text = "Pengirim: Anonim (Rahasia)"
        } else {
            holder.tvPengirim.text = "Pengirim: ${laporan.nama_pengirim}"
        }

        if (!laporan.foto_bukti.isNullOrEmpty()) {
            holder.ivFoto.visibility = View.VISIBLE
            holder.ivFoto.setImageURI(Uri.parse(laporan.foto_bukti))
        } else {
            holder.ivFoto.visibility = View.GONE
        }

        // --- LOGIKA HAK AKSES ADMIN ---
        if (rolePengguna == "admin") {
            holder.btnHapus.visibility = View.VISIBLE
            holder.btnHapus.setOnClickListener { onDeleteClick(laporan) }

            // Jadikan teks status bisa diklik dan beri ikon pensil
            holder.tvStatus.text = "Status: ${laporan.status} ✏️"
            holder.tvStatus.setOnClickListener { onStatusClick(laporan) }
        } else {
            holder.btnHapus.visibility = View.GONE

            // Teks status biasa, tidak bisa diklik
            holder.tvStatus.text = "Status: ${laporan.status}"
            holder.tvStatus.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int {
        return daftarLaporan.size
    }

    fun updateData(dataBaru: List<Laporan>) {
        daftarLaporan = dataBaru
        notifyDataSetChanged()
    }
}