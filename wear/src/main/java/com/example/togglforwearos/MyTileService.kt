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


private val RESOURCES_VERSION = "1"
class MyTileService : TileService() {
//    val x = com.google.common.util.concurrent.Futures.transform
    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) =
        Futures.immediateFuture(
            Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setTimeline(
                Timeline.Builder().addTimelineEntry(
                    TimelineEntry.Builder().setLayout(
                        Layout.Builder().setRoot(
                            Text.Builder().setText("Hello world!").build()
                        ).build()
                    ).build()
                ).build()
            ).build()
        )

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