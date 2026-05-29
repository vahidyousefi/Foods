package ir.vy.food.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

fun autoMarginSystemBars(view1: View, applySides: Boolean) {

    ViewCompat.setOnApplyWindowInsetsListener(view1) { v, insets ->
        // get size (statusBar, NavigationBar, notch/Camera)
        val systemBars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = systemBars.top
            bottomMargin = systemBars.bottom
            // auto margin for LandScape
            if (applySides) {
                leftMargin = systemBars.left
                rightMargin = systemBars.right
            }
        }
        // other views can use
        insets
    }
}

fun autoMarginRecycler(view1: View, applySides: Boolean) {

    ViewCompat.setOnApplyWindowInsetsListener(view1) { v, insets ->
        // get size (statusBar, NavigationBar, notch/Camera)
        val systemBars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
//            topMargin = systemBars.top
            bottomMargin = systemBars.bottom
            // auto margin for LandScape
            if (applySides) {
                leftMargin = systemBars.left
                rightMargin = systemBars.right
            }
        }
        // other views can use
        insets
    }
}

fun autoMarginToolbar(view1: View, applySides: Boolean) {

    ViewCompat.setOnApplyWindowInsetsListener(view1) { v, insets ->
        // get size (statusBar, NavigationBar, notch/Camera)
        val systemBars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        )
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = systemBars.top
//            bottomMargin = systemBars.bottom
            // auto margin for LandScape
            if (applySides) {
                leftMargin = systemBars.left
                rightMargin = systemBars.right
            }
        }
        // other views can use
        insets
    }
}