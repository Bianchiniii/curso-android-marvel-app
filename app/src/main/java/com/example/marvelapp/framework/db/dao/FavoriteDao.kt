package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.bianchini.vinicius.matheus.core.data.DbConstants.FAVORITES_TABLE_NAME
import com.example.marvelapp.framework.db.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM $FAVORITES_TABLE_NAME")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertFavorites(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun removeFavorites(favoriteEntity: FavoriteEntity)
}