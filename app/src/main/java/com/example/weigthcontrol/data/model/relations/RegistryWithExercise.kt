package com.example.weigthcontrol.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.weigthcontrol.data.model.Exercise
import com.example.weigthcontrol.data.model.Registry

data class RegistryWithExercise(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exerId",
        entityColumn = "exerciseId"
    )
    val registry: Registry
)