package com.android.fri.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.fri.R
import com.android.fri.helper.DatabaseHandler
import com.android.fri.model.Picture
import com.android.fri.util.Constants
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.ByteArrayOutputStream


class DetailFragment : Fragment() {

    private var likes: String? = null
    private var userImage: String? = null
    private var name: String? = null
    private var userName: String? = null
    private var picture: String? = null
    private var isFavorite: Boolean = false
    private var description: String? = null
    private var updated_at: String? = null
    private var bio:String? = null

    lateinit var databaseHandler: DatabaseHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            likes = it.getString(Constants.ARG_LIKES)
            userImage = it.getString(Constants.ARG_USER_IMAGE)
            name = it.getString(Constants.ARG_NAME)
            userName = it.getString(Constants.ARG_USER_NAME)
            picture = it.getString(Constants.ARG_PICTURE)
            isFavorite = it.getBoolean(Constants.ARG_IS_FAVORITE)
            description = it.getString(Constants.ARG_DESCRIPTION)
            updated_at = it.getString(Constants.ARG_UPDATED_AT)
            bio = it.getString(Constants.ARG_BIO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail, container, false)

        val img: ImageView = view.findViewById(R.id.image)
        val tvDescription: TextView = view.findViewById(R.id.description)
        val tvName: TextView = view.findViewById(R.id.name)
        val imgUserImage: ImageView = view.findViewById(R.id.userImage)
        val tvLikes: TextView = view.findViewById(R.id.likes)
        val tvUserName: TextView = view.findViewById(R.id.userName)
        val tvUpdatedAt: TextView = view.findViewById(R.id.updated_at)
        val tvBio: TextView = view.findViewById(R.id.bio)
        val imgFavorite: ImageView = view.findViewById(R.id.ic_favorite)

        databaseHandler = DatabaseHandler(requireActivity().applicationContext)

        tvDescription.text = description
        tvUpdatedAt.text = updated_at
        tvBio.text = bio
        tvLikes.text = likes
        tvName.text = name
        tvUserName.text =userName

        Glide.with(requireActivity().applicationContext).asBitmap().load(picture).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(img)
        Glide.with(requireActivity().applicationContext).asBitmap().load(userImage).centerCrop().circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgUserImage)

        imgFavorite.setOnClickListener(View.OnClickListener {

            try{

                val pictureConverted = convertImage(img)
                val userImageConverted = convertImage(imgUserImage)
                val pictureSave = Picture(0,likes, userImage, name, userName, picture, false,description,updated_at,
                        bio,pictureConverted,userImageConverted)
                val success = databaseHandler.addPicture(pictureSave)

                if(success > 0) {
                    imgFavorite.setImageResource(R.drawable.ic_favorite_red)
                }

            }catch (e: Exception){
                print(e)
            }

        })

        return view
    }

    fun convertImage (img : ImageView) : ByteArray {
        val stream = ByteArrayOutputStream()
        try{

            val bitmap = (img.getDrawable().getCurrent() as BitmapDrawable).bitmap
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        }catch (e: Exception){
            print(e)
        }
        return stream.toByteArray()
    }


    companion object {
        @JvmStatic
        fun newInstance(likes: String?, userImage: String?, name: String?, userName: String?, picture: String?, isFavorite: Boolean, description: String?, updated_at: String?, bio: String?  ) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(Constants.ARG_LIKES, likes)
                        putString(Constants.ARG_USER_IMAGE, userImage)
                        putString(Constants.ARG_NAME, name)
                        putString(Constants.ARG_USER_NAME, userName)
                        putString(Constants.ARG_PICTURE, picture)
                        putBoolean(Constants.ARG_IS_FAVORITE, isFavorite)
                        putString(Constants.ARG_DESCRIPTION, description)
                        putString(Constants.ARG_UPDATED_AT, updated_at)
                        putString(Constants.ARG_BIO, bio)

                    }
                }
    }


}