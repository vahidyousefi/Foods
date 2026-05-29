package ir.vy.food.ui.mainScreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import ir.vy.food.R
import ir.vy.food.databinding.ActivityMainBinding
import ir.vy.food.databinding.DialogAddItemBinding
import ir.vy.food.databinding.DialogDeleteItemBinding
import ir.vy.food.model.data.Foods
import ir.vy.food.model.db.FoodDao
import ir.vy.food.model.db.MyDataBase
import ir.vy.food.ui.foodScreen.FoodActivity
import ir.vy.food.utils.BASE_URL_IMG
import ir.vy.food.utils.addThousandSeparator
import ir.vy.food.utils.autoMarginRecycler
import ir.vy.food.utils.getNumericText
import ir.vy.food.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity(), AdapterFoods.FoodEvents, MainScreenContract.View {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: AdapterFoods
    private lateinit var foodDao: FoodDao
    private lateinit var presenter: MainScreenContract.Presenter
    private lateinit var fullList: MutableList<Foods>
    private val stateHandler: PresenterStateHolder by viewModels()

    //
    private lateinit var searchTextWatcher: android.text.TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // auto margin from systemBars
        autoMarginRecycler(binding.recyclerMain, false)
        // color icons in statusBar > false = white
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
//            isAppearanceLightNavigationBars = true
        }
        //---------------------------------------------------------------------------------------

        // create presenter
        presenter = MainScreenPresenter(
            MyDataBase.getDataBase(this).foodDao,
            stateHandler.saveState
        )
        // access to FoodDao in MyDataBase
        foodDao = MyDataBase.getDataBase(this).foodDao

        // for first run
        val sharedPreference = getSharedPreferences("dataFood", Context.MODE_PRIVATE)
        if (sharedPreference.getBoolean("firstRun", true)) {
            presenter.firstRun()
            sharedPreference.edit { putBoolean("firstRun", false) }
        }

        // remove all foods in dataBase , Recycler
        binding.btnRemoveAllItem.setOnClickListener {
            showDialogRemoveAllData()
        }

        // btn add new item
        binding.btnItemAdd.setOnClickListener {
            addNewFood()
        }

        // search box
        searchTextWatcher = object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                presenter.onSearchFood(s.toString())
            }

        }
        binding.edtSearch.addTextChangedListener(searchTextWatcher)
