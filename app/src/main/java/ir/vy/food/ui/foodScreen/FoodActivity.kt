package ir.vy.food.ui.foodScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import ir.vy.food.R
import ir.vy.food.databinding.ActivityFoodBinding
import ir.vy.food.databinding.DialogDeleteItemBinding
import ir.vy.food.databinding.DialogUpdateItemBinding
import ir.vy.food.model.data.Foods
import ir.vy.food.model.db.FoodDao
import ir.vy.food.model.db.MyDataBase
import ir.vy.food.utils.addThousandSeparator
import ir.vy.food.utils.getNumericText
import ir.vy.food.utils.showToast
import ir.vy.food.utils.withThousandSeparator

class FoodActivity : AppCompatActivity(), FoodContract.View {
    private lateinit var binding: ActivityFoodBinding
    private lateinit var foodDao: FoodDao
    private var isFavorite = false
    private lateinit var foodPresenter: FoodContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // show toolbar
        setSupportActionBar(binding.toolBarFood)

        // show icon arrow back in toolbar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // change icon back
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_add)

        // set title collapsing in scroll
        binding.collapsingTollbar.title = ""

        // Hide title in image collapsing
        binding.collapsingTollbar.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        foodDao = MyDataBase.getDataBase(this).foodDao

        // get data Food from (dialog) MainActivity
        val foodId = intent.getIntExtra("food_id", -1)

        foodPresenter = FoodPresenter(foodId, foodDao)
        foodPresenter.onAttach(this)

        binding.fabEditFood.setOnClickListener {
            foodPresenter.onEditClicked()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun detailsFood(food: Foods) {
        binding.foodName.text = food.txtSubject
        binding.detailFood.text = food.txtDis1
        binding.timeFood.text = "مدت زمان آماده شدن : " + food.txtTime + " دقیقه"
        binding.priceFood.text = food.txtPrice.withThousandSeparator()
        // 4.0 -> 4   and  2.3 -> 2.3
        binding.numberRating.text = food.rating.toString().removeSuffix(".0")

        Glide.with(this)
            .load(food.urlImage)
            .into(binding.imgFood)
    }

    // change data in item
    @SuppressLint("SetTextI18n")
    private fun dialogEditFood(food: Foods) {

        val dialogUpdate = AlertDialog.Builder(this).create()
        val updateDialogBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialogUpdate.setView(updateDialogBinding.root)
        dialogUpdate.setCancelable(true)
        dialogUpdate.show()

        // get information food
        updateDialogBinding.dialogEdtFoodName.setText(food.txtSubject)
        updateDialogBinding.dialogEdtFoodDis.setText(food.txtDis1)
        updateDialogBinding.dialogEdtFoodTime.setText(food.txtTime)
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice)

        Glide.with(baseContext)
            .load(food.urlImage)
            .into(updateDialogBinding.imgFoodEdt)

        // format input to ,
        updateDialogBinding.dialogEdtFoodPrice.addThousandSeparator(
            maxDigits = 9,
            counterLayout = updateDialogBinding.updateFoodPriceLayout
        )
        // show price in edtPrice
        updateDialogBinding.dialogEdtFoodPrice.setText(food.txtPrice.replace(",", ""))

        updateDialogBinding.dialogUpBtnDone.setOnClickListener {

            if (
                updateDialogBinding.dialogEdtFoodName.length() > 0 &&
                updateDialogBinding.dialogEdtFoodDis.length() > 0 &&
                updateDialogBinding.dialogEdtFoodTime.length() > 0 &&
                updateDialogBinding.dialogEdtFoodPrice.length() > 0
            ) {

                val txtName = updateDialogBinding.dialogEdtFoodName.text.toString()
                val txtDis = updateDialogBinding.dialogEdtFoodDis.text.toString()
                val txtTime = updateDialogBinding.dialogEdtFoodTime.text.toString()
                // save data in database without ,
                val txtPrice = updateDialogBinding.dialogEdtFoodPrice.getNumericText()

                // edit food in newFood
                val newFood = Foods(
                    id = food.id,
                    txtSubject = txtName,
                    txtDis1 = txtDis,
                    txtTime = txtTime,
                    txtPrice = txtPrice,
                    urlImage = food.urlImage,
                    numOfNumber = food.numOfNumber,
                    rating = food.rating
                )

                // update item in dataBase
//                foodDao.updateFood(newFood)
                // update data in FoodActivity
                foodPresenter.onUpdateFood(newFood)
                detailsFood(newFood)

                dialogUpdate.dismiss()
                showToast("اطلاعات آپدیت شد")

            } else showToast("لطفا اطلاعات را کامل کنید !")

        }
        updateDialogBinding.dialogUpBtnDelete.setOnClickListener {

            val dialogDelete = AlertDialog.Builder(this).create()
            val viewDialogDelete = DialogDeleteItemBinding.inflate(layoutInflater)
            dialogDelete.setView(viewDialogDelete.root)
            dialogDelete.setCancelable(true)
            dialogDelete.show()

            viewDialogDelete.dialogEdtFoodName.text = "((  " + food.txtSubject + "  ))"

            viewDialogDelete.dialogBtnDeleteDone.setOnClickListener {

                val txtName = viewDialogDelete.dialogEdtFoodName.text.toString()
                dialogDelete.dismiss()
//                foodDao.deleteFood(food)
                foodPresenter.onDeleteFood()

                Toast.makeText(this, " $txtName حذف شد !", Toast.LENGTH_SHORT).show()
            }

            viewDialogDelete.dialogBtnDeleteCancel.setOnClickListener {
                dialogDelete.dismiss()
            }
            dialogUpdate.dismiss()
        }

        updateDialogBinding.dialogUpBtnCancel.setOnClickListener {
            dialogUpdate.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menutoolbar, menu)
        return true
    }

    // back to mainActivity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // btn backArrow
            android.R.id.home -> {
                finish()
                true
            }

            R.id.favorite -> {
                isFavorite = !isFavorite
                if (isFavorite) {
                    item.setIcon(R.drawable.ic_heart)
                } else {
                    item.setIcon(R.drawable.ic_heart2)
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // ---------------------------------------------
    override fun backToMainScreen() = finish()

    override fun showDetailsFood(foods: Foods) = detailsFood(foods)

//    override fun showDetailsFood(foodId: Int) {
//        if(foodId != -1){
//            detailsFood(foodDao.getFoodById(foodId))
//        } else onPause()
//    }

    override fun showDialogEditFood(foods: Foods) = dialogEditFood(foods)

    override fun deleteFood(foods: Foods) = finish()

    override fun updateFood(foods: Foods) = detailsFood(foods)

    override fun showProgress() {}

    override fun onDestroy() {
        super.onDestroy()
        foodPresenter.onDetach()
    }
}