package com.curso.free.ui.lecturer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_MODE_NONE
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.Green
import com.curso.free.global.base.PREF_USER_ID
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.textColor
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.CardButton
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.ui.ProfileItems
import com.curso.free.global.ui.TextFullPageScrollable
import com.curso.free.global.ui.rememberVideoLibrary
import com.curso.free.global.util.imageBuildr
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.HomeAllArticlesView
import com.curso.free.ui.common.LecturerCoursesView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LecturerScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: LectureViewModel = hiltViewModel(),
    lecturerId: String,
    lecturerName: String,
    mode: Int, // STUDENT = 1,
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    val scaffoldState = remember { SnackbarHostState() }
    OnLaunchScreenScope {
        prefModel.findPrefString(PREF_USER_ID) { id ->
            id ?: return@findPrefString
            viewModel.fetchLecturer(lecturerId = lecturerId, studentId = id)
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            //LoadingBar(state.isLoading)
            //Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = LocalContext.current.imageBuildr(state.lecturer.imageUri),
                    success = { (painter, _) ->
                        Image(
                            contentScale = ContentScale.Crop,
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
            Text(
                text = state.lecturer.lecturerName.ifEmpty {
                    lecturerName
                },
                color = isSystemInDarkTheme().textColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            if (mode == 1) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    CardButton(
                        onClick = {
                            if (state.studentId.isEmpty()) {
                                scope.launch {
                                    scaffoldState.showSnackbar(message = "Failed")
                                }
                                return@CardButton
                            }
                            viewModel.followUnfollow(state.studentId, state.alreadyFollowed, {
                                scope.launch {
                                    scaffoldState.showSnackbar(message = "Done")
                                }
                            }) {
                                scope.launch {
                                    scaffoldState.showSnackbar(message = "Failed")
                                }
                            }
                        },
                        color = if (state.alreadyFollowed) Green else MaterialTheme.colorScheme.primary,
                        text = if (state.alreadyFollowed) "Unfollow" else "Follow",
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ProfileItems(
                    icon = rememberVideoLibrary(color = Color.Blue),
                    color = Color.Blue,
                    title = "Courses",
                    numbers = state.courses.size.toString(),
                )
                ProfileItems(
                    icon = Icons.Default.Person,
                    color = Color.Green,
                    title = "Followers",
                    numbers = viewModel.lecturerFollowers(mode),
                )
                ProfileItems(
                    icon = Icons.Default.Star,
                    color = Color.Yellow,
                    title = "Rate",
                    numbers = viewModel.lecturerRate,
                )
            }
            PagerTab(
                pagerState = pagerState,
                onClick = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                list = listOf(
                    "About",
                    "Courses",
                    "Articles",
                ),
            ) { page: Int ->
                when (page) {
                    0 -> TextFullPageScrollable(state.lecturer.brief)
                    1 -> LecturerCoursesView(state.courses) { course ->
                        prefModel.writeArguments(
                            COURSE_SCREEN_ROUTE,
                            course.id,
                            course.title,
                            if (mode == 1) COURSE_MODE_STUDENT else COURSE_MODE_NONE,
                            course
                        )
                        navController.navigate(COURSE_SCREEN_ROUTE)
                    }

                    else -> {
                        HomeAllArticlesView(
                            articles = state.articles,
                        ) { article ->
                            prefModel.writeArguments(
                                ARTICLE_SCREEN_ROUTE,
                                article.id, article.title,
                                if (mode == 1) COURSE_MODE_STUDENT else COURSE_MODE_NONE,
                                article
                            )
                            navController.navigate(ARTICLE_SCREEN_ROUTE)
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
