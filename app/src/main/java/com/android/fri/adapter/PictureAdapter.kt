package com.android.fri.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.android.fri.R
import com.android.fri.model.Picture
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory


class PictureAdapter(private var context: Context, private var pictureList: List<Picture>) :

        RecyclerView.Adapter<PictureAdapter.MyViewHolder>() {

    var onItemClick: ((Picture) -> Unit)? = null
    var onItemFavoriteClick: ((Picture) -> Unit)? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userImage: ImageView = view.findViewById(R.id.userImage)
        var likes: TextView = view.findViewById(R.id.likes)
        var name: TextView = view.findViewById(R.id.name)
        var userName: TextView = view.findViewById(R.id.userName)
        var img: ImageView = view.findViewById(R.id.image)
        var imgFavorite: ImageView = view.findViewById(R.id.ic_favorite)

        init {
            img.setOnClickListener {
                onItemClick?.invoke(pictureList[adapterPosition])
            }

            imgFavorite.setOnClickListener {
                onItemFavoriteClick?.invoke(pictureList[adapterPosition])
            }

        }

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.picture_row, parent, false)

        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val picture = pictureList[position]
        holder.likes.text = picture.getLikes()
        holder.name.text = picture.getName()
        holder.userName.text = picture.getUserName()
        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()


        if(picture.getPicture()?.isNotEmpty()!!){
            Glide.with(context).load(picture.getPicture()).centerCrop().transition(withCrossFade(factory))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.img)
        }

        if(picture.getUserImage()?.isNotEmpty()!!){
            Glide.with(context).load(picture.getUserImage()).centerCrop().circleCrop().transition(withCrossFade(factory))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.userImage)
        }

        if(picture.getUserImage()!!.isEmpty()){
            val bitmap = BitmapFactory.decodeByteArray(picture.getPictureBlob(), 0, picture.getPictureBlob().size)
            holder.userImage.setImageBitmap(bitmap)
        }

        if(picture.getPicture()!!.isEmpty()){
            val bitmap = BitmapFactory.decodeByteArray(picture.getUserImageBlob(), 0, picture.getUserImageBlob().size)
            holder.img.setImageBitmap(bitmap)
        }

        if(picture.getIsFavorite()){
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_red)
        }


    }
    override fun getItemCount(): Int {
        return pictureList.size
    }

    fun updatePictureList (pictures: List<Picture>) {
        this.pictureList = pictures;
        notifyDataSetChanged();
    }

}