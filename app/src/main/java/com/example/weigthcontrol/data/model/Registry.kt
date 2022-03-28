package com.example.weigthcontrol.data.model

import androidx.room.*

@Entity(tableName = "registry")
data class Registry(
    @ColumnInfo(name = "timestamp") val timestamp: String,
    @ColumnInfo(name = "exerciseId") val exerciseId: Int,
    @ColumnInfo(name = "weight") val weight: Int
) {
    @PrimaryKey(autoGenerate = true) var regId: Int = 0
}