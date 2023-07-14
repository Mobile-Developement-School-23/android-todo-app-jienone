@file:Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")

package com.example.yandextodo.view.screens.detail.composables
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yandextodo.core.utils.convertTimestampToDate
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDeadline() {
    val checkedState = remember { mutableStateOf(false) }
    var selectedDate: Long?

    // Create a state variable to hold the selected date millis
    val selectedDateMillis = remember { mutableStateOf<Long?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Сделать до",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checkedState.value,
                onCheckedChange = { checkedState.value = it }
            )
        }

        if (checkedState.value) {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {
                val datePickerState = rememberDatePickerState()
                val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
                DatePickerDialog(
                    onDismissRequest = {
                        checkedState.value = false
                        openDialog.value = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                selectedDate = datePickerState.selectedDateMillis
                                // Update the selected date millis state variable
                                selectedDateMillis.value = datePickerState.selectedDateMillis
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }

        // Display the selected date millis in the Text element

        val dateToString = convertTimestampToDate(selectedDateMillis.value)
        Text(text = dateToString ?: "", color = Color.Blue)
    }
}
