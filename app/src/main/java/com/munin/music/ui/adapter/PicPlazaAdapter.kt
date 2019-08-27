package com.munin.music.ui.adapter

import android.content.Context
import android.widget.ImageView
import com.munin.library.mvvm.BindingViewAdapter
import com.munin.library.mvvm.BindingViewHolder
import com.munin.music.BR
import com.munin.music.R
import com.munin.music.data.PictureData
import kotlinx.android.synthetic.main.layout_picture.view.*

class PicPlazaAdapter(context: Context?) : BindingViewAdapter<PictureData.RecommendsBean>(context) {
    override fun setLayoutAndViewType() {
        bindLayoutByType(0, R.layout.layout_picture)
    }
    override fun bindViewHolder(holder: BindingViewHolder, position: Int, type: Int, data: PictureData.RecommendsBean) {
        if (holder.itemView.picture_img_view is ImageView) {
            var bean = data
            var key = data.cover?.key;
            if(position%2==0){
                holder.itemView.layoutParams.height/=2
            }else{
                holder.itemView.layoutParams.height/=1
            }
        }
        holder.binding.setVariable(BR.data,data)
        holder.binding.executePendingBindings()
    }

    override fun bindViewType(position: Int, data: PictureData.RecommendsBean): Int {
        return 0
    }
}
