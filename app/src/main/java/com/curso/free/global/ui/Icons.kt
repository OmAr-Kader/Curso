package com.curso.free.global.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Composable
fun rememberAttachMoney(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "attach_money",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color), //Color.Black
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.042f, 34.792f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.396f)
                reflectiveQuadToRelative(-0.375f, -0.938f)
                verticalLineToRelative(-2.166f)
                quadToRelative(-1.958f, -0.334f, -3.375f, -1.396f)
                quadToRelative(-1.417f, -1.063f, -2.208f, -2.771f)
                quadToRelative(-0.25f, -0.458f, -0.021f, -0.979f)
                quadToRelative(0.229f, -0.521f, 0.729f, -0.771f)
                quadToRelative(0.5f, -0.208f, 1f, 0f)
                reflectiveQuadToRelative(0.75f, 0.708f)
                quadToRelative(0.75f, 1.375f, 1.896f, 2.063f)
                quadToRelative(1.146f, 0.687f, 2.771f, 0.687f)
                quadToRelative(1.916f, 0f, 3.187f, -0.937f)
                quadToRelative(1.271f, -0.938f, 1.271f, -2.604f)
                quadToRelative(0f, -1.709f, -1.083f, -2.709f)
                quadToRelative(-1.084f, -1f, -4.334f, -2.041f)
                quadToRelative(-3.083f, -0.959f, -4.5f, -2.459f)
                quadToRelative(-1.416f, -1.5f, -1.416f, -3.875f)
                reflectiveQuadToRelative(1.562f, -3.875f)
                quadToRelative(1.563f, -1.5f, 3.771f, -1.708f)
                verticalLineTo(6.5f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                quadToRelative(0.541f, 0f, 0.937f, 0.375f)
                reflectiveQuadToRelative(0.396f, 0.917f)
                verticalLineToRelative(2.125f)
                quadToRelative(1.542f, 0.167f, 2.687f, 0.917f)
                quadToRelative(1.146f, 0.75f, 1.896f, 1.833f)
                quadToRelative(0.292f, 0.417f, 0.125f, 0.938f)
                quadToRelative(-0.166f, 0.52f, -0.708f, 0.729f)
                quadToRelative(-0.458f, 0.208f, -0.979f, 0.041f)
                quadToRelative(-0.521f, -0.166f, -0.896f, -0.625f)
                quadToRelative(-0.542f, -0.708f, -1.396f, -1.062f)
                quadToRelative(-0.854f, -0.354f, -2.021f, -0.354f)
                quadToRelative(-1.875f, 0f, -2.958f, 0.854f)
                quadToRelative(-1.083f, 0.854f, -1.083f, 2.312f)
                quadToRelative(0f, 1.459f, 1.166f, 2.375f)
                quadToRelative(1.167f, 0.917f, 4.375f, 1.875f)
                quadToRelative(2.917f, 0.875f, 4.355f, 2.5f)
                quadToRelative(1.437f, 1.625f, 1.437f, 4.167f)
                quadToRelative(-0.042f, 2.667f, -1.625f, 4.25f)
                quadToRelative(-1.583f, 1.583f, -4.375f, 2f)
                verticalLineToRelative(2.083f)
                quadToRelative(0f, 0.542f, -0.396f, 0.938f)
                quadToRelative(-0.396f, 0.396f, -0.937f, 0.396f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberVideoLibrary(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "video_library",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 23f)
                lineToRelative(7.125f, -4.583f)
                quadToRelative(0.625f, -0.375f, 0.625f, -1.084f)
                quadToRelative(0f, -0.708f, -0.625f, -1.083f)
                lineTo(20f, 11.667f)
                quadToRelative(-0.667f, -0.417f, -1.333f, -0.063f)
                quadTo(18f, 11.958f, 18f, 12.75f)
                verticalLineToRelative(9.167f)
                quadToRelative(0f, 0.791f, 0.667f, 1.145f)
                quadToRelative(0.666f, 0.355f, 1.333f, -0.062f)
                close()
                moveToRelative(-8.458f, 8.125f)
                quadToRelative(-1.084f, 0f, -1.875f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.875f)
                verticalLineTo(6.25f)
                quadToRelative(0f, -1.083f, 0.792f, -1.854f)
                quadToRelative(0.791f, -0.771f, 1.875f, -0.771f)
                horizontalLineTo(33.75f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(22.208f)
                quadToRelative(0f, 1.084f, -0.771f, 1.875f)
                quadToRelative(-0.771f, 0.792f, -1.854f, 0.792f)
                close()
                moveToRelative(0f, -2.667f)
                horizontalLineTo(33.75f)
                verticalLineTo(6.25f)
                horizontalLineTo(11.542f)
                verticalLineToRelative(22.208f)
                close()
                moveTo(6.25f, 36.375f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.771f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.854f)
                verticalLineTo(10.208f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadToRelative(0.917f, -0.396f)
                quadToRelative(0.583f, 0f, 0.958f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                verticalLineTo(33.75f)
                horizontalLineToRelative(23.542f)
                quadToRelative(0.541f, 0f, 0.937f, 0.396f)
                reflectiveQuadToRelative(0.396f, 0.937f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveTo(11.542f, 6.25f)
                verticalLineToRelative(22.208f)
                verticalLineTo(6.25f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberAssignment(color: Color): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "assignment",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(color),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13.125f, 28.167f)
                horizontalLineToRelative(8.625f)
                quadToRelative(0.542f, 0f, 0.917f, -0.396f)
                reflectiveQuadToRelative(0.375f, -0.938f)
                quadToRelative(0f, -0.541f, -0.375f, -0.916f)
                reflectiveQuadToRelative(-0.917f, -0.375f)
                horizontalLineToRelative(-8.625f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.375f)
                quadToRelative(-0.396f, 0.375f, -0.396f, 0.916f)
                quadToRelative(0f, 0.584f, 0.396f, 0.959f)
                quadToRelative(0.395f, 0.375f, 0.937f, 0.375f)
                close()
                moveToRelative(0f, -6.875f)
                horizontalLineToRelative(13.75f)
                quadToRelative(0.542f, 0f, 0.937f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                quadToRelative(0f, -0.583f, -0.396f, -0.958f)
                quadToRelative(-0.395f, -0.375f, -0.937f, -0.375f)
                horizontalLineToRelative(-13.75f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.395f)
                quadToRelative(-0.396f, 0.396f, -0.396f, 0.938f)
                quadToRelative(0f, 0.542f, 0.396f, 0.917f)
                quadToRelative(0.395f, 0.375f, 0.937f, 0.375f)
                close()
                moveToRelative(0f, -6.834f)
                horizontalLineToRelative(13.75f)
                quadToRelative(0.542f, 0f, 0.937f, -0.395f)
                quadToRelative(0.396f, -0.396f, 0.396f, -0.938f)
                quadToRelative(0f, -0.542f, -0.396f, -0.937f)
                quadToRelative(-0.395f, -0.396f, -0.937f, -0.396f)
                horizontalLineToRelative(-13.75f)
                quadToRelative(-0.542f, 0f, -0.937f, 0.396f)
                quadToRelative(-0.396f, 0.395f, -0.396f, 0.937f)
                reflectiveQuadToRelative(0.396f, 0.938f)
                quadToRelative(0.395f, 0.395f, 0.937f, 0.395f)
                close()
                moveToRelative(-5.25f, 17.625f)
                horizontalLineToRelative(24.25f)
                verticalLineTo(7.875f)
                horizontalLineTo(7.875f)
                verticalLineToRelative(24.208f)
                close()
                moveToRelative(0f, -24.208f)
                verticalLineToRelative(24.208f)
                verticalLineTo(7.875f)
                close()
                moveToRelative(0f, 26.833f)
                quadToRelative(-1.083f, 0f, -1.854f, -0.77f)
                quadToRelative(-0.771f, -0.771f, -0.771f, -1.855f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.083f, 0.771f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.854f, -0.771f)
                horizontalLineToRelative(8.083f)
                quadToRelative(0.292f, -1.458f, 1.417f, -2.375f)
                reflectiveQuadTo(20f, 1.958f)
                quadToRelative(1.5f, 0f, 2.625f, 0.917f)
                reflectiveQuadToRelative(1.417f, 2.375f)
                horizontalLineToRelative(8.083f)
                quadToRelative(1.083f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.771f, 0.771f, 1.854f)
                verticalLineToRelative(24.208f)
                quadToRelative(0f, 1.084f, -0.771f, 1.855f)
                quadToRelative(-0.771f, 0.77f, -1.854f, 0.77f)
                close()
                moveTo(20f, 7.042f)
                quadToRelative(0.583f, 0f, 0.979f, -0.396f)
                reflectiveQuadToRelative(0.396f, -0.979f)
                quadToRelative(0f, -0.584f, -0.396f, -0.979f)
                quadToRelative(-0.396f, -0.396f, -0.979f, -0.396f)
                reflectiveQuadToRelative(-0.979f, 0.396f)
                quadToRelative(-0.396f, 0.395f, -0.396f, 0.979f)
                quadToRelative(0f, 0.583f, 0.396f, 0.979f)
                reflectiveQuadToRelative(0.979f, 0.396f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberUpload(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "upload",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 26.792f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineTo(12f)
                lineToRelative(-3.833f, 3.792f)
                quadToRelative(-0.417f, 0.375f, -0.937f, 0.375f)
                quadToRelative(-0.521f, 0f, -0.896f, -0.375f)
                quadToRelative(-0.375f, -0.417f, -0.375f, -0.959f)
                quadToRelative(0f, -0.541f, 0.375f, -0.916f)
                lineToRelative(6.041f, -6.042f)
                quadToRelative(0.209f, -0.208f, 0.438f, -0.292f)
                quadTo(19.75f, 7.5f, 20f, 7.5f)
                quadToRelative(0.25f, 0f, 0.479f, 0.083f)
                quadToRelative(0.229f, 0.084f, 0.438f, 0.292f)
                lineTo(27f, 13.958f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.896f)
                reflectiveQuadToRelative(-0.375f, 0.938f)
                quadToRelative(-0.417f, 0.375f, -0.958f, 0.375f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.417f)
                lineTo(21.333f, 12f)
                verticalLineToRelative(13.458f)
                quadToRelative(0f, 0.584f, -0.395f, 0.959f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveTo(9.542f, 32.958f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.791f)
                quadToRelative(-0.791f, -0.792f, -0.791f, -1.834f)
                verticalLineToRelative(-4.291f)
                quadToRelative(0f, -0.542f, 0.395f, -0.938f)
                quadToRelative(0.396f, -0.396f, 0.938f, -0.396f)
                quadToRelative(0.542f, 0f, 0.917f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.938f)
                verticalLineToRelative(4.291f)
                horizontalLineToRelative(20.916f)
                verticalLineToRelative(-4.291f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.396f, 0.917f, -0.396f)
                quadToRelative(0.583f, 0f, 0.958f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.938f)
                verticalLineToRelative(4.291f)
                quadToRelative(0f, 1.042f, -0.791f, 1.834f)
                quadToRelative(-0.792f, 0.791f, -1.834f, 0.791f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberExitToApp(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "exit_to_app",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16.375f, 26.958f)
                quadToRelative(-0.375f, -0.416f, -0.375f, -1f)
                quadToRelative(0f, -0.583f, 0.375f, -0.958f)
                lineToRelative(3.667f, -3.708f)
                horizontalLineToRelative(-13.5f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadTo(5.208f, 20f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.959f, -0.395f)
                horizontalLineToRelative(13.5f)
                lineToRelative(-3.709f, -3.709f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.937f)
                quadToRelative(0f, -0.563f, 0.417f, -0.979f)
                quadToRelative(0.375f, -0.417f, 0.958f, -0.417f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                lineToRelative(6.041f, 6.083f)
                quadToRelative(0.209f, 0.209f, 0.313f, 0.438f)
                quadToRelative(0.104f, 0.229f, 0.104f, 0.479f)
                quadToRelative(0f, 0.292f, -0.104f, 0.5f)
                quadToRelative(-0.104f, 0.208f, -0.313f, 0.417f)
                lineTo(18.292f, 27f)
                quadToRelative(-0.417f, 0.417f, -0.959f, 0.396f)
                quadToRelative(-0.541f, -0.021f, -0.958f, -0.438f)
                close()
                moveTo(7.833f, 34.75f)
                quadToRelative(-1.041f, 0f, -1.833f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.875f)
                verticalLineToRelative(-6.791f)
                quadToRelative(0f, -0.584f, 0.375f, -0.959f)
                reflectiveQuadToRelative(0.959f, -0.375f)
                quadToRelative(0.541f, 0f, 0.916f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.959f)
                verticalLineToRelative(6.791f)
                horizontalLineToRelative(24.292f)
                verticalLineTo(7.833f)
                horizontalLineTo(7.833f)
                verticalLineToRelative(6.875f)
                quadToRelative(0f, 0.584f, -0.375f, 0.959f)
                reflectiveQuadToRelative(-0.916f, 0.375f)
                quadToRelative(-0.584f, 0f, -0.959f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                verticalLineTo(7.833f)
                quadToRelative(0f, -1.083f, 0.792f, -1.854f)
                quadToRelative(0.792f, -0.771f, 1.833f, -0.771f)
                horizontalLineToRelative(24.292f)
                quadToRelative(1.083f, 0f, 1.875f, 0.771f)
                reflectiveQuadToRelative(0.792f, 1.854f)
                verticalLineToRelative(24.25f)
                quadToRelative(0f, 1.084f, -0.792f, 1.875f)
                quadToRelative(-0.792f, 0.792f, -1.875f, 0.792f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberAddAPhoto(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "add_a_photo",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.125f, 30.667f)
                quadToRelative(3.042f, 0f, 5.063f, -2.042f)
                quadToRelative(2.02f, -2.042f, 2.02f, -5.042f)
                reflectiveQuadToRelative(-2.02f, -5.021f)
                quadToRelative(-2.021f, -2.02f, -5.063f, -2.02f)
                quadToRelative(-3f, 0f, -5f, 2.02f)
                quadToRelative(-2f, 2.021f, -2f, 5.021f)
                reflectiveQuadToRelative(2f, 5.042f)
                quadToRelative(2f, 2.042f, 5f, 2.042f)
                close()
                moveTo(4.417f, 36.583f)
                quadToRelative(-1.084f, 0f, -1.875f, -0.791f)
                quadTo(1.75f, 35f, 1.75f, 33.958f)
                verticalLineToRelative(-20.75f)
                quadToRelative(0f, -1.041f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.875f, -0.792f)
                horizontalLineToRelative(5.833f)
                lineToRelative(2.208f, -2.625f)
                quadToRelative(0.334f, -0.416f, 0.854f, -0.645f)
                quadToRelative(0.521f, -0.23f, 1.146f, -0.23f)
                horizontalLineToRelative(8.584f)
                quadToRelative(0.541f, 0f, 0.937f, 0.375f)
                reflectiveQuadToRelative(0.396f, 0.917f)
                verticalLineToRelative(4.833f)
                horizontalLineTo(4.417f)
                verticalLineToRelative(20.75f)
                horizontalLineToRelative(27.5f)
                verticalLineTo(18.125f)
                horizontalLineToRelative(1.333f)
                quadToRelative(0.25f, 0f, 0.479f, 0.104f)
                quadToRelative(0.229f, 0.104f, 0.417f, 0.292f)
                quadToRelative(0.187f, 0.187f, 0.292f, 0.417f)
                quadToRelative(0.104f, 0.229f, 0.104f, 0.52f)
                verticalLineToRelative(14.5f)
                quadToRelative(0f, 1.042f, -0.792f, 1.834f)
                quadToRelative(-0.792f, 0.791f, -1.833f, 0.791f)
                close()
                moveToRelative(27.5f, -26f)
                horizontalLineTo(29.75f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.395f)
                quadToRelative(-0.375f, -0.396f, -0.375f, -0.938f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                horizontalLineToRelative(2.167f)
                verticalLineTo(5.75f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.916f, -0.375f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                verticalLineToRelative(2.208f)
                horizontalLineToRelative(2.166f)
                quadToRelative(0.584f, 0f, 0.959f, 0.375f)
                reflectiveQuadToRelative(0.375f, 0.917f)
                quadToRelative(0f, 0.583f, -0.375f, 0.958f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                horizontalLineToRelative(-2.166f)
                verticalLineToRelative(2.167f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                close()
                moveToRelative(-27.5f, 2.625f)
                verticalLineToRelative(20.75f)
                verticalLineToRelative(-20.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberAddVideo(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "add_video",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16.792f, 26.542f)
                quadToRelative(0.583f, 0f, 0.958f, -0.375f)
                reflectiveQuadToRelative(0.375f, -0.959f)
                verticalLineTo(21.25f)
                horizontalLineToRelative(3.958f)
                quadToRelative(0.542f, 0f, 0.938f, -0.375f)
                quadToRelative(0.396f, -0.375f, 0.396f, -0.917f)
                quadToRelative(0f, -0.583f, -0.396f, -0.958f)
                reflectiveQuadToRelative(-0.938f, -0.375f)
                horizontalLineToRelative(-3.958f)
                verticalLineToRelative(-3.958f)
                quadToRelative(0f, -0.542f, -0.375f, -0.938f)
                quadToRelative(-0.375f, -0.396f, -0.958f, -0.396f)
                quadToRelative(-0.542f, 0f, -0.917f, 0.396f)
                reflectiveQuadToRelative(-0.375f, 0.938f)
                verticalLineToRelative(3.958f)
                horizontalLineToRelative(-3.958f)
                quadToRelative(-0.584f, 0f, -0.959f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.958f)
                quadToRelative(0f, 0.542f, 0.375f, 0.917f)
                reflectiveQuadToRelative(0.959f, 0.375f)
                horizontalLineTo(15.5f)
                verticalLineToRelative(3.958f)
                quadToRelative(0f, 0.584f, 0.375f, 0.959f)
                reflectiveQuadToRelative(0.917f, 0.375f)
                close()
                moveToRelative(-10.5f, 6.541f)
                quadToRelative(-1.042f, 0f, -1.834f, -0.791f)
                quadToRelative(-0.791f, -0.792f, -0.791f, -1.834f)
                verticalLineTo(9.542f)
                quadToRelative(0f, -1.042f, 0.791f, -1.834f)
                quadToRelative(0.792f, -0.791f, 1.834f, -0.791f)
                horizontalLineToRelative(20.916f)
                quadToRelative(1.042f, 0f, 1.834f, 0.791f)
                quadToRelative(0.791f, 0.792f, 0.791f, 1.834f)
                verticalLineToRelative(8.5f)
                lineToRelative(5.375f, -5.375f)
                quadToRelative(0.334f, -0.334f, 0.75f, -0.167f)
                quadToRelative(0.417f, 0.167f, 0.417f, 0.625f)
                verticalLineToRelative(13.75f)
                quadToRelative(0f, 0.458f, -0.417f, 0.625f)
                quadToRelative(-0.416f, 0.167f, -0.75f, -0.208f)
                lineToRelative(-5.375f, -5.334f)
                verticalLineToRelative(8.5f)
                quadToRelative(0f, 1.042f, -0.791f, 1.834f)
                quadToRelative(-0.792f, 0.791f, -1.834f, 0.791f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(9.542f)
                horizontalLineTo(6.292f)
                verticalLineToRelative(20.916f)
                close()
                moveToRelative(0f, 0f)
                verticalLineTo(9.542f)
                verticalLineToRelative(20.916f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberImage(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "image",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.458f, 28.333f)
                horizontalLineToRelative(17.125f)
                quadToRelative(0.459f, 0f, 0.646f, -0.354f)
                quadToRelative(0.188f, -0.354f, -0.104f, -0.729f)
                lineTo(24.458f, 21f)
                quadToRelative(-0.208f, -0.25f, -0.541f, -0.25f)
                quadToRelative(-0.334f, 0f, -0.542f, 0.25f)
                lineToRelative(-4.75f, 6.208f)
                lineToRelative(-3.25f, -4.375f)
                quadToRelative(-0.208f, -0.25f, -0.521f, -0.25f)
                quadToRelative(-0.312f, 0f, -0.521f, 0.25f)
                lineToRelative(-3.375f, 4.417f)
                quadToRelative(-0.25f, 0.375f, -0.083f, 0.729f)
                quadToRelative(0.167f, 0.354f, 0.583f, 0.354f)
                close()
                moveTo(7.875f, 34.75f)
                quadToRelative(-1.042f, 0f, -1.833f, -0.792f)
                quadToRelative(-0.792f, -0.791f, -0.792f, -1.833f)
                verticalLineTo(7.875f)
                quadToRelative(0f, -1.042f, 0.792f, -1.833f)
                quadToRelative(0.791f, -0.792f, 1.833f, -0.792f)
                horizontalLineToRelative(24.25f)
                quadToRelative(1.042f, 0f, 1.833f, 0.792f)
                quadToRelative(0.792f, 0.791f, 0.792f, 1.833f)
                verticalLineToRelative(24.25f)
                quadToRelative(0f, 1.042f, -0.792f, 1.833f)
                quadToRelative(-0.791f, 0.792f, -1.833f, 0.792f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(24.25f)
                verticalLineTo(7.875f)
                horizontalLineTo(7.875f)
                verticalLineToRelative(24.25f)
                close()
                moveToRelative(0f, -24.25f)
                verticalLineToRelative(24.25f)
                verticalLineToRelative(-24.25f)
                close()
            }
        }.build()
    }
}