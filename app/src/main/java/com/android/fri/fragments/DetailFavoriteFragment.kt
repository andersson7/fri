package com.android.fri.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.fri.R
import com.android.fri.util.Constants


class DetailFavoriteFragment : Fragment() {

    private var likes: String? = null
    private lateinit var userImage: ByteArray
    private var name: String? = null
    private var userName: String? = null
    private lateinit var picture: ByteArray
    private var isFavorite: Boolean = false
    private var description: String? = null
    private var updated_at: String? = null
    private var bio:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            likes = it.getString(Constants.ARG_LIKES)
            userImage = it.getByteArray(Constants.ARG_USER_IMAGE)!!
            name = it.getString(Constants.ARG_NAME)
            userName = it.getString(Constants.ARG_USER_NAME)
            picture = it.getByteArray(Constants.ARG_PICTURE)!!
            isFavorite = it.getBoolean(Constants.ARG_IS_FAVORITE)
            description = it.getString(Constants.ARG_DESCRIPTION)
            updated_at = it.getString(Constants.ARG_UPDATED_AT)
            bio = it.getString(Constants.ARG_BIO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_detail_favorite, container, false)

        val img: ImageView = view.findViewById(R.id.image)
        val tvDescription: TextView = view.findViewById(R.id.description)
        val tvName: TextView = view.findViewById(R.id.name)
        val imgUserImage: ImageView = view.findViewById(R.id.userImage)
        val tvLikes: TextView = view.findViewById(R.id.likes)
        val tvUserName: TextView = view.findViewById(R.id.userName)
        val tvUpdatedAt: TextView = view.findViewById(R.id.updated_at)
        val tvBio: TextView = view.findViewById(R.id.bio)
        val imgFavorite: ImageView = view.findViewById(R.id.ic_favorite)


        tvDescription.text = description
        tvUpdatedAt.text = updated_at
        tvBio.text = bio
        tvLikes.text = likes
        tvName.text = name
        tvUserName.text = userName
        imgFavorite.setImageResource(R.drawable.ic_favorite_red)


        img.setImageBitmap(BitmapFactory.decodeByteArray(userImage, 0, userImage.size))
        imgUserImage.setImageBitmap(BitmapFactory.decodeByteArray(picture, 0, picture.size))

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(likes: String?, userImage: ByteArray, name: String?, userName: String?, picture: ByteArray, isFavorite: Boolean, description: String?, updated_at: String?, bio: String?  ) =
                DetailFavoriteFragment().apply {
                    arguments = Bundle().apply {
                        putString(Constants.ARG_LIKES, likes)
                        putByteArray(Constants.ARG_USER_IMAGE, userImage)
                        putString(Constants.ARG_NAME, name)
                        putString(Constants.ARG_USER_NAME, userName)
                        putByteArray(Constants.ARG_PICTURE, picture)
                        putBoolean(Constants.ARG_IS_FAVORITE, isFavorite)
                        putString(Constants.ARG_DESCRIPTION, description)
                        putString(Constants.ARG_UPDATED_AT, updated_at)
                        putString(Constants.ARG_BIO, bio)

                    }
                }
    }


}