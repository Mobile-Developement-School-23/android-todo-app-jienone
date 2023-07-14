package com.example.yandextodo.view.screens.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yandextodo.R
import com.example.yandextodo.view.screens.detail.composables.DeleteButton
import com.example.yandextodo.view.screens.detail.composables.DropDownMenu
import com.example.yandextodo.view.screens.detail.composables.PickDeadline
import com.example.yandextodo.view.screens.detail.composables.UserInput


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailFragmentUI() {
    Scaffold(
        Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                    ){

                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = R.string.content_desc_back_to_previous_screen.toString(),
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                    TextButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = "Сохранить", color = Color.Black)
                    }
                }
            }
        }
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(8.dp)
                    .background(MaterialTheme.colors.secondaryVariant)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                UserInput(onMessageSent = {})

            }

            Text(
                modifier = Modifier.padding(top=12.dp, start = 16.dp),
                text = "Важность",
                fontSize = 16.sp
            )
            DropDownMenu()
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            PickDeadline()

            Divider(
                Modifier.padding(16.dp)
            )

            DeleteButton {}
        }

    }
}

@Preview
@Composable
fun DetailPrev() {
    DetailFragmentUI()
}