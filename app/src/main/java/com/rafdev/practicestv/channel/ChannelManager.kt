package com.rafdev.practicestv.channel

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.tvprovider.media.tv.Channel
import androidx.tvprovider.media.tv.ChannelLogoUtils.storeChannelLogo
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import com.rafdev.practicestv.R
import com.rafdev.practicestv.data.model.ResponseModel
import com.rafdev.practicestv.ui.MainActivity

class ChannelManager(private val context: Context) {

    companion object {
        const val CHANNEL = "channelManager"
    }

    private val CHANNEL_RECOMMENDED = "Recommended"
    private val CHANNEL_NEW = "New Channels"

//    private lateinit var previewProgram: PreviewProgram


    fun createRecommendedChannelIfNeeded(): Long {
        val recommendedChannelId = findChannelIdByName(CHANNEL_RECOMMENDED)
        if (recommendedChannelId == -1L) {
            val newChannelId = createChannel(CHANNEL_RECOMMENDED)
            (context as MainActivity).requestChannelBrowsable(newChannelId)
            return newChannelId
        }
        return recommendedChannelId
    }

    private fun createChannel(channelName: String): Long {
        val channelBuilder = Channel.Builder()
        channelBuilder.setType(TvContractCompat.Channels.TYPE_PREVIEW).setDisplayName(channelName)

        val channelUri = context.contentResolver.insert(
            TvContractCompat.Channels.CONTENT_URI, channelBuilder.build().toContentValues()
        )

        val channelId = ContentUris.parseId(channelUri!!)
        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap

        storeChannelLogo(context, channelId, logoBitmap)
        return channelId
    }

