package com.curso.free.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.curso.free.global.base.backDarkThr
import com.curso.free.global.base.darker
import com.curso.free.global.base.textColor
import com.curso.free.global.base.textForPrimaryColor

@Composable
fun ChatView(
    isEnabled: Boolean,
    chatText: String,
    onTextChanged: (String) -> Unit,
    list: List<com.curso.free.data.model.MessageForData>,
    isUserMessage: (com.curso.free.data.model.MessageForData) -> Boolean,
    send: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(list) {
        if (list.lastIndex > 1) {
            scrollState.scrollToItem(list.lastIndex)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(start = 5.dp, end = 5.dp, bottom = 7.dp),
            state = scrollState,
        ) {
            items(list) {
                val isThatUserMessage = isUserMessage.invoke(it)
                val colorCard = if (it.fromStudent) {
                    if (isThatUserMessage) MaterialTheme.colorScheme.secondary else isSystemInDarkTheme().backDarkThr
                } else {
                    MaterialTheme.colorScheme.primary
                }
                val colorText = if (it.fromStudent) {
                    if (isThatUserMessage) isSystemInDarkTheme().textForPrimaryColor else isSystemInDarkTheme().textColor
                } else {
                    isSystemInDarkTheme().textForPrimaryColor
                }
                Box {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(if (isThatUserMessage) Alignment.CenterStart else Alignment.CenterEnd)
                    )
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .widthIn(0.dp, 300.dp)
                            .align(if (!isThatUserMessage) Alignment.CenterStart else Alignment.CenterEnd)
                            .padding(5.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = colorCard,
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp,
                    ) {
                        Column {
                            if (it.fromStudent) {
                                Text(
                                    "From: ${it.senderName}",
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 2.5.dp)
                                        .alpha(0.8F),
                                    color = if (isThatUserMessage) isSystemInDarkTheme().textForPrimaryColor else isSystemInDarkTheme().textColor,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    it.message,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp, top = 2.5.dp, bottom = 10.dp),
                                    color = colorText,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            } else {
                                Text(
                                    it.message,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(10.dp),
                                    color = colorText,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isEnabled,
                shape = RoundedCornerShape(8.dp),
                trailingIcon = {
                    IconButton(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .align(Alignment.CenterVertically)
                            .padding(5.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background.darker(0.3F),
                                shape = CircleShape
                            ),
                        onClick = send
                    ) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = "",
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background.darker(0.3F),
                    unfocusedContainerColor = MaterialTheme.colorScheme.background.darker(0.3F),
                ),
                value = chatText,
                onValueChange = onTextChanged,
                singleLine = false
            )
        }
    }
}
