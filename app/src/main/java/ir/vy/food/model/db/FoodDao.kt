package ir.vy.food.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.vy.food.model.data.Foods

// create data access object with Dao
@Dao
interface FoodDao {

    //----------- add / update ----------------
    // add a new item data (food)
    @Insert
    fun insertFood(food: Foods)

    // update item data (food)
    @Update
    fun updateFood(food: Foods)

    // add all data
    @Insert
    fun insertAllFoods(data: ArrayList<Foods>)

    // add new item or update item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(food: Foods)
    //-------------------------------------------

    // delete data (food)
    @Delete
    fun deleteFood(food: Foods)

    // Searching in data (food)
    @Query("SELECT * FROM table_food  WHERE txtSubject LIKE '%' || :searching || '%'")
    fun searchFoods(searching: String): List<Foods>

    // delete all data (food)
    @Query("DELETE FROM table_food")
    fun deleteAllFoods()

    // return all data (food)
    @Query("SELECT * FROM table_food ORDER BY id DESC")
    fun getAllFood(): MutableList<Foods>

    @Query("SELECT * FROM table_food WHERE id = :id")
    fun getFoodById(id: Int): Foods
}