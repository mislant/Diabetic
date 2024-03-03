package com.diabetic.ui.screen.main.component

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.theme.DiabeticMaterialTheme
import com.example.diabetic.R

private val cards = listOf(
    Card(
        R.drawable.card_test,
        "Test title",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sed fringilla arcu. Integer maximus vulputate orci, a maximus ipsum. Fusce sed varius velit. Nullam scelerisque ipsum vel eros scelerisque rhoncus. Mauris interdum eget ligula id accumsan. Donec iaculis egestas risus non malesuada. Quisque neque mauris, ultrices a gravida ac, congue mollis leo. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut porttitor magna et ligula aliquam tincidunt"
    ),
    Card(
        R.drawable.card_test,
        "Test title",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sed fringilla arcu. Integer maximus vulputate orci, a maximus ipsum. Fusce sed varius velit. Nullam scelerisque ipsum vel eros scelerisque rhoncus. Mauris interdum eget ligula id accumsan. Donec iaculis egestas risus non malesuada. Quisque neque mauris, ultrices a gravida ac, congue mollis leo. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut porttitor magna et ligula aliquam tincidunt"
    ),
    Card(
        R.drawable.card_test,
        "Test title",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sed fringilla arcu. Integer maximus vulputate orci, a maximus ipsum. Fusce sed varius velit. Nullam scelerisque ipsum vel eros scelerisque rhoncus. Mauris interdum eget ligula id accumsan. Donec iaculis egestas risus non malesuada. Quisque neque mauris, ultrices a gravida ac, congue mollis leo. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut porttitor magna et ligula aliquam tincidunt"
    ),
)

private data class Card(
    val image: Int,
    val title: String,
    val content: String
)


@Composable
fun EducationCards(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(cards) {
            EducationCard(card = it)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun EducationCard(card: Card) {
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            painter = painterResource(id = card.image),
            contentDescription = null,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .aspectRatio(3f / 2f)
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = card.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = card.content)
        }
    }
}

@Preview
@Composable
fun EducationCardsPreview() {
    DiabeticMaterialTheme {
        EducationCards()
    }
}

@Preview
@Composable
fun EducationCardPreview() {
    DiabeticMaterialTheme {
        EducationCard(cards[0])
    }
}