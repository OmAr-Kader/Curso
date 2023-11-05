package com.curso.free.global.base

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Color.darker: Color
    get() = Color(ColorUtils.blendARGB(this@darker.toArgb(), Purple40.toArgb(), 0.15F))

@androidx.compose.runtime.Composable
@androidx.compose.runtime.ReadOnlyComposable
fun Color.darker(f: Float = 0.15F): Color = Color(ColorUtils.blendARGB(this@darker.toArgb(), Purple40.toArgb(), f))


@androidx.compose.runtime.Composable
@androidx.compose.runtime.ReadOnlyComposable
fun rateColor(rate: Double): Color = Color(ColorUtils.blendARGB(Color.Yellow.toArgb(), Color.DarkGray.toArgb(), (rate / 5.0).toFloat()))

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.textForPrimaryColor: Color
    get() = if (this) Color.Black else Color.White

@androidx.compose.runtime.Composable
fun Boolean.outlinedTextFieldStyle(): androidx.compose.material3.TextFieldColors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    errorBorderColor = error,
    unfocusedBorderColor = textColor,
    focusedPlaceholderColor = Color.Gray,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
)

@androidx.compose.runtime.Composable
fun Boolean.outlinedDisabledStyle(isError: Boolean): androidx.compose.material3.TextFieldColors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    disabledBorderColor = if (isError) error else  textColor,
    disabledLabelColor = textColor,
    disabledPlaceholderColor = textColor,
    disabledContainerColor = Color.Transparent,
    disabledTextColor = textColor,
    errorBorderColor = error,
    unfocusedBorderColor = textColor,
    focusedPlaceholderColor = Color.Gray,
    focusedTextColor = textColor,
    unfocusedTextColor = textColor,
)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.backDark: Color
    get() = if (this) Color(0xFF1F1F1F) else Color.White

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.backDarkSec: Color
    get() = if (this) Color(0xFF3D3D3D) else Color(0xFFC9C9C9)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.backDarkThr: Color
    get() = if (this) Color(0xFF646464) else Color(0xFFACACAC)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.backGreyTrans: Color
    get() = if (this) Color(0x59555555) else Color(0x59AAAAAA)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.textColor: Color
    get() = if (this) Color.White else Color.Black

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.textGrayColor: Color
    get() = if (this) Color.LightGray else Color.DarkGray

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.error: Color
    get() = if (this) Color(0xFFFF1515) else Color(0xFF9B0000)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val Boolean.textHintColor: Color
    get() = if (this) Color(0xFFAFAFAF) else Color(0xFF505050)

@get:androidx.compose.runtime.Composable
@get:androidx.compose.runtime.ReadOnlyComposable
val shadowColor: Color
    get() = Color(0, 0, 0, 50)

val Purple80: Color = Color(0xFFD0BCFF)
val PurpleGrey80: Color = Color(0xFFCCC2DC)
val Pink80: Color = Color(0xFFEFB8C8)

val Purple40: Color = Color(0xFF6650a4)
val PurpleGrey40: Color = Color(0xFF625b71)
val Pink40: Color = Color(0xFF7D5260)
val Green: Color = Color(0xFF01BD01)
val Blue: Color = Color(0xFF0D17D5)

val DarkGray: Color = Color(0xFF202020)
val LightViolet: Color = Color(0xFFE5D7E8)
val Yellow: Color = Color(0xFFE0E00C)
