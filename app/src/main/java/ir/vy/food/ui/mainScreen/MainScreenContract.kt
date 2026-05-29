package ir.vy.food.ui.mainScreen

import ir.vy.food.model.data.Foods
import ir.vy.food.utils.BasePresenter
import ir.vy.food.utils.BaseView

interface MainScreenContract {

    interface Presenter : BasePresenter<View> {

        fun firstRun()
        fun onSearchFood(filter: String)
        fun onAddNewFoodClicked(food: Foods)
        fun onDeleteAllClicked()
        fun onUpdateFood(food: Foods, pos: Int)
        fun onDeleteFood(food: Foods, pos: Int)
    }

    interface View : BaseView {

        fun showAllFoods(data: MutableList<Foods>)
        fun addNewFood(newFood: Foods)
        fun deleteFood(oldFood: Foods, pos: Int)
        fun updateFood(editingFood: Foods, pos: Int)
        fun refreshFoods(data: List<Foods>)
        fun restoreSearchQuery(query: String)
    }
}