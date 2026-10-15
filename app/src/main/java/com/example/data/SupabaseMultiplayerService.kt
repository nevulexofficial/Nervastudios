package com.example.data

import com.example.BuildConfig
import com.example.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SupabaseMultiplayerService {

    private val supabaseClient = SupabaseClient.getInstance()
    private val client = supabaseClient.httpClient
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private val supabaseUrl: String
        get() = supabaseClient.supabaseUrl

    private val supabaseKey: String
        get() = supabaseClient.supabaseAnonKey

    val isConfigured: Boolean
        get() = supabaseClient.isConfigured

    // Local cached state for immediate responsiveness and offline/demo fallback
    private val mockPresenceList = mutableListOf(
        MultiplayerPresence("p_1", "Carlos DT", "Real Madrid FC", 1, 1450, "online", 28),
        MultiplayerPresence("p_2", "Alexandre", "Manchester Blues", 1, 1380, "online", 35),
        MultiplayerPresence("p_3", "Mateo Rossi", "Milán Rossonero", 2, 1310, "looking_for_match", 42),
        MultiplayerPresence("p_4", "Sébastien", "Paris Saint DT", 3, 1260, "looking_for_match", 31),
        MultiplayerPresence("p_5", "Lucas Silva", "Flamengo Star", 4, 1210, "online", 49),
        MultiplayerPresence("p_6", "Diego Nerva", "Gamma Nerva Stars", 12, 1205, "looking_for_match", 24)
    )

    private val mockLobbies = mutableListOf(
        MultiplayerLobby(
            id = "LOBBY_001",
            roomCode = "NERVA-1024",
            hostManagerId = "p_3",
            hostClubName = "Milán Rossonero",
            hostElo = 1310,
            status = LobbyStatus.WAITING,
            isPrivate = false
        ),
        MultiplayerLobby(
            id = "LOBBY_002",
            roomCode = "NERVA-7751",
            hostManagerId = "p_4",
            hostClubName = "Paris Saint DT",
            hostElo = 1260,
            status = LobbyStatus.WAITING,
            isPrivate = false
        )
    )

    private val mockLeaderboard = mutableListOf(
        MultiplayerLeaderboardEntry(1, "bot_1", "Carlos DT", "Real Madrid FC", 1450, 24, 18, 3, 3, 52, 19, "División 1"),
        MultiplayerLeaderboardEntry(2, "bot_2", "Alexandre", "Manchester Blues", 1380, 20, 14, 2, 4, 44, 22, "División 1"),
        MultiplayerLeaderboardEntry(3, "bot_3", "Mateo Rossi", "Milán Rossonero", 1310, 19, 12, 4, 3, 38, 20, "División 2"),
        MultiplayerLeaderboardEntry(4, "bot_4", "Sébastien", "Paris Saint DT", 1260, 16, 9, 3, 4, 31, 21, "División 3"),
        MultiplayerLeaderboardEntry(5, "bot_5", "Lucas Silva", "Flamengo Star", 1210, 15, 8, 2, 5, 27, 24, "División 4"),
        MultiplayerLeaderboardEntry(6, "bot_6", "Diego Nerva", "Gamma Nerva Stars", 1205, 10, 5, 2, 3, 18, 14, "Liga Gamma 12")
    )

    /**
     * Enviar heartbeat de presencia a Supabase
     */
    suspend fun sendPresence(presence: MultiplayerPresence): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            val existing = mockPresenceList.indexOfFirst { it.managerId == presence.managerId }
            if (existing >= 0) mockPresenceList[existing] = presence else mockPresenceList.add(presence)
            return@withContext true
        }

        try {
            val json = JSONObject().apply {
                put("manager_id", presence.managerId)
                put("manager_name", presence.managerName)
                put("club_name", presence.clubName)
                put("club_division", presence.clubDivision)
                put("elo_rating", presence.eloRating)
                put("status", presence.status)
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_presence")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "resolution=merge-duplicates")
                .post(json.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Obtener lista de jugadores y managers en línea
     */
    suspend fun getOnlinePresence(): List<MultiplayerPresence> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext mockPresenceList.toList()

        try {
            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_presence?select=*&order=last_seen.desc&limit=20")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext mockPresenceList.toList()
                val body = response.body?.string() ?: return@withContext mockPresenceList.toList()
                val array = JSONArray(body)
                val list = mutableListOf<MultiplayerPresence>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        MultiplayerPresence(
                            managerId = obj.optString("manager_id", "m_$i"),
                            managerName = obj.optString("manager_name", "Manager"),
                            clubName = obj.optString("club_name", "Club"),
                            clubDivision = obj.optInt("club_division", 12),
                            eloRating = obj.optInt("elo_rating", 1200),
                            status = obj.optString("status", "online"),
                            pingMs = (20..50).random()
                        )
                    )
                }
                if (list.isNotEmpty()) list else mockPresenceList.toList()
            }
        } catch (_: Exception) {
            mockPresenceList.toList()
        }
    }

    /**
     * Obtener salas activas en espera
     */
    suspend fun getActiveLobbies(): List<MultiplayerLobby> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext mockLobbies.toList()

        try {
            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_lobbies?select=*&status=eq.waiting&order=created_at.desc&limit=15")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext mockLobbies.toList()
                val body = response.body?.string() ?: return@withContext mockLobbies.toList()
                val array = JSONArray(body)
                val list = mutableListOf<MultiplayerLobby>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        MultiplayerLobby(
                            id = obj.optString("id", "LOBBY_$i"),
                            roomCode = obj.optString("room_code", "NERVA-$i"),
                            hostManagerId = obj.optString("host_manager_id", ""),
                            hostClubName = obj.optString("host_club_name", "Club"),
                            hostElo = obj.optInt("host_elo", 1200),
                            guestManagerId = obj.optString("guest_manager_id", null),
                            guestClubName = obj.optString("guest_club_name", null),
                            status = LobbyStatus.WAITING,
                            isPrivate = obj.optBoolean("is_private", false)
                        )
                    )
                }
                list
            }
        } catch (_: Exception) {
            mockLobbies.toList()
        }
    }

    /**
     * Crear una nueva sala multijugador
     */
    suspend fun createLobby(
        roomCode: String,
        hostManagerId: String,
        hostClubName: String,
        hostElo: Int,
        isPrivate: Boolean = false
    ): MultiplayerLobby? = withContext(Dispatchers.IO) {
        val lobbyId = "LOBBY_" + System.currentTimeMillis()
        val newLobby = MultiplayerLobby(
            id = lobbyId,
            roomCode = roomCode,
            hostManagerId = hostManagerId,
            hostClubName = hostClubName,
            hostElo = hostElo,
            status = LobbyStatus.WAITING,
            isPrivate = isPrivate
        )

        if (!isConfigured) {
            mockLobbies.add(0, newLobby)
            return@withContext newLobby
        }

        try {
            val json = JSONObject().apply {
                put("id", lobbyId)
                put("room_code", roomCode)
                put("host_manager_id", hostManagerId)
                put("host_club_name", hostClubName)
                put("host_elo", hostElo)
                put("status", "waiting")
                put("is_private", isPrivate)
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_lobbies")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .addHeader("Content-Type", "application/json")
                .post(json.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) newLobby else {
                    mockLobbies.add(0, newLobby)
                    newLobby
                }
            }
        } catch (_: Exception) {
            mockLobbies.add(0, newLobby)
            newLobby
        }
    }

    /**
     * Unirse a una sala existente mediante código
     */
    suspend fun joinLobbyByCode(
        roomCode: String,
        guestManagerId: String,
        guestClubName: String,
        guestElo: Int
    ): MultiplayerLobby? = withContext(Dispatchers.IO) {
        if (!isConfigured) {
            val index = mockLobbies.indexOfFirst { it.roomCode.equals(roomCode.trim(), ignoreCase = true) }
            if (index >= 0) {
                val current = mockLobbies[index]
                val updated = current.copy(
                    guestManagerId = guestManagerId,
                    guestClubName = guestClubName,
                    guestElo = guestElo,
                    status = LobbyStatus.READY
                )
                mockLobbies[index] = updated
                return@withContext updated
            }
            return@withContext null
        }

        try {
            val json = JSONObject().apply {
                put("guest_manager_id", guestManagerId)
                put("guest_club_name", guestClubName)
                put("guest_elo", guestElo)
                put("status", "ready")
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_lobbies?room_code=eq.${roomCode.trim()}")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .addHeader("Content-Type", "application/json")
                .patch(json.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    MultiplayerLobby(
                        id = "LOBBY_JOINED",
                        roomCode = roomCode,
                        hostManagerId = "host",
                        hostClubName = "Rival Online",
                        guestManagerId = guestManagerId,
                        guestClubName = guestClubName,
                        guestElo = guestElo,
                        status = LobbyStatus.READY
                    )
                } else null
            }
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Obtener tabla mundial de clasificación ELO
     */
    suspend fun getLeaderboard(): List<MultiplayerLeaderboardEntry> = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext mockLeaderboard.toList()

        try {
            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_leaderboard?select=*&order=elo_rating.desc&limit=25")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return@withContext mockLeaderboard.toList()
                val body = response.body?.string() ?: return@withContext mockLeaderboard.toList()
                val array = JSONArray(body)
                val list = mutableListOf<MultiplayerLeaderboardEntry>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        MultiplayerLeaderboardEntry(
                            rank = i + 1,
                            managerId = obj.optString("manager_id", "m_$i"),
                            managerName = obj.optString("manager_name", "DT"),
                            clubName = obj.optString("club_name", "Club"),
                            eloRating = obj.optInt("elo_rating", 1200),
                            matchesPlayed = obj.optInt("matches_played", 0),
                            wins = obj.optInt("wins", 0),
                            draws = obj.optInt("draws", 0),
                            losses = obj.optInt("losses", 0),
                            goalsFor = obj.optInt("goals_for", 0),
                            goalsAgainst = obj.optInt("goals_against", 0),
                            divisionTier = obj.optString("division_tier", "División 1")
                        )
                    )
                }
                if (list.isNotEmpty()) list else mockLeaderboard.toList()
            }
        } catch (_: Exception) {
            mockLeaderboard.toList()
        }
    }

    /**
     * Sincronizar y actualizar el logo del club en la base de datos de Supabase
     */
    suspend fun updateClubLogo(clubId: String, logoUri: String, base64Data: String): Boolean = withContext(Dispatchers.IO) {
        if (!isConfigured) return@withContext true
        try {
            val json = JSONObject().apply {
                put("club_id", clubId)
                put("logo_url", logoUri)
                if (base64Data.isNotBlank()) {
                    put("logo_base64", base64Data.take(50000))
                }
                put("updated_at", System.currentTimeMillis())
            }

            val request = Request.Builder()
                .url("$supabaseUrl/rest/v1/multiplayer_presence?manager_id=eq.$clubId")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer $supabaseKey")
                .addHeader("Content-Type", "application/json")
                .patch(json.toString().toRequestBody(jsonMediaType))
                .build()

            client.newCall(request).execute().use { response ->
                response.isSuccessful
            }
        } catch (_: Exception) {
            false
        }
    }
}
