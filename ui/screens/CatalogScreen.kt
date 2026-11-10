package com.alubarika.store.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alubarika.store.data.Product
import com.alubarika.store.viewmodel.CartViewModel
import com.alubarika.store.viewmodel.CatalogUiState
import com.alubarika.store.viewmodel.CatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    catalogViewModel: CatalogViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (Product) -> Unit,
    onCartClick: () -> Unit
) {
    val uiState by catalogViewModel.uiState.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alubarika Store") },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        if (cartItems.isNotEmpty()) {
                            Text(" ${cartItems.size}")
                        }
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState) {
            is CatalogUiState.Loading -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CatalogUiState.Error -> {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Couldn't load products: ${state.message}")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { catalogViewModel.loadProducts() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            is CatalogUiState.Success -> {
                if (state.products.isEmpty()) {
                    Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                        Text("No products yet. Add some in Firestore.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(padding),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.products) { product ->
                            ProductCard(product = product, onClick = { onProductClick(product) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxWidth().height(120.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(product.name, style = MaterialTheme.typography.titleSmall)
            Text("₦${"%.2f".format(product.price)}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
