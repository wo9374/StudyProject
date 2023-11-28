package com.ljb.data.model

import com.ljb.domain.model.NumberModel

/**
 * data 레이어에서 사용하는 데이터모델인 [NumberEntity]를
 * domain 레이어에서 사용하는 데이터모델인 [NumberModel]로 변환
 */
fun NumberEntity.toNumberModel(): NumberModel = NumberModel(id = this.id, value = this.value)

/**
 * domain 레이어에서 사용하는 데이터모델인 [NumberModel]를
 * data 레이어에서 사용하는 데이터모델인 [NumberEntity]로 변환
 */
fun NumberModel.toNumberEntity(): NumberEntity = NumberEntity(value = this.value)