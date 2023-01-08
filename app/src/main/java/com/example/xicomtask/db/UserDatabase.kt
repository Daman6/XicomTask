package com.example.newsapi.db

import android.content.Context
import androidx.room.*
import com.example.xicomtask.model.UserModel

@Database(
    entities = [UserModel::class],
    version = 1,
    exportSchema = false
)

abstract class UserDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object{

        private var instance: UserDatabase? = null
        private val lock= Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock)
        {
            instance ?: createDatabase(context).also{ instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "user_db.db"
            ).fallbackToDestructiveMigration()
                .build()

    }
}