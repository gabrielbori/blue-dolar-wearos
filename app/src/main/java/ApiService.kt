import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
//    @GET("frontend/quotations/dolar") // replace with your API endpoint
//    @Headers(
//        "sec-ch-ua-platform: macOS",
//        "Referer: https://www.dolarito.ar/",
//        "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36",
//        "Accept: application/json, text/plain, */*",
//        "auth-client: 0022200edebd6eaee37427532323d88b",
//        "sec-ch-ua: Chromium;v=130, Google Chrome;v=130, Not?A_Brand;v=99",
//        "sec-ch-ua-mobile: ?0"
//    )
//    suspend fun getValue(): Response<ValueResponse>
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

//data class ValueResponse(
//    val oficial: CurrencyInfo,
//    val bancos: CurrencyInfo,
//    val tarjeta: CurrencyInfo,
//    val mep: CurrencyInfo,
//    val ccl: CurrencyInfo,
//    val letsBit: CurrencyInfo,
//    val plusCambio: CurrencyInfo,
//    val ledesMep: CurrencyInfo,
//    val ledesCcl: CurrencyInfo,
//    val informal: CurrencyInfo,
//    val mayorista: CurrencyInfo,
//    val netflix: CurrencyInfo,
//    val cripto: CurrencyInfo,
//    val plusInversiones: CurrencyInfo,
//    val plusCrypto: CurrencyInfo,
//    val buenbit: CurrencyInfo,
//    val satoshitango: CurrencyInfo,
//    val diarco: CurrencyInfo
//)
//
//data class CurrencyInfo(
//    val name: String,
//    val buy: Double?,
//    val sell: Double?,
//    val timestamp: Long,
//    val variation: Double?,
//    val spread: Double?,
//    val volumen: Double?,
//    val extra: Any?
//)
