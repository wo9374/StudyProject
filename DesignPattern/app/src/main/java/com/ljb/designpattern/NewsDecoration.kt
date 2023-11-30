package com.ljb.designpattern

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ljb.extension.dp

class NewsDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = 10.dp
        outRect.left = 10.dp
        outRect.right = 10.dp

        //val position = parent.getChildAdapterPosition(view) //각 아이템뷰의 순서 (index)
        //val totalItemCount = state.itemCount                //총 아이템 수
        //val scrollPosition = state.targetScrollPosition     //스크롤 됬을때 아이템 position
        //outRect.set(0,0,0,0)                                //left, top, bottom, right 한번에 주는 속성
    }
}