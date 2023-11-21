package com.example.tugaspertemuan12_room

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugaspertemuan12_room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: SupporterDatabaseHelper
    private lateinit var supporterAdapter: SupporterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi binding untuk layout activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi objek SupporterDatabaseHelper untuk berinteraksi dengan database
        db = SupporterDatabaseHelper(this)

        // Menginisialisasi adapter untuk RecyclerView
        supporterAdapter = SupporterAdapter(db.getAllSupporters().toMutableList(), this)

        // Mengatur layout manager dan adapter untuk RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = supporterAdapter

        // Menambahkan listener untuk tombol tambah supporter
        binding.addButton.setOnClickListener{
            // Membuat intent untuk membuka AddSupporterActivity
            val intent = Intent(this, AddSupporterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Mengupdate data supporter setiap kali activity di-resume
        supporterAdapter = SupporterAdapter(db.getAllSupporters().toMutableList(), this)
        binding.recyclerView.adapter = supporterAdapter
    }
}
