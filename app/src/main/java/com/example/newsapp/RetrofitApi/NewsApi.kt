import com.example.newsapp.models.ConstValue
import com.example.newsapp.models.NewsRespons
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("country")
        countryCode : String = "us",
        @Query("page")
        pageNumber : Int = 1,
        @Query("sortBy")
        published: String = "publishedAt",
        @Query("category")
        category: String = "",
        @Query("apiKey")
        apiKey: String = ConstValue.API_KEY
    ) : Response<NewsRespons>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("searchIn")
        searchIn : String = "title",
        @Query("page")
        pageNumber : Int = 1,
        @Query("sortBy")
        published: String = "publishedAt",
        @Query("language")
        lang : String = "ru",
        @Query("apiKey")
        apiKey: String = ConstValue.API_KEY
    ) : Response<NewsRespons>

}