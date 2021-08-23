package com.jaydev.github.common

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(
	@Dimension(unit = Dimension.DP) var dp: Float,
	@Dimension(unit = Dimension.DP) var headSpace: Float = 0f,
	@Dimension(unit = Dimension.DP) var footSpace: Float = 0f
): RecyclerView.ItemDecoration() {

	override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
		val itemPosition = parent.getChildAdapterPosition(view)
		when {
			itemPosition == 0 -> {
				outRect.top = view.context.dpToPx(headSpace)
				outRect.bottom = view.context.dpToPx(dp)
			}
			itemPosition != parent.adapter?.itemCount!! - 1 -> outRect.bottom = view.context.dpToPx(dp)
			itemPosition == parent.adapter?.itemCount!! - 1 -> outRect.bottom = view.context.dpToPx(footSpace)
		}
	}
}
