package com.android.fri.model

class Picture(id:Int, likes: String?, userImage: String?, name: String?, userName: String?, picture: String?,
              isFavorite: Boolean, description: String?, updated_at: String?, bio: String?, userImageBlob:ByteArray, pictureBlob:ByteArray ) {

    private var id: Int
    private var likes: String
    private var userImage: String
    private var name: String
    private var userName: String
    private var picture: String
    private var isFavorite: Boolean
    private var description: String
    private var updated_at: String
    private var bio:String
    private var userImageBlob: ByteArray
    private var pictureBlob: ByteArray

    init {
        this.id = id
        this.likes = likes!!
        this.userImage = userImage!!
        this.name = name!!
        this.userName = userName!!
        this.picture = picture!!
        this.isFavorite = isFavorite
        this.description = description!!
        this.updated_at = updated_at!!
        this.bio = bio!!
        this.userImageBlob = userImageBlob
        this.pictureBlob = pictureBlob
    }
    fun getId(): Int{
        return id;
    }
    fun setId(id: Int){
        this.id = id
    }
    fun getLikes(): String? {
        return likes
    }
    fun setLikes(likes: String?) {
        this.likes = likes!!
    }
    fun getUserImage(): String? {
        return userImage
    }
    fun setUserImage(userImage: String?) {
        this.userImage = userImage!!
    }
    fun getName(): String?{
        return name
    }
    fun setName(name: String?){
        this.name = name!!
    }
    fun getUserName(): String? {
        return userName
    }
    fun setUserName(userName: String?) {
        this.userName = userName!!
    }
    fun getPicture(): String? {
        return picture
    }
    fun setPicture(picture: String?){
        this.picture = picture!!
    }
    fun getIsFavorite(): Boolean {
        return isFavorite
    }
    fun setIsFavorite(isFavorite: Boolean){
        this.isFavorite = isFavorite
    }
    fun getDescription(): String? {
        return description
    }
    fun setDescription(description: String?){
        this.description = description!!
    }
    fun getUpdated_at(): String? {
        return updated_at
    }
    fun setUpdated_at(updated_at: String?){
        this.updated_at = updated_at!!
    }
    fun getBio(): String?{
        return bio
    }
    fun setBio(bio: String?){
        this.bio = bio!!
    }
    fun getUserImageBlob(): ByteArray {
        return userImageBlob
    }
    fun setUserImageBlob(userImageBlob: ByteArray) {
        this.userImageBlob = userImageBlob
    }
    fun getPictureBlob(): ByteArray {
        return pictureBlob
    }
    fun setPictureBlob(pictureBlob: ByteArray){
        this.pictureBlob = pictureBlob
    }

}