package com.curso.free.ui.lecturer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.curso.free.data.firebase.upload
import com.curso.free.data.model.Lecturer
import com.curso.free.data.util.REALM_SUCCESS
import com.curso.free.di.Project
import com.curso.free.global.util.getMimeType
import com.curso.free.global.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val project: Project
) : ViewModel(), kotlinx.coroutines.CoroutineScope {

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    private val _state = mutableStateOf(State())
    val state: MutableState<State> = _state

    fun android.content.Context.login(invoke: (Lecturer, Int) -> Unit, failed: (String) -> Unit) {
        val s = _state.value
        if (s.email.isEmpty() || s.password.isEmpty()) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        if (!isNetworkAvailable()) {
            failed.invoke("Failed: Internet is disconnected")
            return
        }
        _state.value = state.value.copy(isProcessing = true)
        doLogIn(s, invoke, failed)
    }

    private fun doLogIn(s: State, invoke: (Lecturer, Int) -> Unit, failed: (String) -> Unit) {
        this@LogInViewModel.launch {
            loginRealm(s).let { user ->
                kotlinx.coroutines.coroutineScope {
                    if (user != null) {
                        project.lecturer.getLecturerEmail(s.email) {
                            value.saveUserState(invoke = invoke, failed = failed)
                        }
                    } else {
                        _state.value = state.value.copy(isProcessing = false)
                        failed.invoke("Failed")
                    }
                }
            }
        }
    }

    private fun Lecturer?.saveUserState(invoke: (Lecturer, Int) -> Unit, failed: (String) -> Unit) {
        this@saveUserState?.let {
            this@LogInViewModel.launch {
                project.course.getLecturerCourses(it._id.toHexString()) {
                    _state.value = state.value.copy(isProcessing = false)
                    invoke.invoke(it, value.size)
                }
            }
        } ?: kotlin.run {
            _state.value = state.value.copy(isProcessing = false)
            failed.invoke("Failed")
        }
    }

    fun android.content.Context.signUp(invoke: (Lecturer) -> Unit, failed: (String) -> Unit) {
        val s = _state.value
        if (s.email.isEmpty() || s.password.isEmpty() || s.brief.isEmpty() || s.imageUri.isEmpty() || s.university.isEmpty() || s.specialty.isEmpty() || s.lecturerName.isEmpty() || s.mobile.isEmpty()) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        if (!isNetworkAvailable()) {
            failed.invoke("Failed: Internet is disconnected")
            return
        }
        _state.value = state.value.copy(isProcessing = true)
        doSignUp(s, invoke, failed)
    }

    private fun android.content.Context.doSignUp(s: State, invoke: (Lecturer) -> Unit, failed: (String) -> Unit) {
        this@LogInViewModel.launch {
            realmSignIn(s, failed).let { user ->
                kotlinx.coroutines.coroutineScope {
                    if (user != null) {
                        _state.value = state.value.copy(alreadyLoggedIn = true)
                        val uri = android.net.Uri.parse(s.imageUri)
                        project.fireApp.upload(uri, "LecturerImage/${user.id} ${System.currentTimeMillis()}" + getMimeType(uri), {
                            doInsertLecturer(s = s, it = it, invoke = invoke, failed = failed)
                        }, {
                            _state.value = state.value.copy(isProcessing = false)
                            failed.invoke("Failed")
                        })
                    } else {
                        _state.value = state.value.copy(isProcessing = false)
                        failed.invoke("Failed")
                    }
                }
            }
        }
    }

    private fun doInsertLecturer(s: State, it: String, invoke: (Lecturer) -> Unit, failed: (String) -> Unit) {
        this@LogInViewModel.launch {
            project.lecturer.insertLecturer(
                Lecturer(
                    lecturerName = s.lecturerName,
                    email = s.email,
                    mobile = s.mobile,
                    rate = 5.0,
                    raters = 0,
                    brief = s.brief,
                    imageUri = it,
                    specialty = s.specialty,
                    university = s.university,
                    approved = false,
                )
            ).let {
                if (it.result == REALM_SUCCESS && it.value != null) {
                    _state.value = state.value.copy(isProcessing = false)
                    invoke.invoke(it.value)
                } else {
                    failed.invoke("Failed")
                }
            }
        }
    }

    private suspend fun realmSignIn(s: State, failed: (String) -> Unit): io.realm.kotlin.mongodb.User? {
        return kotlin.run {
            return@run try {
                if (state.value.alreadyLoggedIn) {
                    project.realmSync.realmApp.currentUser.let {
                        it
                            ?: project.realmSync.realmApp.emailPasswordAuth.registerUser(
                                s.email, s.password
                            ).let {
                                loginRealm(s)
                            }
                    }
                } else {
                    project.realmSync.realmApp.emailPasswordAuth.registerUser(
                        s.email, s.password
                    ).let {
                        loginRealm(s)
                    }
                }
            } catch (e: io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException) {
                _state.value = state.value.copy(isProcessing = false)
                failed.invoke("Failed: Already Exists")
                null
            } catch (e: io.realm.kotlin.mongodb.exceptions.ServiceException) {
                null
            } catch (e: io.realm.kotlin.mongodb.exceptions.AppException) {
                null
            }
        }
    }


    private suspend fun loginRealm(s: State): io.realm.kotlin.mongodb.User? {
        return try {
            project.realmSync.realmApp.login(
                Credentials.emailPassword(s.email, s.password)
            ).let {
                return@let if (it.loggedIn) it else null
            }
        } catch (e: io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException) {
            null
        }
    }

    fun isLogin(it: Boolean) {
        _state.value = state.value.copy(isLogIn = it, isErrorPressed = false)
    }

    fun setEmail(it: String) {
        _state.value = state.value.copy(email = it, isErrorPressed = false)
    }

    fun setPassword(it: String) {
        _state.value = state.value.copy(password = it, isErrorPressed = false)
    }

    fun setName(it: String) {
        _state.value = state.value.copy(lecturerName = it, isErrorPressed = false)
    }

    fun setMobile(it: String) {
        _state.value = state.value.copy(mobile = it, isErrorPressed = false)
    }

    fun setBrief(it: String) {
        _state.value = state.value.copy(brief = it, isErrorPressed = false)
    }

    fun setSpecialty(it: String) {
        _state.value = state.value.copy(specialty = it, isErrorPressed = false)
    }

    fun setUniversity(it: String) {
        _state.value = state.value.copy(university = it, isErrorPressed = false)
    }

    fun setImageUri(it: String) {
        _state.value = state.value.copy(imageUri = it, isErrorPressed = false)
    }

    data class State(
        val email: String = "",
        val password: String = "",
        val lecturerName: String = "",
        val mobile: String = "",
        val specialty: String = "",
        val university: String = "",
        val brief: String = "",
        val imageUri: String = "",
        val isErrorPressed: Boolean = false,
        val isLogIn: Boolean = true,
        val isProcessing: Boolean = false,
        val alreadyLoggedIn: Boolean = false,
    )
}