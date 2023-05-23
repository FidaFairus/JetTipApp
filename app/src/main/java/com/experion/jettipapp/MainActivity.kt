package com.experion.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalProvider

import androidx.compose.ui.platform.LocalSoftwareKeyboardController

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.experion.jettipapp.components.InputField
import com.experion.jettipapp.ui.theme.JetTipAppTheme
import com.experion.jettipapp.util.calculateTotalPerPerson
import com.experion.jettipapp.util.calculateTotalTip
import com.experion.jettipapp.widgets.RoundIconButton


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                //TopHeader()
                MainContent()
            }
        }
    }

    override fun setTheme(resid: Int) {
        super.setTheme(resid)
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {

    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: Double = 133.0) {
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    BillForm() { billamt ->
        Log.d("amt", "main contentt:$billamt")
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState= remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.value*100).toInt()
    val range = IntRange(start = 1, endInclusive = 100)
    val tipAmountState= remember {
        mutableStateOf(0.0)
    }
    val splitByState = remember {
        mutableStateOf(1)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    Column() {
        TopHeader(totalPerPerson = totalPerPersonState.value )

        Surface(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(6.dp), verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputField(valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    })
                if (validState) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Split",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(100.dp))
                        Row(
                            modifier = Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value =
                                        if(splitByState.value>1) {
                                            splitByState.value - 1
                                        }
                                    else 1
                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                            splitBy=splitByState.value,
                                            tipPercentage=tipPercentage)

                                },
                            )

                            Text(
                                text = "${splitByState.value}", modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(9.dp)
                            )
                            RoundIconButton(imageVector = Icons.Default.Add,
                                onClick = {
                                    if (splitByState.value<range.last){
                                        splitByState.value=splitByState.value+1
                                    }
                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                            splitBy=splitByState.value,
                                            tipPercentage=tipPercentage)
                                })
                        }
                    }
                    //tip row
                    Row(
                        modifier = Modifier.padding(24.dp),

                        ) {
                        Text(
                            text = "Tip",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Spacer(
                            modifier = Modifier.width(200.dp)
                        )

                        Text(
                            text = "$ ${tipAmountState.value}",
                            modifier = Modifier.align(Alignment.CenterVertically),

                            )

                    }
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$tipPercentage %")
                        Spacer(modifier = Modifier.height(10.dp))

                        //slider
                        Slider(modifier = Modifier.padding(start = 14.dp, end = 14.dp),value = sliderPositionState.value,
                            onValueChange = {newVal ->
                                sliderPositionState.value=newVal
                                tipAmountState.value=
                                    calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                                        tipPercentage)

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(totalBill = totalBillState.value.toDouble(),
                                    splitBy=splitByState.value,
                                    tipPercentage=tipPercentage)
                            })
                    }

                } else {

                }
            }
        }
    }

}






