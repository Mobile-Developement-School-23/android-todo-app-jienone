package com.example.yandextodo.view.screens.detail.composables

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yandextodo.R

@Composable
fun DropDownMenu() {
    val listItems = arrayOf("Нет", "Средний", "Важный")
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
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
//            Icon(
//                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
//                contentDescription = "Dropdown Icon",
//                tint = Color.Black,
//                modifier = Modifier.padding(start = 4.dp)
//            )
        }

        if (expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listItems.forEach { selectedOption ->
                    DropdownMenuItem(onClick = {
                        selectedItem = selectedOption
                        Toast.makeText(contextForToast, selectedOption, Toast.LENGTH_SHORT).show()
                        expanded = false
                    }) {
                        Text(text = selectedOption)
                    }
                }
            }
        }

    }
}
