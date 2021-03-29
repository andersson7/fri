package com.android.fri.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.android.fri.model.Picture


class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "FriDatabase"
        private val TABLE_PICTURE = "PICTURE"
        private val COLUMN_ID = "ID"
        private val COLUMN_LIKE = "LIKE"
        private val COLUMN_USER_IMAGE = "USER_IMAGE"
        private val COLUMN_NAME = "NAME"
        private val COLUMN_USER_NAME = "USER_NAME"
        private val COLUMN_PICTURE = "PICTURE"
        private val COLUMN_IS_FAVORITE = "IS_FAVORITE"
        private val COLUMN_DESCRIPTION = "DESCRIPTION"
        private val COLUMN_UPDATED_AT = "UPDATED_AT"
        private val COLUMN_BIO = "BIO"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        try{
            val CREATE_PICTURE_TABLE = ("CREATE TABLE " + TABLE_PICTURE + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_LIKE + " TEXT,"
                    + COLUMN_USER_IMAGE + " BLOB, "
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_USER_NAME + " TEXT, "
                    + COLUMN_PICTURE + " BLOB, "
                    + COLUMN_IS_FAVORITE + " TEXT, "
                    + COLUMN_DESCRIPTION + " TEXT, "
                    + COLUMN_UPDATED_AT + " TEXT, "
                    + COLUMN_BIO + " TEXT "
                    + ")")
            db?.execSQL(CREATE_PICTURE_TABLE)
        }catch (e: Exception){
            print(e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try{
            db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURE)
            onCreate(db)
        }catch (e: Exception){
            print(e)
        }
    }

    fun addPicture(picture: Picture):Long{
        var success:Long = -1
        try{
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COLUMN_LIKE, picture.getLikes())
            contentValues.put(COLUMN_USER_IMAGE, picture.getUserImageBlob())
            contentValues.put(COLUMN_NAME, picture.getName())
            contentValues.put(COLUMN_USER_NAME, picture.getUserName())
            contentValues.put(COLUMN_PICTURE, picture.getPictureBlob())
            contentValues.put(COLUMN_IS_FAVORITE, "1")
            contentValues.put(COLUMN_DESCRIPTION, picture.getDescription())
            contentValues.put(COLUMN_UPDATED_AT, picture.getUpdated_at())
            contentValues.put(COLUMN_BIO, picture.getBio())
            success = db.insert(TABLE_PICTURE, null, contentValues)
            db.close()
        }catch (e: Exception ){
            print(e)
        }
        return success
    }

    fun viewPictures( user: String ):List<Picture>{
        val pictures:ArrayList<Picture> = ArrayList<Picture>()
        val selectQuery = "SELECT * FROM $TABLE_PICTURE"+ " WHERE " + COLUMN_USER_NAME + " LIKE '%" + user + "%' OR " + COLUMN_NAME + " LIKE '%" + user + "%'"

        try{
            val db = this.readableDatabase
            var cursor: Cursor? = null
            try{
                cursor = db.rawQuery(selectQuery, null)
            }catch (e: SQLiteException) {
                db.execSQL(selectQuery)
                return ArrayList()
            }
            if (cursor.moveToFirst()) {
                do {
                    val picture = Picture(
                            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LIKE)),
                            "",
                            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                            "",
                            true,
                            cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_UPDATED_AT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_BIO)),
                            cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_IMAGE)),
                            cursor.getBlob(cursor.getColumnIndex(COLUMN_PICTURE)))
                    pictures.add(picture)
                } while (cursor.moveToNext())
                if(!cursor.isClosed){
                    cursor.close()
                }
            }
        }catch (e: Exception ){
            print(e)
        }

        return pictures
    }

    fun deletePicture(picture: Picture):Int{
        var success:Int = -1
        try{
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(COLUMN_ID, picture.getId())
            success = db.delete(TABLE_PICTURE, COLUMN_ID +"="+picture.getId(),null)
            db.close()
        }catch (e: Exception){
            print(e)
        }
        return success
    }

    fun existPicture( userName : String, date : String):Int{
        val countQuery = "SELECT * FROM $TABLE_PICTURE" + " WHERE " + COLUMN_UPDATED_AT + " = '" + date + "' AND " + COLUMN_USER_NAME + " = '" + userName + "'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        db.close()
        cursor.close()
        return count
    }


}