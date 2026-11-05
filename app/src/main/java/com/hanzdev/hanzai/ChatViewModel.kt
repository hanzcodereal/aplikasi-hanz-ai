package com.hanzdev.hanzai

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.Chat
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Satu pesan di dalam obrolan. */
data class ChatMessage(
    val text: String? = null,
    val imageBitmap: Bitmap? = null,
    val isUser: Boolean,
    val isError: Boolean = false
)

class ChatViewModel : ViewModel() {

    companion object {
        /** Model bawaan. Ganti di sini bila ingin model default lain. */
        const val DEFAULT_MODEL = "gemini-2.0-flash"

        /** Daftar model yang bisa dipilih di menu. */
        val MODEL_OPTIONS = listOf(
            "gemini-2.0-flash",
            "gemini-3.6-flash",
            "gemini-3.8-flash",
            "gemini-3.5-flash-lite"
        )

        private const val PLACEHOLDER_KEY = "API_KEY_BELUM_DIISI"

        private const val SYSTEM_PROMPT =
            "Kamu adalah Hanz AI, asisten pintar buatan Hanz Dev Official. " +
                "Jawablah dalam bahasa Indonesia yang jelas, ramah, dan mudah dipahami, " +
                "kecuali pengguna meminta bahasa lain."
    }

    /** Riwayat pesan; disimpan di ViewModel agar tidak hilang saat layar diputar. */
    val messages = mutableStateListOf<ChatMessage>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedModel = MutableStateFlow(DEFAULT_MODEL)
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    private var chat: Chat? = null
    private var history: List<Content> = emptyList()

    private fun buildChat(modelName: String, history: List<Content>): Chat {
        // Gemma tidak mendukung system instruction, jadi hanya dipasang untuk model Gemini.
        val model = if (modelName.startsWith("gemini")) {
            GenerativeModel(
                modelName = modelName,
                apiKey = BuildConfig.apiKey,
                systemInstruction = content { text(SYSTEM_PROMPT) }
            )
        } else {
            GenerativeModel(modelName = modelName, apiKey = BuildConfig.apiKey)
        }
        return model.startChat(history)
    }

    fun updateModel(modelName: String) {
        if (modelName == _selectedModel.value) return
        // Simpan riwayat percakapan agar tetap nyambung setelah ganti model
        history = chat?.history?.toList() ?: history
        _selectedModel.value = modelName
        chat = null
    }

    fun clearChat() {
        messages.clear()
        history = emptyList()
        chat = null
    }

    fun sendMessage(bitmap: Bitmap?, prompt: String?) {
        if (_isLoading.value) return
        if (bitmap == null && prompt.isNullOrBlank()) return

        messages.add(ChatMessage(text = prompt, imageBitmap = bitmap, isUser = true))
        _isLoading.value = true

        viewModelScope.launch {
            try {
                if (BuildConfig.apiKey.isBlank() || BuildConfig.apiKey == PLACEHOLDER_KEY) {
                    throw IllegalStateException("API key belum diisi")
                }
                val activeChat = chat ?: buildChat(_selectedModel.value, history).also { chat = it }
                val response = withContext(Dispatchers.IO) {
                    activeChat.sendMessage(
                        content {
                            bitmap?.let { image(it) }
                            prompt?.let { text(it) }
                        }
                    )
                }
                val answer = response.text
                if (answer.isNullOrBlank()) {
                    messages.add(
                        ChatMessage(
                            text = "Hanz AI tidak memberikan jawaban. Coba ubah pertanyaan Anda.",
                            isUser = false,
                            isError = true
                        )
                    )
                } else {
                    messages.add(ChatMessage(text = answer, isUser = false))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                messages.add(ChatMessage(text = friendlyError(e), isUser = false, isError = true))
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Mengubah error teknis menjadi pesan Bahasa Indonesia yang mudah dipahami. */
    private fun friendlyError(e: Throwable): String {
        val detail = generateSequence(e) { it.cause }
            .joinToString(" ") { "${it::class.java.simpleName} ${it.message.orEmpty()}" }
            .lowercase()

        return when {
            "api key belum diisi" in detail ->
                "API Key Gemini belum diisi. Isi apiKey di local.properties (atau GitHub Secret GEMINI_API_KEY), lalu build ulang aplikasi."
            "api key" in detail || "invalidapikey" in detail || "api_key_invalid" in detail ->
                "API Key tidak valid. Periksa kembali API Key Gemini Anda."
            "unknownhost" in detail || "unable to resolve" in detail ||
                "connectexception" in detail || "failed to connect" in detail ->
                "Tidak ada koneksi internet. Periksa jaringan Anda lalu coba lagi."
            "timeout" in detail || "timed out" in detail ->
                "Koneksi terlalu lama merespons. Coba lagi sebentar lagi."
            "quota" in detail || "429" in detail || "resource_exhausted" in detail ->
                "Kuota penggunaan habis atau terlalu banyak permintaan. Coba lagi nanti."
            "not found" in detail || "404" in detail || "no longer available" in detail ->
                "Model \"${_selectedModel.value}\" tidak tersedia. Pilih model lain lewat menu titik tiga di kanan atas."
            "location" in detail ->
                "Layanan Gemini belum tersedia di wilayah Anda."
            "blocked" in detail || "safety" in detail ->
                "Permintaan diblokir oleh filter keamanan. Coba gunakan kalimat yang berbeda."
            else ->
                "Terjadi kesalahan: ${e.localizedMessage ?: "tidak diketahui"}"
        }
    }
}
