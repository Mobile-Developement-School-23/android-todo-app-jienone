package com.example.yandextodo.view.screens.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropDownMenu() {
    val listItems = arrayOf("Нет", "Средний", "Важный")
    var expanded by remember { mutableStateOf(false) }

    // remember the selected item
    var selectedItem by remember { mutableStateOf(listItems[0]) }

    Column(
        modifier = Modifier
        .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            Text(
                text = selectedItem,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listItems.forEach { selectedOption ->
                    DropdownMenuItem(onClick = {
                        selectedItem = selectedOption
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }

    }
}
