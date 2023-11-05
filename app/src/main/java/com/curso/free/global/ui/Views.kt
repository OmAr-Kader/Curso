package com.curso.free.global.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.curso.free.data.util.loggerError
import com.curso.free.global.base.darker
import com.curso.free.global.base.error
import com.curso.free.global.base.outlinedDisabledStyle
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textForPrimaryColor
import com.curso.free.global.base.textHintColor
import com.curso.free.global.util.fetchHour
import com.curso.free.global.util.fetchMinute
import com.curso.free.global.util.fetchTimeFromCalender
import com.curso.free.global.util.imageBuildr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardButton(
    onClick: (() -> Unit),
    text: String,
    width: Dp = 80.dp,
    height: Dp = 30.dp,
    curve: Dp = height / 2,
    fontSize: TextUnit = 11.sp,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = isSystemInDarkTheme().textForPrimaryColor
) {
    Card(
        modifier = Modifier
            .width(width)
            .height(height)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        shape = RoundedCornerShape(curve),
        onClick = onClick,
        colors = CardDefaults.cardColors(color),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                //fontWeight = FontWeight.Thin,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerTab(
    pagerState: PagerState,
    onClick: (page: Int) -> Unit,
    list: List<String>,
    color: Color = MaterialTheme.colorScheme.background.darker,
    shape: Shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = color,
        shape = shape
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Column(
            Modifier
                .fillMaxSize()
        ) {
            TabRow(
                modifier = Modifier.fillMaxWidth(),
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color(0),
                contentColor = Color(0),
                indicator = {},
                divider = {},
            ) {
                list.forEachIndexed { index, title ->
                    val animated = animateIntAsState(
                        targetValue = if (pagerState.currentPage == index) 10 else 40,
                        label = "round"
                    )
                    val animatedSize = animateIntAsState(
                        targetValue = if (pagerState.currentPage == index) 90 else 70,
                        label = "size"
                    )
                    Tab(
                        text = {
                            Card(
                                modifier = Modifier
                                    .width(animatedSize.value.dp)
                                    .height(animatedSize.value.dp / 2)
                                    .padding(start = 5.dp, end = 5.dp),
                                shape = RoundedCornerShape(animated.value.dp),
                                colors = CardDefaults.cardColors(if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary), //MaterialTheme.colorScheme.secondary
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = title,
                                        color = isSystemInDarkTheme().textForPrimaryColor,
                                        fontSize = animatedSize.value.sp / 8,
                                        //fontWeight = FontWeight.Thin,
                                        style = MaterialTheme.typography.titleSmall,
                                        maxLines = 1,
                                    )
                                }
                            }
                        },
                        selected = pagerState.currentPage == index,
                        selectedContentColor = Color(0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        onClick = {
                            onClick.invoke(index)
                        },
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight(),
                pageContent = pageContent,
            )
        }
    }
}

@Composable
fun ProfileItems(
    icon: ImageVector,
    color: Color,
    title: String,
    numbers: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(10.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = color,
            contentDescription = title,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
        )
        Text(
            text = title,
            color = isSystemInDarkTheme().textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 10.sp,
            modifier = Modifier
                .padding(3.dp)
        )
        Text(
            text = numbers,
            color = isSystemInDarkTheme().textHintColor,
            style = MaterialTheme.typography.bodySmall,
            fontSize = 10.sp,
            modifier = Modifier
                .padding(3.dp)
        )
    }
}

@Composable
fun UpperNavBar(
    list: List<String>,
    currentIndex: Int,
    listState: LazyListState = rememberLazyListState(),
    onClick: (index: Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        contentPadding = PaddingValues(top = 5.dp, bottom = 5.dp),
        state = listState,
    ) {
        itemsIndexed(list) { i, it ->
            val containerColor = animateColorAsState(
                if (currentIndex == i) MaterialTheme.colorScheme.primary else Color.Transparent, label = "containerColor",
                animationSpec = tween(500, easing = LinearEasing),
            )
            val contentColor = animateColorAsState(
                if (currentIndex == i) isSystemInDarkTheme().textForPrimaryColor else isSystemInDarkTheme().textColor, label = "contentColor",
            )
            Box(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
            ) {
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = containerColor.value,
                        contentColor = contentColor.value,
                    ),
                    border = BorderStroke(
                        width = 1.0.dp,
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {
                        onClick(i)
                    },
                ) {
                    Text(text = it, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun BackButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .width(56.dp)
            .height(56.dp)
            .padding(start = 20.dp, top = 20.dp),
        color = Color.Transparent,
    ) {
        Card(
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    onClick.invoke()
                },
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 10.dp, focusedElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = isSystemInDarkTheme().textForPrimaryColor
                )
            }
        }
    }
}


