package com.feldelysium.designsystem.components.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.feldelysium.designsystem.R
import com.feldelysium.designsystem.components.container.FeldDivider
import com.feldelysium.designsystem.foundation.material.FeldMarker
import com.feldelysium.designsystem.foundation.material.MarkerFilmSpec
import com.feldelysium.designsystem.foundation.material.markerFilmMaterial
import com.feldelysium.designsystem.theme.FeldTheme

public enum class FeldIconId {
    ArrowBack,
    Chevron,
    Close,
    Accept,
    Add,
    FastAdd,
    Edit,
    Delete,
    Save,
    More,
    MoreHoriz,
    Search,
    Undo,
    Redo,
    Copy,
    Share,

    Play,
    Pause,
    Stop,
    Autoplay,
    Speed,
    Sound,
    Mute,
    Microphone,
    Video,
    Image,
    Cinema,
    PlaybackText,
    Cassette,
    Journal,

    Import,
    BulkImport,
    Export,
    Paste,
    Tag,
    AuthorSamples,
    DialogueStyle,
    Typeface,
    Palette,

    Settings,
    Options,
    Language,
    Calendar,
    PanelCalendar,
    Panel,
    Time,

    Casino,
    Reals,
    Heart,
    Map,
    Location,
    RadioButtonChecked,
    RadioButtonUnchecked,
    BackingCube,
    BackingCircle,
}

/** Draws a [FeldIconId] icon, by default on a chalk film. */
@Composable
public fun FeldIcon(
    id: FeldIconId,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = Color.Unspecified,
    contentDescription: String? = null,
    seed: Int? = null,
    markerFilmSpec: MarkerFilmSpec = FeldMarker.Chalk,
    film: Boolean = true,
) {
    @DrawableRes val res: Int = when (id) {
        FeldIconId.ArrowBack -> R.drawable.feld_ic_back
        FeldIconId.Chevron -> R.drawable.feld_ic_chevron
        FeldIconId.Close -> R.drawable.feld_ic_cancel
        FeldIconId.Accept -> R.drawable.feld_ic_accept
        FeldIconId.Add -> R.drawable.feld_ic_add
        FeldIconId.FastAdd -> R.drawable.feld_ic_fast_add
        FeldIconId.Edit -> R.drawable.feld_ic_edit
        FeldIconId.Delete -> R.drawable.feld_ic_trash
        FeldIconId.Save -> R.drawable.feld_ic_save
        FeldIconId.More -> R.drawable.feld_ic_more
        FeldIconId.MoreHoriz -> R.drawable.feld_ic_more_horiz
        FeldIconId.Search -> R.drawable.feld_ic_search
        FeldIconId.Undo -> R.drawable.feld_ic_undo
        FeldIconId.Redo -> R.drawable.feld_ic_redo
        FeldIconId.Copy -> R.drawable.feld_ic_copy
        FeldIconId.Share -> R.drawable.feld_ic_share
        FeldIconId.Play -> R.drawable.feld_ic_play
        FeldIconId.Pause -> R.drawable.feld_ic_pause
        FeldIconId.Stop -> R.drawable.feld_ic_stop
        FeldIconId.Autoplay -> R.drawable.feld_ic_autoplay
        FeldIconId.Speed -> R.drawable.feld_ic_speed
        FeldIconId.Sound -> R.drawable.feld_ic_sound
        FeldIconId.Mute -> R.drawable.feld_ic_mute
        FeldIconId.Microphone -> R.drawable.feld_ic_microphone
        FeldIconId.Video -> R.drawable.feld_ic_video
        FeldIconId.Image -> R.drawable.feld_ic_image
        FeldIconId.Cinema -> R.drawable.feld_ic_tape
        FeldIconId.PlaybackText -> R.drawable.feld_ic_playback_text
        FeldIconId.Cassette -> R.drawable.feld_ic_cassette
        FeldIconId.Journal -> R.drawable.feld_ic_journal
        FeldIconId.Import -> R.drawable.feld_ic_edit_import
        FeldIconId.BulkImport -> R.drawable.feld_ic_bulk_import
        FeldIconId.Export -> R.drawable.feld_ic_export
        FeldIconId.Paste -> R.drawable.feld_ic_paste
        FeldIconId.Tag -> R.drawable.feld_ic_tag
        FeldIconId.AuthorSamples -> R.drawable.feld_ic_author_samples
        FeldIconId.DialogueStyle -> R.drawable.feld_ic_dialog
        FeldIconId.Typeface -> R.drawable.feld_ic_typeface
        FeldIconId.Palette -> R.drawable.feld_ic_palette
        FeldIconId.Settings -> R.drawable.feld_ic_settings
        FeldIconId.Options -> R.drawable.feld_ic_options
        FeldIconId.Language -> R.drawable.feld_ic_language
        FeldIconId.Calendar -> R.drawable.feld_ic_calendar
        FeldIconId.PanelCalendar -> R.drawable.feld_ic_panel_calendar
        FeldIconId.Panel -> R.drawable.feld_ic_panel
        FeldIconId.Time -> R.drawable.feld_ic_time
        FeldIconId.Casino -> R.drawable.feld_ic_dice_5
        FeldIconId.Reals -> R.drawable.feld_ic_real
        FeldIconId.Heart -> R.drawable.feld_ic_heart
        FeldIconId.Map -> R.drawable.feld_ic_map
        FeldIconId.Location -> R.drawable.feld_ic_location
        FeldIconId.RadioButtonChecked -> R.drawable.feld_ic_radio_button_checked
        FeldIconId.RadioButtonUnchecked -> R.drawable.feld_ic_radio_button_unchecked
        FeldIconId.BackingCube -> R.drawable.feld_ic_backing_cube
        FeldIconId.BackingCircle -> R.drawable.feld_ic_backing_circle
    }

    Icon(
        painter = painterResource(res),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .then(
                if (film) {
                    Modifier.markerFilmMaterial(
                        markerFilmSpec,
                        id.ordinal + (seed ?: 0),
                        bakeKey = seed,
                    )
                } else {
                    Modifier
                },
            ),
        tint = tint,
    )
}

@Composable
public fun FlatDiamond(color: Color, modifier: Modifier = Modifier, size: Dp = 28.dp, outline: Boolean = false) {
    Canvas(modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w / 2f, 0f)
            lineTo(w, h / 2f)
            lineTo(w / 2f, h)
            lineTo(0f, h / 2f)
            close()
        }
        if (outline) {
            drawPath(path, color, style = Stroke(1.5.dp.toPx()))
        } else {
            drawPath(path, color)
        }
    }
}

@Preview(
    widthDp = 400,
    heightDp = 500,
    showBackground = true,
    backgroundColor = 0xFF0B1011,
)
@Composable
private fun FlatIconPreview() {
    FeldTheme {
        val icons = FeldIconId.entries

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(icons) { id ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        FeldIcon(
                            id = id,
                            size = 24.dp,
                        )

                        Text(
                            text = id.name,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            Spacer(Modifier.size(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FlatDiamond(
                    color = Color.White,
                    size = 28.dp,
                )

                FeldDivider(
                    vertical = true,
                    thickness = 2.dp,
                    segment = 15.dp,
                    changeChance = 0.6f,
                )

                FlatDiamond(
                    color = Color.White,
                    size = 28.dp,
                    outline = true,
                )
            }
        }
    }
}
