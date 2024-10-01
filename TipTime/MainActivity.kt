/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipInput(value: String, onValueChanged: (String) -> Unit, modifier: Modifier = Modifier){
    //generates a field accepting a tip amount
    TextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    //billInput.value gets the value from the State object
}

@Composable
fun PercentInput(value: String, onValueChanged: (String) -> Unit, modifier: Modifier = Modifier){
    //onValueChanged allows
    //generates a field accepting a tip amount
    TextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun TipTimeLayout() {
    var billInput by remember { mutableStateOf("0.00") }
    val amount = billInput.toDoubleOrNull() ?: 0.0
    var tipPercentage by remember { mutableStateOf("0.00") }
    //defaults to 20%
    val percent = tipPercentage.toDoubleOrNull() ?: 20.0
    val tip = calculateTip(amount, percent)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*Text(
            text = stringResource(R.string.eightdollartip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start),
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )*/
        Text(
            text = stringResource(R.string.calculate_tip),
            //use style variable as indicated in the last
            //text thing to get a better font
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start),
            style = MaterialTheme.typography.headlineMedium,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Serif,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.bill_amount),
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 24.dp)
                .align(alignment = Alignment.Start)
        )
        TipInput(billInput,{ billInput = it },modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth())

        Text(
            text = stringResource(R.string.percent_prompt),
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(bottom = 16.dp, top = 24.dp)
                .align(alignment = Alignment.Start)
        )
        PercentInput(tipPercentage, { tipPercentage = it }, modifier = Modifier
            .padding(bottom = 32.dp)
            .fillMaxWidth())

        Text(
            text = stringResource(R.string.tip_amount,tip),
            //style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.Serif,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(R.drawable.tipspaker),
            contentDescription = null
            /*Modifier
                .align(Alignment.TopCenter)
                .padding(60.dp)*/
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

/**
 * Calculates the tip based on the user input and format the tip amount
 * according to the local currency.
 * Example would be "$10.00".
 */
private fun calculateTip(amount: Double, tipPercent: Double): String {
    val tip = tipPercent / 100 * amount
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}
