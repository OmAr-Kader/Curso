package com.curso.free.ui.course

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import com.curso.free.global.util.imageBuildr
import com.curso.free.global.util.videoConfig
import com.curso.free.global.util.videoItem
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import java.net.URI


@Composable
fun VideoViewScreen(
    videoUri: String,
    videoTitle: String,
) {
    Scaffold {
        Column {
            VideoPlayer(
                mediaItems = listOf(
                    videoItem(URI.create(videoUri).toString(), videoTitle)
                ),
                handleLifecycle = true,
                autoPlay = true,
                usePlayerController = true,
                enablePip = true,
                handleAudioFocus = true,
                controllerConfig = videoConfig,
                repeatMode = RepeatMode.NONE,
                /*onCurrentTimeChanged = {

                },
                playerInstance = { // ExoPlayer instance (Experimental)
                    addAnalyticsListener(
                        object : AnalyticsListener {
                            // player logger
                        }
                    )
                },*/
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}


@Composable
fun ImageViewScreen(
    imageUri: String,
    @Suppress("UNUSED_PARAMETER") courseTitle: String,
) {
    Scaffold {
        Column(
            modifier = Modifier.background(Color(0xFF1F1F1F))
        ) {
            SubcomposeAsyncImage(
                model = LocalContext.current.imageBuildr(imageUri),
                success = { (painter, _) ->
                    Image(
                        contentScale = ContentScale.Fit,
                        painter = painter,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                },
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                contentDescription = "Image",
            )
        }
    }
}