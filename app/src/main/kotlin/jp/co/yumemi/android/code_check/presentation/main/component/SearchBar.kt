package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.code_check.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit = {},
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(16.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White),
        value = text,
        onValueChange = { text = it },
        placeholder = {
            Text(
                modifier = Modifier
                    .padding(0.dp),
                text = stringResource(R.string.searchInputText_hint),
                maxLines = 1,
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = Color.Black,
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(text)
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(4.dp),
                imageVector = Icons.Default.Search,
                contentDescription = "Search Mark",
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        text = ""
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.Gray.copy(alpha = 0.7f)),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close All",
                        tint = Color.White,
                    )
                }
            }
        },
        singleLine = true,
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchBar()
}
