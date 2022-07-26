package com.example.togglforwearos


import android.graphics.Color
import androidx.wear.tiles.*
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders.*
import androidx.wear.tiles.LayoutElementBuilders.*
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.ResourceBuilders.ImageResource
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.Futures
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.future
import java.time.Instant

import com.example.togglforwearos.dataLayer.APITokenRepository
import com.example.togglforwearos.dataLayer.TogglRepository
import com.example.togglforwearos.dataLayer.TogglWebApi
import org.json.JSONObject


const val TILE_FRESHNESS_INTERVAL_MILLISECONDS: Long = 5 * 60 * 1000 // 5 minutes
private val TRACKED_DURATION_SECONDS: Long = 24 * 60 * 60

// dimensions
private val PROGRESS_BAR_THICKNESS = dp(12f)
private val BUTTON_SIZE = dp(48f)
private val BUTTON_RADIUS = dp(24f)
private val BUTTON_PADDING = dp(12f)
private val VERTICAL_SPACING_HEIGHT = dp(8f)

// Complete degrees for a circle (relates to [Arc] component)
private const val ARC_TOTAL_DEGREES = 320f

// identifiers
private const val ID_IMAGE_START_RUN = "image_start_run"
private const val ID_CLICK_START_RUN = "click_start_run"


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

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest) = serviceScope.future {

        // Retrieves device parameters to later retrieve font styles for any text in the Tile.
        val deviceParams = requestParams.deviceParameters!!

        val sharedPref = getSharedPref(applicationContext)

        var tileBuilder: Tile.Builder
//        try {
//
//            try {
//                val togglRepository = TogglRepository(applicationContext)
//                val timeEntries = togglRepository.userInfo!!.timeEntries
//                val runningTimeEntryJSONObject = tooglWebAPI.getCurrentTimeEntry()
//                var runningTimeEntry = togglRepository.getRunningTimer()
//                if (runningTimeEntryJSONObject != null
//                    && runningTimeEntryJSONObject.has("data")
//                    && runningTimeEntryJSONObject["data"] is JSONObject
//                ) {
//                    val data = runningTimeEntryJSONObject.getJSONObject("data")
//                    runningTimeEntry = if (data.has("pid")) {
//                        TimeEntry(
//                            data,
//                            userInfo.projectsMap[data.getString("pid")]
//                        )
//                    } else {
//                        TimeEntry(data, null)
//                    }
//                }
//                // Creates Tile.
//                tileBuilder = Tile.Builder()
//                    // If there are any graphics/images defined in the Tile's layout, the system will
//                    // retrieve them via onResourcesRequest() and match them with this version number.
//                    .setResourcesVersion(RESOURCES_VERSION)
//                    .setFreshnessIntervalMillis(TILE_FRESHNESS_INTERVAL_MILLISECONDS)
//                    // Creates a timeline to hold one or more tile entries for a specific time periods.
//                    .setTimeline(
//                        Timeline.Builder()
//                            .addTimelineEntry(
//                                TimelineEntry.Builder()
//                                    .setLayout(
//                                        Layout.Builder()
//                                            .setRoot(
//                                                // Creates the root [Box] [LayoutElement]
//                                                layout(timeEntries, runningTimeEntry, deviceParams)
//                                            )
//                                            .build()
//                                    )
//                                    .build()
//                            )
//                            .build()
//                    )
//            } catch (e: TogglWebApi.WrongHttpResponseException) {
//                tileBuilder = errorMessageTileBuilder(makeMyExceptionMessage(e), requestParams)
//            } catch (e: Exception) {
//                tileBuilder = errorMessageTileBuilder(makeMyExceptionMessage(e), requestParams)
//                e.printStackTrace()
//            }
//        } catch (e: APITokenRepository.NoAPITokenFoundException) {
//            tileBuilder = errorMessageTileBuilder(makeMyExceptionMessage(e), requestParams)
//            e.printStackTrace()
//        }

        tileBuilder = errorMessageTileBuilder("not implemented", requestParams)
        tileBuilder.build()
//        errorMessageTileBuilder("If u see me, dev screwed up", requestParams).build()

    }



    fun errorMessageTileBuilder(
        errorMessage: String,
        requestParams: RequestBuilders.TileRequest
    ): Tile.Builder {
        return Tile.Builder()
            .setResourcesVersion(RESOURCES_VERSION)
            .setFreshnessIntervalMillis(TILE_FRESHNESS_INTERVAL_MILLISECONDS)
            .setTimeline(
                Timeline.Builder()
                    .addTimelineEntry(
                        TimelineEntry.Builder()
                            .setLayout(
                                Layout.Builder()
                                    .setRoot(
                                        LayoutElementBuilders.Box.Builder()
                                            .setWidth(expand())
                                            .setHeight(expand())
                                            .addContent(
                                                Text.Builder()
                                                    .setText(errorMessage)
                                                    .setMaxLines(5)
                                                    .build()
                                            )
                                            .setModifiers(
                                                ModifiersBuilders.Modifiers.Builder()
                                                    .setClickable(
                                                        ModifiersBuilders.Clickable.Builder()
                                                            .setId("clickableId")
                                                            .setOnClick(ActionBuilders.LoadAction.Builder().build())
                                                            .build()
                                                    ).build()
                                            ).build()
                                    ).build()
                            ).build()
                    ).build()
            )
    }

    override fun onResourcesRequest(requestParams: RequestBuilders.ResourcesRequest) =
        Futures.immediateFuture(
            Resources.Builder()
                .setVersion(RESOURCES_VERSION)
                .addIdToImageMapping(
                    "image_inline", ImageResource.Builder()
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


    // Creates a simple [Box] container that lays out its children one over the other. In our
    // case, an [Arc] that shows progress on top of a [Column] that includes the current steps
    // [Text], the total steps [Text], a [Spacer], and a running icon [Image].
    private fun layout(
        timeEntries: List<TimeEntry>,
        runningTimeEntry: TimeEntry?,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ) =
        LayoutElementBuilders.Box.Builder()
            // Sets width and height to expand and take up entire Tile space.
            .setWidth(expand())
            .setHeight(expand())

            // Adds an [Arc] via local function.
            .addContent(
                getTimelineArc(
                    timeEntries,
                    trackedDurationSeconds = TRACKED_DURATION_SECONDS,
                    thickness = PROGRESS_BAR_THICKNESS,
                    totalArcLengthDegrees = ARC_TOTAL_DEGREES
                )
            )
            // Make tile refreshable
            .setModifiers(
                ModifiersBuilders.Modifiers.Builder()
                    .setClickable(
                        ModifiersBuilders.Clickable.Builder()
                            .setId("clickableId")
                            .setOnClick(ActionBuilders.LoadAction.Builder().build())
                            .build()
                    ).build()
            )

            // TODO: Add Column containing the rest of the data.
            // Adds a [Column] containing the two [Text] objects, a [Spacer], and a [Image].
            .addContent(
                Column.Builder()
                    // Adds a [Text] via local function.
                    .addContent(
                        runningEntryNameText(
                            runningTimeEntry,
                            deviceParameters
                        )
                    ).addContent(
                        runningEntryDurationText(
                            runningTimeEntry,
                            deviceParameters
                        )
                    )
                    // TODO: Add Spacer and Image representations of our step graphic.
                    // DO LATER
                    .build()
            ).build()


    // Creates an [Arc] representing current progress towards steps goal.
    fun getTimelineArc(
        timeEntries: List<TimeEntry>,
        trackedDurationSeconds: Long,
        totalArcLengthDegrees: Float,
        thickness: DimensionBuilders.DpProp = dp(12f),
        endEpoch: Long = Instant.now().epochSecond
    ): LayoutElementBuilders.Arc {
        if (totalArcLengthDegrees < 0f || totalArcLengthDegrees > 360f) {
            throw Exception("totalArcLengthDegrees can't be less than 0 or more than 360")
        }


        val epochNow = Instant.now().epochSecond
        val startEpoch = epochNow - trackedDurationSeconds

        fun sceondsToDegrees(seconds: Long): Float =
            (seconds.toDouble() / trackedDurationSeconds.toDouble() * totalArcLengthDegrees).toFloat()

        fun getActualStartEpoch(inputStartEpoch: Long): Long =
            if (startEpoch > inputStartEpoch) {
                startEpoch
            } else {
                inputStartEpoch
            }

        fun getActualEndEpoch(inputEndEpoch: Long): Long = if (endEpoch > inputEndEpoch) {
            inputEndEpoch
        } else {
            endEpoch
        }

        var builder = LayoutElementBuilders.Arc.Builder()
            .setAnchorAngle(DimensionBuilders.degrees(-totalArcLengthDegrees / 2))
            // Aligns the contents of this container relative to anchor angle above.
            // ARC_ANCHOR_START - Anchors at the start of the elements. This will cause elements
            // added to an arc to begin at the given anchor_angle, and sweep around to the right.
            .setAnchorType(LayoutElementBuilders.ARC_ANCHOR_START)

        val sortedTimeEmtries = timeEntries.sorted()
        var previousEntryEndTimeEpoch: Long = startEpoch
        for (i in 0..(sortedTimeEmtries.size - 1)) {
            val timeEntry = sortedTimeEmtries[i]
            if (timeEntry.isCurrentlyRunning) {
                timeEntry.endTimeEpoch = epochNow
            }
            if ((timeEntry.startTimeEpoch >= startEpoch && timeEntry.endTimeEpoch!! <= endEpoch) ||
                (timeEntry.startTimeEpoch < startEpoch && timeEntry.endTimeEpoch!! > startEpoch) ||
                (timeEntry.startTimeEpoch < endEpoch && timeEntry.endTimeEpoch!! > endEpoch)
            ) {
                var actualStartEpoch = getActualStartEpoch(timeEntry.startTimeEpoch)
                if (actualStartEpoch > previousEntryEndTimeEpoch) {
                    // Adding filler ArcLine
                    builder.addContent(
                        LayoutElementBuilders.ArcLine.Builder()
                            .setLength(
                                DimensionBuilders.degrees(
                                    sceondsToDegrees(
                                        actualStartEpoch - previousEntryEndTimeEpoch
                                    )
                                )
                            )
                            .setColor(ColorBuilders.argb(Color.TRANSPARENT))
                            .setThickness(thickness)
                            .build()
                    )
                }


                var actualEndEpoch: Long = getActualEndEpoch(timeEntry.endTimeEpoch!!)
                if (i < sortedTimeEmtries.size - 1) {
                    val nextEntry = sortedTimeEmtries[i + 1]

                    if (nextEntry.startTimeEpoch < actualEndEpoch) {
                        actualEndEpoch = nextEntry.startTimeEpoch
                    }
                } else {
                    val debug = 0
                }
                builder.addContent(
                    LayoutElementBuilders.ArcLine.Builder()
                        .setLength(
                            DimensionBuilders.degrees(
                                sceondsToDegrees(
                                    actualEndEpoch - actualStartEpoch
                                )
                            )
                        )
                        .setColor(ColorBuilders.argb(timeEntry.projectColor))
                        .setThickness(thickness)
                        .build()
                )
                previousEntryEndTimeEpoch = actualEndEpoch
            }
        }

        return builder.build()
    }


    // Creates a [Text]
    private fun runningEntryNameText(
        runningTimeEntry: TimeEntry?,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ): Text {

//        val m = ModifiersBuilders.Modifiers.Builder()
//            .setBorder(
//                ModifiersBuilders.Border.Builder()
//                    .setWidth(dp(6f))
//                    .setColor(argb(Color.BLUE))
//                    .build()
//            ).setPadding(
//                ModifiersBuilders.Padding.Builder()
//                    .setAll(dp(6f))
//                    .build()
//            ).setSemantics(ModifiersBuilders.Semantics.Builder()
//                .setContentDescription("str")
//                .build()
//            ).build()
        if (runningTimeEntry != null) {
            return Text.Builder()
                .setText(runningTimeEntry.projectName)
                .setFontStyle(
                    FontStyles.title3(deviceParameters)
                        .setColor(argb(runningTimeEntry.projectColor))
                        .build()
                )//.setModifiers(m)
                .build()
        } else {
            return Text.Builder()
                .setText(resources.getString(R.string.no_current_timer))
                .setFontStyle(FontStyles.title3(deviceParameters).build())
                .build()
        }
    }


    private fun runningEntryDurationText(
        runningTimeEntry: TimeEntry?,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ): Text {

        if (runningTimeEntry != null) {
            return Text.Builder()
                .setText(durationSecondsToString(Instant.now().epochSecond - runningTimeEntry.startTimeEpoch))
                .setFontStyle(
                    FontStyles.title3(deviceParameters)
                        .setColor(argb(runningTimeEntry.projectColor))
                        .build()
                )//.setModifiers(m)
                .build()
        } else {
            return Text.Builder()
                .setText("")
                .setFontStyle(FontStyles.title3(deviceParameters).build())
                .build()
        }
    }
}


