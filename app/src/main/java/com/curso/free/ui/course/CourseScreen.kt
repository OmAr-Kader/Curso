package com.curso.free.ui.course

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.curso.free.data.model.AboutCourseData
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.data.util.logger
import com.curso.free.global.base.Blue
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.COURSE_MODE_NONE
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.CREATE_COURSE_SCREEN_ROUTE
import com.curso.free.global.base.Green
import com.curso.free.global.base.LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.PREF_USER_COURSES
import com.curso.free.global.base.PurpleGrey40
import com.curso.free.global.base.TIMELINE_SCREEN_ROUTE
import com.curso.free.global.base.VIDEO_SCREEN_ROUTE
import com.curso.free.global.base.Yellow
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.darker
import com.curso.free.global.base.shadowColor
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textHintColor
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.CardButton
import com.curso.free.global.ui.IconText
import com.curso.free.global.ui.LoadingBar
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.util.toString
import com.curso.free.global.util.videoImageBuildr
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.ChatView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseScreen(
    navController: NavController,
    viewModel: CourseViewModel = hiltViewModel(),
    prefModel: PrefViewModel,
    courseId: String,
    courseTitle: String,
    mode: Int,
    course: com.curso.free.data.model.CourseForData?,
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val list = remember {
        if (mode != COURSE_MODE_NONE) {
            listOf(
                "Timeline",
                "About",
                "Chat"
            )
        } else {
            listOf(
                "Timeline",
                "About",
            )
        }
    }
    val scaffoldState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f
    ) {
        list.size
    }
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = state.course.briefVideo,
        imageLoader = context.videoImageBuildr,
    )
    LaunchedEffect(pagerState) {
        if (mode != COURSE_MODE_NONE) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                viewModel.changeFocus(page == 2)
            }
        }
    }
    OnLaunchScreenScope {
        prefModel.findUserBase { userBase ->
            userBase ?: return@findUserBase
            when (mode) {
                COURSE_MODE_STUDENT -> {
                    viewModel.getCourse(course, courseId, userBase.id, userBase.name)
                    viewModel.getMainConversation(courseId)
                    logger("WWW", courseId)
                }

                COURSE_MODE_LECTURER -> {
                    viewModel.getCourseLecturer(course, courseId, userBase.id, userBase.name)
                    viewModel.getMainConversation(courseId)
                }

                else -> {
                    viewModel.getCourseLecturer(course, courseId, userBase.id, userBase.name)
                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState) {
                Snackbar(it, containerColor = isSystemInDarkTheme().backDarkSec, contentColor = isSystemInDarkTheme().textColor)
            }
        },
    ) {
        Column {
            LoadingBar(state.isLoading, true)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        prefModel.writeArguments(VIDEO_SCREEN_ROUTE, state.course.briefVideo, state.course.title)
                        navController.navigate(VIDEO_SCREEN_ROUTE)
                    },
                color = isSystemInDarkTheme().backDark
            ) {
                Image(
                    contentScale = ContentScale.Fit,
                    painter = painter,
                    contentDescription = "Video",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = PurpleGrey40,
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.course.title.ifEmpty { courseTitle },
                    color = isSystemInDarkTheme().textColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(top = 5.dp, start = 5.dp, end = 5.dp)
                )
                if (mode != COURSE_MODE_NONE) {
                    CardButton(
                        onClick = {
                            if (mode == COURSE_MODE_LECTURER) {
                                prefModel.writeArguments(CREATE_COURSE_SCREEN_ROUTE, state.course.id, state.course.title)
                                navController.navigate(CREATE_COURSE_SCREEN_ROUTE)
                            } else if (mode == COURSE_MODE_STUDENT && !state.alreadyEnrolled) {
                                prefModel.findUserBase {
                                    if (it == null) {
                                        scope.launch {
                                            scaffoldState.showSnackbar(message = "Failed")
                                        }
                                        return@findUserBase
                                    }
                                    viewModel.enroll(it.id, it.name, {
                                        prefModel.updatePref(PREF_USER_COURSES, (it.courses + 1).toString())
                                        scope.launch {
                                            scaffoldState.showSnackbar(message = "Done")
                                        }
                                    }) {
                                        scope.launch {
                                            scaffoldState.showSnackbar(message = "Failed")
                                        }
                                    }
                                }
                            }
                        },
                        color = if (!state.alreadyEnrolled) MaterialTheme.colorScheme.primary else Green,
                        text = if (mode == COURSE_MODE_LECTURER) {
                            "Edit"
                        } else if (!state.alreadyEnrolled) "Enroll Now" else "Enrolled",
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .padding(end = 5.dp, start = 3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = shadowColor),
                onClick = {
                    prefModel.writeArguments(LECTURER_SCREEN_ROUTE, state.course.lecturerId, state.course.lecturerName)
                    navController.navigate(LECTURER_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = state.course.lecturerName,
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Row {
                IconText(
                    modifier = Modifier
                        .padding(end = 5.dp, start = 3.dp)
                        .weight(1F),
                    imageVector = Icons.Default.Person,
                    text = state.course.students.size.toString(),
                    tint = Blue,
                )
                IconText(
                    modifier = Modifier
                        .padding(end = 5.dp, start = 3.dp)
                        .weight(1F),
                    imageVector = Icons.Default.Star,
                    text = viewModel.courseRate,
                    tint = Yellow,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
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
                    0 -> TimeLineView(state, mode) {
                        scope.launch {
                            prefModel.writeArguments(TIMELINE_SCREEN_ROUTE, "", "", obj = it)
                            navController.navigate(route = TIMELINE_SCREEN_ROUTE)
                        }
                    }

                    1 -> AboutScrollable(state.course.about)
                    else -> ChatView(
                        isEnabled = state.textFieldFocus,
                        chatText = state.chatText,
                        onTextChanged = viewModel.changeChatText,
                        list = state.conversation?.messages ?: listOf(),
                        isUserMessage = {
                            it.senderId == state.userId
                        }
                    ) {
                        viewModel.apply {
                            prefModel.findUserBase { userBase ->
                                userBase ?: return@findUserBase
                                context.send(mode = mode, id = userBase.id, name = userBase.name) {
                                    scope.launch {
                                        scaffoldState.showSnackbar(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        BackButton {
            navController.navigateUp()
        }
    }
}


@Composable
fun AboutScrollable(
    about: List<AboutCourseData>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = buildAnnotatedString {
                about.forEach {
                    withStyle(style = SpanStyle(fontSize = it.font.sp)) {
                        append(it.text)
                        append("\n")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp, start = 20.dp)
                .verticalScroll(rememberScrollState()),
            color = isSystemInDarkTheme().textColor,
            //fontSize = 14.sp,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun TimeLineView(
    state: CourseViewModel.State,
    mode: Int,
    nav: (SessionForDisplay) -> Unit,
) {
    val course = state.course
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(course.timelines) { i, timeline ->
            Column(
                Modifier.clickable {
                    SessionForDisplay(course, timeline, mode, state.userId, state.userName, i).let { one ->
                        nav.invoke(one)
                    }
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
                    fontSize = 14.sp,
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
        }
    }
}
