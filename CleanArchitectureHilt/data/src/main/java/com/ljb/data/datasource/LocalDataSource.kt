package com.ljb.data.datasource

import com.ljb.data.database.NumbersDao
import com.ljb.data.model.NumberEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocalDataSource {
    fun getNumbers(): Flow<List<NumberEntity>>
    suspend fun insertNumber(numberEntity: NumberEntity)
    suspend fun clearNumbers()
}

class LocalDataSourceImpl @Inject constructor(private val dao: NumbersDao): LocalDataSource{
    override fun getNumbers(): Flow<List<NumberEntity>> {
        return dao.getNumbers()
    }

    override suspend fun insertNumber(numberEntity: NumberEntity) {
        dao.insertNumber(numberEntity)
    }

    override suspend fun clearNumbers() {
        dao.clearNumbers()
    }
}