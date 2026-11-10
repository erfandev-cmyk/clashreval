package com.cocwar.assistant.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val tag: String,
    val townHall: Int
)

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY id DESC")
    fun all(): Flow<List<ProfileEntity>>
    @Insert suspend fun insert(p: ProfileEntity): Long
    @Delete suspend fun delete(p: ProfileEntity)
}

@Database(entities = [ProfileEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun profiles(): ProfileDao
    companion object {
        @Volatile private var inst: AppDb? = null
        fun get(c: Context): AppDb = inst ?: synchronized(this) {
            inst ?: Room.databaseBuilder(c.applicationContext, AppDb::class.java, "coc.db").build().also { inst = it }
        }
    }
}
