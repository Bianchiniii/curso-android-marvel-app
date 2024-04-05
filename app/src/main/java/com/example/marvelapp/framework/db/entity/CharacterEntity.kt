package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bianchini.vinicius.matheus.core.data.CharacterDbConstants

@Entity(tableName = CharacterDbConstants.CHARACTERS_TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val autoId: Int = 0,
    @ColumnInfo(name = CharacterDbConstants.CHARACTERS_COLUMN_INFO_ID)
    val id: Int,
    @ColumnInfo(name = CharacterDbConstants.CHARACTERS_COLUMN_INFO_NAME)
    val name: String,
    @ColumnInfo(name = CharacterDbConstants.CHARACTERS_COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)