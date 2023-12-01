package com.douglasbruce.data.models

import com.douglasbruce.utils.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String,
) : BaseModel(TYPE_CHOSEN_WORD)
