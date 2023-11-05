package com.curso.free.ui.lecturer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.curso.free.R
import com.curso.free.data.firebase.filePicker
import com.curso.free.global.base.HOME_LECTURER_SCREEN_ROUTE
import com.curso.free.global.base.backDark
import com.curso.free.global.base.backDarkSec
import com.curso.free.global.base.darker
import com.curso.free.global.base.error
import com.curso.free.global.base.outlinedTextFieldStyle
import com.curso.free.global.base.textColor
import com.curso.free.global.ui.CardAnimationButton
import com.curso.free.global.ui.rememberAddAPhoto
import com.curso.free.global.util.imageBuildr
import com.curso.free.ui.PrefViewModel
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(
    navController: NavController,
    prefModel: PrefViewModel,
    viewModel: LogInViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state = viewModel.state.value
    val scaffoldState = remember { SnackbarHostState() }

    val isEmailError = state.isErrorPressed && state.email.isEmpty()
    val isPasswordError = state.isErrorPressed && state.password.isEmpty()
    val isNameError = state.isErrorPressed && state.lecturerName.isEmpty()
    val isMobileError = state.isErrorPressed && state.mobile.isEmpty()
    val isUniversityError = state.isErrorPressed && state.university.isEmpty()
    val isSpecialtyError = state.isErrorPressed && state.specialty.isEmpty()
    val isBriefError = state.isErrorPressed && state.brief.isEmpty()
    val isImageError = state.isErrorPressed && state.imageUri.isEmpty()
    val verticalScroll = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val imagePicker = context.filePicker(true) {
        viewModel.setImageUri(it.toString())
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "AppIcon",
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 20.dp)
                        .width(40.dp)
                        .height(40.dp), tint = Color.White
                )
                Text(
                    text = "Hello There.",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 35.sp,
                )
                Text(
                    text = "Login or sign up to continue.",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 14.sp,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (!state.isLogIn) {
                    Surface(
                        modifier = Modifier
                            .width(120.dp)
                            .height(120.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .border(
                                if (isImageError) 2.dp else 0.dp,
                                if (isImageError) isSystemInDarkTheme().error else Color.Transparent,
                                CircleShape
                            ),
                        color = Color.Gray,
                    ) {
                        if (state.imageUri.isNotEmpty()) {
                            SubcomposeAsyncImage(
                                model = context.imageBuildr(state.imageUri),
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
                        } else {
                            IconButton(
                                onClick = {
                                    imagePicker.invoke()
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(20.dp),
                                    imageVector = rememberAddAPhoto(),
                                    contentDescription = "AddPhoto",
                                    tint = isSystemInDarkTheme().textColor,
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1F),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = isSystemInDarkTheme().backDark,
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                            .verticalScroll(verticalScroll)
                            .padding(16.dp)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.email,
                            onValueChange = {
                                viewModel.setEmail(it)
                            },
                            placeholder = { Text(text = "Enter Email") },
                            label = { Text(text = "Email") },
                            supportingText = {
                                if (isEmailError) {
                                    Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                }
                            },
                            isError = isEmailError,
                            maxLines = 1,
                            colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        )
                        OutlinedTextField(
                            value = state.password,
                            onValueChange = {
                                viewModel.setPassword(it)
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            placeholder = { Text(text = "Enter Password") },
                            label = { Text(text = "Password") },
                            supportingText = {
                                if (isPasswordError) {
                                    Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                }
                            },
                            isError = isPasswordError,
                            maxLines = 1,
                            colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        )
                        AnimatedVisibility(
                            visible = !state.isLogIn,
                            enter = slideInVertically(),
                            exit = slideOutVertically()
                        ) {
                            Column(
                                modifier = Modifier.background(color = isSystemInDarkTheme().backDark)
                            ) {
                                OutlinedTextField(
                                    value = state.lecturerName,
                                    onValueChange = {
                                        viewModel.setName(it)
                                    },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    placeholder = { Text(text = "Enter Name") },
                                    label = { Text(text = "Name") },
                                    supportingText = {
                                        if (isNameError) {
                                            Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                        }
                                    },
                                    isError = isNameError,
                                    maxLines = 1,
                                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                )
                                OutlinedTextField(
                                    value = state.mobile,
                                    onValueChange = {
                                        viewModel.setMobile(it)
                                    },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    placeholder = { Text(text = "Enter Mobile") },
                                    label = { Text(text = "Mobile") },
                                    supportingText = {
                                        if (isMobileError) {
                                            Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                        }
                                    },
                                    isError = isMobileError,
                                    maxLines = 1,
                                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                )
                                OutlinedTextField(
                                    value = state.specialty,
                                    onValueChange = {
                                        viewModel.setSpecialty(it)
                                    },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    placeholder = { Text(text = "Enter Specialty") },
                                    label = { Text(text = "Specialty") },
                                    supportingText = {
                                        if (isSpecialtyError) {
                                            Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                        }
                                    },
                                    isError = isSpecialtyError,
                                    maxLines = 1,
                                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                )
                                OutlinedTextField(
                                    value = state.university,
                                    onValueChange = {
                                        viewModel.setUniversity(it)
                                    },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    placeholder = { Text(text = "Enter University") },
                                    label = { Text(text = "University") },
                                    supportingText = {
                                        if (isUniversityError) {
                                            Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                        }
                                    },
                                    isError = isUniversityError,
                                    maxLines = 1,
                                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                )
                                OutlinedTextField(
                                    value = state.brief,
                                    onValueChange = {
                                        viewModel.setBrief(it)
                                    },
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth(),
                                    placeholder = { Text(text = "Enter Info About you") },
                                    label = { Text(text = "About") },
                                    supportingText = {
                                        if (isBriefError) {
                                            Text(text = "Shouldn't be empty", color = isSystemInDarkTheme().error, fontSize = 10.sp)
                                        }
                                    },
                                    isError = isBriefError,
                                    colors = isSystemInDarkTheme().outlinedTextFieldStyle(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                )
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(bottom = 15.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CardAnimationButton(
                    isChoose = state.isLogIn,
                    isProcess = state.isProcessing,
                    text = "Login",
                    onClick = {
                        if (state.isLogIn) {
                            viewModel.apply {
                                context.login({ it, i ->
                                    prefModel.updateUserBase(
                                        PrefViewModel.UserBase(
                                            id = it._id.toHexString(),
                                            name = it.lecturerName,
                                            email = it.email,
                                            password = state.password,
                                            courses = i,
                                        )
                                    ) {
                                        scope.launch {
                                            prefModel.writeArguments(HOME_LECTURER_SCREEN_ROUTE, it._id.toHexString(), it.lecturerName)
                                            navController.navigate(route = HOME_LECTURER_SCREEN_ROUTE) {
                                                popUpTo(navController.graph.id) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }) {
                                    scope.launch {
                                        scaffoldState.showSnackbar(it)
                                    }
                                }
                            }
                        } else {
                            viewModel.isLogin(true)
                        }
                    }
                )
                Divider(
                    color = Color.Gray, modifier = Modifier
                        .height(30.dp)
                        .width(1.dp)
                )
                CardAnimationButton(
                    isChoose = !state.isLogIn,
                    isProcess = state.isProcessing,
                    text = "Sign up",
                    onClick = {
                        if (!state.isLogIn) {
                            viewModel.apply {
                                context.signUp({ value ->
                                    prefModel.updateUserBase(
                                        PrefViewModel.UserBase(
                                            id = value._id.toHexString(),
                                            name = value.lecturerName,
                                            email = value.email,
                                            password = state.password,
                                            courses = 0,
                                        )
                                    ) {
                                        scope.launch {
                                            prefModel.writeArguments(HOME_LECTURER_SCREEN_ROUTE, value._id.toHexString(), value.lecturerName)
                                            navController.navigate(route = HOME_LECTURER_SCREEN_ROUTE) {
                                                popUpTo(navController.graph.id) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }) {
                                    scope.launch {
                                        scaffoldState.showSnackbar(it)
                                    }
                                }
                            }
                        } else {
                            viewModel.isLogin(false)
                        }
                    }
                )
            }
        }
    }
}
