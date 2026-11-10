package com.alubarika.store.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alubarika.store.data.Product
import com.alubarika.store.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    cartViewModel: CartViewModel,
    onBack: () -> Unit,
    onAddedToCart: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(product.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("₦${"%.2f".format(product.price)}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(product.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                if (product.stock > 0) "In stock: ${product.stock}" else "Out of stock",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    cartViewModel.addToCart(product)
                    onAddedToCart()
                },
                enabled = product.stock > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add to Cart")
            }
        }
    }
}
