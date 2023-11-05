package com.curso.free.ui.lecturer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.curso.free.data.model.TimelineData
import com.curso.free.data.firebase.filePicker
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.CardAnimationButton
import com.curso.free.global.ui.CardButton
import com.curso.free.global.ui.DialogDateTimePicker
import com.curso.free.global.base.Green
import com.curso.free.global.base.IMAGE_SCREEN_ROUTE
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.OutlinedTextFieldButton
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.ui.RadioDialog
import com.curso.free.global.ui.SubTopScreenButton
import com.curso.free.global.ui.UpperNavBar
import com.curso.free.global.base.VIDEO_SCREEN_ROUTE
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.backDarkThr
import com.curso.free.global.base.darker
import com.curso.free.global.base.error
import com.curso.free.global.base.outlinedTextFieldStyle
import com.curso.free.global.ui.rememberAddVideo
import com.curso.free.global.ui.rememberUpload
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textForPrimaryColor
import com.curso.free.global.base.textHintColor
import com.curso.free.global.util.toString
import com.curso.free.global.util.videoImageBuildr
import com.curso.free.ui.PrefViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreateCourseScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: CreateCourseViewModel = hiltViewModel(),
    courseId: String,
    courseTitle: String,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = remember { SnackbarHostState() }
    val state = viewModel.state.value
    val list = remember {
        listOf(
            "Basics",
            "Timelines",
        )
    }
    val scrollState = rememberLazyListState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0F
    ) {
        list.size
    }
    val context = LocalContext.current
    val imagePicker = context.filePicker(true) {
        viewModel.setImageUri(it.toString())
    }
    val videoPicker = context.filePicker(false) {
        viewModel.setBriefVideo(it.toString())
    }

    fun saveOrEdit(isDraft: Boolean, userBase: PrefViewModel.UserBase) {
        if (!isDraft && state.timelines.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add Timeline")
            }
            return
        }
        if (!isDraft && state.briefVideo.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add Brief Video")
            }
            return
        }
        if (!isDraft && state.imageUri.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add Thumbnail Image")
            }
            return
        }
        if (!isDraft && state.about.map { it.text.isEmpty() }.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add About Course")
            }
            return
        }
        if (state.course != null) {
            viewModel.apply {
                context.edit(isDraft, lecturerId = userBase.id, lecturerName = userBase.name) {
                    if (it != null) {
                        scope.launch {
                            navController.navigateUp()
                        }
                    } else {
                        scope.launch {
                            scaffoldState.showSnackbar("Failed")
                        }
                    }
                }
            }
        } else {
            viewModel.apply {
                context.save(isDraft, lecturerId = userBase.id, lecturerName = userBase.name) {
                    if (it != null) {
                        scope.launch {
                            navController.navigateUp()
                        }
                    } else {
                        scope.launch {
                            scaffoldState.showSnackbar("Failed")
                        }
                    }
                }
            }
        }
    }
    OnLaunchScreenScope {
        if (courseId.isNotEmpty()) {
            viewModel.getCourse(courseId)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState) {
                Snackbar(it, containerColor = isSystemInDarkTheme().backDarkSec, contentColor = isSystemInDarkTheme().textColor)
            }
        },
    ) {
        Column(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background.darker())
        ) {
            BriefVideoView(state, videoPicker) {
                prefModel.writeArguments(VIDEO_SCREEN_ROUTE, state.briefVideo, state.courseTitle)
                navController.navigate(VIDEO_SCREEN_ROUTE)
            }
            MainBarInfoView(state, imagePicker) {
                prefModel.writeArguments(IMAGE_SCREEN_ROUTE, state.imageUri, state.courseTitle)
                navController.navigate(IMAGE_SCREEN_ROUTE)
            }
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1F),
            ) {
                PagerTab(
                    pagerState = pagerState,
                    onClick = { index ->
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    list = list,
                ) { page: Int ->
                    when (page) {
                        0 -> BasicsView(viewModel, courseTitle, scrollState) {
                            scope.launch {
                                scrollState.scrollToItem(it)
                            }
                        }

                        else -> TimelinesView(state.timelines, state.course?.isDraft != -1, { i ->
                            viewModel.deleteTimeLine(i)
                        }) { it, i ->
                            viewModel.makeDialogVisible(it, i)
                        }
                    }
                }
            }
            BottomBar(viewModel = viewModel, prefModel = prefModel) { userBase ->
                saveOrEdit(true, userBase)
            }
        }
        if (state.dialogMode != 0) {
            DialogWithImage(
                viewModel,
            ) {
                prefModel.writeArguments(VIDEO_SCREEN_ROUTE, state.timelineData.video, state.timelineData.title)
                navController.navigate(VIDEO_SCREEN_ROUTE)
            }
        }
        if (state.dateTimePickerMode != 0) {
            DialogDateTimePicker(
                dateTime = state.timelineData.date,
                mode = state.dateTimePickerMode,
                changeMode = {
                    viewModel.displayTimePicker()
                },
                snake = {
                    scope.launch {
                        scaffoldState.showSnackbar(it)
                    }
                },
                close = {
                    viewModel.closeDateTimePicker()
                },
            ) { timeSelected ->
                viewModel.confirmTimelineDateTimePicker(timeSelected)
            }
        }
        if (state.isConfirmDialogVisible) {
            DialogForUpload(
                viewModel
            ) {
                viewModel.changeUploadDialogGone(false)
                prefModel.findUserBase { userBase ->
                    saveOrEdit(false, userBase ?: return@findUserBase)
                }
            }
        }
        BackButton {
            navController.navigateUp()
        }
        if (state.course?.isDraft == 1) {
            SubTopScreenButton(imageVector = Icons.Default.Delete) {
                viewModel.deleteCourse {
                    navController.navigateUp()
                }
            }
        }
    }
}


