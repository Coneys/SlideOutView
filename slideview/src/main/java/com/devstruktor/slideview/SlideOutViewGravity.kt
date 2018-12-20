package com.devstruktor.slideview

import android.view.Gravity
import android.view.ViewPropertyAnimator

sealed class SlideOutViewGravity {

    abstract val intRepresentation: Int

    abstract fun prepareExpandAnimation(animator: ViewPropertyAnimator)
            : ViewPropertyAnimator

    abstract fun prepareHideAnimation(animator: ViewPropertyAnimator, width: Int, height: Int, anchor: Float)
            : ViewPropertyAnimator

    protected abstract fun calculateExpandValue(initialValue: Float, anchor: Float): Float

    abstract fun applyState(state: SlideOutViewState, view: SlideOutView, anchor: Float = 0f)


    abstract val horizontal: Boolean

    fun resetViewTransition(view: SlideOutView) {
        if (horizontal) {
            view.translationX = 0f
        } else
            view.translationY = 0f
    }

    object BOTTOM : SlideOutViewGravity() {

        override val horizontal: Boolean = false

        override val intRepresentation: Int = Gravity.BOTTOM

        override fun prepareHideAnimation(
            animator: ViewPropertyAnimator, width: Int, height: Int, anchor: Float
        ) = animator.apply {
            val expandValue = calculateExpandValue(height.toFloat(), anchor)
            translationY(expandValue)
        }

        override fun calculateExpandValue(initialValue: Float, anchor: Float): Float {
            val trueAnchor = 1f - anchor
            return initialValue * trueAnchor
        }

        override fun applyState(state: SlideOutViewState, view: SlideOutView, anchor: Float) {
            val height = view.height.toFloat()
            val result = when (state) {
                SlideOutViewState.HIDDEN -> height
                SlideOutViewState.ANCHORED -> calculateExpandValue(height, anchor)
                SlideOutViewState.EXPANDED -> 0f
            }
            view.translationY = result
        }

        override fun prepareExpandAnimation(animator: ViewPropertyAnimator) = animator.apply {
            translationY(0f)
        }


    }

    object TOP : SlideOutViewGravity() {

        override val horizontal: Boolean = false

        override val intRepresentation: Int = Gravity.TOP


        override fun prepareHideAnimation(
            animator: ViewPropertyAnimator, width: Int, height: Int, anchor: Float
        ) = animator.apply {
            val expandValue = calculateExpandValue(height.toFloat(), anchor)
            translationY(expandValue)
        }


        override fun calculateExpandValue(initialValue: Float, anchor: Float): Float {
            val trueAnchor = 1f - anchor
            return -(initialValue * trueAnchor)
        }

        override fun applyState(state: SlideOutViewState, view: SlideOutView, anchor: Float) {
            val height = view.height.toFloat()
            val result = when (state) {
                SlideOutViewState.HIDDEN -> -height
                SlideOutViewState.ANCHORED -> calculateExpandValue(height, anchor)
                SlideOutViewState.EXPANDED -> 0f
            }
            view.translationY = result
        }

        override fun prepareExpandAnimation(animator: ViewPropertyAnimator) = animator.apply {
            translationY(0f)
        }

    }

    object LEFT : SlideOutViewGravity() {


        override val intRepresentation: Int = Gravity.LEFT

        override val horizontal: Boolean = true

        override fun prepareHideAnimation(
            animator: ViewPropertyAnimator, width: Int, height: Int, anchor: Float
        ) = animator.apply {
            val expandValue = calculateExpandValue(width.toFloat(), anchor)
            translationX(expandValue)
        }

        override fun calculateExpandValue(initialValue: Float, anchor: Float): Float {
            val trueAnchor = 1f - anchor
            return -(initialValue * trueAnchor)
        }

        override fun applyState(state: SlideOutViewState, view: SlideOutView, anchor: Float) {
            val width = view.width.toFloat()
            val result = when (state) {
                SlideOutViewState.HIDDEN -> -width
                SlideOutViewState.ANCHORED -> calculateExpandValue(width, anchor)
                SlideOutViewState.EXPANDED -> 0f
            }
            view.translationX = result
        }

        override fun prepareExpandAnimation(animator: ViewPropertyAnimator) = animator.apply {
            translationX(0f)
        }
    }

    object RIGHT : SlideOutViewGravity() {

        override val intRepresentation: Int = Gravity.RIGHT

        override val horizontal: Boolean = true

        override fun prepareHideAnimation(
            animator: ViewPropertyAnimator, width: Int, height: Int, anchor: Float
        ) = animator.apply {
            val expandValue = calculateExpandValue(width.toFloat(), anchor)
            translationX(expandValue)
        }

        override fun calculateExpandValue(initialValue: Float, anchor: Float): Float {
            val trueAnchor = 1f - anchor
            return (initialValue * trueAnchor)
        }


        override fun applyState(state: SlideOutViewState, view: SlideOutView, anchor: Float) {
            val width = view.width.toFloat()
            val result = when (state) {
                SlideOutViewState.HIDDEN -> width
                SlideOutViewState.ANCHORED -> calculateExpandValue(width, anchor)
                SlideOutViewState.EXPANDED -> 0f
            }
            view.translationX = result
        }

        override fun prepareExpandAnimation(animator: ViewPropertyAnimator) = animator.apply {
            translationX(0f)
        }
    }


    companion object {
        fun fromInt(value: Int): SlideOutViewGravity {
            return when (value) {
                Gravity.BOTTOM -> BOTTOM
                Gravity.TOP -> TOP
                Gravity.LEFT -> LEFT
                else -> RIGHT
            }
        }
    }
}