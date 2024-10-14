package com.example.sqlpractice2hw

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION)
{
    companion object {
        private val DATABASE_NAME = "PRODUCT_DATABASE"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "product_table"
        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_WEIGHT = "weight"
        val KEY_PRICE = "price"
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    override fun onCreate(p0: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME
                + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_WEIGHT + " DOUBLE, " +
                KEY_PRICE + " INTEGER" +")")

        p0.execSQL(query)
    }

    fun addProduct(product: Product){
        val values = ContentValues()
        values.put(KEY_NAME, product.name)
        values.put(KEY_ID, product.id)
        values.put(KEY_WEIGHT, product.weight)
        values.put(KEY_PRICE, product.price)
        val db = this.writableDatabase
        db.insert(TABLE_NAME,null,values)
        db.close()

    }


    @SuppressLint("Range")
    fun getInfo(): MutableList<Product> {
        val productList: MutableList<Product> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }
        var idFromDB: Int
        var nameFromDB: String
        var weightFromDB: String
        var priceFromDB: String

        if (cursor.moveToFirst()){
            do {
                idFromDB = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
                nameFromDB = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                weightFromDB = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WEIGHT))
                priceFromDB = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PRICE))
                val product = Product(idFromDB,nameFromDB,weightFromDB,priceFromDB)
                productList.add(product)
            } while (cursor.moveToNext())
        }

        return productList
    }


    fun removeAll(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null,null)
    }

    fun updateProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,product.id)
        contentValues.put(KEY_NAME,product.name)
        contentValues.put(KEY_WEIGHT,product.weight)
        contentValues.put(KEY_PRICE,product.price)
        db.update(TABLE_NAME,contentValues,"id=" + product.id, null)
        db.close()
    }

    fun deleteProduct(product: Product){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,product.id)
        db.delete(TABLE_NAME,"id=" + product.id, null)
        db.close()
    }

    fun deleteProductOnId(id: Int){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,id)
        db.delete(TABLE_NAME,"id=" + id, null)
        db.close()
    }



    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)

    }

    override fun getReadableDatabase(): SQLiteDatabase {
        return super.getReadableDatabase()
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        return super.getWritableDatabase()
    }

    override fun setIdleConnectionTimeout(idleConnectionTimeoutMs: Long) {
        super.setIdleConnectionTimeout(idleConnectionTimeoutMs)
    }

    override fun setOpenParams(openParams: SQLiteDatabase.OpenParams) {
        super.setOpenParams(openParams)
    }

    override fun setLookasideConfig(slotSize: Int, slotCount: Int) {
        super.setLookasideConfig(slotSize, slotCount)
    }

    override fun setWriteAheadLoggingEnabled(enabled: Boolean) {
        super.setWriteAheadLoggingEnabled(enabled)
    }

    override fun getDatabaseName(): String {
        return super.getDatabaseName()
    }

    override fun close() {
        super.close()
    }
}