@Composable
fun BriefVideoView(
    state: CreateCourseViewModel.State,
    videoPicker: () -> Unit,
    nav: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(
        model = state.briefVideo,
        imageLoader = LocalContext.current.videoImageBuildr,
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = isSystemInDarkTheme().backDark
    ) {
        if (state.briefVideo.isNotEmpty()) {
            Image(
                contentScale = ContentScale.Fit,
                painter = painter,
                contentDescription = "Video",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x40000000)),
                contentAlignment = Alignment.Center,
            ) {
                Row {
                    IconButton(
                        modifier = Modifier
                            .width(45.dp)
                            .padding(5.dp)
                            .height(45.dp),
                        onClick = {
                            videoPicker.invoke()
                        }
                    ) {
                        Icon(
                            imageVector = rememberUpload(),
                            contentDescription = "Upload",
                            tint = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(
                        modifier = Modifier
                            .width(45.dp)
                            .padding(5.dp)
                            .height(45.dp),
                        onClick = {
                            nav.invoke()
                        }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                        )
                    }
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    modifier = Modifier
                        .width(60.dp)
                        .padding(5.dp)
                        .height(60.dp),
                    onClick = {
                        videoPicker.invoke()
                    }) {
                    Icon(
                        imageVector = rememberAddVideo(),
                        tint = isSystemInDarkTheme().textColor,
                        contentDescription = "AddVideo"
                    )
                }
            }
        }
    }
}

@Composable
fun MainBarInfoView(
    state: CreateCourseViewModel.State,
    imagePicker: () -> Unit,
    nav: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            if (state.imageUri != "") {
                CardButton(
                    width = 120.dp,
                    height = 45.dp,
                    fontSize = 11.sp,
                    onClick = {
                        nav.invoke()
                    },
                    text = "Display Image",
                )
                CardButton(
                    width = 120.dp,
                    height = 45.dp,
                    fontSize = 11.sp,
                    color = Green,
                    textColor = Color.Black,
                    onClick = {
                        imagePicker.invoke()
                    },
                    text = "Re-upload",
                )
            } else {
                CardButton(
                    width = 120.dp,
                    height = 45.dp,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textColor = isSystemInDarkTheme().textForPrimaryColor,
                    onClick = {
                        imagePicker.invoke()
                    },
                    text = "Upload Thumbnail",
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    viewModel: CreateCourseViewModel,
    prefModel: PrefViewModel,
    draftOnClick: (PrefViewModel.UserBase) -> Unit,
) {
    val state = viewModel.state.value
    Row(
        verticalAlignment = Alignment.Bottom, modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (state.course?.isDraft != -1) {
            CardAnimationButton(
                isChoose = true,
                isProcess = state.isDraftProcessing,
                text = "Draft",
                color = isSystemInDarkTheme().error,
                textColor = Color.White,
                onClick = {
                    prefModel.findUserBase { userBase ->
                        draftOnClick.invoke(userBase ?: return@findUserBase)
                    }
                }
            )
            Divider(
                color = Color.Gray, modifier = Modifier
                    .height(30.dp)
                    .width(1.dp)
            )
        }
        CardAnimationButton(
            isChoose = true,
            isProcess = state.isProcessing,
            text = "Upload",
            onClick = {
                viewModel.changeUploadDialogGone(true)
            }
        )
    }
}

@Composable
fun BasicsView(
    viewModel: CreateCourseViewModel,
    courseTitle: String,
    scrollState: LazyListState,
    scrollToEnd: (Int) -> Unit,
) {
    val state = viewModel.state.value

    val isCourseTitleError = state.isErrorPressed && state.courseTitle.isEmpty()
    val isPriceError = state.isErrorPressed && state.price.isEmpty()
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), state = scrollState) {
        item {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.courseTitle.ifEmpty { courseTitle },
                onValueChange = {
                    viewModel.setCourseTitle(it)
                },
                placeholder = { Text(text = "Enter Course Title") },
                label = { Text(text = "Course Title") },
                supportingText = {
                    if (isCourseTitleError) {
                        Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                    }
                },
                isError = isCourseTitleError,
                maxLines = 1,
                colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
        }
        item {
            OutlinedTextField(
                value = state.price,
                onValueChange = {
                    viewModel.setPrice(it)
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                placeholder = { Text(text = "Enter Price") },
                label = { Text(text = "Price") },
                supportingText = {
                    if (isPriceError) {
                        Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                    }
                },
                isError = isPriceError,
                maxLines = 1,
                colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
        itemsIndexed(state.about) { i, (font, text) ->
            val isHeadline = font > 20
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = {
                    viewModel.changeAbout(it, i)
                },
                placeholder = { Text(text = if (isHeadline) "Enter About Headline" else "Enter About Details") },
                label = { Text(text = if (isHeadline) "About Headline" else "About Details") },
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = font.sp),
                colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            //.align(Alignment.CenterVertically)
                            .padding(5.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background.darker(0.3F),
                                shape = CircleShape
                            ),
                        onClick = {
                            if (i == state.about.lastIndex) {
                                viewModel.makeFontDialogVisible()
                                scrollToEnd.invoke(state.about.size + 4)
                            } else {
                                viewModel.removeAboutIndex(i)
                            }
                        }
                    ) {
                        Icon(
                            if (i == state.about.lastIndex) Icons.Filled.Add else Icons.Default.Delete,
                            contentDescription = "",
                        )
                    }
                }
            )
        }
        if (state.isFontDialogVisible) {
            item {
                AboutCreator(
                    viewModel
                )
            }
        }
    }
}