    private fun findChannelIdByName(channelName: String): Long {

        val projection = arrayOf(
            TvContractCompat.Channels._ID, TvContractCompat.Channels.COLUMN_DISPLAY_NAME
        )

        val cursor: Cursor? = context.contentResolver.query(
            TvContractCompat.Channels.CONTENT_URI, projection, null, null, null
        )
        var channelId = -1L
        cursor?.use { c ->
            while (c.moveToNext()) {
                val channelIdQuery =
                    c.getLong(c.getColumnIndexOrThrow(TvContractCompat.Channels._ID))
                val channelNameQuery =
                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.Channels.COLUMN_DISPLAY_NAME))
                if (channelName == channelNameQuery) {
                    channelId = channelIdQuery
                }
            }
        }

        return channelId

    }

    @SuppressLint("RestrictedApi")
    fun updateProgramsForChannel(channelId: Long, newPrograms: List<ResponseModel>) {
        val existingPrograms = getProgramsForChannel(channelId)
        Log.i(CHANNEL, "programas -> $existingPrograms")

        if (existingPrograms.isEmpty()) {
            // No hay programas, insertar todos los nuevos
            Log.i(CHANNEL, "existen programas")
            insertAllPrograms(channelId, newPrograms)
            return
        }

        // Mapa para almacenar el ID del programa por título
        val existingProgramsMap = existingPrograms.associateBy { it.title }
        Log.i(CHANNEL, "existen programasMAP -> $existingProgramsMap")


        newPrograms.forEach { newProgram ->
            val existingProgram = existingProgramsMap[newProgram.title]
            Log.i(CHANNEL, "existen !!!-> $existingProgram")

            if (existingProgram != null) {
                // Programa existente, actualizar si es necesario
                if (existingProgram.title == newProgram.title) {
                    updateProgram(channelId, existingProgram.id, newProgram)
                }
            } else {
                // Nuevo programa, insertar
                insertProgram(channelId, newProgram)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun insertAllPrograms(channelId: Long, programs: List<ResponseModel>) {
        programs.forEach { program ->
            val programBuilder = PreviewProgram.Builder()
                .setChannelId(channelId)
                .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
                .setTitle(program.title)
                .setPosterArtUri(Uri.parse(program.url))
                .setInternalProviderId(channelId.toString())
                .setIntentUri(Uri.parse("content://practicestv.rafdev"))

            context.contentResolver.insert(
                TvContractCompat.PreviewPrograms.CONTENT_URI,
                programBuilder.build().toContentValues()
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun updateProgram(channelId: Long, programId: Long, newProgram: ResponseModel) {
        val updateUri = TvContractCompat.buildPreviewProgramUri(programId)
        val programBuilder = PreviewProgram.Builder()
            .setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
            .setTitle(newProgram.title)
            .setPosterArtUri(Uri.parse(newProgram.url))
            .setInternalProviderId(channelId.toString())
            .setIntentUri(Uri.parse("content://practicestv.rafdev"))


        context.contentResolver.update(
            updateUri,
            programBuilder.build().toContentValues(),
            null,
            null
        )
    }

    @SuppressLint("RestrictedApi")
    private fun insertProgram(channelId: Long, program: ResponseModel) {
        val programBuilder = PreviewProgram.Builder()
            .setChannelId(channelId)
            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
            .setTitle(program.title)
            .setPosterArtUri(Uri.parse(program.url))
            .setInternalProviderId(channelId.toString())
            .setIntentUri(Uri.parse("content://practicestv.rafdev"))


        context.contentResolver.insert(
            TvContractCompat.PreviewPrograms.CONTENT_URI,
            programBuilder.build().toContentValues()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun getProgramsForChannel(channelId: Long): List<PreviewProgram> {
        val programs = mutableListOf<PreviewProgram>()
        val projection = arrayOf(
            TvContractCompat.PreviewPrograms._ID,
            TvContractCompat.PreviewPrograms.COLUMN_TITLE,
            TvContractCompat.PreviewPrograms.COLUMN_POSTER_ART_URI
        )

        val uri = TvContractCompat.buildPreviewProgramsUriForChannel(channelId)

        val cursor: Cursor? = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val programId =
                    c.getLong(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms._ID))
                val title =
                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_TITLE))
                val posterArtUri =
                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_POSTER_ART_URI))

                val program = PreviewProgram.Builder()
                    .setId(programId)
                    .setTitle(title)
                    .setPosterArtUri(Uri.parse(posterArtUri))
                    .build()

                programs.add(program)
            }
        }

        return programs
    }
    //---------------
//    @SuppressLint("RestrictedApi")
//    fun updateProgramsForChannel(channelId: Long, newPrograms: List<ResponseModel>) {
//        val hasPrograms = checkIfChannelHasPrograms(channelId)
//
//        if (hasPrograms) {
//            Log.i(CHANNEL, "Channel ID $channelId already has programs.")
//        } else {
//            Log.i(CHANNEL, "Channel ID $channelId has no programs.")
//            val programBuilder = PreviewProgram.Builder()
//
//            newPrograms.forEach {
//                programBuilder.setChannelId(channelId)
//                    .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
//                    .setTitle(it.title)
//                    .setPosterArtUri(Uri.parse(it.url))
//                    .setInternalProviderId(channelId.toString())
//
//                context.contentResolver.insert(
//                    TvContractCompat.PreviewPrograms.CONTENT_URI,
//                    programBuilder.build().toContentValues()
//                )
//            }
//        }
//    }
//
//    @SuppressLint("RestrictedApi")
//    private fun checkIfChannelHasPrograms(channelId: Long): Boolean {
//        val projection = arrayOf(TvContractCompat.PreviewPrograms._ID)
//
//        // Construye la URI para el canal específico
//        val uri = TvContractCompat.buildPreviewProgramsUriForChannel(channelId)
//
//        val cursor: Cursor? = context.contentResolver.query(
//            uri,
//            projection,
//            null,
//            null,
//            null
//        )
//
//        var hasPrograms = false
//        cursor?.use { c ->
//            if (c.moveToFirst()) {
//                hasPrograms = true
//            }
//        }
//
//        return hasPrograms
//
//    }
//----------------
//    @SuppressLint("RestrictedApi")
//    private fun getProgramsForChannel(channelId: Long): List<ResponseModel> {
//        val programs = mutableListOf<ResponseModel>()
//        val projection = arrayOf(
//            TvContractCompat.PreviewPrograms._ID,
//            TvContractCompat.PreviewPrograms.COLUMN_TITLE,
//            TvContractCompat.PreviewPrograms.COLUMN_INTERNAL_PROVIDER_ID
//        )
//
//        val cursor: Cursor? = context.contentResolver.query(
//            TvContractCompat.PreviewPrograms.CONTENT_URI,
//            projection,
//            "${TvContractCompat.PreviewPrograms.COLUMN_CHANNEL_ID} = ?",
//            arrayOf(channelId.toString()),
//            null
//        )
//
//        cursor?.use { c ->
//            while (c.moveToNext()) {
//                val title = c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_TITLE))
//                val url = c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_INTERNAL_PROVIDER_ID))
//                val program = ResponseModel(title, url)
//                programs.add(program)
//            }
//        }
//
//        return programs
//    }

}
//    @SuppressLint("RestrictedApi")
//    fun createChannel() {
//        // Crear el canal
//        val channelBuilder = Channel.Builder()
//        channelBuilder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
//            .setDisplayName("Recomendados")
//
//        val channelUri = context.contentResolver.insert(
//            TvContractCompat.Channels.CONTENT_URI, channelBuilder.build().toContentValues()
//        )
//
//        val channelId = ContentUris.parseId(channelUri!!)
//
//        // add logo Bitmap
//        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
//        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap
//        storeChannelLogo(context, channelId, logoBitmap)
//
//        // add programs to channel
//        val programBuilder = PreviewProgram.Builder()
//        programBuilder.setChannelId(channelId)
//            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
//            .setTitle("Title")
//            .setDescription("Program description")
//            .setPosterArtUri(Uri.parse("https://cdn.pixabay.com/photo/2022/08/28/01/40/cyberpunk-city-7415576_1280.jpg"))
//            .setInternalProviderId(channelId.toString())
//
//        val programUri = context.contentResolver.insert(
//            TvContractCompat.PreviewPrograms.CONTENT_URI,
//            programBuilder.build().toContentValues()
//        )
//
//        val programId = ContentUris.parseId(programUri!!)
//
//        Log.e(CHANNEL, "channelId=$channelId, programId=$programId")
//
//        listChannels()
//        listProgramsForChannel(19)
//    }
//
//    private fun listChannels() {
//        val projection = arrayOf(
//            TvContractCompat.Channels._ID,
//            TvContractCompat.Channels.COLUMN_DISPLAY_NAME
//        )
//
//        val cursor: Cursor? = context.contentResolver.query(
//            TvContractCompat.Channels.CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
//
//        cursor?.use { c ->
//            while (c.moveToNext()) {
//                val channelId = c.getLong(c.getColumnIndexOrThrow(TvContractCompat.Channels._ID))
//                val channelName =
//                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.Channels.COLUMN_DISPLAY_NAME))
//                Log.d(CHANNEL, "Channel ID: $channelId, Name: $channelName")
//                // Aquí puedes agregar cualquier lógica adicional para manejar los canales existentes
//            }
//        }
//    }
//
//    @SuppressLint("RestrictedApi")
//    private fun listProgramsForChannel(channelId: Long) {
//        val projection = arrayOf(
//            TvContractCompat.PreviewPrograms._ID,
//            TvContractCompat.PreviewPrograms.COLUMN_TITLE,
//            TvContractCompat.PreviewPrograms.COLUMN_POSTER_ART_URI
//            // Añade otras columnas que necesites obtener
//        )
//
//        // Construye la URI para el canal específico
//        val uri = TvContractCompat.buildPreviewProgramsUriForChannel(channelId)
//
//        val cursor: Cursor? = context.contentResolver.query(
//            uri,
//            projection,
//            null,
//            null,
//            null
//        )
//
//        cursor?.use { c ->
//            while (c.moveToNext()) {
//                val programId =
//                    c.getLong(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms._ID))
//                val programTitle =
//                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_TITLE))
//                val posterArtUri =
//                    c.getString(c.getColumnIndexOrThrow(TvContractCompat.PreviewPrograms.COLUMN_POSTER_ART_URI))
//
//                Log.d(
//                    CHANNEL,
//                    "Program ID: $programId, Title: $programTitle, Poster Art URI: $posterArtUri"
//                )
//                // Aquí puedes agregar cualquier lógica adicional para manejar los programas
//            }
//        }
//    }

