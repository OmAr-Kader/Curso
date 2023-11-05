package com.curso.free.ui.course

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.curso.free.data.model.ArticleTextDate
import com.curso.free.data.util.toMessageData
import com.curso.free.global.base.Blue
import com.curso.free.global.base.COURSE_MODE_LECTURER
import com.curso.free.global.base.CREATE_COURSE_SCREEN_ROUTE
import com.curso.free.global.base.IMAGE_SCREEN_ROUTE
import com.curso.free.global.base.LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.Yellow
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.shadowColor
import com.curso.free.global.base.textColor
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.CardButton
import com.curso.free.global.ui.IconText
import com.curso.free.global.ui.LoadingBar
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.PagerTab
import com.curso.free.global.util.imageBuildr
import com.curso.free.ui.PrefViewModel
import com.curso.free.ui.common.ChatView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleScreen(
    navController: NavController,
    viewModel: ArticleViewModel = hiltViewModel(),
    prefModel: PrefViewModel,
    articleId: String,
    articleTitle: String,
    mode: Int,
    article: com.curso.free.data.model.ArticleForData?,
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val list = remember {
        listOf(
            "Article",
            "Chat"
        )
    }
    val scaffoldState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        list.size
    }
    val context = LocalContext.current
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.changeFocus(page == 1)
        }
    }
    OnLaunchScreenScope {
        prefModel.findUserBase { userBase ->
            userBase ?: return@findUserBase
            viewModel.getArticle(article, articleId, userBase.id, userBase.name)
            viewModel.getMainArticleConversation(articleId)
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
                        prefModel.writeArguments(IMAGE_SCREEN_ROUTE, state.article.imageUri, state.article.title)
                        navController.navigate(IMAGE_SCREEN_ROUTE)
                    },
                color = isSystemInDarkTheme().backDark
            ) {
                SubcomposeAsyncImage(
                    model = LocalContext.current.imageBuildr(state.article.imageUri),
                    success = { (painter, _) ->
                        Image(
                            contentScale = ContentScale.Fit,
                            painter = painter,
                            contentDescription = "Video",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    },
                    contentScale = ContentScale.FillBounds,
                    filterQuality = FilterQuality.None,
                    contentDescription = "Image",
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.article.title.ifEmpty { articleTitle },
                    color = isSystemInDarkTheme().textColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(start = 5.dp, end = 5.dp)
                )
                if (mode == COURSE_MODE_LECTURER) {
                    CardButton(
                        onClick = {
                            prefModel.writeArguments(CREATE_COURSE_SCREEN_ROUTE, state.article.id, state.article.title)
                            navController.navigate(CREATE_COURSE_SCREEN_ROUTE)
                        },
                        color = MaterialTheme.colorScheme.primary,
                        text = "Edit",
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .padding(end = 5.dp, start = 3.dp),
                colors = ButtonDefaults.buttonColors(containerColor = shadowColor),
                onClick = {
                    prefModel.writeArguments(LECTURER_SCREEN_ROUTE, state.article.lecturerId, state.article.lecturerName)
                    navController.navigate(LECTURER_SCREEN_ROUTE)
                }
            ) {
                Text(
                    text = state.article.lecturerName,
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
                    text = state.article.readerIds.size.toString(),
                    tint = Blue,
                )
                IconText(
                    modifier = Modifier
                        .padding(end = 5.dp, start = 3.dp)
                        .weight(1F),
                    imageVector = Icons.Default.Star,
                    text = viewModel.articleRate,
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
                    0 -> ArticleScrollable(state.article.text)
                    else -> ChatView(
                        isEnabled = state.textFieldFocus,
                        chatText = state.chatText,
                        onTextChanged = viewModel.changeChatText,
                        list = state.conversation?.messages?.toMessageData() ?: mutableListOf(),
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
fun ArticleScrollable(
    about: List<ArticleTextDate>
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