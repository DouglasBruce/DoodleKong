package com.douglasbruce.data.models

import com.douglasbruce.data.Room
import com.douglasbruce.utils.Constants.TYPE_PHASE_CHANGE

data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long,
    val drawingPlayer: String? = null,
) : BaseModel(TYPE_PHASE_CHANGE)
