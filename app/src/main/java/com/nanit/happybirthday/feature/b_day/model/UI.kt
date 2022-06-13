package com.nanit.happybirthday.feature.b_day.model

import com.nanit.happybirthday.R

data class UI(
    val background: Int,
    val backgroundImg: Int,
    val colorBoarder: Int,
    val photoPlace: Int,
    val takePhoto: Int,
) {
    companion object {
        private fun listUI() = listOf(
            UI(
                R.color.lightBlueGreen,
                R.drawable.i_os_bg_fox,
                R.color.paleTeal,
                R.drawable.ic_default_place_holder_green,
                R.drawable.ic_camera_icon_green
            ),
            UI(
                R.color.lightBlueGrey,
                R.drawable.i_os_bg_pelican,
                R.color.lightTeal,
                R.drawable.ic_default_place_holder_blue,
                R.drawable.ic_camera_icon_blue
            ),
            UI(
                R.color.pale,
                R.drawable.i_os_bg_elephant,
                R.color.goldenYellow,
                R.drawable.ic_default_place_holder_yellow,
                R.drawable.ic_camera_icon_yellow
            )
        )

        fun getUI(): UI {
            return listUI()[(0..listUI().lastIndex).random()]
        }
    }
}