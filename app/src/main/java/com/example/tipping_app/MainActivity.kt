package com.example.tipping_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipping_app.ui.theme.TippingAppTheme
import java.text.NumberFormat
import androidx.compose.material3.Icon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TippingAppTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()) {
                    TippingApp()
                }
            }
        }
    }
}

@Composable
fun TippingMainBody(modifier: Modifier = Modifier) {
    var amountInput by remember {
        mutableStateOf("")
    }

    var tipInput by remember {
        mutableStateOf("")
    }

    var roundUp by remember {
        mutableStateOf(false)
    }

    var tipAmount = tipInput.toDoubleOrNull() ?: 0.0
    var amount = amountInput.toDoubleOrNull() ?: 0.0

    val tip = calculateTip(amount, tipAmount, roundUp)

    Column (modifier = modifier
        .verticalScroll(rememberScrollState())
        .safeDrawingPadding()
        .padding(horizontal = 40.dp)
        .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(
            stringResource(id = R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start),
            color = Color.Gray
        )

        EditTextField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            value = amountInput,
            onValueChange = {amountInput = it},
            label = R.string.bill_amount,
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = R.drawable.money
        )

        EditTextField(modifier = Modifier
            .fillMaxWidth(),
            value = tipInput,
            onValueChange = { tipInput = it },
            label = R.string.how_was_the_service,
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = R.drawable.percent
        )

        RoundTheTipRow(modifier = Modifier, roundUp = roundUp, onRoundUpChanged = { roundUp = it })

        Text(
            stringResource(id = R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer (modifier = Modifier.height(150.dp))
    }
}

@Composable
fun RoundTheTipRow(modifier: Modifier = Modifier, roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit) {
    Row(modifier = modifier
        .padding(top = 15.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(R.string.round_up_tip))
        Switch(checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End))
    }
}

@Composable
fun EditTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit, @StringRes label: Int, keyboardOptions: KeyboardOptions, @DrawableRes leadingIcon: Int) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) }
    )
}

@Preview(showBackground = true)
@Composable
fun TippingApp() {
    TippingMainBody()
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = amount * tipPercent / 100
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}