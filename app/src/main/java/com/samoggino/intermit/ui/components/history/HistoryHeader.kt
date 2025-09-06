package com.samoggino.intermit.ui.components.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.samoggino.intermit.R

@Composable
fun HistoryHeader(
    selectedCount: Int,
    onDeleteClick: () -> Unit
) {
    Button(
        onClick = onDeleteClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selectedCount > 0) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            if (selectedCount > 0) "Elimina ($selectedCount)"
            else stringResource(R.string.delete_all_sessions)
        )
    }
}

