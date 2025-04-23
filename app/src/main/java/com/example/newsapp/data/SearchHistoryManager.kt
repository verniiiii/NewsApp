import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SearchHistoryManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("search_history", Context.MODE_PRIVATE)

    private val historyKey = "search_history_key"

    // Получаем историю поиска
    fun getSearchHistory(): List<String> {
        val historyString = sharedPreferences.getString(historyKey, "")
        return if (historyString.isNullOrEmpty()) {
            emptyList()
        } else {
            historyString.split(",").take(10)
        }
    }

    // Добавление нового поискового запроса в историю
    fun addSearchQuery(query: String) {
        val history = getSearchHistory().toMutableList()

        if (query in history) {
            history.remove(query)
        }

        history.add(0, query)

        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }

        val historyString = history.joinToString(",")

        sharedPreferences.edit().putString(historyKey, historyString).apply()
    }

    fun clearSearchHistory() {
        sharedPreferences.edit().remove(historyKey).apply()
    }
}
