package com.example.weigthcontrol.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = false) val exercId: Int,
    @ColumnInfo(name = "execise_name") val name: String
)