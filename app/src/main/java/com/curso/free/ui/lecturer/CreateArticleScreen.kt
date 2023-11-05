package com.curso.free.ui.lecturer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.curso.free.data.firebase.filePicker
import com.curso.free.global.ui.BackButton
import com.curso.free.global.ui.CardAnimationButton
import com.curso.free.global.base.IMAGE_SCREEN_ROUTE
import com.curso.free.global.ui.OnLaunchScreenScope
import com.curso.free.global.ui.SubTopScreenButton
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.backDarkThr
import com.curso.free.global.base.darker
import com.curso.free.global.base.error
import com.curso.free.global.util.imageBuildr
import com.curso.free.global.base.outlinedTextFieldStyle
import com.curso.free.global.ui.rememberAddAPhoto
import com.curso.free.global.ui.rememberImage
import com.curso.free.global.ui.rememberUpload
import com.curso.free.global.base.textColor
import com.curso.free.ui.PrefViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateArticleScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: CreateArticleViewModel = hiltViewModel(),
    articleId: String,
    articleTitle: String,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = remember { SnackbarHostState() }
    val state = viewModel.state.value
    val scrollState = rememberLazyListState()

    val context = LocalContext.current
    val imagePicker = context.filePicker(true) {
        viewModel.setImageUri(it.toString())
    }
    fun saveOrEdit(isDraft: Boolean, userBase: PrefViewModel.UserBase) {
        if (!isDraft && state.imageUri.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add Thumbnail Image")
            }
            return
        }
        if (!isDraft && state.articleText.map { it.text.isEmpty() }.isEmpty()) {
            scope.launch {
                scaffoldState.showSnackbar("Should Add Article Text")
            }
            return
        }
        if (state.article != null) {
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
        if (articleId.isNotEmpty()) {
            viewModel.getArticle(articleId)
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(scaffoldState) {
                Snackbar(it, containerColor = isSystemInDarkTheme().backDarkSec, contentColor = isSystemInDarkTheme().textColor)
            }
        },
    ) {
        Box(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background.darker())
        ) {
            ImageView(
                state,
                imagePicker
            ) {
                prefModel.writeArguments(IMAGE_SCREEN_ROUTE, state.imageUri, state.courseTitle)
                navController.navigate(IMAGE_SCREEN_ROUTE)
            }
            Box(
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 200.dp, end = 20.dp, start = 20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.TopCenter),
            ) {
                BasicsViewArticle(viewModel, articleTitle, scrollState) {
                    scope.launch {
                        scrollState.animateScrollBy(70F)
                    }
                }
            }
            BottomBarArticle(viewModel = viewModel, prefModel = prefModel) { isDraft, userBase ->
                saveOrEdit(isDraft, userBase)
            }
        }
        if (state.isConfirmDialogVisible) {
            DialogForUploadArticle(
                viewModel
            ) {
                viewModel.changeUploadDialogGone(false)
                viewModel.deleteArticle {
                    navController.navigateUp()
                }
            }
        }
        BackButton {
            navController.navigateUp()
        }
        SubTopScreenButton(imageVector = Icons.Default.Delete) {
            viewModel.changeUploadDialogGone(true)
        }
    }
}

@Composable
fun ImageView(
    state: CreateArticleViewModel.State,
    imagePicker: () -> Unit,
    nav: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = isSystemInDarkTheme().backDark
    ) {
        if (state.imageUri.isNotEmpty()) {
            SubcomposeAsyncImage(
                model = LocalContext.current.imageBuildr(state.imageUri),
                success = { (painter, _) ->
                    Image(
                        contentScale = ContentScale.Fit,
                        painter = painter,
                        contentDescription = "Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                },
                contentScale = ContentScale.FillBounds,
                filterQuality = FilterQuality.None,
                contentDescription = "Image",
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
                        onClick = imagePicker
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
                            imageVector = rememberImage(),
                            contentDescription = "Image",
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
                    onClick = imagePicker
                ) {
                    Icon(
                        imageVector = rememberAddAPhoto(),
                        tint = isSystemInDarkTheme().textColor,
                        contentDescription = "AddVideo"
                    )
                }
            }
        }
    }
}

@Composable
fun BoxScope.BottomBarArticle(
    viewModel: CreateArticleViewModel,
    prefModel: PrefViewModel,
    draftOnClick: (Boolean, PrefViewModel.UserBase) -> Unit,
) {
    val state = viewModel.state.value
    Row(
        verticalAlignment = Alignment.Bottom, modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .align(Alignment.BottomCenter)
            .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (state.article?.isDraft != -1) {
            CardAnimationButton(
                isChoose = true,
                isProcess = state.isDraftProcessing,
                text = "Draft",
                color = isSystemInDarkTheme().error,
                textColor = Color.White,
                onClick = {
                    prefModel.findUserBase { userBase ->
                        draftOnClick.invoke(true, userBase ?: return@findUserBase)
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
                prefModel.findUserBase { userBase ->
                    draftOnClick.invoke(false, userBase ?: return@findUserBase)
                }
            }
        )
    }
}

@Composable
fun BasicsViewArticle(
    viewModel: CreateArticleViewModel,
    courseTitle: String,
    scrollState: LazyListState,
    scrollToEnd: (Int) -> Unit,
) {
    val state = viewModel.state.value
    val isCourseTitleError = state.isErrorPressed && state.courseTitle.isEmpty()
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
        itemsIndexed(state.articleText) { i, (font, text) ->
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
                            if (i == state.articleText.lastIndex) {
                                viewModel.makeFontDialogVisible()
                                scrollToEnd.invoke(state.articleText.size + 4)
                            } else {
                                viewModel.removeAboutIndex(i)
                            }
                        }
                    ) {
                        Icon(
                            if (i == state.articleText.lastIndex) Icons.Filled.Add else Icons.Default.Delete,
                            contentDescription = "",
                        )
                    }
                }
            )
        }
        if (state.isFontDialogVisible) {
            item {
                AboutCreatorArticle(
                    viewModel
                )
            }
        }
    }
}


@Composable
fun DialogForUploadArticle(
    viewModel: CreateArticleViewModel,
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
                    text = "Are sure you want to delete this article?",
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
fun AboutCreatorArticle(
    viewModel: CreateArticleViewModel,
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
