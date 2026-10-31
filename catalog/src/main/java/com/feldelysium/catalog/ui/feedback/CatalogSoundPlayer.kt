package com.feldelysium.catalog.ui.feedback

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import java.util.concurrent.ConcurrentHashMap

/** Reference SoundPool setup: build once, load everything up front, play by id. */
internal class CatalogSoundPlayer(private val context: Context) {

    private val pool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()

    private val ids = HashMap<String, Int>()
    private val ready = ConcurrentHashMap.newKeySet<Int>()
    private var customId = 0

    init {
        pool.setOnLoadCompleteListener { _, id, status -> if (status == 0) ready += id }
        SoundPreset.entries.mapNotNull { it.asset }.forEach { path ->
            runCatching { context.assets.openFd(path).use { ids[path] = pool.load(it, 1) } }
        }
    }

    fun setCustom(uri: Uri?): Boolean {
        if (customId != 0) {
            pool.unload(customId)
            ready -= customId
            customId = 0
        }
        if (uri == null) return true
        return runCatching {
            context.contentResolver.openAssetFileDescriptor(uri, "r")!!.use { customId = pool.load(it, 1) }
        }.isSuccess
    }

    fun play(preset: SoundPreset, volume: Float, pitch: Float) {
        val id = if (preset == SoundPreset.Custom) customId else preset.asset?.let { ids[it] } ?: 0
        if (id != 0 && id in ready) {
            pool.play(id, volume, volume, 1, 0, pitch.coerceIn(0.5f, 2f))
        }
    }

    fun release() = pool.release()
}
