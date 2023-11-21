package com.example.tugaspertemuan12_room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspertemuan12_room.databinding.ActivityAddSupporterBinding

class AddSupporterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSupporterBinding
    private lateinit var db: SupporterDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi binding untuk layout activity_add_supporter
        binding = ActivityAddSupporterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi objek SupporterDatabaseHelper untuk berinteraksi dengan database
        db = SupporterDatabaseHelper(this)

        // Menambahkan listener untuk tombol kembali
        binding.addSupporterBackButton.setOnClickListener {
            onBackPressed()
        }

        // Menambahkan listener untuk tombol simpan
        binding.addSupporterSaveButton.setOnClickListener {
            // Mengambil data dari input pengguna (nama supporter dan klub)
            val supporter = binding.addSupporterNameEditText.text.toString()
            val club = binding.addSupporterClubEditText.text.toString()

            // Memastikan bahwa input tidak kosong
            if (supporter.isNotEmpty() && club.isNotEmpty()) {
                // Membuat objek SupporterNote dari input pengguna
                val supporterNote = SupporterNote(0, supporter, club)

                // Menyimpan data supporter ke dalam database
                val success = db.insertSupporter(supporterNote)
                if (success) {
                    // Jika penyimpanan berhasil, menutup activity dan menampilkan pesan sukses
                    finish()
                    Toast.makeText(this, "Supporter Disimpan", Toast.LENGTH_SHORT).show()
                } else {
                    // Jika penyimpanan gagal, menampilkan pesan kesalahan
                    Toast.makeText(this, "Gagal menyimpan supporter", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Jika ada input yang kosong, menampilkan pesan kesalahan
                Toast.makeText(this, "Nama supporter dan fakultas harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
