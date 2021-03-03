package com.example.myapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditState(
    var requestEdit: Boolean,
    var name: String,
    var nameHasBeenSet: Boolean = false
) {
    fun showMainmenu(): Boolean {
        return !requestEdit
    }
}


@ExperimentalMaterialApi
@Composable
fun MainComposable(
    onClickEdit: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    ModalBottomSheetLayout(
        scrimColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.32f),
        sheetShape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        sheetState = bottomSheetState,
        sheetContent = {
            SheetContentComposable(bottomSheetState = bottomSheetState)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Button(
                    onClick = onClickEdit,
                    modifier = Modifier.padding(top = 50.dp)
                ) {
                    Text(text = "Click me")
                }
            }
            item { Text(text = "Dummy") }
        }

    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditMenuComposable(
    onSubmit: (name: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        var currentEditState by remember {
            mutableStateOf(
                EditState(
                    requestEdit = false,
                    name = "",
                    nameHasBeenSet = false
                )
            )
        }

        val goToMainMenuInput = {
            currentEditState = EditState(
                requestEdit = false,
                name = "",
                nameHasBeenSet = false
            )
        }

        if (currentEditState.nameHasBeenSet) {
            onSubmit(currentEditState.name)
        }

        when {
            currentEditState.showMainmenu() -> {
                MenuComposable(
                    onClickEdit = {
                        currentEditState =
                            EditState(requestEdit = true, name = "", nameHasBeenSet = false)
                    }
                )
            }
            currentEditState.requestEdit -> {
                EditComposable(
                    onSubmit = { input ->
                        // save
                        currentEditState = EditState(
                            requestEdit = false,
                            name = input,
                            nameHasBeenSet = true
                        )
                    }
                )
            }

        }

//        backPressHandler(
//            enabled = currentEditState.nameHasBeenSet,
//            onBackPressed = {
//                when {
//                    currentEditState.nameHasBeenSet -> {
//                        goToMainMenuInput()
//                    }
//
//                }
//            }
//        )
    }
}

@ExperimentalMaterialApi
@Composable
fun MenuComposable(
    onClickEdit: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Button(
                onClick = onClickEdit,
                modifier = Modifier.padding(top = 50.dp)
            ) {
                Text(text = "Edit")
            }
        }


    }


}

@Composable
fun EditComposable(
    onSubmit: (name: String) -> Unit
) {
    var input by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = FocusRequester()
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Type something and then click submit without closing the keyboard")
        OutlinedTextField(
            value = input,
            onValueChange = { text ->
                if (text.text.length < 15 && text.text.count { it == '\n' } < 1) { // single line only
                    input = text
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        Button(
            onClick = {
                coroutineScope.launch {
                    delay(10)
                    onSubmit(input.text)
                }
            },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Submit")
        }
        DisposableEffect(true) {
            focusRequester.requestFocus()
            onDispose {  }
        }
    }

}


@ExperimentalMaterialApi
@Composable
fun SheetContentComposable(
    modifier: Modifier = Modifier,
    bottomSheetState: ModalBottomSheetState,
    ) {

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        item { 
            Text(text = "I shouldnt be seen")
        }
    }

}