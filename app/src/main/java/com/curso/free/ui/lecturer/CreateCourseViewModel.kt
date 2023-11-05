package com.curso.free.ui.lecturer

import com.curso.free.data.firebase.NotificationData
import com.curso.free.data.firebase.PushNotification
import com.curso.free.data.firebase.deleteFile
import com.curso.free.data.firebase.subscribeToTopic
import com.curso.free.data.firebase.upload
import com.curso.free.data.model.AboutCourseData
import com.curso.free.data.model.Course
import com.curso.free.data.model.CourseForData
import com.curso.free.data.model.TimelineData
import com.curso.free.data.util.logger
import com.curso.free.data.util.toAboutCourse
import com.curso.free.data.util.toAboutCourseData
import com.curso.free.data.util.toTimeline
import com.curso.free.data.util.toTimelineData
import com.curso.free.global.base.COURSE_MODE_STUDENT
import com.curso.free.global.base.COURSE_SCREEN_ROUTE
import com.curso.free.global.util.getMimeType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

@dagger.hilt.android.lifecycle.HiltViewModel
class CreateCourseViewModel @javax.inject.Inject constructor(
    private val project: com.curso.free.di.Project
) : androidx.lifecycle.ViewModel(), kotlinx.coroutines.CoroutineScope {

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = kotlinx.coroutines.Dispatchers.Default + kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    private val _state = androidx.compose.runtime.mutableStateOf(State())
    val state: androidx.compose.runtime.State<State> = _state

    fun getCourse(id: String) {
        this@CreateCourseViewModel.launch {
            project.course.getCoursesById(id) {
                this.value ?: return@getCoursesById
                if (result == com.curso.free.data.util.REALM_SUCCESS) {
                    _state.value = state.value.copy(
                        course = CourseForData(value, System.currentTimeMillis()),
                        courseTitle = value.title,
                        price = value.price,
                        about = value.about.toAboutCourseData().ifEmpty {
                            mutableListOf<AboutCourseData>().apply {
                                add(AboutCourseData(text = "", font = 22))
                            }
                        },
                        briefVideo = value.briefVideo,
                        imageUri = value.imageUri,
                        timelines = value.timelines.toTimelineData().toMutableList(),
                    )
                }
            }
        }
    }

    fun deleteCourse(invoke: () -> Unit) {
        this@CreateCourseViewModel.launch {
            project.course.deleteCourse(Course(state.value.course ?: return@launch)).let {
                if (it == com.curso.free.data.util.REALM_SUCCESS) {
                    invoke.invoke()
                }
            }
        }
    }

    fun android.content.Context.save(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        invoke: (Course?) -> Unit
    ) {
        val s = _state.value
        if (
            (isDraft && (s.courseTitle.isEmpty() || s.price.isEmpty())) ||
            (!isDraft && (
                    s.courseTitle.isEmpty() ||
                            s.about.map { it.text }.isEmpty() ||
                            s.briefVideo.isEmpty() ||
                            s.imageUri.isEmpty() ||
                            s.timelines.isEmpty())
                    )
        ) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        _state.value = state.value.copy(isProcessing = !isDraft, isDraftProcessing = isDraft)
        doSave(isDraft, lecturerId, lecturerName, s, invoke)
    }

    private suspend fun android.content.Context.courseForSave(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Course) -> Unit,
        failed: () -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        uploadImage(lecturerId, { imageUri ->
            uploadBriefVideo(lecturerId, { briefVideo ->
                uploadTimelineVideoSave(lecturerId, { timelines ->
                    Course(
                        title = s.courseTitle,
                        lecturerName = lecturerName,
                        lecturerId = lecturerId,
                        price = s.price,
                        imageUri = imageUri,
                        about = s.about.toAboutCourse(),
                        briefVideo = briefVideo,
                        timelines = timelines.toTimeline(),
                        isDraft = if (isDraft) 1 else -1,
                        lastEdit = System.currentTimeMillis(),
                    ).let {
                        invoke.invoke(it)
                    }
                }, failed)
            }, failed)
        }, failed)
    }

    private fun android.content.Context.doSave(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Course?) -> Unit
    ) {
        this@CreateCourseViewModel.launch {
            courseForSave(isDraft, lecturerId, lecturerName, s, { course ->
                this@CreateCourseViewModel.launch {
                    project.course.insertCourse(
                        course
                    ).let {
                        if (it.value != null) {
                            val courseId = it.value._id.toHexString()
                            subscribeToTopic(courseId) {
                                pushNotification(
                                    topicId = it.value.lecturerId,
                                    msgTitle = "New Course",
                                    message = "${course.lecturerName} start new Course",
                                    argOne = it.value._id.toHexString(),
                                    argTwo = it.value.title,
                                )
                            }
                        }
                        _state.value = state.value.copy(isProcessing = false, isDraftProcessing = false)
                        invoke.invoke(it.value)
                    }
                }
            }, {
                invoke.invoke(null)
            })
        }
    }

    fun android.content.Context.edit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        invoke: (Course?) -> Unit
    ) {
        val s = _state.value
        if (
            (isDraft && (s.courseTitle.isEmpty() || s.price.isEmpty())) ||
            (!isDraft && (
                    s.courseTitle.isEmpty() ||
                            s.about.map { it.text }.isEmpty() ||
                            s.briefVideo.isEmpty() ||
                            s.imageUri.isEmpty() ||
                            s.timelines.isEmpty())
                    )
        ) {
            _state.value = state.value.copy(isErrorPressed = true)
            return
        }
        _state.value = state.value.copy(isProcessing = !isDraft, isDraftProcessing = isDraft)
        doEdit(isDraft, lecturerId, lecturerName, s, invoke)
    }

    private suspend fun android.content.Context.courseForEdit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Course) -> Unit,
        failed: () -> Unit,
    ) = kotlinx.coroutines.coroutineScope {
        val c = s.course ?: kotlin.run {
            failed.invoke()
            return@coroutineScope
        }
        uploadImage(lecturerId, { imageUri ->
            uploadBriefVideo(lecturerId, { briefVideo ->
                uploadTimelineVideoEdit(lecturerId, c, { timelines ->
                    Course(
                        title = s.courseTitle,
                        lecturerName = lecturerName,
                        lecturerId = lecturerId,
                        price = s.price,
                        imageUri = imageUri,
                        about = s.about.toAboutCourse(),
                        briefVideo = briefVideo,
                        timelines = timelines.toTimeline(),
                        lastEdit = System.currentTimeMillis(),
                        isDraft = if (isDraft) 1 else -1,
                        _id = ObjectId.invoke(c.id)
                    ).let {
                        invoke.invoke(it)
                    }
                }, failed)
            }, failed)
        }, failed)
    }

    private fun android.content.Context.doEdit(
        isDraft: Boolean,
        lecturerId: String,
        lecturerName: String,
        s: State,
        invoke: (Course?) -> Unit
    ) {
        this@CreateCourseViewModel.launch {
            courseForEdit(isDraft, lecturerId = lecturerId, lecturerName = lecturerName, s = s, { course ->
                doEditCourse(s = s, course = course, invoke = invoke)
            }, {
                invoke.invoke(null)
            })
        }
    }

    private fun doEditCourse(s: State, course: Course, invoke: (Course?) -> Unit) {
        this@CreateCourseViewModel.launch l@{
            project.course.editCourse(
                Course(s.course ?: return@l),
                course
            ).let {
                if (it.value != null) {
                    pushNotification(
                        topicId = it.value.lecturerId,
                        msgTitle = "Check what's new in the article",
                        message = "${it.value.lecturerName} edit ${it.value.title}",
                        argOne = it.value._id.toHexString(),
                        argTwo = it.value.title,
                    )
                }
                _state.value = state.value.copy(isProcessing = false, isDraftProcessing = false)
                invoke.invoke(it.value)
            }
        }
    }

    fun setCourseTitle(it: String) {
        _state.value = state.value.copy(courseTitle = it, isErrorPressed = false)
    }

    fun setPrice(it: String) {
        _state.value = state.value.copy(price = it, isErrorPressed = false)
    }

    fun makeFontDialogVisible() {
        _state.value = state.value.copy(isFontDialogVisible = true)
    }

    fun addAbout(type: Int) {
        val list = state.value.about.toMutableList()
        list.add(AboutCourseData(text = "", font = if (type == 0) 14 else 22))
        _state.value = state.value.copy(about = list, isFontDialogVisible = false, isErrorPressed = false)
    }

    fun removeAboutIndex(index: Int) {
        val list = state.value.about.toMutableList()
        list.removeAt(index)
        _state.value = state.value.copy(about = list, dummy = state.value.dummy + 1)
    }

    fun changeAbout(it: String, index: Int) {
        val list = state.value.about.toMutableList()
        list[index] = list[index].copy(text = it)
        _state.value = state.value.copy(about = list, dummy = state.value.dummy + 1)
    }

    fun setBriefVideo(it: String) {
        _state.value = state.value.copy(briefVideo = it, isErrorPressed = false)
    }

    fun setImageUri(it: String) {
        _state.value = state.value.copy(imageUri = it, isErrorPressed = false)
    }

    fun setTitleTimeline(it: String) {
        _state.value = state.value.run {
            copy(timelineData = timelineData.copy(title = it), isDialogPressed = false)
        }
    }

    fun setDegreeTimeline(it: Int) {
        _state.value = state.value.run {
            copy(timelineData = timelineData.copy(degree = it), isDialogPressed = false)
        }
    }

    fun setDurationTimeLine(it: String) {
        _state.value = state.value.run {
            copy(timelineData = timelineData.copy(duration = it), isDurationDialogVisible = false, isDialogPressed = false)
        }
    }

    fun setVideoTimeLine(it: String) {
        _state.value = state.value.run {
            copy(timelineData = timelineData.copy(video = it), isDialogPressed = false)
        }
    }

    fun setDurationDialogVisible(it: Boolean) {
        _state.value = state.value.copy(isDurationDialogVisible = it, isDialogPressed = false)
    }

    fun displayDateTimePicker() {
        _state.value = state.value.copy(dateTimePickerMode = 1, isDialogPressed = false)
    }

    fun displayTimePicker() {
        _state.value = state.value.copy(dateTimePickerMode = 2, isDialogPressed = false)
    }

    fun closeDateTimePicker() {
        _state.value = state.value.copy(dateTimePickerMode = 0, isDialogPressed = false)
    }

    fun confirmTimelineDateTimePicker(selectedDateMillis: Long) {
        _state.value = state.value.run {
            copy(dateTimePickerMode = 0, timelineData = timelineData.copy(date = selectedDateMillis), isDialogPressed = false)
        }
    }

    fun setTimelineNote(it: String) {
        _state.value = state.value.run {
            copy(timelineData = timelineData.copy(note = it), isErrorPressed = false)
        }
    }

    fun setIsExam(isExam: Boolean) {
        _state.value = state.value.copy(
            isDialogPressed = false,
            timelineData = TimelineData("", -1L, "", "", "", if (isExam) 1 else 0, 0),
            timelineIndex = -1,
        )
    }

    fun changeUploadDialogGone(it: Boolean) {
        _state.value = state.value.copy(isConfirmDialogVisible = it)
    }

    fun makeDialogGone() {
        _state.value = state.value.copy(
            dialogMode = 0,
            timelineData = TimelineData("", -1L, "", "", "", 0, 0),
            timelineIndex = -1,
            isDialogPressed = false,
            isErrorPressed = false
        )
    }

    fun makeDialogVisible(timeline: TimelineData?, index: Int = -1) {
        timeline?.also {
            _state.value = state.value.copy(
                dialogMode = 2,
                timelineData = timeline,
                timelineIndex = index,
                isDialogPressed = false,
                isErrorPressed = false
            )
        } ?: run {
            _state.value = state.value.copy(
                dialogMode = 1,
                isErrorPressed = false,
                isDialogPressed = false
            )
        }
    }

    fun addEditTimeline() {
        val s = state.value
        if (s.timelineData.date == -1L || s.timelineData.title.isEmpty() ||
            (s.timelineData.isExam && (s.timelineData.duration.isEmpty())) ||
            (!s.timelineData.isExam && (s.timelineData.video.isEmpty()))
        ) {
            _state.value = s.copy(isDialogPressed = true)
            return
        }
        if (s.dialogMode == 1) {
            addTimeline(s)
        } else {
            editTimeline(s)
        }
    }

    fun deleteTimeLine(i: Int) {
        val timelines = state.value.timelines.toMutableList()
        timelines.removeAt(i)
        _state.value = state.value.copy(timelines = timelines)
    }

    private fun addTimeline(s: State) {
        val timelines = s.timelines.toMutableList()
        timelines.add(
            s.timelineData
        )
        timelines.sortBy { it.date }
        _state.value = state.value.copy(
            dialogMode = 0,
            timelines = timelines,
            isErrorPressed = false,
            isDialogPressed = false,
            timelineData = TimelineData("", -1L, "", "", "", 0, 0),
            timelineIndex = -1,
        )
    }

    private fun editTimeline(s: State) {
        val timelines = s.timelines.toMutableList()
        timelines[s.timelineIndex] = s.timelineData
        timelines.sortBy { it.date }
        _state.value = state.value.copy(
            dialogMode = 0,
            timelines = timelines,
            isErrorPressed = false,
            isDialogPressed = false,
            timelineData = TimelineData("", -1L, "", "", "", 0, 0),
            timelineIndex = -1,
        )
    }

    private fun android.content.Context.uploadTimelineVideoEdit(
        lecturerId: String,
        course: CourseForData,
        invoke: (MutableList<TimelineData>) -> Unit,
        failed: () -> Unit
    ) {
        val s = state.value
        course.timelines.map { it.video }.let { currentVideos ->
            s.timelines.filter {
                !currentVideos.contains(it.video)
            }
        }.also { new ->
            if (new.isEmpty()) {
                invoke.invoke(s.timelines)
                return
            }
            new.onEachIndexed { i, it ->
                doUploadTmeLineVideo(lecturerId, android.net.Uri.parse(it.video), { str ->
                    s.timelines[i] = s.timelines[i].copy(video = str)
                    if (i == new.lastIndex) {
                        invoke.invoke(s.timelines)
                    }
                }, failed)
            }
        }
    }

    private fun android.content.Context.uploadTimelineVideoSave(
        lecturerId: String,
        invoke: (MutableList<TimelineData>) -> Unit,
        failed: () -> Unit
    ) {
        val s = state.value
        val empty = s.timelines.toMutableList()
        empty.onEachIndexed { i, it ->
            doUploadTmeLineVideo(lecturerId, android.net.Uri.parse(it.video), {
                s.timelines[i] = s.timelines[i].copy(video = it)
                if (i == empty.lastIndex) {
                    invoke.invoke(s.timelines)
                }
            }, failed)
        }
    }

    private fun android.content.Context.doUploadTmeLineVideo(lecturerId: String, uri: android.net.Uri, invoke: (String) -> Unit, failed: () -> Unit) {
        this@CreateCourseViewModel.launch {
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                project.fireApp.upload(uri, lecturerId + "/" + "VT_" + System.currentTimeMillis() + getMimeType(uri), {
                    invoke.invoke(it)
                }, {
                    failed.invoke()
                })
            }
        }
    }

    private fun android.content.Context.uploadImage(lecturerId: String, invoke: (String) -> Unit, failed: () -> Unit) {
        val s = state.value
        val courseUri = s.course?.imageUri
        if (s.imageUri != courseUri && s.imageUri.isNotEmpty()) {
            val uri = android.net.Uri.parse(s.imageUri)
            this@CreateCourseViewModel.launch {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    kotlinx.coroutines.coroutineScope {
                        if ((courseUri ?: return@coroutineScope).contains("https")) {
                            project.fireApp.deleteFile(courseUri)
                        } else return@coroutineScope
                    }
                    project.fireApp.upload(uri, lecturerId + "/" + "IMG_" + System.currentTimeMillis() + getMimeType(uri), {
                        invoke.invoke(it)
                        _state.value = state.value.copy(imageUri = it, isErrorPressed = false)
                    }, {
                        failed.invoke()
                    })
                }
            }
        } else {
            invoke.invoke(s.imageUri)
        }
    }

    private fun android.content.Context.uploadBriefVideo(lecturerId: String, invoke: (String) -> Unit, failed: () -> Unit) {
        val s = state.value
        val briefUri = s.course?.briefVideo
        if (s.briefVideo != briefUri && s.briefVideo.isNotEmpty()) {
            val uri = android.net.Uri.parse(s.briefVideo)
            this@CreateCourseViewModel.launch {
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    kotlinx.coroutines.coroutineScope {
                        if ((briefUri ?: return@coroutineScope).contains("https")) {
                            project.fireApp.deleteFile(briefUri)
                        } else return@coroutineScope
                    }
                    project.fireApp.upload(uri, lecturerId + "/" + "V" + System.currentTimeMillis() + getMimeType(uri), {
                        invoke.invoke(it)
                        _state.value = state.value.copy(briefVideo = it, isErrorPressed = false)
                    }, {
                        failed.invoke()
                    })
                }
            }
        } else {
            invoke.invoke(s.briefVideo)
        }
    }

    private fun pushNotification(
        topicId: String,
        msgTitle: String,
        message: String,
        argOne: String,
        argTwo: String,
    ) {
        this@CreateCourseViewModel.launch {
            subscribeToTopic(topicId) {

            }
            project.notificationRepo.postNotification(
                PushNotification(
                    to = "/topics/$topicId",
                    topic = "/topics/$topicId",
                    data = NotificationData(
                        title = msgTitle,
                        message = message,
                        routeKey = COURSE_SCREEN_ROUTE,
                        argOne = argOne,
                        argTwo = argTwo,
                        argThree = COURSE_MODE_STUDENT,
                    )
                )
            ).let { response ->
                if (response.isSuccessful) {
                    logger("pushNotification", "Response: ${com.google.gson.Gson().toJson(response)}")
                } else {
                    logger("pushNotification", "Response: ${response.errorBody()}")
                }
            }
        }
    }

    data class State(
        val course: CourseForData? = null,
        val about: List<AboutCourseData> = mutableListOf<AboutCourseData>().apply { add(AboutCourseData(text = "", font = 22)) },
        val timelines: MutableList<TimelineData> = mutableListOf(),
        val timelineData: TimelineData = TimelineData("", -1L, "", "", "", 0, 0),
        val courseTitle: String = "",
        val price: String = "",
        val briefVideo: String = "",
        val imageUri: String = "",
        val timelineIndex: Int = -1,
        val isErrorPressed: Boolean = false,
        val isProcessing: Boolean = false,
        val isDraftProcessing: Boolean = false,
        val dialogMode: Int = 0,
        val dateTimePickerMode: Int = 0,
        val courseTimePickerMode: Int = 0,
        val isDialogPressed: Boolean = false,
        val isDurationDialogVisible: Boolean = false,
        val isConfirmDialogVisible: Boolean = false,
        val isFontDialogVisible: Boolean = false,
        val dummy: Int = 0,
    )

}