package com.alubarika.store.repository

import com.alubarika.store.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val productsCollection = db.collection("products")

    suspend fun getProducts(): List<Product> {
        val snapshot = productsCollection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Product::class.java)?.copy(id = doc.id)
        }
    }

    suspend fun getProduct(id: String): Product? {
        val doc = productsCollection.document(id).get().await()
        return doc.toObject(Product::class.java)?.copy(id = doc.id)
    }
}
