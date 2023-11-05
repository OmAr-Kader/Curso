package com.curso.free.ui.student

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.STUDENT_SCREEN_ROUTE
import com.curso.free.global.base.TIMELINE_SCREEN_ROUTE
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.shadowColor
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textForPrimaryColor
import com.curso.free.global.ui.ListBody
import com.curso.free.global.ui.LoadingBar
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.UpperNavBar
import com.curso.free.global.ui.rememberExitToApp
import com.curso.free.global.util.firstSpace
import com.curso.free.global.util.imageBuildr
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.HomeAllArticlesView
import com.curso.free.ui.common.HomeAllCoursesView
import com.curso.free.ui.common.HomeOwnCoursesView
import com.curso.free.ui.common.MainItem
import com.curso.free.ui.common.TimelineItem
import kotlinx.coroutines.launch

val list: List<String> = listOf(
    "https://fellow.app/wp-content/uploads/2021/07/2-9.jpg",
    "https://www.regus.com/work-us/wp-content/uploads/sites/18/2019/11/shutterstock_633364835_How-to-open-meeting-with-impact_resize-to-1024px-x-400px-landscape.jpg",
    "https://d3njjcbhbojbot.cloudfront.net/api/utilities/v1/imageproxy/https://coursera-course-photos.s3.amazonaws.com/da/f86c90ae6211e5907fe98be3e612d3/2_meetings.jpg?auto=format%2Ccompress&dpr=1",
    "https://f.hubspotusercontent40.net/hubfs/4592742/shutterstock_1705576870%20%281%29.jpg",
)

