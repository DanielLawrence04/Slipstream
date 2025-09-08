package com.example.formula1.AppComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.formula1.R


@Composable
fun LoginPageTitleText(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 24.sp,
            color = Color.White,
            fontFamily = FontFamily(
                Font(R.font.audiowide_regular)
            )
        )
    )
}

@Composable
fun LoginHeaderComposable(resourceId: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Chevron inside a circle on the left side
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colorResource(id = R.color.light_red), shape = CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                tint = Color.Red,
                contentDescription = "Back",
                modifier = Modifier.size(24.dp)
            )
        }

        // Image in the center of the row
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.CenterVertically)
        )

        // Spacer to maintain symmetry
        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(75.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = "Google Icon",
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun EmailOutlinedTextField(email: String, onValueChange: (String) -> Unit, isError: Boolean) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = email,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(colorResource(R.color.light_red), shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.email_icon_red),
                contentDescription = "Email Icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (email .isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.Red,
                        contentDescription = "Clear Email"
                    )
                }
            }
        },
        placeholder = { RegularText("Email", 16.sp, Color.Red) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_red),
            unfocusedContainerColor = colorResource(R.color.light_red),
            focusedBorderColor = if (isError) Color.Red else Color.Transparent,
            unfocusedBorderColor = if (isError) Color.Red else Color.Transparent,
            errorBorderColor = Color.Red
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular))),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun PasswordOutlinedTextField(password: MutableState<String>, placeholder: String, isError: Boolean) {
    val focusManager = LocalFocusManager.current
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password.value,
        onValueChange = {  password.value = it },
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .background(colorResource(R.color.light_red), shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.lock_icon_red),
                contentDescription = "Lock Icon",
                modifier = Modifier.size(20.dp)
            )
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(
                    imageVector = if (passwordVisible.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    tint = Color.Red,
                    contentDescription = if (passwordVisible.value) "Hide Password" else "Show Password"
                )
            }
        },
        placeholder = { RegularText(placeholder, 16.sp, Color.Red) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_red),
            unfocusedContainerColor = colorResource(R.color.light_red),
            focusedBorderColor = if (isError) Color.Red else Color.Transparent,
            unfocusedBorderColor = if (isError) Color.Red else Color.Transparent,
            errorBorderColor = Color.Red
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular))),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )

}

@Composable
fun NameOutlinedTextField(name: String, onValueChange: (String) -> Unit, isError: Boolean) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = name,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(colorResource(R.color.light_red), shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.user_icon_red),
                contentDescription = "User Icon",
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (name .isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.Red,
                        contentDescription = "Clear Name"
                    )
                }
            }
        },
        placeholder = { RegularText("Full Name", 16.sp, Color.Red) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_red),
            unfocusedContainerColor = colorResource(R.color.light_red),
            focusedBorderColor = if (isError) Color.Red else Color.Transparent,
            unfocusedBorderColor = if (isError) Color.Red else Color.Transparent,
            errorBorderColor = Color.Red
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black, fontFamily = FontFamily(Font(R.font.dmsans_regular))),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun RememberMeRow(onRememberMeChange: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(true) }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onRememberMeChange(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Red,
                    uncheckedColor = colorResource(R.color.light_red),
                    checkmarkColor = Color.White
                )
            )
            Spacer(Modifier.width(2.dp))
            RegularText("Remember me", 14.sp, colorResource(R.color.secondary_text))
        }
        RegularText("Forgot Password", 14.sp, Color.Red)
    }
}

@Composable
fun LoginAndRegisterButton(text: String, cornerRadius: Dp, opacity: Float, onContinueClick: () -> Unit) {
    Button(
        onClick = { onContinueClick() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(opacity))
    ) {
        MediumText(text, fontSize = 16.sp, Color.White)
    }
}

@Composable
fun ContinueWithComposable() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(4.dp),
            color = colorResource(R.color.secondary_text)
        )
        Text(
            text = "or connect with",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(
                    Font(R.font.dmsans_regular)
                )
            ),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(4.dp),
            color = colorResource(R.color.secondary_text)
        )
    }
}

@Composable
fun LoginFooterComposable(firstText: String, secondText: String, textClicked: () -> Unit) {
    RegularText(firstText, 16.sp, colorResource(R.color.secondary_text))
    Spacer(modifier = Modifier.width(5.dp))
    Text(
        text = secondText,
        style = TextStyle(fontSize = 16.sp,color = Color.Red,fontFamily = FontFamily(Font(R.font.dmsans_medium)), textDecoration = TextDecoration.Underline),
        modifier = Modifier.clickable { textClicked() }
    )
}

@Composable
fun RegistrationCompleteDialogBox(onContinue: () -> Unit) {
    Dialog(onDismissRequest = { onContinue() }) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .background(Color(0xFF8BC349)).height(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.checkbox_white_icon),
                            contentDescription = "Checkbox",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    MediumText(text = "SUCCESS", color = Color.White, fontSize = 16.sp)
                }
                Spacer(Modifier.height(50.dp))
                RegularText(
                    text = "Congratulations, your account has been successfully created.",
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(25.dp))
                Button(
                    onClick = { onContinue() },
                    modifier = Modifier
                        .width(450.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC349))
                ) {
                    MediumText("Continue", fontSize = 16.sp, Color.White)
                }
                Spacer(Modifier.height(25.dp))
            }
        }
    }
}






@Composable
fun RegularText(text: String, fontSize: TextUnit, color: Color) {
    Text(text = text,style = TextStyle(fontSize = fontSize,color = color,fontFamily = FontFamily(Font(R.font.dmsans_regular))))
}

@Composable
fun MediumText(text: String, fontSize: TextUnit, color: Color) {
    Text(text = text,style = TextStyle(fontSize = fontSize,color = color,fontFamily = FontFamily(Font(R.font.dmsans_medium))))
}

@Composable
fun BoldText(text: String, fontSize: TextUnit, color: Color) {
    Text(text = text,style = TextStyle(fontSize = fontSize,color = color,fontFamily = FontFamily(Font(R.font.dmsans_bold))))
}

@Composable
fun RussoOneText(text: String, fontSize: TextUnit, color: Color) {
    Text(text = text,style = TextStyle(fontSize = fontSize,color = color,fontFamily = FontFamily(Font(R.font.russo_one_regular))))
}


