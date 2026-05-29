package ir.vy.food.ui.mainScreen

import androidx.lifecycle.SavedStateHandle
import ir.vy.food.model.data.Foods
import ir.vy.food.model.data.foodsList
import ir.vy.food.model.db.FoodDao

class MainScreenPresenter(
    private val foodDao: FoodDao,
    private val stateHandler: SavedStateHandle,
) : MainScreenContract.Presenter {
    private var mainView: MainScreenContract.View? = null

    override fun onAttach(view: MainScreenContract.View) {
        mainView = view

        val data = foodDao.getAllFood()
        mainView!!.showAllFoods(data)

        val saveQuery = stateHandler.get<String>("query") ?: ""
        if (saveQuery.isNotEmpty()) {
            mainView?.restoreSearchQuery(saveQuery)
        }
    }

    override fun onDetach() {
        mainView = null
    }

    override fun firstRun() {
        foodDao.insertAllFoods(foodsList)
    }

    override fun onSearchFood(filter: String) {

        stateHandler["query"] = filter

        if (filter.isNotEmpty()) {
            // show filter
            mainView!!.refreshFoods(foodDao.searchFoods(filter))
            // show all without filter
        } else mainView!!.refreshFoods(foodDao.getAllFood())
    }

    override fun onAddNewFoodClicked(food: Foods) {
        // add to dataBase
        foodDao.insertOrUpdate(food)
        // show in recycler
        mainView!!.addNewFood(food)
    }

    override fun onDeleteAllClicked() {
        // delete all data
        foodDao.deleteAllFoods()
        // refresh view
        mainView!!.refreshFoods(foodDao.getAllFood())
    }

    override fun onUpdateFood(food: Foods, pos: Int) {
        // update food in dataBase
        foodDao.updateFood(food)
        // update food in View
        mainView!!.updateFood(food, pos)
    }

    override fun onDeleteFood(food: Foods, pos: Int) {
        // delete a food in dataBase
        foodDao.deleteFood(food)
        // delete in View
        mainView!!.deleteFood(food, pos)
    }
}