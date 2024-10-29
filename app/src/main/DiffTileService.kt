import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ColorBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import androidx.wear.tiles.TilesService

class DiffTileService : TilesService() {

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        // Sample data - replace these with actual values from your data source
        val venta = 1200.0
        val compra = 1175.0
        val diff = venta - compra
        val updatedTime = "2024-10-29 15:05" // Update this with the actual updated time

        // Determine color based on diff value
        val diffColor = if (diff <= 0) ColorBuilders.argb(android.graphics.Color.RED) else ColorBuilders.argb(android.graphics.Color.GREEN)

        return TileBuilders.Tile.Builder()
            .setResourcesVersion("1")
            .setFreshnessIntervalMillis(1000 * 60) // Update every minute
            .setLayout(
                LayoutElementBuilders.Layout.Builder()
                    .setRoot(
                        LayoutElementBuilders.Column.Builder()
                            .addContent(
                                LayoutElementBuilders.Text.Builder()
                                    .setText("Venta: $venta")
                                    .setFontStyle(
                                        LayoutElementBuilders.FontStyle.Builder()
                                            .setColor(ColorBuilders.argb(android.graphics.Color.WHITE))
                                            .build()
                                    )
                                    .build()
                            )
                            .addContent(
                                LayoutElementBuilders.Text.Builder()
                                    .setText("Compra: $compra")
                                    .setFontStyle(
                                        LayoutElementBuilders.FontStyle.Builder()
                                            .setColor(ColorBuilders.argb(android.graphics.Color.WHITE))
                                            .build()
                                    )
                                    .build()
                            )
                            .addContent(
                                LayoutElementBuilders.Text.Builder()
                                    .setText("Diff: $diff")
                                    .setFontStyle(
                                        LayoutElementBuilders.FontStyle.Builder()
                                            .setColor(diffColor)
                                            .build()
                                    )
                                    .build()
                            )
                            .addContent(
                                LayoutElementBuilders.Text.Builder()
                                    .setText("Updated: $updatedTime")
                                    .setFontStyle(
                                        LayoutElementBuilders.FontStyle.Builder()
                                            .setColor(ColorBuilders.argb(android.graphics.Color.LTGRAY))
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        return ResourceBuilders.Resources.Builder()
            .setVersion("1")
            .build()
    }