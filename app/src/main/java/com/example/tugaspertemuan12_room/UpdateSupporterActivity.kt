package com.example.tugaspertemuan12_room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspertemuan12_room.databinding.ActivityUpdateSupporterBinding

class UpdateSupporterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateSupporterBinding
    private lateinit var db: SupporterDatabaseHelper
    private var supporterId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi binding untuk layout activity_update_supporter.xml
        binding = ActivityUpdateSupporterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi objek database helper
        db = SupporterDatabaseHelper(this)

        // Mendapatkan ID supporter yang akan diperbarui dari intent
        supporterId = intent.getIntExtra("supporter_id", -1)

        // Menutup aktivitas jika ID supporter tidak valid
        if (supporterId == -1){
            finish()
            return
        }

        // Mendapatkan data supporter berdasarkan ID dan menampilkan di UI
        val supporterNote = db.getSupporterByID(supporterId)
        binding.updateSupporterNameEditText.setText(supporterNote.supporterName)
        binding.updateSupporterClubEditText.setText(supporterNote.clubName)

        // Mengatur aksi tombol untuk menyimpan perubahan data supporter
        binding.updateSupporterButton.setOnClickListener{
            val newTitle = binding.updateSupporterNameEditText.text.toString()
            val newContent = binding.updateSupporterClubEditText.text.toString()

            // Membuat objek SupporterNote yang diperbarui
            val updatedNote = SupporterNote(supporterId, newTitle, newContent)

            // Memperbarui data supporter dalam database
            val success = db.updateSupporter(updatedNote)

            // Menangani hasil dari pembaruan data
            if (success) {
                // Menutup aktivitas jika pembaruan berhasil
                finish()
                Toast.makeText(this, "Supporter Diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                // Menampilkan pesan gagal jika pembaruan tidak berhasil
                Toast.makeText(this, "Gagal memperbarui supporter", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
