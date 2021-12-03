package com.example.togglforwearos

import com.google.common.util.concurrent.Futures


import androidx.wear.tiles.LayoutElementBuilders.Layout
import androidx.wear.tiles.LayoutElementBuilders.Text
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
//import com.google.common.util.concurrent.Futures

private val RESOURCES_VERSION = "1"
class MyTileService : TileService() {
    val x = com.google.common.util.concurrent.Futures.transform
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
            .build()
        )
}