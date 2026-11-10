package com.cocwarassistant.database

import android.content.Context
import androidx.room.*
import com.cocwarassistant.core.*
import kotlinx.coroutines.flow.Flow

@Dao interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY updatedAt DESC") fun observeAll(): Flow<List<PlayerProfileEntity>>
    @Query("SELECT * FROM profiles WHERE id=:id") suspend fun get(id: String): PlayerProfileEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(profile: PlayerProfileEntity)
    @Delete suspend fun delete(profile: PlayerProfileEntity)
}
@Dao interface UnitDao {
    @Query("SELECT * FROM profile_units WHERE profileId=:profileId") suspend fun getForProfile(profileId: String): List<ProfileUnitEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAll(units: List<ProfileUnitEntity>)
    @Query("DELETE FROM profile_units WHERE profileId=:profileId") suspend fun deleteForProfile(profileId: String)
}
@Dao interface AnalysisDao {
    @Query("SELECT * FROM analyses ORDER BY createdAt DESC") fun observeAll(): Flow<List<AnalysisEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: AnalysisEntity)
    @Query("DELETE FROM analyses WHERE id=:id") suspend fun delete(id: String)
}
@Dao interface PlanDao {
    @Query("SELECT * FROM plans ORDER BY createdAt DESC") fun observeAll(): Flow<List<PlanEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(item: PlanEntity)
    @Query("DELETE FROM plans WHERE id=:id") suspend fun delete(id: String)
}

@Database(entities=[PlayerProfileEntity::class, ProfileUnitEntity::class, AnalysisEntity::class, PlanEntity::class], version=1, exportSchema=false)
abstract class WarDatabase: RoomDatabase() {
    abstract fun profiles(): ProfileDao
    abstract fun units(): UnitDao
    abstract fun analyses(): AnalysisDao
    abstract fun plans(): PlanDao
    companion object { @Volatile private var INSTANCE: WarDatabase? = null
        fun get(context: Context): WarDatabase = INSTANCE ?: synchronized(this) { INSTANCE ?: Room.databaseBuilder(context.applicationContext, WarDatabase::class.java, "war_assistant.db").fallbackToDestructiveMigration().build().also { INSTANCE=it } }
    }
}
