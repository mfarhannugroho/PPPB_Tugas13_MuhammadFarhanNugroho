package com.example.tugaspertemuan12_room

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SupporterAdapter(private var supporters: MutableList<SupporterNote>, private val context: Context) :
    RecyclerView.Adapter<SupporterAdapter.SupporterViewHolder>() {

    private val db: SupporterDatabaseHelper = SupporterDatabaseHelper(context)

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
            // Menampilkan dialog konfirmasi sebelum menghapus supporter
            AlertDialog.Builder(context).apply {
                setTitle("Hapus Supporter")
                setMessage("Apakah kamu yakin untuk menghapusnya?")
                setPositiveButton("Ya") { _, _ ->
                    // Menghapus supporter dari database dan daftar
                    db.deleteSupporter(supporterNote.id)
                    supporters.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Supporter telah dihapus", Toast.LENGTH_SHORT).show()
                }
                setNegativeButton("Tidak", null)
            }.create().show()
        }
    }
}
