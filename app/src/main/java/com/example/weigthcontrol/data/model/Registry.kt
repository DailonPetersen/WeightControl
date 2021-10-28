package com.example.weigthcontrol.data.model

import androidx.room.*

@Entity(tableName = "registry")
data class Registry(
    @PrimaryKey(autoGenerate = false) val regId: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "exerciseId") val exerciseId: Int,
    @ColumnInfo(name = "weight") val weight: Int
)