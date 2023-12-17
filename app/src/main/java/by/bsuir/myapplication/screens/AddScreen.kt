package by.bsuir.myapplication.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import by.bsuir.myapplication.AddEditViewModel
import by.bsuir.myapplication.DateDefaults
import by.bsuir.myapplication.HomeViewModel
import by.bsuir.myapplication.MaskVisualTransformation
import by.bsuir.myapplication.navigation.Screen
import by.bsuir.myapplication.ui.theme.secondary_color
import by.bsuir.myapplication.ui.theme.text_color
import by.bsuir.vitaliybaranov.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(navController: NavController, coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
    val viewModel = koinViewModel<AddEditViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.initViewModel(null)
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
            {
                Text(
                    text = stringResource(id = R.string.addNoteString),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //containerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = secondary_color,
                        textColor = MaterialTheme.colorScheme.primary,
                    ),
                    value = uiState.date,
                    onValueChange = {newText ->
                        if (newText.length <= DateDefaults.DATE_LENGTH) {
                            viewModel.setNoteDate(newText)
                        }
                    },
                    visualTransformation = MaskVisualTransformation(DateDefaults.DATE_MASK),
                    label = {
                        Text(text = stringResource(id = R.string.date_label),
                            color = text_color
                        )
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //containerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = secondary_color,
                        textColor = MaterialTheme.colorScheme.primary,
                    ),

                    value = uiState.goal,
                    onValueChange = { newText ->
                        viewModel.setNoteGoal(newText)
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.note_label),
                            color = text_color
                        )
                    },
                    maxLines = 7,

                    )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //containerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = secondary_color,
                        textColor = MaterialTheme.colorScheme.primary,
                    ),

                    value = uiState.temp,
                    onValueChange = { newText ->
                        viewModel.setNoteTemp(newText)
                    },
                    label = {
                        Text(
                            text = "Temperature",
                            color = text_color
                        )
                    },
                    maxLines = 7,
                    )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //containerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = secondary_color,
                        textColor = MaterialTheme.colorScheme.primary,
                    ),

                    value = uiState.maxwind,
                    onValueChange = { newText ->
                        viewModel.setNoteMaxWind(newText)
                    },
                    label = {
                        Text(
                            text = "Max wind",
                            color = text_color
                        )
                    },
                    maxLines = 7,
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        //containerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = secondary_color,
                        textColor = MaterialTheme.colorScheme.primary,
                    ),

                    value = uiState.condition,
                    onValueChange = { newText ->
                        viewModel.setNoteCondition(newText)
                    },
                    label = {
                        Text(
                            text = "Condition",
                            color = text_color
                        )
                    },
                    maxLines = 7,
                )

                Button(onClick = {
                    viewModel.saveNote()
                    coroutineScope.launch {
                        val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "New note added",
                            actionLabel = "Ok"
                        )
                    }
                    navController.navigate(Screen.MainScreen.route)

                }, enabled = uiState.temp!=""&&uiState.maxwind!=""&& uiState.condition!=""&& uiState.goal != "" && uiState.date != "" && uiState.date.length == 8,modifier = Modifier.padding(vertical = 16.dp), colors =  ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.primary)){
                    Text(text = stringResource(id = R.string.addNote), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                }

            }
        }
    }
}
