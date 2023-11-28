package com.ljb.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * data 레이어에서 사용하는 데이터 모델.
 */
@Entity(tableName = "numbers")
data class NumberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Int
)
