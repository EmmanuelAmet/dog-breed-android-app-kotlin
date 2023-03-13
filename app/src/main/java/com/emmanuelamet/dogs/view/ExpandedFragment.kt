package com.emmanuelamet.dogs.view

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.emmanuelamet.dogs.R
import kotlinx.android.synthetic.main.fragment_expanded.*

class ExpandedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expanded, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrow_icon.setOnClickListener {

            if (disc_layout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(
                    motherLayout,
                    AutoTransition()
                )
                disc_layout.visibility = View.VISIBLE
                motherLayout.setBackgroundColor(Color.parseColor("#72B3CFB4"))
                arrow_icon.setImageResource(R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(
                    motherLayout,
                    AutoTransition()
                )
                disc_layout.visibility = View.GONE
                motherLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
                arrow_icon.setImageResource(R.drawable.ic_down_arrow)
            }
        }
    }

}