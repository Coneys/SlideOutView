package com.devstruktor.slideview.listener

import com.devstruktor.slideview.SlideOutViewState

typealias StateChangeListener = (oldState: SlideOutViewState, newState: SlideOutViewState) -> Unit
