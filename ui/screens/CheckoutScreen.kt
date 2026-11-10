package com.alubarika.store.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alubarika.store.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    var isPlacingOrder by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val cartItems by cartViewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Order Summary", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            cartItems.forEach { item ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.product.name} x${item.quantity}")
                    Text("₦${"%.2f".format(item.lineTotal)}")
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text(
                    "₦${"%.2f".format(cartViewModel.cartTotal)}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(24.dp))

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    isPlacingOrder = true
                    errorMessage = null
                    cartViewModel.checkout { result ->
                        isPlacingOrder = false
                        result.onSuccess { orderId -> onOrderPlaced(orderId) }
                        result.onFailure { e -> errorMessage = e.message ?: "Order failed" }
                    }
                },
                enabled = !isPlacingOrder && cartItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isPlacingOrder) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text("Place Order")
                }
            }
        }
    }
}

@Composable
fun OrderConfirmationScreen(orderId: String, onDone: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Order placed!", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Order ID: $orderId")
            Spacer(Modifier.height(24.dp))
            Button(onClick = onDone) { Text("Continue Shopping") }
        }
    }
}
