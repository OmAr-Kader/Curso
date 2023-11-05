package com.curso.free.ui.student

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.curso.free.data.model.Certificate
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.base.TIMELINE_SCREEN_ROUTE
import com.curso.free.global.base.rateColor
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textGrayColor
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.ImageForCurveItem
import com.curso.free.global.ui.ListBody
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.ui.ProfileItems
import com.curso.free.global.ui.rememberAssignment
import com.curso.free.global.ui.rememberAttachMoney
import com.curso.free.global.ui.rememberVideoLibrary
import com.curso.free.global.util.ifNotEmpty
import com.curso.free.global.util.imageBuildr
import com.curso.free.global.util.toString
import com.curso.free.ui.PrefViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudentScreen(
    navController: NavController,
    viewModel: StudentViewModel = hiltViewModel(),
    prefModel: PrefViewModel,
    studentId: String,
    studentName: String,
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    OnLaunchScreenScope {
        viewModel.inti(studentId)
    }
    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = LocalContext.current.imageBuildr(state.student.imageUri),
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
                text = state.student.studentName.ifEmpty {
                    studentName
                },
                color = isSystemInDarkTheme().textColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
            )
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
                    icon = rememberAssignment(color = Color.Green),
                    color = Color.Green,
                    title = "Certificates",
                    numbers = state.certificates.size.toString(),
                )
                ProfileItems(
                    icon = Icons.Default.Star,
                    color = Color.Yellow,
                    title = "Rate",
                    numbers = viewModel.certificatesRate.toString(),
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
                    "Timeline",
                    "Courses",
                    "Certificates",
                ),
            ) { page: Int ->
                when (page) {
                    0 -> StudentTimeLineView(state.sessionForDisplay) { course ->
                        scope.launch {
                            prefModel.writeArguments(TIMELINE_SCREEN_ROUTE, "", "", obj = course)
                            navController.navigate(route = TIMELINE_SCREEN_ROUTE)
                        }
                    }
                    1 -> StudentCoursesView(state.courses) { course ->
                        scope.launch {
                            prefModel.writeArguments(
                                COURSE_SCREEN_ROUTE,
                                course.id,
                                course.title,
                                COURSE_MODE_STUDENT,
                                course
                            )
                            navController.navigate(COURSE_SCREEN_ROUTE)
                        }
                    }
                    else -> CertificatesView(state.certificates) { }
                }
            }
        }
        BackButton {
            navController.navigateUp()
        }
    }
}


@Composable
fun StudentTimeLineView(
    courses: List<SessionForDisplay>,
    nav: (SessionForDisplay) -> Unit,
) {
    ListBody(list = courses, bodyClick = { course ->
        nav.invoke(course)
    }) { (title, _, dateStr, _, _, _,_, courseName, _, _, _, _, _, _, duration, imageUri, _) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ImageForCurveItem(imageUri, 80.dp)
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = title,
                    color = isSystemInDarkTheme().textColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 5.dp, vertical = 3.dp)
                )
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(
                        text = courseName,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 15.dp, end = 15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Date: $dateStr",
                            color = isSystemInDarkTheme().textGrayColor,
                            fontSize = 10.sp,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            modifier = Modifier.weight(1.5F)
                        )
                        duration.ifNotEmpty {
                            Text(
                                text = "Duration: $duration",
                                color = isSystemInDarkTheme().textGrayColor,
                                fontSize = 10.sp,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                modifier = Modifier.weight(1F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentCoursesView(
    courses: List<CourseForData>,
    nav: (CourseForData) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(courses) { course ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(5.dp)
                    .clickable {
                        nav.invoke(course)
                    },
                shape = RoundedCornerShape(
                    size = 15.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box {
                        SubcomposeAsyncImage(
                            model = LocalContext.current.imageBuildr(course.imageUri),
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
                                        .width(70.dp)
                                        .height(70.dp)
                                )
                            },
                            contentScale = ContentScale.Crop,
                            filterQuality = FilterQuality.None,
                            contentDescription = "Image"
                        )
                    }
                    Column {
                        Text(
                            text = course.title,
                            color = isSystemInDarkTheme().textColor,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .padding(start = 5.dp, end = 5.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(end = 5.dp, start = 3.dp)
                                    .weight(1.0F),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person",
                                    tint = isSystemInDarkTheme().textColor,
                                    modifier = Modifier
                                        .width(15.dp)
                                        .height(15.dp)
                                )
                                Text(
                                    text = course.lecturerName,
                                    color = isSystemInDarkTheme().textColor,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 50.dp, start = 3.dp)
                            ) {
                                Icon(
                                    imageVector = rememberAttachMoney(color = MaterialTheme.colorScheme.primary),
                                    contentDescription = "Money",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .width(15.dp)
                                        .height(15.dp)
                                )
                                Text(
                                    text = course.price,
                                    color = isSystemInDarkTheme().textColor,
                                    fontSize = 10.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CertificatesView(
    certificates: List<Certificate>,
    @Suppress("UNUSED_PARAMETER") nav: (route: String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(certificates) { certificate ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(5.dp)
                    .clickable {

                    },
                shape = RoundedCornerShape(
                    size = 15.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Image",
                            tint = rateColor(certificate.rate),
                            modifier = Modifier
                                .width(70.dp)
                                .padding(20.dp)
                                .height(70.dp)
                        )
                    }
                    Column {
                        Text(
                            text = certificate.title,
                            color = isSystemInDarkTheme().textColor,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 5.dp, end = 5.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(end = 5.dp, start = 3.dp)
                                    .weight(1.0F),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Person",
                                    tint = isSystemInDarkTheme().textColor,
                                    modifier = Modifier
                                        .width(15.dp)
                                        .height(15.dp)
                                )
                                Text(
                                    text = certificate.date.toString,
                                    color = isSystemInDarkTheme().textColor,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 50.dp, start = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Money",
                                    tint = Color.Yellow,
                                    modifier = Modifier
                                        .width(15.dp)
                                        .height(15.dp)
                                )
                                Text(
                                    text = certificate.rate.toString(),
                                    color = isSystemInDarkTheme().textColor,
                                    fontSize = 10.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