//    fun createChannel() {
//        val builder = Channel.Builder()
//        builder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
//            .setDisplayName("Recomendados")
////            .setAppLinkIntentUri(uri)
//
//
//        var channelUri = context.contentResolver.insert(
//            TvContractCompat.Channels.CONTENT_URI, builder.build().toContentValues()
//        )
//
//        var channelId = ContentUris.parseId(channelUri!!)
//
//        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
//        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap
//
//        storeChannelLogo(context, channelId, logoBitmap)
//
//
//        //add programs
//
//        val builderP = PreviewProgram.Builder()
//        builderP.setChannelId(channelId)
//            .setType(TvContractCompat.PreviewPrograms.TYPE_CLIP)
//            .setTitle("Title")
//            .setDescription("Program description")
//            .setPosterArtUri(Uri.parse("https://cdn.pixabay.com/photo/2022/08/28/01/40/cyberpunk-city-7415576_1280.jpg"))
//            .setInternalProviderId(channelId.toString())
//
//        var programUri = context.contentResolver.insert(
//            TvContractCompat.PreviewPrograms.CONTENT_URI,
//            builder.build().toContentValues()
//        )
//
//        val programId = ContentUris.parseId(programUri!!)
//
//        Log.e(CHANNEL, "channelId=$channelId, programId=$programId")
//
//    }


