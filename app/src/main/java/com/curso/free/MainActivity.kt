package com.curso.free

import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.curso.free.data.model.ArticleForData
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.global.base.ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.CREATE_ARTICLE_SCREEN_ROUTE
import com.curso.free.global.base.CREATE_COURSE_SCREEN_ROUTE
import com.curso.free.global.base.CursoTheme
import com.curso.free.global.base.HOME_LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.HOME_STUDENT_SCREEN_ROUTE
import com.curso.free.global.base.IMAGE_SCREEN_ROUTE
import com.curso.free.global.base.LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.LOG_IN_LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.LOG_IN_STUDENT_SCREEN_ROUTE
import com.curso.free.global.base.SPLASH_SCREEN_ROUTE
import com.curso.free.global.base.STUDENT_SCREEN_ROUTE
import com.curso.free.global.base.TIMELINE_SCREEN_ROUTE
import com.curso.free.global.base.VIDEO_SCREEN_ROUTE
import com.curso.free.global.ui.OnLaunchScreenScope
import kotlinx.coroutines.launch

@dagger.hilt.android.AndroidEntryPoint
class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            androidx.core.splashscreen.SplashScreen.apply {
                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        true
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            CursoTheme {
                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    intent.extras.HandleIntent(invokeNormal = { isStudent ->
                        Main(isStudent = isStudent)
                    }) { isStudent: Boolean, routeKey: String, argOne: String, argTwo: String, argThree: Int ->
                        MainNotification(isStudent = isStudent, routeKey = routeKey, argOne = argOne, argTwo = argTwo, argThree = argThree)
                    }
                }
            }
        }
    }
}

@Composable
fun MainNotification(
    isStudent: Boolean,
    prefModel: com.curso.free.ui.PrefViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    routeKey: String,
    argOne: String,
    argTwo: String,
    argThree: Int,
) {
    prefModel.writeArguments(route = routeKey, one = argOne, two = argTwo, three = argThree)
    MainScreen(isStudent, prefModel, routeKey)
}

@Composable
fun Main(
    isStudent: Boolean,
    prefModel: com.curso.free.ui.PrefViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    MainScreen(isStudent, prefModel, null)
}

