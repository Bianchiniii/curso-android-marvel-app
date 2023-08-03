package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bianchini.vinicius.matheus.core.data.DbConstants.COLUMN_INFO_ID
import com.bianchini.vinicius.matheus.core.data.DbConstants.COLUMN_INFO_IMAGE_URL
import com.bianchini.vinicius.matheus.core.data.DbConstants.COLUMN_INFO_name
import com.bianchini.vinicius.matheus.core.data.DbConstants.FAVORITES_TABLE_NAME

@Entity(tableName = FAVORITES_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_INFO_name)
    val name: String,
    @ColumnInfo(name = COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)
