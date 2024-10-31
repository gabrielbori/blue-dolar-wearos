import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

@GET("dolar/v2/general") // Correct endpoint
@Headers(
    "sec-ch-ua-platform: macOS",
    "Referer: https://www.finanzasargy.com/",
    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36",
    "sec-ch-ua: Chromium;v=130, Google Chrome;v=130, Not?A_Brand;v=99",
    "api-client: finanzasargy",
    "sec-ch-ua-mobile: ?0"
)
suspend fun getCurrencyData(): Response<ApiResponse> // Update with the correct response type

}
data class CurrencyPanel(
    val titulo: String,
    val venta: String,
    val compra: String,
    val cierre: String,
    val historico: String,
    val fecha: String? = null, // Optional field for dates that may not be present
    val lastUpdate: String? = null // Optional for "Dólar Blue"
)
data class Advertisement(
    val titulo: String,
    val compra: String,
    val venta: String,
    val cierre: String,
    val fecha: String,
    val compraUsd: String? = null,
    val ventaUsd: String? = null,
    val cierreUsd: String? = null,
    val sponsor: Boolean? = null // Optional field for sponsored ads
)
data class ApiResponse(
    val panel: List<CurrencyPanel>,
    val publicidades: List<Advertisement>
)
