package com.alubarika.store.repository

import com.alubarika.store.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val ordersCollection = db.collection("orders")

    suspend fun placeOrder(order: Order): String {
        val docRef = ordersCollection.add(order).await()
        return docRef.id
    }
}
