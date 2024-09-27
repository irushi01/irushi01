package com.example.sqlliteapp3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val ageEditText = findViewById<EditText>(R.id.ageEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        val searchIdEditText = findViewById<EditText>(R.id.searchIdEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchResultTextView = findViewById<TextView>(R.id.searchResultTextView)

        // Save data to SQLite database
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()

            if (name.isNotEmpty() && age != null) {
                val id = databaseHelper.insertUser(name, age)
                Toast.makeText(this, "User saved with ID: $id", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
            }
        }

        // Search for a user by ID
        searchButton.setOnClickListener {
            val searchId = searchIdEditText.text.toString().toIntOrNull()

            if (searchId != null) {
                val cursor = databaseHelper.getUserById(searchId)

                if (cursor != null && cursor.moveToFirst()) {
                    val name =
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                    val age =
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AGE))
                    searchResultTextView.text = "ID: $searchId\nName: $name\nAge: $age"
                } else {
                    searchResultTextView.text = "No user found with ID: $searchId"
                }
                cursor?.close()
            } else {
                Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}