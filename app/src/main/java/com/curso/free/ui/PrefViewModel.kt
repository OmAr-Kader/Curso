package com.curso.free.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.curso.free.data.firebase.getFcmLogout
import com.curso.free.data.model.Preference
import com.curso.free.data.model.SessionForDisplay
import com.curso.free.data.util.REALM_SUCCESS
import com.curso.free.data.util.loggerError
import com.curso.free.di.Project
import com.curso.free.global.base.PREF_USER_COURSES
import com.curso.free.global.base.PREF_USER_EMAIL
import com.curso.free.global.base.PREF_USER_ID
import com.curso.free.global.base.PREF_USER_NAME
import com.curso.free.global.base.PREF_USER_PASSWORD
import com.curso.free.global.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.syncSession
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PrefViewModel @Inject constructor(
    private val project: Project
) : ViewModel(), kotlinx.coroutines.CoroutineScope {

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }


    private val _state = mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    val getArgumentOne: (String) -> String?
        get() = {
            state.value.argOne[it]
        }

    val getArgumentTwo: (String) -> String?
        get() = {
            state.value.argTwo[it]
        }

    val getArgumentThree: (String) -> Int?
        get() = {
            state.value.argThree[it]
        }

    val getArgumentJson: (String) -> Any?
        get() = {
            state.value.argJson[it]
        }

    fun writeArguments(route: String, one: String, two: String) {
        state.value.apply {
            argOne[route] = one
            argTwo[route] = two
        }
    }

    fun writeArguments(route: String, one: String, two: String, three: Int? = null, obj: Any? = null) {
        state.value.apply {
            argOne[route] = one
            argTwo[route] = two
            three?.let { argThree[route] = it }
            obj?.let { argJson[route] = it }
        }
    }

    private var preferences: MutableList<Preference> = mutableListOf()
    private val preference = preferences

    private var prefsJob: kotlinx.coroutines.Job? = null

    private fun inti(invoke: List<Preference>.() -> Unit) {
        prefsJob?.cancel()
        prefsJob = this@PrefViewModel.launch {
            project.preference.prefs {
                preferences = this@prefs.toMutableList()
                invoke.invoke(this@prefs)
            }
        }
    }

    fun android.content.Context.downloadChanges(invoke: () -> Unit) {
        this@PrefViewModel.launch {
            if (isNetworkAvailable()) {
                downloadAllServerChanges(invoke)
            } else return@launch
        }
    }

    private suspend fun downloadAllServerChanges(invoke: () -> Unit) {
        kotlinx.coroutines.coroutineScope {
            try {
                project.realmSync.cloud?.syncSession?.downloadAllServerChanges(10.seconds)
                project.realmSync.cloud?.subscriptions?.waitForSynchronization(10.seconds)
                kotlinx.coroutines.coroutineScope {
                    invoke.invoke()
                }
            } catch (e: io.realm.kotlin.mongodb.exceptions.BadFlexibleSyncQueryException) {
                loggerError("syncSessionU", e.stackTraceToString())
                invoke.invoke()
            } catch (e: IllegalStateException) {
                loggerError("syncSessionU", e.stackTraceToString())
                invoke.invoke()
            } catch (e: io.realm.kotlin.mongodb.exceptions.SyncException) {
                loggerError("syncSessionU", e.stackTraceToString())
                invoke.invoke()
            } catch (e: Exception) {
                invoke.invoke()
            }
        }
    }

    fun signOut(invoke: () -> Unit, failed: () -> Unit) {
        this@PrefViewModel.launch {
            project.preference.apply {
                deletePrefAll().checkDeleting({
                    kotlin.runCatching {
                        try {
                            project.realmSync.realmApp.currentUser?.logOut()
                            kotlinx.coroutines.coroutineScope {
                                getFcmLogout(invoke)
                            }
                        } catch (e: io.realm.kotlin.mongodb.exceptions.ServiceException) {
                            failed.invoke()
                        } catch (e: io.realm.kotlin.mongodb.exceptions.AppException) {
                            failed.invoke()
                        } catch (e: IllegalStateException) {
                            failed.invoke()
                        }
                    }.getOrElse {
                        failed.invoke()
                    }
                }, failed)
            }
        }
    }

    private suspend fun Int.checkDeleting(invoke: suspend () -> Unit, failed: suspend () -> Unit) {
        if (this@checkDeleting == REALM_SUCCESS) {
            invoke.invoke()
        } else {
            failed.invoke()
        }
    }


    fun android.content.Context.checkIsUserValid(userBase: UserBase, isStudent: Boolean, invoke: () -> Unit, failed: () -> Unit) {
        this@PrefViewModel.launch {
            project.realmSync.realmApp.apply {
                kotlinx.coroutines.coroutineScope {
                    val curr = this@apply.currentUser
                    if (curr !== null) {
                        curr
                    } else {
                        if (!isNetworkAvailable()) {
                            return@coroutineScope null
                        }
                        this@apply.login(
                            Credentials.emailPassword(userBase.email, userBase.password)
                        )
                    }
                }.let {
                    if (it == null || !it.loggedIn) {
                        failed.invoke()
                        return@launch
                    }
                    if (!isStudent) {
                        project.lecturer.getLecturer(userBase.id) {
                            if (value != null) {
                                invoke.invoke()
                            } else failed.invoke()
                        }
                    } else {
                        project.student.getStudent(userBase.id) {
                            if (value != null) {
                                invoke.invoke()
                            } else failed.invoke()
                        }
                    }
                }
            }
        }
    }

    fun findPrefString(
        key: String,
        value: (it: String?) -> Unit,
    ): String {
        if (preference.isEmpty()) {
            inti {
                value.invoke(this@inti.find { it1 -> it1.ketString == key }?.value)
            }
        } else {
            value.invoke(preference.find { it.ketString == key }?.value)
        }
        return preference.toString()
    }

    fun findUserBase(
        value: (it: UserBase?) -> Unit,
    ) {
        if (preference.isEmpty()) {
            inti {
                value.invoke(this@inti.fetchUserBase())
            }
        } else {
            value.invoke(preference.fetchUserBase())
        }
    }

    private fun List<Preference>.fetchUserBase(): UserBase? {
        val id = find { it.ketString == PREF_USER_ID }?.value ?: return null
        val name = find { it.ketString == PREF_USER_NAME }?.value ?: return null
        val email = find { it.ketString == PREF_USER_EMAIL }?.value ?: return null
        val password = find { it.ketString == PREF_USER_PASSWORD }?.value ?: return null
        val courses = find { it.ketString == PREF_USER_COURSES }?.value?.toIntOrNull() ?: 0
        return UserBase(id = id, name = name, email = email, password = password, courses = courses)
    }

    fun updateUserBase(userBase: UserBase, invoke: () -> Unit) {
        this@PrefViewModel.launch {
            updatePref(PREF_USER_ID, userBase.id) {
                updatePref(PREF_USER_NAME, userBase.name) {
                    updatePref(PREF_USER_EMAIL, userBase.email) {
                        updatePref(PREF_USER_PASSWORD, userBase.password) {
                            invoke.invoke()
                        }
                    }
                }
            }
        }
    }

    private suspend fun updatePref(key: String, newValue: String, invoke: suspend () -> Unit) {
        kotlinx.coroutines.coroutineScope {
            preference.indexOfFirst {
                it.ketString == key
            }.let {
                if (it != -1) {
                    val new = kotlinx.coroutines.coroutineScope {
                        project.preference.updatePref(
                            preference[it],
                            newValue
                        )
                    }
                    kotlinx.coroutines.coroutineScope {
                        preference[it] = new
                    }
                    kotlinx.coroutines.coroutineScope {
                        invoke.invoke()
                    }
                } else {
                    val new = Preference(
                        ketString = key,
                        value = newValue,
                    )
                    project.preference.insertPref(new)
                    kotlinx.coroutines.coroutineScope {
                        preference.add(new)
                    }
                    kotlinx.coroutines.coroutineScope {
                        invoke.invoke()
                    }
                }
            }
        }
    }


    fun updatePref(key: String, newValue: String) {
        this@PrefViewModel.launch {
            preference.indexOfFirst {
                it.ketString == key
            }.let {
                if (it != -1) {
                    val new = kotlinx.coroutines.coroutineScope {
                        project.preference.updatePref(
                            preference[it],
                            newValue
                        )
                    }
                    kotlinx.coroutines.coroutineScope {
                        preference[it] = new
                    }
                } else {
                    val new = Preference(
                        ketString = key,
                        value = newValue,
                    )
                    project.preference.insertPref(new)
                    kotlinx.coroutines.coroutineScope {
                        preference.add(new)
                    }
                }
            }
        }
    }

    data class UserBase(
        val id: String,
        val name: String,
        val email: String,
        val password: String,
        val courses: Int,
    )

    data class State(
        val argOne: HashMap<String, String> = hashMapOf(),
        val argTwo: HashMap<String, String> = hashMapOf(),
        val argThree: HashMap<String, Int> = hashMapOf(),
        val argJson: HashMap<String, Any> = hashMapOf(),
        val sessionForDisplay: SessionForDisplay? = null
    )

}