package com.alubarika.store.data

data class Order(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderLine> = emptyList(),
    val total: Double = 0.0,
    val status: String = "pending",
    val createdAt: Long = System.currentTimeMillis()
)

data class OrderLine(
    val productId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0
)
