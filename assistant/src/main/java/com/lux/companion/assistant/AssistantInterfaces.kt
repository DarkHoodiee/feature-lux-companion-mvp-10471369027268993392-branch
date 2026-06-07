package com.lux.companion.assistant

interface AssistantInterface {
    suspend fun processVoiceInput(audioData: ByteArray)
    suspend fun generateResponse(prompt: String): String
}

interface MemoryInterface {
    fun saveContext(key: String, value: String)
    fun getContext(key: String): String?
}
