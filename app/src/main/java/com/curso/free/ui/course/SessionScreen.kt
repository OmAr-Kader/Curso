package com.curso.free.ui.course

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.PurpleGrey40
import com.curso.free.global.base.VIDEO_SCREEN_ROUTE
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.shadowColor
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textGrayColor
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.ui.TextFullPageScrollable
import com.curso.free.global.util.videoImageBuildr
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.ChatView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SessionScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: SessionViewModel = hiltViewModel(),
    session: SessionForDisplay,
) {
    val userId = remember {
        if (session.mode == 0) session.studentId else session.lecturerId
    }
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = session.video,
        imageLoader = context.videoImageBuildr,
    )
    val scaffoldState = remember { SnackbarHostState() }
    val list = remember {
        listOf(
            "About",
            "Chat",
        )
    }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        list.size
    }
    OnLaunchScreenScope {
        viewModel.getTimelineConversation(session.courseId, session.timelineIndex)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.changeFocus(page == 1)
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
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clickable {
                        prefModel.writeArguments(VIDEO_SCREEN_ROUTE, session.video, session.title)
                        navController.navigate(
                            VIDEO_SCREEN_ROUTE
                        )
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
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = session.title,
                color = isSystemInDarkTheme().textColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = session.courseName,
                color = isSystemInDarkTheme().textGrayColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
                    .clickable {
                        prefModel.writeArguments(
                            COURSE_SCREEN_ROUTE,
                            session.courseId,
                            session.courseName,
                            if (session.mode == 0) COURSE_MODE_STUDENT else COURSE_MODE_LECTURER,
                        )
                        navController.navigate(COURSE_SCREEN_ROUTE)
                    }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .padding(end = 5.dp, start = 3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = shadowColor),
                onClick = {
                    prefModel.writeArguments(LECTURER_SCREEN_ROUTE, session.lecturerId, session.lecturerName)
                    navController.navigate(LECTURER_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = session.lecturerName,
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
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
                    0 -> TextFullPageScrollable(session.note)
                    else -> ChatView(
                        isEnabled = state.textFieldFocus,
                        chatText = state.chatText,
                        onTextChanged = viewModel.changeChatText,
                        list = state.conversation?.messages ?: mutableListOf(),
                        isUserMessage = {
                            it.senderId == userId
                        }
                    ) {
                        viewModel.apply {
                            context.send(sessionForDisplay = session) {
                                scope.launch {
                                    scaffoldState.showSnackbar(it)
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