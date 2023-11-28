package com.ljb.data.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import com.ljb.data.model.NumberEntity
import kotlinx.coroutines.flow.Flow

@Database(entities = [NumberEntity::class], version = 1, exportSchema = false)
abstract class NumbersDatabase : RoomDatabase(){
    abstract fun dao(): NumbersDao
}

@Dao
interface NumbersDao{
    @Query("SELECT * FROM numbers")
    fun getNumbers(): Flow<List<NumberEntity>> // 쿼리 결과를 바로 LiveData 타입으로 리턴

    @Insert
    suspend fun insertNumber(numberEntity: NumberEntity)

    @Query("DELETE FROM numbers")
    suspend fun clearNumbers()
}