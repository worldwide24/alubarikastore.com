package com.alubarika.store.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alubarika.store.data.Product
import com.alubarika.store.ui.screens.*
import com.alubarika.store.viewmodel.CartViewModel
import com.alubarika.store.viewmodel.CatalogViewModel

private object Routes {
    const val CATALOG = "catalog"
    const val PRODUCT_DETAIL = "product_detail"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val CONFIRMATION = "confirmation/{orderId}"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    val catalogViewModel: CatalogViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    // Holds the currently selected product between the catalog and detail screens.
    var selectedProduct: Product? by remember { mutableStateOf<Product?>(null) }

    NavHost(navController = navController, startDestination = Routes.CATALOG) {
        composable(Routes.CATALOG) {
            CatalogScreen(
                catalogViewModel = catalogViewModel,
                cartViewModel = cartViewModel,
                onProductClick = { product ->
                    selectedProduct = product
                    navController.navigate(Routes.PRODUCT_DETAIL)
                },
                onCartClick = { navController.navigate(Routes.CART) }
            )
        }
        composable(Routes.PRODUCT_DETAIL) {
            selectedProduct?.let { product ->
                ProductDetailScreen(
                    product = product,
                    cartViewModel = cartViewModel,
                    onBack = { navController.popBackStack() },
                    onAddedToCart = { navController.popBackStack() }
                )
            }
        }
        composable(Routes.CART) {
            CartScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                onCheckout = { navController.navigate(Routes.CHECKOUT) }
            )
        }
        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                onOrderPlaced = { orderId ->
                    navController.navigate("confirmation/$orderId") {
                        popUpTo(Routes.CATALOG)
                    }
                }
            )
        }
        composable(Routes.CONFIRMATION) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            OrderConfirmationScreen(
                orderId = orderId,
                onDone = {
                    navController.navigate(Routes.CATALOG) {
                        popUpTo(Routes.CATALOG) { inclusive = true }
                    }
                }
            )
        }
    }
}
