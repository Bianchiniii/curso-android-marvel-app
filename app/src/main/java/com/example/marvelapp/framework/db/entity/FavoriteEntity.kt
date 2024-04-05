package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bianchini.vinicius.matheus.core.data.FavoriteDbConstants.COLUMN_INFO_ID
import com.bianchini.vinicius.matheus.core.data.FavoriteDbConstants.COLUMN_INFO_IMAGE_URL
import com.bianchini.vinicius.matheus.core.data.FavoriteDbConstants.COLUMN_INFO_NAME
import com.bianchini.vinicius.matheus.core.data.FavoriteDbConstants.FAVORITES_TABLE_NAME
import com.bianchini.vinicius.matheus.core.domain.model.Character

@Entity(tableName = FAVORITES_TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = COLUMN_INFO_NAME)
    val name: String,
    @ColumnInfo(name = COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)

fun List<FavoriteEntity>.toCharactersModel() = map {
    Character(
        id = it.id,
        name = it.name,
        imageUrl = it.imageUrl
    )
}

