package edu.moravian.csci215.tic_tac_toe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AppBackgroundBrush =
    Brush.linearGradient(
        colors =
            listOf(
                Pine900,
                Pine800,
                Pine700,
            ),
    )

@Composable
fun AppBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(AppBackgroundBrush),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 28.dp, top = 20.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.08f))
                    .padding(horizontal = 76.dp, vertical = 76.dp),
        )
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 18.dp, bottom = 36.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Mint200.copy(alpha = 0.14f))
                    .padding(horizontal = 92.dp, vertical = 92.dp),
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            content = content,
        )
    }
}

@Composable
fun LuxeCard(
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(22.dp),
    expandContent: Boolean = false,
    content: @Composable BoxScope.() -> Unit,
) {
    Card(
        modifier =
            modifier
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(30.dp),
                ),
        shape = RoundedCornerShape(30.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f),
            ),
    ) {
        Box(
            modifier =
                (if (expandContent) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color.White.copy(alpha = 0.05f),
                                    Color.Transparent,
                                ),
                        ),
                    )
                    .padding(padding),
            content = content,
        )
    }
}
