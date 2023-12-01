package com.douglasbruce.data.models

import com.douglasbruce.utils.Constants.TYPE_JOIN_ROOM_HANDSHAKE

data class JoinRoomHandshake(
    val username: String,
    val roomName: String,
    val clientId: String,
) : BaseModel(TYPE_JOIN_ROOM_HANDSHAKE)
