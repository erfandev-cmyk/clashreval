package com.cocwarassistant.account

import android.content.Context
import com.cocwarassistant.core.*
import com.cocwarassistant.database.WarDatabase
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AccountRepository(context: Context) {
    private val db = WarDatabase.get(context)
    fun observeProfiles(): Flow<List<PlayerProfileEntity>> = db.profiles().observeAll()
    suspend fun saveProfile(profile: PlayerProfileEntity, units: List<ProfileUnitEntity>) { db.profiles().upsert(profile); db.units().deleteForProfile(profile.id); db.units().upsertAll(units) }
    suspend fun createDefault(name: String, tag: String, th: Int): String { val id=UUID.randomUUID().toString(); saveProfile(PlayerProfileEntity(id,name,tag,th,0,System.currentTimeMillis()), emptyList()); return id }
    suspend fun delete(profile: PlayerProfileEntity) { db.units().deleteForProfile(profile.id); db.profiles().delete(profile) }
    suspend fun getUnits(id:String)=db.units().getForProfile(id)
}
