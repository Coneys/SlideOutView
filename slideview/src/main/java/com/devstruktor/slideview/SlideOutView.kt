package com.devstruktor.slideview

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.Interpolator
import com.devstruktor.slideview.listener.ListenerHolder
import com.devstruktor.slideview.listener.StateChangeListener
import java.util.*


class SlideOutView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var stateListenerHolder = ListenerHolder<StateChangeListener>()
    private var stateAnimationListenerHolder = ListenerHolder<StateChangeListener>()

    var animationDuration = 700
    var interpolator: Interpolator = FastOutSlowInInterpolator()

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
    private var oldState: SlideOutViewState
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
                oldState = initialState
                val slideGravityNumber = getInteger(R.styleable.SlideOutView_slideFrom, Gravity.BOTTOM)
                slideGravity = SlideOutViewGravity.fromInt(slideGravityNumber)
                anchor = getFloat(R.styleable.SlideOutView_anchor, 0f)
                animationDuration = getInt(R.styleable.SlideOutView_animDuration, 700)

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
        oldState = currentState
        currentState = state
        stateListenerHolder.listeners.values.forEach {
            it.invoke(oldState, currentState)
        }
        applyCurrentStateToViewWithAnimation(
            slideGravity, currentState,
            anchor
        )
    }

    private fun hide(gravity: SlideOutViewGravity, state: SlideOutViewState, anchor: Float = 0f) {
        gravity.prepareHideAnimation(animate(), width, height, anchor)
            .setDuration(animationDuration.toLong())
            .setInterpolator(interpolator)
            .withEndAction {
                stateAnimationListenerHolder.listeners.values.forEach {
                    it.invoke(oldState, state)
                }
            }
            .start()
    }

    private fun show(gravity: SlideOutViewGravity, state: SlideOutViewState) {
        gravity.prepareExpandAnimation(animate())
            .setDuration(animationDuration.toLong())
            .setInterpolator(interpolator)
            .withEndAction {
                stateAnimationListenerHolder.listeners.values.forEach {
                    it.invoke(oldState, state)
                }
            }
            .start()
    }

    fun setGravity(gravity: SlideOutViewGravity) {
        slideGravity.resetViewTransition(this)
        slideGravity = gravity
        gravity.applyState(currentState, this, anchor)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        applyCurrentStateToView(slideGravity, initialState, anchor)
    }

    private fun applyCurrentStateToViewWithAnimation(
        slideOutViewGravity: SlideOutViewGravity,
        slideOutViewState: SlideOutViewState,
        anchor: Float
    ) {
        when (slideOutViewState) {
            SlideOutViewState.HIDDEN -> hide(slideOutViewGravity, slideOutViewState)
            SlideOutViewState.ANCHORED -> hide(slideOutViewGravity, slideOutViewState, anchor)
            SlideOutViewState.EXPANDED -> show(slideOutViewGravity, slideOutViewState)
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


    override fun onSaveInstanceState(): Parcelable? {

        val bundle = Bundle()
        bundle.putInt("gravity", slideGravity.intRepresentation)
        bundle.putSerializable("state", currentState)
        bundle.putFloat("anchor", anchor)
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        if (state is Bundle) {
            slideGravity = SlideOutViewGravity.fromInt(state.getInt("gravity"))
            currentState = state.getSerializable("state") as SlideOutViewState
            anchor = state.getFloat("anchor")
            applyCurrentStateToView(slideGravity, currentState, anchor)
            val superState = state.getParcelable<Parcelable>("superState")
            super.onRestoreInstanceState(superState)
        }

    }

    fun addOnStateChangeListener(tag: String = UUID.randomUUID().toString(),listener: StateChangeListener) {
        stateListenerHolder.addListener(listener, tag)
    }

    fun removeOnStateChangeListener(tag: String) {
        stateListenerHolder.removeListener(tag)
    }

    fun addOnStateAnimationFinishedListener(tag: String = UUID.randomUUID().toString(),listener: StateChangeListener) {
        stateAnimationListenerHolder.addListener(listener, tag)
    }

    fun removeOnStateAnimationFinishedListener(tag: String) {
        stateAnimationListenerHolder.removeListener(tag)
    }


}
