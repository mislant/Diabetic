package com.diabetic.ui.screen.component

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.diabetic.ui.screen.MainActivity
import com.diabetic.ui.screen.StatisticActivity
import com.diabetic.ui.theme.DiabeticMaterialTheme
import kotlin.reflect.KClass

@Composable
internal fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        windowInsets = BottomAppBarDefaults.windowInsets
    ) {
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxHeight()
                    .width(240.dp),
                Arrangement.SpaceAround
            ) {
                BottomButton(
                    icon = Icons.Default.WaterDrop,
                    text = "Основное",
                    toActivity = MainActivity::class
                )
                BottomButton(
                    icon = Icons.Default.BarChart,
                    text = "Статистика",
                    toActivity = StatisticActivity::class
                )
            }
        }
    }
}

@Composable
private fun BottomButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    toActivity: KClass<out ComponentActivity>
) {
    val ctx = LocalContext.current

    Column(
        modifier
            .fillMaxHeight()
            .width(105.dp)
            .clickable {
                if (ctx.getCurrentActivity()!!::class == toActivity) {
                    return@clickable
                }

                Intent(ctx, toActivity.java).also {
                    ctx.startActivity(it)
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(35.dp),
            imageVector = icon,
            contentDescription = null
        )
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

private fun Context.getCurrentActivity(): ComponentActivity? {
    return when (this) {
        is ComponentActivity -> this
        else -> null
    }
}

@Preview
@Composable
private fun BottomBarPreview() {
    DiabeticMaterialTheme {
        BottomBar()
    }
}