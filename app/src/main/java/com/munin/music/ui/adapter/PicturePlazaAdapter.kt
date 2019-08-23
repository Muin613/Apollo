package com.munin.music.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.munin.library.image.ImageLoadUtils
import com.munin.library.utils.ScreenUtils
import com.munin.library.view.widget.RoundImageView
import com.munin.music.R
import com.munin.music.data.PictureData
import kotlinx.android.synthetic.main.layout_picture.view.*

class PicturePlazaAdapter : RecyclerView.Adapter<PicturePlazaAdapter.PicturePlazaViewHolder>() {
    var data = ArrayList<PictureData.RecommendsBean>()

    override fun onBindViewHolder(holder: PicturePlazaViewHolder, position: Int) {
        if (holder.itemView.picture_img_view is ImageView) {
            var bean = data[position]
            var key = data[position].cover?.key;
            if(position%2==0){
            holder.itemView.layoutParams.height/=2
            }else{
                holder.itemView.layoutParams.height/=1
            }
            ImageLoadUtils.loadImg(holder.itemView.context, "https://hbimg.huabanimg.com/" + key, holder.itemView.picture_img_view)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicturePlazaViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_picture,null)
        view.let {
            if (it.layoutParams == null) {
                it.layoutParams = ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(parent.context)/2, ScreenUtils.getScreenWidth(parent.context)/2)
            }
            it.layoutParams.height = ScreenUtils.getScreenWidth(parent.context)/2
            it.layoutParams.width =ScreenUtils.getScreenWidth(parent.context)/2;
        }

        return PicturePlazaViewHolder(view)
    }


    inner class PicturePlazaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
