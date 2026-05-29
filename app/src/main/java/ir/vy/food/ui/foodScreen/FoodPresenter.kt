package ir.vy.food.ui.foodScreen

import ir.vy.food.model.data.Foods
import ir.vy.food.model.db.FoodDao

class FoodPresenter(private val foodId: Int, private val foodDao: FoodDao) :
    FoodContract.Presenter {

    private var foodView: FoodContract.View? = null
    private lateinit var food: Foods

    override fun onAttach(view: FoodContract.View) {
        foodView = view
        food = foodDao.getFoodById(foodId)
        foodView?.showDetailsFood(food)
    }

    override fun onBackArrow() {
        foodView?.backToMainScreen()
    }

    override fun onEditClicked() {
        foodView?.showDialogEditFood(food)
    }

    override fun onDeleteFood() {
        foodDao.deleteFood(food)
        foodView?.deleteFood(food)
    }

    override fun onUpdateFood(foods: Foods) {
        foodDao.updateFood(foods)
        food = foods
        foodView?.updateFood(food)
    }

    override fun onDetach() {
        foodView = null
    }
}