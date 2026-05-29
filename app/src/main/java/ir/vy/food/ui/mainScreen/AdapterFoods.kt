package ir.vy.food.ui.mainScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.vy.food.databinding.ItemFoods2Binding
import ir.vy.food.model.data.Foods
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AdapterFoods(
    private val data: MutableList<Foods>,
    private val foodEvents: FoodEvents,
) :
    RecyclerView.Adapter<AdapterFoods.FoodsHolder>() {

    inner class FoodsHolder(private val binding: ItemFoods2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bindData(position: Int) {
            binding.itemTxtSubject.text = data[position].txtSubject
            binding.itemTxtDis1.text = data[position].txtDis1
            binding.itemTxtTime.text = "زمان آماده شدن : ${data[position].txtTime} دقیقه"
//            binding.itemTxtPrice.text = data[position].txtPrice
            binding.itemTxtPrice.text = try {
                String.format("%,d", data[position].txtPrice.replace(",", "").toLong())
            } catch (e: Exception) {
                data[position].txtPrice
            }

            binding.itemRatingMain.rating = data[position].rating
            binding.itemTxtRating.text = data[position].numOfNumber.toString()

            Glide
                .with(binding.root.context)
                .load(data[position].urlImage)
                .override(320, 320)
                .transform(RoundedCornersTransformation(16, 4))
                .into(binding.itemImgMain)

            itemView.setOnClickListener {
                foodEvents.onFoodClicked(data[adapterPosition], adapterPosition)
            }
            itemView.setOnLongClickListener {
                foodEvents.onFoodLongClicked(data[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsHolder {
        val binding = ItemFoods2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodsHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FoodsHolder, position: Int) {
        holder.bindData(position)
    }

    fun addFood(newFood: Foods) {
        //  اضافه کردن آیتم جدید
        data.add(0, newFood)
        // به recycler بفهمونیم که ایتم اضافه کردیم
        notifyItemInserted(0)
    }

    fun removeFood(oldFood: Foods, oldPosition: Int) {
        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    fun updateFood(newFood: Foods, position: Int) {
        data[position] = newFood
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: MutableList<Foods>) {
        // set new data to list
        data.clear()
        data.addAll(newList)
        // فهماندن به ریسایکلر که اطلاعات تغییر کرده
        notifyDataSetChanged()
    }

    interface FoodEvents {
        fun onFoodClicked(food: Foods, position: Int)
        fun onFoodLongClicked(food: Foods, pos: Int)
    }
}
