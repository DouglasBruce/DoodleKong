package com.douglasbruce.data.models

import com.douglasbruce.utils.Constants.TYPE_NEW_WORDS

data class NewWords(
    val newWords: List<String>
) : BaseModel(TYPE_NEW_WORDS)
