package com.example.tugaspertemuan12_room

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SupporterAdapter(
    private var supporters: MutableList<SupporterNote>,
    private val context: Context,
    private val db: FirebaseFirestore
) : RecyclerView.Adapter<SupporterAdapter.SupporterViewHolder>() {

    class SupporterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val supporterTextView: TextView = itemView.findViewById(R.id.supporterTextView)
        val clubTextView: TextView = itemView.findViewById(R.id.clubTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupporterViewHolder {
        // Menginflate layout untuk setiap item supporter
        val view = LayoutInflater.from(parent.context).inflate(R.layout.supporter_item, parent, false)
        return SupporterViewHolder(view)
    }

    override fun getItemCount(): Int = supporters.size

    override fun onBindViewHolder(holder: SupporterViewHolder, position: Int) {
        // Mendapatkan data supporter pada posisi tertentu
        val supporterNote = supporters[position]

        // Menetapkan data supporter ke dalam tampilan
        holder.supporterTextView.text = supporterNote.supporterName
        holder.clubTextView.text = supporterNote.clubName

        // Menambahkan listener untuk tombol update
        holder.updateButton.setOnClickListener {
            // Membuat intent untuk membuka UpdateSupporterActivity dengan mengirimkan ID supporter
            val intent = Intent(holder.itemView.context, UpdateSupporterActivity::class.java).apply {
                putExtra("supporter_id", supporterNote.id)
            }
            context.startActivity(intent)
        }

        // Menambahkan listener untuk tombol delete
        holder.deleteButton.setOnClickListener {
            // Menghapus data supporter dari Firestore
            db.collection("supporters").document(supporterNote.id)
                .delete()
                .addOnSuccessListener {
                    // Pengecekan sebelum menghapus item dari daftar
                    if (position < supporters.size) {
                        supporters.removeAt(position)
                        notifyItemRemoved(position)
                        showToast("Supporter telah dihapus")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Gagal menghapus supporter: $e")
                }
        }
    }

    // Mengambil data supporter dari Firestore dan memperbarui tampilan
    fun refreshData() {
        db.collection("supporters")
            .get()
            .addOnSuccessListener { result ->
                supporters.clear()
                for (document in result) {
                    val supporterNote = document.toObject(SupporterNote::class.java)
                    supporterNote.id = document.id
                    supporters.add(supporterNote)
                }
                notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                showToast("Gagal mengambil data supporter: $e")
            }
    }

    // Menampilkan pesan toast
    private fun showToast(message: String) {
        // Implementasi showToast
    }
}