@Composable
fun SubTopScreenButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(top = 20.dp, end = 20.dp),
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Card(
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    onClick.invoke()
                },
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, pressedElevation = 10.dp, focusedElevation = 10.dp),
            colors = CardDefaults.cardColors(containerColor = isSystemInDarkTheme().error),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = "SubTopScreenButton",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CardAnimationButton(
    isChoose: Boolean,
    isProcess: Boolean,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = isSystemInDarkTheme().textForPrimaryColor,
    onClick: () -> Unit,
) {
    val animated = animateIntAsState(
        targetValue = if (isChoose) 10 else 40,
        label = "round"
    )
    val animatedSize = animateIntAsState(
        targetValue = if (isChoose) 100 else 80,
        label = "size"
    )
    val c: Color = if (isChoose) {
        color
    } else {
        if (isProcess) {
            Color.Gray
        } else {
            MaterialTheme.colorScheme.secondary
        }
    }
    Card(
        modifier = Modifier
            .width(animatedSize.value.dp)
            .height(animatedSize.value.dp / 2)
            .padding(horizontal = 5.dp)
            .clickable {
                onClick.invoke()
            },
        shape = RoundedCornerShape(animated.value.dp),
        colors = CardDefaults.cardColors(c),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (!isChoose || !isProcess) {
                Text(
                    text = text,
                    color = textColor,
                    fontSize = animatedSize.value.sp / 9,
                    //fontWeight = FontWeight.Thin,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize().padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun OnLaunchScreen(invoke: () -> Unit) {
    val isLaunched = remember {
        mutableStateOf(false)
    }
    if (!isLaunched.value) {
        isLaunched.value = true
        invoke.invoke()
    }
}

@Composable
fun OnLaunchScreenScope(invoke: suspend kotlinx.coroutines.CoroutineScope.() -> Unit) {
    val isLaunched = remember {
        mutableStateOf(false)
    }
    if (!isLaunched.value) {
        isLaunched.value = true
        LaunchedEffect(key1 = isLaunched, block = invoke)
    }
}

@Composable
fun <D> ListBody(
    list: List<D>,
    bodyClick: (D) -> Unit,
    additionalItem: (@Composable () -> Unit)? = null,
    content: @Composable (D) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (additionalItem != null) {
            item {
                additionalItem()
            }
        }
        items(list) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(5.dp)
                    .clickable {
                        bodyClick.invoke(it)
                    },
                shape = RoundedCornerShape(
                    size = 15.dp
                ),
            ) {
                content(it)
            }
        }
    }
}

@Composable
fun <D> ListBodyEdit(
    list: List<D>,
    additionalItem: (@Composable () -> Unit)? = null,
    itemColor: Color = Color.Transparent,
    content: @Composable (D) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (additionalItem != null) {
            item {
                additionalItem()
            }
        }
        items(list) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(
                    size = 15.dp
                ),
                color = itemColor
            ) {
                content(it)
            }
        }
    }
}

@Composable
fun ImageForCurveItem(
    imageUri: String,
    size: Dp,
) {
    Box(
        modifier = Modifier.size(size)
    ) {
        coil.compose.SubcomposeAsyncImage(
            model = LocalContext.current.imageBuildr(imageUri),
            success = { (painter, _) ->
                Image(
                    contentScale = ContentScale.Crop,
                    painter = painter,
                    contentDescription = "Image",
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                topEnd = 15.dp,
                                bottomEnd = 15.dp
                            )
                        )
                        .fillMaxSize()
                )
            },
            error = {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "aa")
            },
            onError = {
                loggerError("AsyncImagePainter.Error", it.result.throwable.stackTraceToString())
            },
            contentScale = ContentScale.Crop,
            filterQuality = FilterQuality.None,
            contentDescription = "Image"
        )
    }
}