@Composable
fun TimelinesView(
    timelines: List<TimelineData>,
    isDraft: Boolean,
    delete: (Int) -> Unit,
    onclick: (TimelineData?, Int) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onclick.invoke(null, -1)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Timeline", tint = isSystemInDarkTheme().textForPrimaryColor)
            }
        },
        containerColor = MaterialTheme.colorScheme.background.darker
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(timelines) { i, timeline ->
                Row {
                    Column(
                        modifier = Modifier
                            .weight(1F)
                            .clickable {
                                onclick.invoke(timeline, i)
                            }
                    ) {
                        Text(
                            text = timeline.title,
                            color = isSystemInDarkTheme().textColor,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(start = 7.dp, end = 7.dp, top = 7.dp),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            text = "Date: ${timeline.date.toString}",
                            color = isSystemInDarkTheme().textHintColor,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = 14.dp, end = 14.dp, top = 5.dp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        if (timeline.mode == 1) {
                            Row {
                                Text(
                                    text = "Duration: ${timeline.duration}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(start = 14.dp, bottom = 5.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Text(
                                    text = "Degree: ${timeline.degree}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(start = 20.dp, bottom = 5.dp),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                        Divider(color = MaterialTheme.colorScheme.background.darker(0.3F))
                    }
                    if (isDraft) {
                        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                            IconButton(onClick = { delete.invoke(i) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = isSystemInDarkTheme().textColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogWithImage(
    viewModel: CreateCourseViewModel,
    nav: () -> Unit,
) {
    val state = viewModel.state.value
    val list = remember {
        listOf("15 min", "30 min", "45 min", "1 hour", "1.5 hour", "2 hour")
    }
    val isTitleError = state.isDialogPressed && state.timelineData.title.isEmpty()
    val isDateTimeError = state.isDialogPressed && state.timelineData.date == -1L
    val isDurationError = state.isDialogPressed && state.timelineData.duration == ""
    val isErrorVideo = state.isDialogPressed && state.timelineData.video.isEmpty()

    val videoTimelinePicker = LocalContext.current.filePicker(false) {
        viewModel.setVideoTimeLine(it.toString())
    }
    val painter = rememberAsyncImagePainter(
        model = state.timelineData.video,
        imageLoader = LocalContext.current.videoImageBuildr,
    )
    Dialog(
        onDismissRequest = {
            viewModel.makeDialogGone()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = isSystemInDarkTheme().backDark)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.dialogMode == 1) {
                    UpperNavBar(list = listOf("Session", "Exam"), if (state.timelineData.isExam) 1 else 0) {
                        viewModel.setIsExam(it == 1)
                    }
                }
                if (!state.timelineData.isExam) {
                    if (state.timelineData.video.isEmpty()) {
                        Button(
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = isSystemInDarkTheme().textColor,
                            ),
                            onClick = {
                                videoTimelinePicker.invoke()
                            },
                            /*shape = RoundedCornerShape(3.dp),
                            border = BorderStroke(3.dp, MaterialTheme.colorScheme.primary),*/
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(10.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isErrorVideo) isSystemInDarkTheme().error else isSystemInDarkTheme().textColor,
                                    shape = RoundedCornerShape(5.dp)
                                )
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(text = "Session Video", fontSize = 12.sp)
                            }
                        }
                    } else {
                        Image(
                            contentScale = ContentScale.Fit,
                            painter = painter,
                            contentDescription = "Video",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable {
                                    nav.invoke()
                                }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = state.timelineData.title,
                        onValueChange = {
                            viewModel.setTitleTimeline(it)
                        },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        placeholder = { Text(text = "Enter Timeline Title") },
                        label = { Text(text = "Timeline Title") },
                        supportingText = {
                            if (isTitleError) {
                                Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                            }
                        },
                        isError = isTitleError,
                        maxLines = 1,
                        colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )
                    OutlinedTextFieldButton(
                        text = if (state.timelineData.date == -1L) "" else state.timelineData.date.toString,
                        placeholder = "Enter Date",
                        label = "Date",
                        isError = isDateTimeError
                    ) {
                        viewModel.displayDateTimePicker()
                    }
                    AnimatedVisibility(
                        visible = state.timelineData.isExam,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        Column(
                            modifier = Modifier.background(isSystemInDarkTheme().backDark)
                        ) {
                            OutlinedTextFieldButton(
                                text = state.timelineData.duration,
                                placeholder = "Enter Exam Duration",
                                label = "Exam Duration",
                                isError = isDurationError
                            ) {
                                viewModel.setDurationDialogVisible(true)
                            }
                            if (state.isDurationDialogVisible) {
                                RadioDialog(current = state.timelineData.duration, list = list, {
                                    viewModel.setDurationDialogVisible(false)
                                }) {
                                    viewModel.setDurationTimeLine(it)
                                }
                            }
                            OutlinedTextField(
                                value = state.timelineData.degree.toString(),
                                onValueChange = {
                                    viewModel.setDegreeTimeline(it.toIntOrNull() ?: return@OutlinedTextField)
                                },
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                placeholder = { Text(text = "Enter Exam Degree") },
                                label = { Text(text = "Exam Degree") },
                                maxLines = 1,
                                colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = state.timelineData.note,
                    onValueChange = {
                        viewModel.setTimelineNote(it)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    placeholder = { Text(text = "Enter Note") },
                    label = { Text(text = "Note") },
                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            viewModel.makeDialogGone()
                        },
                        modifier = Modifier.padding(5.dp),
                    ) {
                        Text("Dismiss", color = isSystemInDarkTheme().textColor)
                    }
                    TextButton(
                        onClick = {
                            viewModel.addEditTimeline()
                        },
                        modifier = Modifier.padding(5.dp),
                    ) {
                        Text("Confirm", color = isSystemInDarkTheme().textColor)
                    }
                }
            }
        }
    }
}


@Composable
fun DialogForUpload(
    viewModel: CreateCourseViewModel,
    invoke: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            viewModel.changeUploadDialogGone(false)
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = isSystemInDarkTheme().backDark)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "By confirm upload your course you haven't ability to delete your course or any timeline",
                    modifier = Modifier.padding(20.dp),
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            viewModel.changeUploadDialogGone(false)
                        },
                        modifier = Modifier.padding(5.dp),
                    ) {
                        Text("Dismiss", color = isSystemInDarkTheme().textColor)
                    }
                    TextButton(
                        onClick = {
                            invoke.invoke()
                        },
                        modifier = Modifier.padding(5.dp),
                    ) {
                        Text("Confirm", color = isSystemInDarkTheme().textColor)
                    }
                }
            }
        }
    }
}

@Composable
fun AboutCreator(
    viewModel: CreateCourseViewModel,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .widthIn(0.dp, 300.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(20.dp),
            color = isSystemInDarkTheme().backDarkThr,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                TextButton(
                    onClick = {
                        viewModel.addAbout(0)
                    },
                    modifier = Modifier.padding(5.dp),
                ) {
                    Text("Small Font", color = isSystemInDarkTheme().textColor)
                }
                Divider(
                    color = Color.Gray, modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                )
                TextButton(
                    onClick = {
                        viewModel.addAbout(1)
                    },
                    modifier = Modifier.padding(5.dp),
                ) {
                    Text("Big Font", color = isSystemInDarkTheme().textColor)
                }
            }
        }
    }
}
