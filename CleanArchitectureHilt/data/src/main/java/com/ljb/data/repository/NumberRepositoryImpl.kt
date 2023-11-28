package com.ljb.data.repository

import com.ljb.data.datasource.LocalDataSource
import com.ljb.data.model.NumberEntity
import com.ljb.data.model.toNumberEntity
import com.ljb.data.model.toNumberModel
import com.ljb.domain.model.NumberModel
import com.ljb.domain.repository.NumberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * domain 레이어에 정의한 [NumberRepository] 인터페이스의 구현 클래스.
 * Repository는 다양한 DataSource를 상황에 맞게 사용하여 UseCase의 요청을 처리할 수 있어야 함
 */
class NumberRepositoryImpl @Inject constructor(private val dataSource: LocalDataSource) : NumberRepository{
    override fun getNumbers(): Flow<List<NumberModel>> {
        // DataSource에서 Flow<List<NumberEntity>>의 형태로 반환하는 데이터를 domain 레이어에서 사용할 수 있게 Flow<List<NumberModel>>로 변환
        return dataSource.getNumbers().map { entityList: List<NumberEntity> ->
            entityList.map { element -> element.toNumberModel() }
        }
    }

    override suspend fun insertNumber(number: NumberModel) {
        // domain 레이어의 usecase에서 파라미터로 넘겨준 NumberModel 타입의 데이터를 database에서 사용하는 NumberEntity 타입의 데이터로 변환하여 insert 호출
        val numberEntity = number.toNumberEntity()
        dataSource.insertNumber(numberEntity)
    }

    override suspend fun clearNumbers() {
        dataSource.clearNumbers()
    }
}