package com.example.chatty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatty.Screens.ChatListScreen
import com.example.chatty.Screens.LoginScreen
import com.example.chatty.Screens.ProfileScreen
import com.example.chatty.Screens.SignUpScreen
import com.example.chatty.Screens.SingleChatScreen
import com.example.chatty.Screens.SingleStatusScreen
import com.example.chatty.Screens.StatusScreen
import com.example.chatty.ui.theme.ChattyTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreens(var route: String) {
    object SignUp : DestinationScreens("signup")
    object Login : DestinationScreens("login")
    object Profile : DestinationScreens("profile")
    object ChatList : DestinationScreens("chatList")
    object SingleChat : DestinationScreens("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }

    object StatusList : DestinationScreens("statusList")
    object SingleStatus : DestinationScreens("singleStatus/{userId}") {
        fun createRoute(id: String) = "singleStatus/$id"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChattyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        var vm = hiltViewModel<ChattyViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreens.SignUp.route) {
            composable(DestinationScreens.SignUp.route) {
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreens.Login.route) {
                LoginScreen(navController, vm)
            }
            composable(DestinationScreens.ChatList.route) {
                ChatListScreen(navController, vm)
            }
            composable(DestinationScreens.SingleChat.route) {
                val chatId=it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController, vm, chatId)
                }
            }
            composable(DestinationScreens.StatusList.route) {
                StatusScreen(navController, vm)
            }
            composable(DestinationScreens.SingleStatus.route) {
                val userId=it.arguments?.getString("userId")
                userId?.let {
                    SingleStatusScreen(navController, vm, userId = it)
                }
            }
            composable(DestinationScreens.Profile.route) {
                ProfileScreen(navController, vm)
            }
        }
    }
}