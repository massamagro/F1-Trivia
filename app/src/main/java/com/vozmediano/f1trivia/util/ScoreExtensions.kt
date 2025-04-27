package com.vozmediano.f1trivia.util

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun main() {

    val uid = when ((1..2).random()) {
        1 -> "2oe5XEad0JSV8MgUABuelXxaKwr2" //vozmediano.edu
        2 -> "r3Zsu3nYMKezNF6zvdWoekGiSBz2" //carlosvozmedianoruiz
        else -> ""
    }
    val score = (1..30).random()
    val minTimestamp = 1737962153000
    val maxTimestamp = System.currentTimeMillis()
    val timestamp = (minTimestamp..maxTimestamp).random()


    val scoreRef = FirebaseFirestore.getInstance().collection("scores")
    val scoreMap = mapOf(
        "uid" to uid,
        "score" to score,
        "timestamp" to timestamp
    )

    scoreRef.add(scoreMap)
        .addOnSuccessListener {
            Log.i("GameActivity", "Score saved successfully!")
        }
        .addOnFailureListener { e ->
            Log.e("GameActivity", "Error saving score: ${e.message}")
        }
}
