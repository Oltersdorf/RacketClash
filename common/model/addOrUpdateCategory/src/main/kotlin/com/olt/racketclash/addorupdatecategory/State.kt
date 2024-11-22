package com.olt.racketclash.addorupdatecategory

data class State(
    val isLoading: Boolean = true,
    val isSavable: Boolean = false,
    val name: String = ""
)