package com.curso.free.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textGrayColor
import com.curso.free.global.ui.EditButton
import com.curso.free.global.ui.ImageForCurveItem
import com.curso.free.global.ui.rememberAttachMoney
import com.curso.free.global.util.firstCapital
import com.curso.free.global.util.firstSpace
import com.curso.free.global.util.ifNotEmpty

@Composable
inline fun MainItem(title: String, imageUri: String, content: @Composable BoxScope.() -> Unit) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 80.dp)
        ) subBox@{
            Text(
                text = title,
                color = isSystemInDarkTheme().textColor,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 5.dp, vertical = 3.dp)
            )
            content.invoke(this@subBox)
        }
        ImageForCurveItem(imageUri, 80.dp)
    }
}


@Composable
inline fun MainItemEdit(
    title: String,
    imageUri: String,
    colorEdit: Color,
    textColorEdit: Color,
    crossinline bodyClick: () -> Unit,
    crossinline editClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterStart)
                .clickable {
                    bodyClick.invoke()
                }
        ) {
            ImageForCurveItem(imageUri, 80.dp)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 80.dp)
                    .align(Alignment.CenterStart)
            ) subBox@{
                Text(
                    text = title,
                    color = isSystemInDarkTheme().textColor,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 5.dp, vertical = 3.dp)
                )
                content.invoke(this@subBox)
            }
        }
        EditButton(
            colorEdit,
            textColorEdit,
        ) {
            editClick.invoke()
        }
    }
}

@Composable
fun BoxScope.AllCourseItem(lecturerName: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 50.dp)
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
                text = lecturerName.firstSpace.firstCapital,
                color = isSystemInDarkTheme().textGrayColor,
                fontSize = 10.sp,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
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
                text = price,
                color = isSystemInDarkTheme().textColor,
                fontSize = 10.sp,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun BoxScope.AllArticleIem(lecturerName: String, readers: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .wrapContentHeight()
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 50.dp),
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
                text = lecturerName.firstSpace.firstCapital,
                color = isSystemInDarkTheme().textGrayColor,
                fontSize = 10.sp,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Readers",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .width(15.dp)
                    .height(15.dp)
            )
            Text(
                text = readers,
                color = isSystemInDarkTheme().textColor,
                fontSize = 10.sp,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
        }
    }
}


@Composable
fun BoxScope.OwnCourseItem(nextTimeLine: String, students: String) {
    Column(
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Text(
            text = nextTimeLine,
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
                .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Student",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Text(
                    text = students,
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}


@Composable
fun BoxScope.OwnArticleItem(readers: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Readers",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(15.dp)
                .height(15.dp)
        )
        Text(
            text = readers,
            color = isSystemInDarkTheme().textColor,
            fontSize = 10.sp,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
        )
    }
}


@Composable
fun BoxScope.LecturerCourseItem(nextTimeLine: String, students: String, price: String) {
    Column(
        Modifier
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
    ) {
        Text(
            text = nextTimeLine,
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
                .padding(start = 15.dp, end = 15.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F),
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Student",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(15.dp)
                        .height(15.dp)
                )
                Text(
                    text = students,
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1F),
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
                    text = price,
                    color = isSystemInDarkTheme().textColor,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun BoxScope.TimelineItem(courseName: String, date: String, duration: String) {
    Column(
        modifier = Modifier.align(Alignment.BottomCenter)
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
                text = "Date: $date",
                color = isSystemInDarkTheme().textGrayColor,
                fontSize = 10.sp,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
            duration.ifNotEmpty {
                Text(
                    text = "Duration: $duration",
                    color = isSystemInDarkTheme().textGrayColor,
                    fontSize = 10.sp,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}