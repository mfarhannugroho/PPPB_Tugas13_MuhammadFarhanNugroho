package com.example.tugaspertemuan12_room

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SupporterDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "supporters_app"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allsupporters"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SUPPORTER = "supporterName"
        private const val COLUMN_CLUB = "clubName"
    }

    // Membuat tabel pada saat pertama kali dibuat database
    override fun onCreate(db: SQLiteDatabase?) {
        val createTabQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_SUPPORTER TEXT, $COLUMN_CLUB TEXT)"
        db?.execSQL(createTabQuery)
    }

    // Menghapus tabel jika versi database diupgrade, kemudian membuat ulang tabel
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTabQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTabQuery)
        onCreate(db)
    }

    // Menyimpan data supporter baru ke dalam database
    fun insertSupporter(supporterNote: SupporterNote): Boolean {
        if (supporterNote.supporterName.isNotEmpty() && supporterNote.clubName.isNotEmpty()) {
            val values = ContentValues().apply {
                put(COLUMN_SUPPORTER, supporterNote.supporterName)
                put(COLUMN_CLUB, supporterNote.clubName)
            }
            writableDatabase.use { db ->
                db.insert(TABLE_NAME, null, values)
            }
            return true
        } else {
            return false
        }
    }

    // Mengambil semua data supporter dari database
    fun getAllSupporters(): List<SupporterNote> {
        val supporterList = mutableListOf<SupporterNote>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORTER))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLUB))

            val supporterNote = SupporterNote(id, title, content)
            supporterList.add(supporterNote)
        }
        cursor.close()
        db.close()
        return supporterList
    }

    // Memperbarui data supporter dalam database
    fun updateSupporter(supporterNote: SupporterNote): Boolean {
        val values = ContentValues().apply {
            put(COLUMN_SUPPORTER, supporterNote.supporterName)
            put(COLUMN_CLUB, supporterNote.clubName)
        }
        val result = writableDatabase.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(supporterNote.id.toString()))
        return result > 0
    }

    // Mengambil data supporter berdasarkan ID-nya
    fun getSupporterByID(supporterId: Int): SupporterNote {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $supporterId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUPPORTER))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CLUB))

        cursor.close()
        db.close()
        return SupporterNote(id, title, content)
    }

    // Menghapus data supporter berdasarkan ID-nya
    fun deleteSupporter(supporterId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(supporterId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}
