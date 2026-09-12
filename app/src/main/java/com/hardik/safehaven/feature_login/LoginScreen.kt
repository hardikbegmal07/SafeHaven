package com.hardik.safehaven.feature_login

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hardik.safehaven.R
import com.hardik.safehaven.feature_login.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onSignUpBtnClicked: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val context = LocalContext.current

    BackHandler {
        // Clear the entire task and exit the app
        (context as? Activity)?.finishAffinity()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFA9D3DC), Color(0xFF4980E0)),
                    startY = 0.7f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {

        CompanyName()

        HeaderTextView("Login")

        CustomTextField(
            "Username",
            loginViewModel.username,
            loginViewModel::onUsernameChanged,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        CustomTextField(
            label = "Password",
            value = loginViewModel.password,
            onValueChange = loginViewModel::onPasswordChanged,
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 30.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                "Forgot Password?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                color = Color(0xFF0E207E),
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { /* TODO */ })
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SubHeaderTextView("Sign Up", {
            onSignUpBtnClicked()
        })

        CommonButton("Continue", { onLoginSuccess() })
    }

}

@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel,
    onLoginUpBtnClicked: () -> Unit,
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFA9D3DC), Color(0xFF4980E0)),
                    startY = 0.7f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
    ) {

        CompanyName()

        HeaderTextView("Sign Up")

        CustomTextField(
            "Username",
            username,
            { username = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        CustomTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        CustomTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        SubHeaderTextView("Login", onLoginUpBtnClicked)

        CommonButton("Continue", { /* TODO */ })
    }

}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {

        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF0E207E),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 2.dp)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF0E207E),
                    shape = RoundedCornerShape(5.dp)
                ),

            singleLine = true,

            shape = RoundedCornerShape(5.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),

            keyboardOptions = keyboardOptions,

            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },

            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Outlined.Visibility
                            } else {
                                Icons.Outlined.VisibilityOff
                            },
                            contentDescription = if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            },
                            tint = Color(0xFF94A7C2),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else {
                null
            }
        )
    }
}


@Composable
fun CommonButton(title: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 80.dp)
            .height(55.dp),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonColors(
            containerColor = Color(0xFF0E207E),
            contentColor = Color.White,
            disabledContentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SubHeaderTextView(title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color(0xFF0E207E),
            modifier = Modifier
                .padding(20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick
                )
        )
    }
}

@Composable
fun HeaderTextView(title: String) {
    Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        color = Color(0xFF0E207E),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp)
    )
}

@Composable
fun CompanyName() {
    val name = stringResource(R.string.app_name)
    Text(
        buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            ) {
                append(name[0])
            }

            withStyle(
                style = SpanStyle(
                    fontSize = 30.sp,
                    color = Color(0xFF0E207E),
                    fontWeight = FontWeight.Bold
                )
            ) {
                for (i in 1 until name.length)
                    append(name[i])
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, bottom = 20.dp),
        textAlign = TextAlign.Center
    )
}