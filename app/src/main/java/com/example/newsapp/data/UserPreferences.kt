import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Для удобства используем расширение для контекста
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context: Context) {
    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")

    // Чтение userId
    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Чтение состояния темной темы
    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false // По умолчанию светлая тема
    }

    // Сохранение userId
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Очистка userId (для выхода)
    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }

    // Сохранение состояния темной темы
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}