//        binding.edtSearch.addTextChangedListener { searchInput ->
////            searchOnDataBase(searchInput!!.toString())
//            presenter.onSearchFood(searchInput.toString())
//        }
        binding.animSearch.playAnimation()

        presenter.onAttach(this)

        // divider for items in recycler
        dividerForItem()
    }

    private fun dividerForItem() {

        val divider = MaterialDividerItemDecoration(this, RecyclerView.VERTICAL).apply {
            dividerColor = getColor(R.color.orange_dark)
            dividerThickness = 1
            isLastItemDecorated = false
        }
        binding.recyclerMain.addItemDecoration(divider)
    }

    // add a new food
    private fun addNewFood() {

        val dialog = AlertDialog.Builder(this).create()

        val viewDialog = DialogAddItemBinding.inflate(layoutInflater)
        dialog.setView(viewDialog.root)
        dialog.setCancelable(true)
        dialog.show()

        // فعال کردن جداکننده سه‌رقمی + محدودیت ۹ رقم + شمارشگر در TextInputLayout
        viewDialog.dialogEdtFoodPrice.addThousandSeparator(
            maxDigits = 9,
            counterLayout = viewDialog.priceInputLayout
        )

        viewDialog.dialogBtnDone.setOnClickListener {
            // condition for checking inputs
            if (
                viewDialog.dialogEdtFoodName.length() > 0 &&
                viewDialog.dialogEdtFoodDis.length() > 0 &&
                viewDialog.dialogEdtFoodTime.length() > 0 &&
                viewDialog.dialogEdtFoodPrice.length() > 0
            ) {

                // get data from inputs Dialog
                val txtName = viewDialog.dialogEdtFoodName.text.toString()
                val txtDis = viewDialog.dialogEdtFoodDis.text.toString()
                val txtTime = viewDialog.dialogEdtFoodTime.text.toString()
//                val txtPrice = viewDialog.dialogEdtFoodPrice.text.toString()
                // get input for saving in dataBase
                val txtPrice = viewDialog.dialogEdtFoodPrice.getNumericText()


                // رندوم این مقادیر ستاره و تعداد نفرات پر بشن
                val txtRatingNumber: Int = (1..180).random()
                val textRatingNumber: Int = (1 until 160).random()
                //بصورت صحیح
                val ratingBarStar: Float = (1..5).random().toFloat()
                // بصورت اعشاری
                val min = 1f
                val max = 5f
                val random = max + Random.nextFloat() * (max - min)

                // put image random in new food
                val foodList = arrayListOf(
                    "184756/Ihno8CnqI9z04DHqINsvvgLTKKldUpaLjvlViGGy",
                    "101716/qWzdut9IgudD5ufZ13enB6ryUTfKRx5Y5gvHTU4U",
                    "184754/mKVwbNH1LaZBE4mx3wXF4O1LCTfUELByUmDxYvWn",
                    "211122/vbt7IRZNYPgahh7xKe8bt4seaoF6yEROi4qTeTsf",
                    "49292/PdfBQy4JonfDolLTBnilwlC2IMj3PWEK06WAOtXw",
                    "492979/rqAm0bNAsNcQk3wy8kYIAR3eEqgQGcT29s6vPZXa",
                    "109255/TZRIerx54vSvZPnAKAfufZrFiOfxCug1lL189NyH"
                )
                val randomForUrl = (1 until 7).random()
                val urlPic = BASE_URL_IMG + foodList[randomForUrl] + ".jpg"

                // create new food
                val newFood = Foods(
                    txtSubject = txtName,
                    txtDis1 = txtDis,
                    txtTime = txtTime,
                    txtPrice = txtPrice,
                    urlImage = urlPic,
                    numOfNumber = textRatingNumber,
                    rating = ratingBarStar
                )

                presenter.onAddNewFoodClicked(newFood)

                // add new food to adapter
//                myAdapter.addFood(newFood)
                // add new food to dataBase
//                foodDao.insertFood(newFood)
                // update recycler
                dialog.dismiss()

                binding.recyclerMain.scrollToPosition(0)
                showToast("غذای جدید اضافه شد !")
                binding.btnRemoveAllItem.isVisible = true

                CoroutineScope(Dispatchers.Main).launch {
                    delay(500)
                    onResume()
                }

            } else showToast("لطفا اطلاعات را کامل کنید !")
        }

        viewDialog.dialogBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    // delete all items
    private fun showDialogRemoveAllData() {
        // show dialog
        val dialogDelete = AlertDialog.Builder(this).create()
        val viewDialogDelete = DialogDeleteItemBinding.inflate(layoutInflater)
        dialogDelete.setView(viewDialogDelete.root)
        dialogDelete.setCancelable(true)
        dialogDelete.show()

        viewDialogDelete.dialogBtnDeleteDone.setOnClickListener {
            dialogDelete.dismiss()
            presenter.onDeleteAllClicked()
            showToast("کل دیتا حذف شد")
            binding.btnRemoveAllItem.visibility = View.GONE
        }
        // cancel
        viewDialogDelete.dialogBtnDeleteCancel.setOnClickListener {
            dialogDelete.dismiss()
        }
    }

    // change data in item
    @SuppressLint("SetTextI18n")
    override fun onFoodClicked(food: Foods, position: Int) {

        // intent open detail foods
        val intent = Intent(this, FoodActivity::class.java)
            .apply {
                putExtra("food_id", food.id)
            }
        startActivity(intent)
    }

    // delete item
    @SuppressLint("SetTextI18n")
    override fun onFoodLongClicked(food: Foods, pos: Int) {

        // show dialog
        val dialogDelete = AlertDialog.Builder(this).create()
        val viewDialogDelete = DialogDeleteItemBinding.inflate(layoutInflater)
        dialogDelete.setView(viewDialogDelete.root)
        dialogDelete.setCancelable(true)
        dialogDelete.show()

        // show name food
        viewDialogDelete.dialogEdtFoodName.text = "((  " + food.txtSubject + "  ))"

        // delete item
        viewDialogDelete.dialogBtnDeleteDone.setOnClickListener {

            val txtName = viewDialogDelete.dialogEdtFoodName.text.toString()
            dialogDelete.dismiss()

            presenter.onDeleteFood(food, pos)

//            myAdapter.removeFood(food, pos)
            // delete food According to ID in DataBase
//            foodDao.deleteFood(food)

            showToast(" $txtName حذف شد !")
        }

        // cancel
        viewDialogDelete.dialogBtnDeleteCancel.setOnClickListener {
            dialogDelete.dismiss()
        }
    }

    // get data updated from FoodActivity and update recycler
    override fun onResume() {
        super.onResume()
        if (::myAdapter.isInitialized) {
            val foods = foodDao.getAllFood()
            myAdapter.setData(foods)
        }
    }

    // -------------------------------------------------------
    override fun showAllFoods(data: MutableList<Foods>) {

        myAdapter = AdapterFoods(data, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun addNewFood(newFood: Foods) {
        myAdapter.addFood(newFood)
    }

    override fun deleteFood(oldFood: Foods, pos: Int) {
        myAdapter.removeFood(oldFood, pos)
    }

    override fun updateFood(editingFood: Foods, pos: Int) {
        myAdapter.updateFood(editingFood, pos)
    }

    override fun refreshFoods(data: List<Foods>) {
        myAdapter.setData(data.toMutableList())
    }

    override fun restoreSearchQuery(query: String) {
        if (::searchTextWatcher.isInitialized) return

        binding.edtSearch.apply {
            removeTextChangedListener(searchTextWatcher)
            setText(query)
            setSelection(query.length)
            addTextChangedListener(searchTextWatcher)
        }
    }

    override fun showProgress() {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}