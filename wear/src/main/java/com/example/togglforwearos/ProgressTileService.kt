package com.example.togglforwearos


import androidx.wear.tiles.LayoutElementBuilders.Layout
import androidx.wear.tiles.LayoutElementBuilders.Text
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.ResourceBuilders.ImageResource
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.future


private val RESOURCES_VERSION = "1"
class ProgressTileService : TileService() {
//    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
//        Futures.immediateFuture(
//            Tile.Builder()
//            .setResourcesVersion(RESOURCES_VERSION)
//            .setTimeline(
//                Timeline.Builder().addTimelineEntry(
//                    TimelineEntry.Builder().setLayout(
//                        Layout.Builder().setRoot(
//                            Text.Builder().setText("Hello world!").build()
//                        ).build()
//                    ).build()
//                ).build()
//            ).build()
//        )
    // For coroutines, use a custom scope we can cancel when the service is destroyed
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    // TODO: Build a Tile.
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) = serviceScope.future {
        // Creates Tile.
        Tile.Builder()
            // If there are any graphics/images defined in the Tile's layout, the system will
            // retrieve them via onResourcesRequest() and match them with this version number.
            .setResourcesVersion(RESOURCES_VERSION)

            // Creates a timeline to hold one or more tile entries for a specific time periods.
            .setTimeline(
                Timeline.Builder()
                    .addTimelineEntry(
                        TimelineEntry.Builder()
                            .setLayout(
                                Layout.Builder()
                                    .setRoot(
                                        Text.Builder().setText("Hello, tiled world!").build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            ).build()
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        Futures.immediateFuture(
            Resources.Builder()
            .setVersion(RESOURCES_VERSION)
            .addIdToImageMapping("image_inline", ImageResource.Builder()
                .setInlineResource(
                    ResourceBuilders.InlineImageResource.Builder()
                    .setData(byteArrayOfInts(0x00, 0xFF, 0xFF, 0x00))
                    .setWidthPx(2)
                    .setHeightPx(2)
                    .setFormat(ResourceBuilders.IMAGE_FORMAT_RGB_565)
                    .build()
                ).build()
            ).build()
        )

    fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }
}