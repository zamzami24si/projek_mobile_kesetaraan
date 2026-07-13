package com.example.projek_mobile_asli.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projek_mobile_asli.R
import com.example.projek_mobile_asli.data.entity.User

class UserAdapter(private var list: List<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    // Menyambungkan ID dari desain XML ke dalam variabel
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama: TextView = view.findViewById(R.id.tv_nama)
        val email: TextView = view.findViewById(R.id.tv_email)
    }

    // Memanggil layout row_user.xml untuk setiap baris
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return ViewHolder(view)
    }

    // Mengisi data ke dalam komponen TextView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        holder.nama.text = user.nama
        holder.email.text = user.email
    }

    // Menghitung total data
    override fun getItemCount(): Int {
        return list.size
    }
}