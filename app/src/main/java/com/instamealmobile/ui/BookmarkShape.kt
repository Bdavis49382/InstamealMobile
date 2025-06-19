package com.instamealmobile.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class SideBookmarkShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()

        val notchSize = 30f

        path.moveTo(0f, 0f)
        path.lineTo(size.width, 0f)
        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        path.lineTo(notchSize, size.height / 2f)
        path.close()

        return Outline.Generic(path)
    }
}
