package com.hardik.safehaven.feature_home

import com.hardik.safehaven.domain.model.SecureItem

// what we DON'T DO
//  MutableState everywhere
//  Logic in COMPOSABLES
//  Random VARIABLES

// What we DO
//  One UiState DataClass
//  One StateFlow
//  UI only observes

data class HomeUiState(
    val items: List<SecureItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
// At any given moment, how does home screen look like? - this data class tells that ....
// NOT how it looks,( color, padding)
// But WHAT DATA IT IS SHOWING