@Composable
fun BoxScope.EditButton(
    color: Color,
    textColor: Color,
    sideButtonClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(40.dp)
            .align(Alignment.CenterEnd)
            .background(color)
            .clickable {
                sideButtonClicked.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = textColor
        )
    }
}

@Composable
fun OutlinedTextFieldButton(
    text: String,
    placeholder: String,
    label: String,
    isError: Boolean,
    onClick: () -> Unit
) {
    /*Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                width = 1.dp,
                color = isSystemInDarkTheme().let { if (isError) it.error else it.textColor },
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = isSystemInDarkTheme().textColor,
            modifier = Modifier.padding(
                start = 10.dp,
                top = 10.dp,
                end = 24.dp,
                bottom = 10.dp
            )
        )
    }*/
    OutlinedTextField(
        value = text,
        onValueChange = {

        },
        enabled = false,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        supportingText = {
            if (isError) {
                Text(text = placeholder, color = isSystemInDarkTheme().error, fontSize = 10.sp)
            }
        },
        isError = isError,
        maxLines = 1,
        colors = isSystemInDarkTheme().outlinedDisabledStyle(isError),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )
}

@Composable
fun RadioDialog(
    current: String,
    list: List<String>,
    onDismissRequest: () -> Unit,
    onClick: (String) -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            list.onEach {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (it == current),
                                onClick = {
                                    onClick.invoke(it)
                                }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (it == current),
                            onClick = { onClick.invoke(it) }
                        )
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleSmall.merge(),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IconText(
    modifier: Modifier,
    text: String,
    imageVector: ImageVector,
    tint: Color,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = text,
            tint = tint,
            modifier = Modifier
                .width(25.dp)
                .height(25.dp)
        )
        Text(
            text = text,
            color = isSystemInDarkTheme().textColor,
            fontSize = 14.sp,
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun LoadingBar(isLoading: Boolean, subScreen: Boolean = true) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
    ) {
        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                trackColor = Color.Transparent,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (subScreen) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .height(3.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDateTimePicker(
    dateTime: Long,
    mode: Int,
    changeMode: () -> Unit,
    snake: (String) -> Unit,
    close: () -> Unit,
    invoke: (Long) -> Unit,
) {
    val current = remember {
        System.currentTimeMillis()
    }
    val time = remember {
        if (dateTime != -1L) dateTime else current
    }
    val statePicker = rememberDatePickerState(time)
    val stateTimePicker = rememberTimePickerState(initialHour = time.fetchHour, initialMinute = time.fetchMinute, is24Hour = true)

    DatePickerDialog(
        onDismissRequest = {
            close.invoke()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (mode == 1) {
                        changeMode.invoke()
                    } else {
                        statePicker.selectedDateMillis ?: return@TextButton
                        val timeSelected = statePicker.selectedDateMillis?.fetchTimeFromCalender?.invoke(
                            stateTimePicker.hour,
                            stateTimePicker.minute
                        ) ?: return@TextButton
                        if (timeSelected < current) {
                            snake.invoke("Invalid Date")
                            return@TextButton
                        }
                        invoke.invoke(timeSelected)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    close.invoke()
                }
            ) {
                Text("CANCEL")
            }
        }
    ) {
        if (mode == 1) {
            DatePicker(
                state = statePicker,
                title = {

                },
                dateValidator = {
                    it > current
                },
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = isSystemInDarkTheme().textForPrimaryColor,
                    weekdayContentColor = isSystemInDarkTheme().textColor,
                    dayContentColor = isSystemInDarkTheme().textColor,
                    disabledDayContentColor = Color.Gray
                )
            )
        } else if (mode == 2) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                contentAlignment = Alignment.Center,
            ) {
                TimePicker(
                    state = stateTimePicker,
                    colors = TimePickerDefaults.colors(
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorSelectedContentColor = isSystemInDarkTheme().textForPrimaryColor,
                        timeSelectorUnselectedContentColor = isSystemInDarkTheme().textColor,
                        clockDialSelectedContentColor = isSystemInDarkTheme().textColor,
                        clockDialUnselectedContentColor = isSystemInDarkTheme().textColor
                    )
                )
            }
        }
    }
}

@Composable
fun TextFullPageScrollable(
    brief: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = brief,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 20.dp)
                .verticalScroll(rememberScrollState()),
            color = isSystemInDarkTheme().textColor,
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}