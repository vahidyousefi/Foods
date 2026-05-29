package ir.vy.food.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.vy.food.model.data.Foods

// Create Database
@Database(version = 1, exportSchema = false, entities = [Foods::class])

abstract class MyDataBase : RoomDatabase() {
    abstract val foodDao: FoodDao

    // Static
    companion object {

        // in other thread
        @Volatile
        private var dataBase: MyDataBase? = null
        fun getDataBase(context: Context): MyDataBase {

            /**
             * synchronized
             *     یعنی جلوگیری از ساخته شدن بیش از یک شی توسط ترد ها
             */
            synchronized(this) {
                if (dataBase == null) {

                    dataBase = Room.databaseBuilder(
                        context,
                        MyDataBase::class.java,
                        "myDatabase.db"
                    )
                        // run in main thread
                        .allowMainThreadQueries()
                        .build()
                }
                return dataBase!!
            }
        }
    }
}