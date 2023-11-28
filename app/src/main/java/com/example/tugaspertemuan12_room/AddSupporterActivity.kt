package com.example.tugaspertemuan12_room

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugaspertemuan12_room.databinding.ActivityAddSupporterBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddSupporterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSupporterBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi binding untuk layout activity_add_supporter
        binding = ActivityAddSupporterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi objek FirebaseFirestore untuk berinteraksi dengan Firestore
        db = FirebaseFirestore.getInstance()

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
                val supporterNote = SupporterNote("", supporter, club)

                // Menyimpan data supporter ke dalam koleksi "supporters" di Firestore
                db.collection("supporters")
                    .add(supporterNote)
                    .addOnSuccessListener { documentReference ->
                        // Jika penyimpanan berhasil, menutup activity dan menampilkan pesan sukses
                        finish()
                        showToast("Supporter Disimpan dengan ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        // Jika penyimpanan gagal, menampilkan pesan kesalahan
                        showToast("Gagal menyimpan supporter: $e")
                    }
            } else {
                // Jika ada input yang kosong, menampilkan pesan kesalahan
                showToast("Nama supporter dan klub harus diisi")
            }
        }
    }

    // Fungsi untuk menampilkan pesan toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
