package com.example.newsapi.db

import androidx.room.*
import com.example.xicomtask.model.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(userModel: UserModel):Long

//    @Query("Select * From articles")
//    fun getAllArticles():LiveData<List<Article>>
//
//    @Delete
//    suspend fun deleteArticle(article: Article)
}