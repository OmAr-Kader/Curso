package com.curso.free.ui.lecturer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.COURSE_MODE_NONE
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.CREATE_ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.CREATE_COURSE_SCREEN_ROUTE
import com.curso.free.global.base.LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.TIMELINE_SCREEN_ROUTE
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.error
import com.curso.free.global.base.shadowColor
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textForPrimaryColor
import com.curso.free.global.ui.FabItem
import com.curso.free.global.ui.ListBodyEdit
import com.curso.free.global.ui.LoadingBar
import com.curso.free.global.ui.MultiFloatingActionButton
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.UpperNavBar
import com.curso.free.global.ui.rememberExitToApp
import com.curso.free.global.util.firstSpace
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.HomeAllArticlesView
import com.curso.free.ui.common.HomeAllCoursesView
import com.curso.free.ui.common.MainItemEdit
import com.curso.free.ui.common.OwnArticleItem
import com.curso.free.ui.common.OwnCourseItem
import com.curso.free.ui.common.TimelineItem
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
    userId: String,
    userName: String,
) {
    val scaffoldState = remember { SnackbarHostState() }
    val state = viewModel.state.value
    val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val buttonsList = remember {
        listOf("Courses", "Articles", "Timelines", "All Courses", "All Articles")
    }
    val colorPri = MaterialTheme.colorScheme.primary
    val colorSec = MaterialTheme.colorScheme.secondary
    OnLaunchScreenScope {
        viewModel.getCoursesForLecturer(userId)
    }
    val animated = animateDpAsState(
        targetValue = if (state.isLoading) 0.dp else 20.dp,
        label = "roundCorner"
    )
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight(),
                drawerContainerColor = isSystemInDarkTheme().backDark,
                drawerTonalElevation = 6.dp,
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
                        prefModel.writeArguments(LECTURER_SCREEN_ROUTE, userId, userName)
                        navController.navigate(LECTURER_SCREEN_ROUTE)
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
            floatingActionButton = {
                MultiFloatingActionButton(
                    items = remember {
                        listOf(FabItem(Icons.Default.Add, "Article", colorSec), FabItem(Icons.Default.Add, "Course", colorPri))
                    },
                ) {
                    if (it == 0) {
                        prefModel.writeArguments(CREATE_ARTICLE_SCREEN_ROUTE, "", "")
                        navController.navigate(CREATE_ARTICLE_SCREEN_ROUTE)
                    } else {
                        prefModel.writeArguments(CREATE_COURSE_SCREEN_ROUTE, "", "")
                        navController.navigate(CREATE_COURSE_SCREEN_ROUTE)
                    }
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
                UpperNavBar(buttonsList, state.currentTab) { tab ->
                    viewModel.updateCurrentTabIndex(tab)
                    when (tab) {
                        0 -> viewModel.getCoursesForLecturer(userId)
                        1 -> viewModel.getArticlesForLecturer(userId)
                        2 -> viewModel.getUpcomingLecturerTimeline()
                        3 -> viewModel.allCourses()
                        4 -> viewModel.allArticles()
                    }
                }
                when (state.currentTab) {
                    0 -> {
                        HomeCoursesView(courses = state.courses, nav = { course ->
                            prefModel.writeArguments(COURSE_SCREEN_ROUTE, course.id, course.title, COURSE_MODE_LECTURER, course)
                            navController.navigate(COURSE_SCREEN_ROUTE)
                        }) { id, title ->
                            prefModel.writeArguments(CREATE_COURSE_SCREEN_ROUTE, id, title)
                            navController.navigate(CREATE_COURSE_SCREEN_ROUTE)
                        }
                    }

                    1 -> {
                        HomeArticlesView(
                            articles = state.articles, {
                                prefModel.writeArguments(ARTICLE_SCREEN_ROUTE, it.id, it.title, COURSE_MODE_LECTURER, it)
                                navController.navigate(ARTICLE_SCREEN_ROUTE)
                            }
                        ) { id, title ->
                            prefModel.writeArguments(CREATE_ARTICLE_SCREEN_ROUTE, id, title)
                            navController.navigate(CREATE_ARTICLE_SCREEN_ROUTE)
                        }
                    }

                    2 -> {
                        HomeTimeLineView(
                            sessions = state.sessionForDisplay,
                            nav = { course ->
                                prefModel.writeArguments(TIMELINE_SCREEN_ROUTE, "", "", obj = course)
                                navController.navigate(route = TIMELINE_SCREEN_ROUTE)
                            }
                        ) { courseId, courseName ->
                            prefModel.writeArguments(CREATE_COURSE_SCREEN_ROUTE, courseId, courseName)
                            navController.navigate(CREATE_COURSE_SCREEN_ROUTE)
                        }
                    }

                    3 -> {
                        HomeAllCoursesView(
                            courses = state.allCourses,
                        ) { course ->
                            prefModel.writeArguments(
                                COURSE_SCREEN_ROUTE,
                                course.id, course.title,
                                COURSE_MODE_NONE,
                                course
                            )
                            navController.navigate(COURSE_SCREEN_ROUTE)
                        }
                    }

                    4 -> {
                        HomeAllArticlesView(
                            articles = state.allArticles,
                        ) { article ->
                            prefModel.writeArguments(
                                ARTICLE_SCREEN_ROUTE,
                                article.id, article.title,
                                COURSE_MODE_NONE,
                                article
                            )
                            navController.navigate(ARTICLE_SCREEN_ROUTE)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeCoursesView(
    courses: List<CourseForData>,
    nav: (CourseForData) -> Unit,
    edit: (String, String) -> Unit
) {
    ListBodyEdit(list = courses) { course ->
        MainItemEdit(
            title = course.title,
            imageUri = course.imageUri,
            colorEdit = if (course.isDraft == 1) isSystemInDarkTheme().error else MaterialTheme.colorScheme.primary,
            textColorEdit = if (course.isDraft == 1) Color.White else isSystemInDarkTheme().textForPrimaryColor,
            bodyClick = {
                nav.invoke(course)
            },
            editClick = {
                edit.invoke(course.id, course.title)
            }
        ) {
            OwnCourseItem(nextTimeLine = course.nextTimeLine, students = course.studentsSize)
        }
    }
}


@Composable
fun HomeArticlesView(
    articles: List<ArticleForData>,
    nav: (ArticleForData) -> Unit,
    edit: (String, String) -> Unit,
) {
    ListBodyEdit(list = articles) { article ->
        MainItemEdit(
            title = article.title,
            imageUri = article.imageUri,
            colorEdit = if (article.isDraft == 1) isSystemInDarkTheme().error else MaterialTheme.colorScheme.primary,
            textColorEdit = if (article.isDraft == 1) Color.White else isSystemInDarkTheme().textForPrimaryColor,
            bodyClick = {
                nav.invoke(article)
            },
            editClick = {
                edit.invoke(article.id, article.title)
            }
        ) {
            OwnArticleItem(article.readers)
        }
    }
}

@Composable
fun HomeTimeLineView(
    sessions: List<SessionForDisplay>,
    nav: (SessionForDisplay) -> Unit,
    edit: (String, String) -> Unit,
) {
    ListBodyEdit(list = sessions) { session ->
        MainItemEdit(
            title = session.title,
            imageUri = session.imageUri,
            colorEdit = if (session.isDraft == 1) isSystemInDarkTheme().error else MaterialTheme.colorScheme.primary,
            textColorEdit = if (session.isDraft == 1) Color.White else isSystemInDarkTheme().textForPrimaryColor,
            bodyClick = {
                nav.invoke(session)
            },
            editClick = {
                edit.invoke(session.courseId, session.courseName)
            }
        ) {
            TimelineItem(courseName = session.courseName, date = session.dateStr, duration = session.duration)
        }
    }
}
