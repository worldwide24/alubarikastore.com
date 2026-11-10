package com.alubarika.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alubarika.store.data.CartItem
import com.alubarika.store.data.Order
import com.alubarika.store.data.OrderLine
import com.alubarika.store.data.Product
import com.alubarika.store.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val cartTotal: Double
        get() = _cartItems.value.sumOf { it.lineTotal }

    fun addToCart(product: Product, quantity: Int = 1) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.product.id == product.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
        } else {
            current.add(CartItem(product, quantity))
        }
        _cartItems.value = current
    }

    fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        _cartItems.value = _cartItems.value.map {
            if (it.product.id == productId) it.copy(quantity = quantity) else it
        }
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.product.id == productId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun checkout(onResult: (Result<String>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
        val order = Order(
            userId = userId,
            items = _cartItems.value.map {
                OrderLine(
                    productId = it.product.id,
                    name = it.product.name,
                    price = it.product.price,
                    quantity = it.quantity
                )
            },
            total = cartTotal
        )
        viewModelScope.launch {
            try {
                val orderId = orderRepository.placeOrder(order)
                clearCart()
                onResult(Result.success(orderId))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }
}
