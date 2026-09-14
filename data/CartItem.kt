package com.alubarika.store.data

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val lineTotal: Double
        get() = product.price * quantity
}
