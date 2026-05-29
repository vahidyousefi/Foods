package ir.vy.food.utils

interface BasePresenter<T> {
    fun onAttach(view: T)
    fun onDetach()
}

interface BaseView {
    fun showProgress()
}