@Composable
fun HomeScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
    userId: String,
    userName: String,
    coursesNumber: Int,
) {
    val scaffoldState = remember { SnackbarHostState() }
    val state = viewModel.state.value
    val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val animated = animateDpAsState(
        targetValue = if (state.isLoading) 0.dp else 20.dp,
        label = "roundCorner"
    )
    val listState = rememberLazyListState()
    OnLaunchScreenScope {
        if (coursesNumber == 0) {
            viewModel.updateCurrentTabIndex(5)
            scope.launch {
                listState.scrollToItem(5)
            }
            viewModel.allCourses()
        } else {
            viewModel.getCoursesForStudent(userId)
        }
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight(),
                drawerTonalElevation = 6.dp,
                drawerContainerColor = isSystemInDarkTheme().backDark
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            56.0.dp//NavigationDrawerTokens.ActiveIndicatorHeight
                        ),
                    color = MaterialTheme.colorScheme.primary,
                ) {
                    Row(
                        Modifier.padding(start = 16.dp, end = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Curso",
                            color = isSystemInDarkTheme().textForPrimaryColor
                        )
                    }
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "Profile") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = isSystemInDarkTheme().backDark,
                        selectedContainerColor = isSystemInDarkTheme().backDark,
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Profile"
                        )
                    },
                    selected = false,
                    onClick = {
                        prefModel.writeArguments(STUDENT_SCREEN_ROUTE, userId, userName)
                        navController.navigate(STUDENT_SCREEN_ROUTE)
                    }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Sign out") },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = isSystemInDarkTheme().backDark,
                        selectedContainerColor = isSystemInDarkTheme().backDark,
                    ),
                    icon = {
                        Icon(
                            imageVector = rememberExitToApp(),
                            modifier = Modifier.size(25.dp),
                            contentDescription = "Sign out"
                        )
                    },
                    selected = false,
                    onClick = {
                        prefModel.signOut({
                            navController.navigateUp()
                        }) {
                            scope.launch {
                                scaffoldState.showSnackbar(message = "Failed")
                            }
                        }
                    }
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(scaffoldState) {
                    Snackbar(it, containerColor = isSystemInDarkTheme().backDarkSec, contentColor = isSystemInDarkTheme().textColor)
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomStart = animated.value,
                                bottomEnd = animated.value,
                                topEnd = 0.dp,
                                topStart = 0.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                modifier = Modifier.padding(start = 10.dp),
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = isSystemInDarkTheme().textForPrimaryColor
                                )
                            }
                            Text(
                                text = "Hello ${userName.firstSpace}",
                                color = isSystemInDarkTheme().textForPrimaryColor
                            )
                            IconButton(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(50.dp)
                                    //.shadow(15.dp, RoundedCornerShape(size = 15.dp))
                                    .clip(RoundedCornerShape(size = 15.dp))
                                    .background(shadowColor),
                                onClick = {
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = isSystemInDarkTheme().textForPrimaryColor
                                )
                            }
                        }
                        TextField(
                            value = "",
                            onValueChange = {},
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                                .clip(RoundedCornerShape(size = 15.dp))
                                .onFocusChanged {

                                },
                            placeholder = { Text("Search") }
                        )
                    }
                }
                LoadingBar(state.isLoading, false)
                UpperNavBar(
                    listOf("Courses", "Available Timelines", "Upcoming Timelines", "Following Articles",  "Following Courses", "All Courses", "All Articles"),
                    state.currentTab,
                    listState
                ) { tab ->
                    when (tab) {
                        0 -> viewModel.getCoursesForStudent(userId)
                        1 -> viewModel.getAvailableStudentTimeline(studentId = userId, studentName = userName)
                        2 -> viewModel.getUpcomingStudentTimeline(studentId = userId, studentName = userName)
                        3 -> viewModel.allArticlesFollowers(studentId = userId)
                        4 -> viewModel.allCoursesFollowers(studentId = userId)
                        5 -> viewModel.allCourses()
                        6 -> viewModel.allArticles()
                    }
                    viewModel.updateCurrentTabIndex(tab)
                }
                when (state.currentTab) {
                    0 -> {
                        HomeOwnCoursesView(courses = state.courses) { course ->
                            prefModel.writeArguments(COURSE_SCREEN_ROUTE, course.id, course.title, COURSE_MODE_STUDENT, course)
                            navController.navigate(COURSE_SCREEN_ROUTE)
                        }
                    }

                    1 -> {
                        ListBody(list = state.sessionForDisplay, bodyClick = { course ->
                            prefModel.writeArguments(TIMELINE_SCREEN_ROUTE, "", "", obj = course)
                            navController.navigate(route = TIMELINE_SCREEN_ROUTE)
                        }) { session ->
                            MainItem(title = session.title, imageUri = session.imageUri) {
                                TimelineItem(courseName = session.courseName, date = session.dateStr, duration = session.duration)
                            }
                        }
                    }

                    2 -> {
                        ListBody(list = state.sessionForDisplay, bodyClick = { course ->
                            prefModel.writeArguments(TIMELINE_SCREEN_ROUTE, "", "", obj = course)
                            navController.navigate(route = TIMELINE_SCREEN_ROUTE)
                        }) { session ->
                            MainItem(title = session.title, imageUri = session.imageUri) {
                                TimelineItem(courseName = session.courseName, date = session.dateStr, duration = session.duration)
                            }
                        }
                    }

                    3 -> {
                        HomeAllArticlesView(
                            articles = state.followedArticles
                        ) { course ->
                            prefModel.writeArguments(
                                ARTICLE_SCREEN_ROUTE,
                                course.id, course.title,
                                COURSE_MODE_STUDENT,
                                course
                            )
                            navController.navigate(ARTICLE_SCREEN_ROUTE)
                        }
                    }

                    4 -> {
                        HomeAllCoursesView(
                            courses = state.followedCourses
                        ) { course ->
                            scope.launch {
                                prefModel.writeArguments(
                                    COURSE_SCREEN_ROUTE,
                                    course.id, course.title,
                                    COURSE_MODE_STUDENT,
                                    course
                                )
                                navController.navigate(COURSE_SCREEN_ROUTE)
                            }
                        }
                    }

                    5 -> {
                        HomeAllCoursesView(
                            courses = state.allCourses, {
                                PageView(list = list)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        ) { course ->
                            prefModel.writeArguments(
                                COURSE_SCREEN_ROUTE,
                                course.id, course.title,
                                COURSE_MODE_STUDENT,
                                course
                            )
                            navController.navigate(COURSE_SCREEN_ROUTE)
                        }
                    }

                    6 -> {
                        HomeAllArticlesView(
                            articles = state.allArticles
                        ) { course ->
                            prefModel.writeArguments(
                                ARTICLE_SCREEN_ROUTE,
                                course.id, course.title,
                                COURSE_MODE_STUDENT,
                                course
                            )
                            navController.navigate(ARTICLE_SCREEN_ROUTE)
                        }
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageView(
    list: List<String>
) {
    val pagerState = rememberPagerState(pageCount = { list.size })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(13.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .width(300.dp)
                .height(200.dp)
                .background(MaterialTheme.colorScheme.background)
                .clip(RoundedCornerShape(20.dp)),
        ) {
            SubcomposeAsyncImage(
                model = LocalContext.current.imageBuildr(list[it]),
                success = { (painter, _) ->
                    Image(
                        contentScale = ContentScale.Crop,
                        painter = painter,
                        contentDescription = "Image"
                    )
                },
                loading = {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                },
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.None,
                contentDescription = "Image"
            )
        }
        Spacer(modifier = Modifier.height(13.dp))
        DotsIndicator(
            totalDots = pagerState.pageCount,
            selectedIndex = pagerState.currentPage,
            selectedColor = MaterialTheme.colorScheme.secondary,
            unSelectedColor = Color(
                ColorUtils.setAlphaComponent(
                    MaterialTheme.colorScheme.secondary.toArgb(),
                    150
                )
            )
        )
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color,
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()

    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(selectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(unSelectedColor)
                )
            }
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}