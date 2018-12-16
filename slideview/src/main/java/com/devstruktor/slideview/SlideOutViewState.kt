package com.devstruktor.slideview

enum class SlideOutViewState {
    HIDDEN,
    ANCHORED,
    EXPANDED;

    companion object {
        fun fromInt(value: Int): SlideOutViewState {
            return when (value) {
                0 -> HIDDEN
                1 -> ANCHORED
                else -> EXPANDED
            }
        }
    }
}