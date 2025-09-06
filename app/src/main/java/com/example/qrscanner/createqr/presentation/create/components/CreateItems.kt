package com.example.qrscanner.createqr.presentation.create.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.createqr.presentation.create.model.QRCodeDetails
import com.example.qrscanner.createqr.presentation.create.model.qrCodeDetailsLists

@Composable
fun CreateItems(
    onClickItem: (QRCodeDetails) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(qrCodeDetailsLists) { qrItem ->
            Item(
                qrCodeDetails = qrItem,
                onClickItem = onClickItem,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Item(
    qrCodeDetails: QRCodeDetails,
    onClickItem: (QRCodeDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18))
            .background(Color.White)
            .clickable {
                onClickItem(qrCodeDetails)
            }
            .padding(
                horizontal = 12.dp,
                vertical = 22.dp
            )
           ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                imageVector = ImageVector.vectorResource(qrCodeDetails.icon),
                contentDescription = null
            )

            Text(
                text = qrCodeDetails.type.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Preview
@Composable
private fun ItemPreview() {
    QRScannerTheme {
        Item(
            qrCodeDetails = qrCodeDetailsLists.first(),
            onClickItem = {}
        )
    }
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        CreateItems(
            onClickItem = {}
        )
    }
}