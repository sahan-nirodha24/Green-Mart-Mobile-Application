package com.example.greenmart

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PHONE TEXT, " +
                "$COLUMN_LOCATION TEXT)")
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertUser(username: String, password: String, email: String, phone: String, location: String): Long {
        // Check if user with the same username, email, or phone already exists
        val db = writableDatabase
        val selection = "$COLUMN_USERNAME = ? OR $COLUMN_EMAIL = ? OR $COLUMN_PHONE = ?"
        val selectionArgs = arrayOf(username, email, phone)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        if (cursor.count > 0) {
            // User with the same username, email, or phone already exists
            cursor.close()
            return -1L // Return -1 to indicate failure
        }

        cursor.close()

        // If no user with the same username, email, or phone exists, insert the new user
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
            put(COLUMN_LOCATION, location)
        }

        return db.insert(TABLE_NAME, null, values) // Insert user and return the row ID
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun getUserDetails(username: String): Map<String, String>? {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val userDetails = mapOf(
                "username" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME)),
                "email" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                "phone" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                "location" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))
            )
            cursor.close()
            return userDetails
        }
        cursor.close()
        return null
    }

    fun updatePassword(username: String, newPassword: String): Int {
        val db = writableDatabase

        // Fetch current password for the user
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_PASSWORD),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            val currentPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

            // Check if the new password is the same as the current password
            if (currentPassword == newPassword) {
                cursor.close()
                return -1 // Return -1 to indicate the same password
            }

            // Update the password if it is different
            val values = ContentValues().apply {
                put(COLUMN_PASSWORD, newPassword)
            }

            val rowsUpdated = db.update(
                TABLE_NAME,
                values,
                "$COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            cursor.close()
            return rowsUpdated
        }

        cursor.close()
        return 0 // Return 0 if the user is not found
    }

}