@Composable
fun MainScreen(
    isStudent: Boolean,
    prefModel: com.curso.free.ui.PrefViewModel,
    routeKey: String?
) {
    CursoTheme {
        androidx.compose.material3.Surface(
            color = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SPLASH_SCREEN_ROUTE
            ) {
                composable(route = SPLASH_SCREEN_ROUTE) {
                    SplashScreen(navController = navController, prefModel = prefModel, isStudent = isStudent, routeKey = routeKey)
                }
                composable(route = LOG_IN_STUDENT_SCREEN_ROUTE) {
                    com.curso.free.ui.student.LogInScreen(navController = navController, prefModel = prefModel)
                }
                composable(route = LOG_IN_LECTURER_SCREEN_ROUTE) {
                    com.curso.free.ui.lecturer.LogInScreen(navController = navController, prefModel = prefModel)
                }
                composable(
                    route = HOME_STUDENT_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.student.HomeScreen(
                        navController = navController,
                        prefModel = prefModel,
                        userId = prefModel.getArgumentOne(HOME_STUDENT_SCREEN_ROUTE) ?: return@composable,
                        userName = prefModel.getArgumentTwo(HOME_STUDENT_SCREEN_ROUTE) ?: return@composable,
                        coursesNumber = prefModel.getArgumentThree(HOME_STUDENT_SCREEN_ROUTE) ?: return@composable,
                    )
                }
                composable(
                    route = HOME_LECTURER_SCREEN_ROUTE
                ) {
                    com.curso.free.ui.lecturer.HomeScreen(
                        navController = navController,
                        prefModel = prefModel,
                        userId = prefModel.getArgumentOne(HOME_LECTURER_SCREEN_ROUTE) ?: return@composable,
                        userName = prefModel.getArgumentTwo(HOME_LECTURER_SCREEN_ROUTE) ?: return@composable,
                    )
                }
                composable(
                    route = CREATE_COURSE_SCREEN_ROUTE
                ) {
                    com.curso.free.ui.lecturer.CreateCourseScreen(
                        navController = navController,
                        courseId = prefModel.getArgumentOne(CREATE_COURSE_SCREEN_ROUTE) ?: return@composable,
                        courseTitle = prefModel.getArgumentTwo(CREATE_COURSE_SCREEN_ROUTE) ?: return@composable,
                        prefModel = prefModel
                    )
                }
                composable(
                    route = CREATE_ARTICLE_SCREEN_ROUTE
                ) {
                    com.curso.free.ui.lecturer.CreateArticleScreen(
                        navController = navController,
                        articleId = prefModel.getArgumentOne(CREATE_ARTICLE_SCREEN_ROUTE) ?: return@composable,
                        articleTitle = prefModel.getArgumentTwo(CREATE_ARTICLE_SCREEN_ROUTE) ?: return@composable,
                        prefModel = prefModel
                    )
                }
                composable(
                    route = ARTICLE_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.course.ArticleScreen(
                        navController = navController,
                        prefModel = prefModel,
                        articleId = prefModel.getArgumentOne(ARTICLE_SCREEN_ROUTE) ?: return@composable,
                        articleTitle = prefModel.getArgumentTwo(ARTICLE_SCREEN_ROUTE) ?: return@composable,
                        mode = prefModel.getArgumentThree(ARTICLE_SCREEN_ROUTE) ?: return@composable,
                        article = prefModel.getArgumentJson(ARTICLE_SCREEN_ROUTE) as? ArticleForData?,
                    )
                }
                composable(
                    route = COURSE_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.course.CourseScreen(
                        navController = navController,
                        prefModel = prefModel,
                        courseId = prefModel.getArgumentOne(COURSE_SCREEN_ROUTE) ?: return@composable,
                        courseTitle = prefModel.getArgumentTwo(COURSE_SCREEN_ROUTE) ?: return@composable,
                        mode = prefModel.getArgumentThree(COURSE_SCREEN_ROUTE) ?: return@composable,
                        course = prefModel.getArgumentJson(COURSE_SCREEN_ROUTE) as? CourseForData?,
                    )
                }
                composable(
                    route = TIMELINE_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.course.SessionScreen(
                        navController = navController,
                        prefModel = prefModel,
                        session = prefModel.getArgumentJson(TIMELINE_SCREEN_ROUTE) as? SessionForDisplay? ?: return@composable,
                    )
                }
                composable(
                    route = LECTURER_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.lecturer.LecturerScreen(
                        prefModel = prefModel,
                        navController = navController,
                        lecturerId = prefModel.getArgumentOne(LECTURER_SCREEN_ROUTE) ?: return@composable,
                        lecturerName = prefModel.getArgumentTwo(LECTURER_SCREEN_ROUTE) ?: return@composable,
                        mode = if (isStudent) 1 else 2,//prefModel.getArgumentThree(com.curso.free.global.LECTURER_SCREEN_ROUTE) ?: 1,
                    )
                }
                composable(
                    route = STUDENT_SCREEN_ROUTE
                ) {
                    com.curso.free.ui.student.StudentScreen(
                        navController = navController,
                        prefModel = prefModel,
                        studentId = prefModel.getArgumentOne(STUDENT_SCREEN_ROUTE) ?: return@composable,
                        studentName = prefModel.getArgumentTwo(STUDENT_SCREEN_ROUTE) ?: return@composable,
                    )
                }
                composable(
                    route = VIDEO_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.course.VideoViewScreen(
                        videoUri = prefModel.getArgumentOne(VIDEO_SCREEN_ROUTE) ?: return@composable,
                        videoTitle = prefModel.getArgumentTwo(VIDEO_SCREEN_ROUTE) ?: return@composable,
                    )
                }
                composable(
                    route = IMAGE_SCREEN_ROUTE,
                ) {
                    com.curso.free.ui.course.ImageViewScreen(
                        imageUri = prefModel.getArgumentOne(IMAGE_SCREEN_ROUTE) ?: return@composable,
                        courseTitle = prefModel.getArgumentTwo(IMAGE_SCREEN_ROUTE) ?: return@composable,
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    navController: androidx.navigation.NavController,
    prefModel: com.curso.free.ui.PrefViewModel,
    isStudent: Boolean,
    routeKey: String?,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun splashNavigate(it: com.curso.free.ui.PrefViewModel.UserBase?) {
        if (it == null) {
            scope.launch {
                navController.navigate(route = isStudent.loginNavigate) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        } else {
            prefModel.apply {
                context.checkIsUserValid(it, isStudent, {
                    scope.launch {
                        isStudent.homeNavigate.let { route ->
                            prefModel.writeArguments(route, it.id, it.name, it.courses)
                            navController.navigate(route = routeKey ?: route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }) {
                    scope.launch {
                        navController.navigate(route = isStudent.loginNavigate) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
    OnLaunchScreenScope {
        prefModel.apply {
            context.downloadChanges {
                prefModel.findUserBase {
                    splashNavigate(it)
                }
            }
        }
    }
    androidx.compose.material3.Surface(
        color = androidx.compose.material3.MaterialTheme.colorScheme.background
    ) {
        androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            AnimatedVisibility(
                visible = true,
                modifier = Modifier
                    .size(100.dp),
                enter = fadeIn(tween(1500), 0.5F) + expandIn(tween(1500), expandFrom = androidx.compose.ui.Alignment.Center),
                label = "AppIcon"
            ) {
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "AppIcon",
                )
            }
        }
    }
}

internal inline val Boolean.loginNavigate: String
    get() {
        return if (this)
            LOG_IN_STUDENT_SCREEN_ROUTE
        else
            LOG_IN_LECTURER_SCREEN_ROUTE
    }

internal inline val Boolean.homeNavigate: String
    get() {
        return if (this)
            HOME_STUDENT_SCREEN_ROUTE
        else
            HOME_LECTURER_SCREEN_ROUTE
    }


@Composable
fun android.os.Bundle?.HandleIntent(
    invokeNormal: @Composable (isStudent: Boolean) -> Unit,
    invoke: @Composable (isStudent: Boolean, routeKey: String, argOne: String, argTwo: String, argThree: Int) -> Unit
) {
    if (this == null) {
        invokeNormal.invoke(false)
        return
    }
    val routeKey = getString("routeKey", null)
    val argOne = getString("argOne", null)
    val argTwo = getString("argTwo", null)
    val argThree = getString("argThree", null)?.toIntOrNull()
    if (routeKey != null && argOne != null && argTwo != null && argThree != null) {
        invoke.invoke(
            getString("current", "2") == "1",
            routeKey,
            argOne,
            argTwo,
            argThree,
        )
    } else {
        invokeNormal.invoke(getString("current", "2") == "1")
    }
}