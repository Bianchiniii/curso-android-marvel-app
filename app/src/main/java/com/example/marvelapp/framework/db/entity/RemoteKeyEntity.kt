package com.example.marvelapp.framework.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bianchini.vinicius.matheus.core.data.RemoteKeyDbConstants

@Entity(RemoteKeyDbConstants.REMOTE_KEYS_TABLE_NAME)
data class RemoteKeyEntity(
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(RemoteKeyDbConstants.REMOTE_KEY_INFO_COLUMN_INFO_OFFSET)
    val nextOffset: Int?,
)
