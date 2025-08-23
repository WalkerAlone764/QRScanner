package com.example.qrscanner.qr_scan.presentation.result

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.qrscanner.core.presentation.util.ObserveAsEvents
import com.example.qrscanner.core.ui.theme.QRScannerTheme
import com.example.qrscanner.qr_scan.domain.model.BarcodeType
import com.example.qrscanner.qr_scan.presentation.result.components.ContactContent
import com.example.qrscanner.qr_scan.presentation.result.components.CopyShareRow
import com.example.qrscanner.qr_scan.presentation.result.components.GeoContent
import com.example.qrscanner.qr_scan.presentation.result.components.LinkContent
import com.example.qrscanner.qr_scan.presentation.result.components.PhoneContent
import com.example.qrscanner.qr_scan.presentation.result.components.ResultAppBar
import com.example.qrscanner.qr_scan.presentation.result.components.TextContent
import com.example.qrscanner.qr_scan.presentation.result.components.WifiContent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ResultRoot(
    onClickBack: () -> Unit,
    viewModel: ResultViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is ResultEvent.ClickCopy -> {
                scope.launch {
                    clipboard.copy(event.content)
                }
            }

            is ResultEvent.ClickShare -> {
                context.share(event.content)
            }

            is ResultEvent.ClickLink -> {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        event.link.toUri()
                    )
                )
            }
        }
    }

    ResultScreen(
        state = state,
        onAction = viewModel::onAction,
        onClickBack = onClickBack
    )
}

@Composable
fun ResultScreen(
    state: ResultState,
    onAction: (ResultAction) -> Unit,
    onClickBack: () -> Unit,
) {

    var qrHeight by remember {
        mutableIntStateOf(0)
    }

    val density = LocalDensity.current

    Scaffold(
        topBar = {
            ResultAppBar(
                onClickBack = onClickBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(innerPadding)
                .padding(top = 22.dp)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .wrapContentSize(),
                contentAlignment = Alignment.TopCenter
            ) {


                Column(
                    modifier = Modifier
                        .padding(top = with(density) { (qrHeight / 2).toDp() })
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(top = with(density) { (qrHeight / 2).toDp() })
                        .padding(
                            vertical = 26.dp,
                            horizontal = 18.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    when (state.type) {
                        BarcodeType.LINK -> {
                            LinkContent(
                                link = state.linkResultWrapper?.url ?: "",
                                onClickLink = {
                                    onAction(
                                        ResultAction.OnClickLink(
                                            state.linkResultWrapper?.url ?: ""
                                        )
                                    )
                                }
                            )
                        }

                        BarcodeType.CONTACT -> {
                            ContactContent(
                                name = state.contactResultWrapper?.formattedName ?: "",
                                email = state.contactResultWrapper?.email ?: "",
                                phone = state.contactResultWrapper?.phone ?: ""
                            )
                        }

                        BarcodeType.PHONE_NUMBER -> {
                            PhoneContent(
                                number = state.phoneResultWrapper?.phone ?: ""
                            )
                        }

                        BarcodeType.GEOLOCATION -> {
                            GeoContent(
                                latitude = state.geolocationResultWrapper?.lat.toString(),
                                longitude = state.geolocationResultWrapper?.long.toString()
                            )
                        }

                        BarcodeType.WIFI -> {
                            WifiContent(
                                ssid = state.wifiResultWrapper?.ssid ?: "",
                                password = state.wifiResultWrapper?.password ?: "",
                                encryptionType = state.wifiResultWrapper?.encryption ?: ""
                            )
                        }

                        BarcodeType.TEXT -> {
                            TextContent(
                                text = state.textResultWrapper?.text ?: "",
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CopyShareRow(
                        onClickCopy = {
                            onAction(ResultAction.OnClickCopy)
                        },
                        onClickShare = {
                            onAction(ResultAction.OnClickShare)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .onSizeChanged {
                            qrHeight = it.height
                        }
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(12.dp),

                            )
                ) {
                    state.bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null
                        )
                    }
                }
            }

        }
    }
}

private fun Context.share(
    text: String
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    this.startActivity(sendIntent)
}

private suspend fun Clipboard.copy(content: String) {
    setClipEntry(
        ClipEntry(
            ClipData.newPlainText(content, content)
        )
    )
}

@Preview
@Composable
private fun Preview() {
    QRScannerTheme {
        ResultScreen(
            state = ResultState(),
            onAction = {},
            onClickBack = {}
        )
    }
}