package com.devstruktor.slideoutview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.devstruktor.slideview.SlideOutViewGravity
import com.devstruktor.slideview.SlideOutViewState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swich_button.setOnClickListener {
            sample_slide_out_view.toggleBaseState()
        }

        set_left.setOnClickListener {
            sample_slide_out_view.setGravity(SlideOutViewGravity.LEFT)
        }

        set_right.setOnClickListener {
            sample_slide_out_view.setGravity(SlideOutViewGravity.RIGHT)
        }

        set_bot.setOnClickListener {
            sample_slide_out_view.setGravity(SlideOutViewGravity.BOTTOM)
        }

        anchor_02.setOnClickListener {
            sample_slide_out_view.setNewAnchor(0.2f)
        }

        anchor_07.setOnClickListener {
            sample_slide_out_view.setNewAnchor(0.7f)
        }

        anchor_state.setOnClickListener {
            sample_slide_out_view.setNewState(SlideOutViewState.ANCHORED)
        }

        expand_state.setOnClickListener {
            sample_slide_out_view.setNewState(SlideOutViewState.EXPANDED)
        }

        hidden_state.setOnClickListener {
            sample_slide_out_view.setNewState(SlideOutViewState.HIDDEN)
        }
    }
}
