package ir.vy.food.ui.foodScreen

import ir.vy.food.model.data.Foods
import ir.vy.food.utils.BasePresenter
import ir.vy.food.utils.BaseView

interface FoodContract {

    interface Presenter : BasePresenter<View> {
        fun onBackArrow()
        fun onEditClicked()
        fun onDeleteFood()
        fun onUpdateFood(foods: Foods)
    }

    interface View : BaseView {
        fun backToMainScreen()

        fun showDetailsFood(foods: Foods)
        fun showDialogEditFood(foods: Foods)
        fun deleteFood(foods: Foods)
        fun updateFood(foods: Foods)
    }
}