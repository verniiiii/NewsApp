package com.example.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsapp.data.UserPreferences
import com.example.newsapp.screens.LoginScreen
import com.example.newsapp.screens.RegisterScreen
import com.example.newsapp.screens.SavedNewsScr
import com.example.newsapp.screens.SearchNews
import com.example.newsapp.screens.TopNewsScreen
import com.example.newsapp.screens.webViewScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)

        lifecycleScope.launch {
            val userId: Int? = userPreferences.userId.first()
            val startDestination = if (userId != null) topNewsScreen else loginScreen

            setContent {
                val navController = rememberNavController()
                val currentBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry.value?.destination?.route
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                NewsAppTheme(
                    darkTheme = false
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                DrawerMenu(
                                    navController = navController,
                                    userPreferences = userPreferences,
                                    drawerState = drawerState,
                                    scope = scope
                                )
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                if (currentRoute !in listOf(
                                        loginScreen::class.qualifiedName,
                                        registerScreen::class.qualifiedName,
                                    )) {
                                    TopAppBar(
                                        title = {
                                            when (currentRoute) {
                                                topNewsScreen::class.qualifiedName -> Text("Популярные новости")
                                                searchNews::class.qualifiedName -> Text("Поиск новостей")
                                                savedNews::class.qualifiedName -> Text("Сохранённые новости")
                                                else -> Text("Новость")
                                            }
                                        },
                                        navigationIcon = {
                                            IconButton(onClick = {
                                                scope.launch {
                                                    drawerState.open()
                                                }
                                            }) {
                                                Icon(Icons.Default.Menu, contentDescription = "Меню")
                                            }
                                        }
                                    )
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = startDestination,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable<loginScreen> {
                                    LoginScreen(navController)
                                }
                                composable<topNewsScreen> {
                                    TopNewsScreen(navController)
                                }
                                composable<searchNews> {
                                    SearchNews(navController)
                                }
                                composable<registerScreen> {
                                    RegisterScreen(navController)
                                }
                                composable<savedNews> {
                                    SavedNewsScr(navController)
                                }
                                composable<webView> { backStackEntry ->
                                    val url = backStackEntry.toRoute<webView>()
                                    webViewScreen(url.url)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerMenu(
    navController: NavController,
    userPreferences: UserPreferences = koinInject(),
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    // Закрытие меню после выбора пункта
    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    // Список пунктов меню
    val menuItems = listOf(
        MenuItem(
            id = "home",
            title = "Главная",
            icon = Icons.Default.Home,
            route = topNewsScreen
        ),
        MenuItem(
            id = "search",
            title = "Поиск новостей",
            icon = Icons.Default.Search,
            route = searchNews
        ),
        MenuItem(
            id = "saved",
            title = "Сохранённые новости",
            icon = Icons.Default.Favorite,
            route = savedNews
        ),
        MenuItem(
            id = "logout",
            title = "Выход",
            icon = Icons.Default.ExitToApp,
            route = loginScreen
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Заголовок меню
        Text(
            text = "Меню",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Разделитель
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        // Список пунктов меню
        LazyColumn {
            items(menuItems) { item ->
                NavigationDrawerItem(
                    label = { Text(item.title) },
                    selected = false,
                    onClick = {
                        if (item.id == "logout") {
                            scope.launch {
                                userPreferences.clearUserId()
                                navController.navigate(item.route) {
                                    popUpTo(topNewsScreen) { inclusive = true }
                                }
                            }
                        } else {
                            navController.navigate(item.route)
                        }
                        closeDrawer()
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

// Модель для пунктов меню
data class MenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val route: Any
)

// Компонент для отображения пункта меню
@Composable
fun DrawerItem(
    item: MenuItem,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


