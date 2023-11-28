package com.example.tugaspertemuan12_room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspertemuan12_room.databinding.ActivityUpdateSupporterBinding
import com.google.firebase.firestore.FirebaseFirestore

class UpdateSupporterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSupporterBinding
    private lateinit var db: FirebaseFirestore
    private var supporterId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding untuk layout activity_update_supporter.xml
        binding = ActivityUpdateSupporterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi objek FirebaseFirestore untuk berinteraksi dengan Firestore
        db = FirebaseFirestore.getInstance()

        // Mendapatkan ID supporter yang akan diperbarui dari intent
        supporterId = intent.getStringExtra("supporter_id") ?: ""

        // Menutup aktivitas jika ID supporter tidak valid
        if (supporterId.isEmpty()) {
            showToast("ID supporter tidak valid.")
            finish()
            return
        }

        // Mengambil data supporter dari Firestore berdasarkan ID
        db.collection("supporters").document(supporterId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Mengonversi dokumen Firestore ke objek SupporterNote
                    val supporterNote = document.toObject(SupporterNote::class.java)
                    supporterNote?.let {
                        // Menampilkan data supporter di UI
                        binding.updateSupporterNameEditText.setText(it.supporterName)
                        binding.updateSupporterClubEditText.setText(it.clubName)
                    }
                } else {
                    // Menutup aktivitas jika dokumen tidak ditemukan
                    showToast("Dokumen tidak ditemukan, mungkin sudah dihapus.")
                    finish()
                }
            }
            .addOnFailureListener { e ->
                // Menutup aktivitas jika terjadi kegagalan dalam mengambil data
                showToast("Gagal mengambil data supporter: $e")
                finish()
            }

        // Mengatur aksi tombol untuk menyimpan perubahan data supporter
        binding.updateSupporterButton.setOnClickListener {
            val newTitle = binding.updateSupporterNameEditText.text.toString()
            val newContent = binding.updateSupporterClubEditText.text.toString()

            // Memastikan ID supporter tidak kosong
            if (supporterId.isNotEmpty()) {
                // Membuat objek SupporterNote yang diperbarui
                val updatedNote = SupporterNote(supporterId, newTitle, newContent)

                // Memperbarui data supporter di Firestore
                db.collection("supporters").document(supporterId)
                    .set(updatedNote)
                    .addOnSuccessListener {
                        // Menutup aktivitas jika pembaruan berhasil
                        finish()
                        showToast("Supporter Diperbarui")
                    }
                    .addOnFailureListener { e ->
                        // Menampilkan pesan gagal jika pembaruan tidak berhasil
                        showToast("Gagal memperbarui supporter: $e")
                    }
            } else {
                // Menampilkan pesan jika ID supporter tidak valid
                showToast("ID supporter tidak valid.")
                finish()
            }
        }
    }

    // Menampilkan pesan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
