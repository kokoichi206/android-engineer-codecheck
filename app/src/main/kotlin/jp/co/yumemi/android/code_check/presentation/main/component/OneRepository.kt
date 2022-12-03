package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OneRepository(
    name: String,
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = name,
        fontSize = 16.sp
    )
    androidx.compose.material.Divider(
        modifier = Modifier
            .padding(0.dp),
        color = Color.Gray,
    )
}

@Preview
@Composable
fun OneRepositoryPreview() {
    OneRepository("kokoichi2/kokoichi2")
}
