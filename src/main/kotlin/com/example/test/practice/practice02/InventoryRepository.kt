package com.example.test.practice.practice02

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class InventoryRepository {
    private val inventories = ConcurrentHashMap<String, AtomicInteger>()

    fun initInventory(productId: String, quantity: Int) {
        inventories[productId] = AtomicInteger(quantity)
    }

    fun getInventory(productId: String): AtomicInteger? {
        return inventories[productId]
    }
}
