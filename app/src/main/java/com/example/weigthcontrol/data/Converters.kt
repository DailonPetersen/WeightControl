package com.example.weigthcontrol.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun stringToDate(string: String): Date? {
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        return format.parse(string)
    }
}