package com.example.marvelapp.framework.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bianchini.vinicius.matheus.core.data.RemoteKeyDbConstants
import com.example.marvelapp.framework.db.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKeyEntity: RemoteKeyEntity)

    @Query("SELECT * FROM ${RemoteKeyDbConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun remoteKey(): RemoteKeyEntity

    @Query("DELETE FROM ${RemoteKeyDbConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun clearAll()
}