//class MainActivity : AppCompatActivity() {
//
//    private lateinit var channelManager: ChannelManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        channelManager = ChannelManager(context = this)
//
//        // Crear canal de Recomendados si no existe
//        val recommendedChannelId = channelManager.createRecommendedChannelIfNeeded()
//
//        // Crear canal de Lo Nuevo si no existe
//        val newChannelId = channelManager.createNewChannelIfNeeded()
//
//        // A partir de aquí, puedes realizar otras operaciones con los canales y programas
//        // por ejemplo, listar los programas de estos canales, etc.
//    }
//}
//class ChannelManager(private val context: Context) {
//
//    companion object {
//        const val CHANNEL = "channelManager"
//    }
//
//    private val CHANNEL_RECOMENDADOS_NAME = "Recomendados"
//    private val CHANNEL_LO_NUEVO_NAME = "Lo Nuevo"
//
//    @SuppressLint("RestrictedApi")
//    fun createRecommendedChannelIfNeeded(): Long {
//        val recommendedChannelId = findChannelIdByName(CHANNEL_RECOMENDADOS_NAME)
//        if (recommendedChannelId == -1L) {
//            return createChannel(CHANNEL_RECOMENDADOS_NAME)
//        }
//        return recommendedChannelId
//    }
//
//    @SuppressLint("RestrictedApi")
//    fun createNewChannelIfNeeded(): Long {
//        val newChannelId = findChannelIdByName(CHANNEL_LO_NUEVO_NAME)
//        if (newChannelId == -1L) {
//            return createChannel(CHANNEL_LO_NUEVO_NAME)
//        }
//        return newChannelId
//    }
//
//    @SuppressLint("RestrictedApi")
//    private fun findChannelIdByName(channelName: String): Long {
//        val projection = arrayOf(TvContractCompat.Channels._ID)
//        val selection = "${TvContractCompat.Channels.COLUMN_DISPLAY_NAME} = ?"
//        val selectionArgs = arrayOf(channelName)
//
//        val cursor: Cursor? = context.contentResolver.query(
//            TvContractCompat.Channels.CONTENT_URI,
//            projection,
//            selection,
//            selectionArgs,
//            null
//        )
//
//        var channelId = -1L
//        cursor?.use {
//            if (it.moveToFirst()) {
//                channelId = it.getLong(it.getColumnIndexOrThrow(TvContractCompat.Channels._ID))
//            }
//        }
//
//        return channelId
//    }
//
//    @SuppressLint("RestrictedApi")
//    private fun createChannel(channelName: String): Long {
//        val channelBuilder = Channel.Builder()
//        channelBuilder.setType(TvContractCompat.Channels.TYPE_PREVIEW)
//            .setDisplayName(channelName)
//
//        val channelUri = context.contentResolver.insert(
//            TvContractCompat.Channels.CONTENT_URI, channelBuilder.build().toContentValues()
//        )
//
//        val channelId = ContentUris.parseId(channelUri!!)
//        val logoDrawable = ContextCompat.getDrawable(context, R.drawable.ic_icon)
//        val logoBitmap = (logoDrawable as BitmapDrawable).bitmap
//
//        storeChannelLogo(context, channelId, logoBitmap)
//
//        // Aquí puedes agregar más configuraciones del canal si es necesario
//
//        return channelId
//    }
//
//    @SuppressLint("RestrictedApi")
//    private fun storeChannelLogo(context: Context, channelId: Long, logoBitmap: Bitmap) {
//        // Implementación para almacenar el logo del canal, si es necesario
//        // Esto puede requerir el uso de TvContractCompatUtils para APIs más bajas
//    }
//
//    // Métodos para agregar programas, listChannels, listProgramsForChannel, etc.
//}

//esta manera esta para estudiar porque fue la seguunda manera de saber el id si existe o no el canal
//private fun findChannelIdByName(channelName: String): Long {
//        val projection = arrayOf(TvContractCompat.Channels._ID)
//        val selection = "${TvContractCompat.Channels.COLUMN_DISPLAY_NAME} = ?"
//        val selectionArgs = arrayOf(channelName)
//
//        val cursor: Cursor? = context.contentResolver.query(
//            TvContractCompat.Channels.CONTENT_URI,
//            projection,
//            selection,
//            selectionArgs,
//            null
//        )
//
//        var channelId = -1L
//        cursor?.use {
//            if (it.moveToFirst()) {
//                channelId = it.getLong(it.getColumnIndexOrThrow(TvContractCompat.Channels._ID))
//            }
//        }
//        Log.i(CHANNEL, "channel exist  ID -> $channelId")
//
//        return channelId
//}
//<-->