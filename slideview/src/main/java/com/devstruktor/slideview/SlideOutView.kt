package com.devstruktor.slideview

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.Gravity


class SlideOutView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    var anchor: Float = 0f
        private set(value) {
            if (value < 0f || value > 1f) {
                throw IllegalArgumentException("Anchor has to be value between 0f and 1f")
            }
            field = value
        }
    private val initialState: SlideOutViewState
    var currentState: SlideOutViewState
        private set
    var slideGravity: SlideOutViewGravity
        private set

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SlideOutView,
            0, 0
        ).apply {

            try {
                val stateNumber = getInteger(R.styleable.SlideOutView_initialState, 0)
                initialState = SlideOutViewState.fromInt(stateNumber)
                currentState = initialState
                val slideGravityNumber = getInteger(R.styleable.SlideOutView_slideFrom, Gravity.BOTTOM)
                slideGravity = SlideOutViewGravity.fromInt(slideGravityNumber)
                anchor = getFloat(R.styleable.SlideOutView_anchor, 0f)

            } finally {
                recycle()
            }
        }
    }


    fun setNewAnchor(value: Float, applyChanges: Boolean = true) {
        anchor = value
        if (applyChanges)
            applyCurrentStateToViewWithAnimation(slideGravity, currentState, value)
    }

    fun toggleBaseState() {
        val newState = when (currentState) {
            SlideOutViewState.HIDDEN -> SlideOutViewState.EXPANDED
            SlideOutViewState.ANCHORED -> SlideOutViewState.HIDDEN
            SlideOutViewState.EXPANDED -> SlideOutViewState.HIDDEN
        }

        setNewState(newState)
    }

    fun setNewState(state: SlideOutViewState) {
        currentState = state
        applyCurrentStateToViewWithAnimation(slideGravity, currentState, anchor)
    }

    private fun hide(gravity: SlideOutViewGravity, anchor: Float = 0f) {
        gravity.prepareHideAnimation(animate(), width, height, anchor).start()
    }

    private fun show(gravity: SlideOutViewGravity) {
        gravity.prepareExpandAnimation(animate()).start()
    }

    fun setGravity(gravity: SlideOutViewGravity) {
        slideGravity.resetViewTransition(this)
        slideGravity = gravity
        gravity.applyState(currentState, this, anchor)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 1) throw IllegalStateException(
            "SlideOutView has to have 1 children" +
                    "Currently has $childCount"
        )

        applyCurrentStateToView(slideGravity, initialState, anchor)

    }

    private fun applyCurrentStateToViewWithAnimation(
        slideOutViewGravity: SlideOutViewGravity,
        slideOutViewState: SlideOutViewState,
        anchor: Float
    ) {
        when (slideOutViewState) {
            SlideOutViewState.HIDDEN -> hide(slideOutViewGravity)
            SlideOutViewState.ANCHORED -> hide(slideOutViewGravity, anchor)
            SlideOutViewState.EXPANDED -> show(slideOutViewGravity)
        }
    }


    private fun applyCurrentStateToView(
        slideOutViewGravity: SlideOutViewGravity,
        slideOutViewState: SlideOutViewState,
        anchor: Float
    ) {
        post {
            slideOutViewGravity.applyState(slideOutViewState, this, anchor)
        }
    }


}
