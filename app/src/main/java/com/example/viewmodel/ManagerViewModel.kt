package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.InitialData
import com.example.data.ManagerRepository
import com.example.data.SupabaseMultiplayerService
import com.example.engine.MatchEngine2D
import com.example.engine.MatchEngineState
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ScreenDestination(val title: String) {
    SPLASH("Splash"),
    HOME("Inicio"),
    MULTIJUGADOR("Multijugador Online"),
    LIGA("Liga"),
    PLANTILLA("Plantilla"),
    MERCADO("Mercado"),
    PARTIDOS("Partidos"),
    ESTADISTICAS("Estadísticas"),
    TACTICAS("Tácticas"),
    PATROCINADORES("Patrocinadores"),
    MI_CLUB("Mi Club"),
    ADMIN("Panel Admin"),
    FORO_DT("Foro DT"),
    AJUSTES("Ajustes"),
    MATCH_2D("Partido 2D");

    companion object {
        val LEAGUE = LIGA
        val TACTICS = TACTICAS
        val MARKET = MERCADO
        val FORUM = FORO_DT
        val SPONSORS = PATROCINADORES
        val MULTIPLAYER = MULTIJUGADOR
    }
}

data class ManagerUiState(
    val currentScreen: ScreenDestination = ScreenDestination.SPLASH,
    val selectedPlayer: Player? = null,
    val playerToSwap: Player? = null,
    val isOfferDialogOpen: Boolean = false,
    val playerForOffer: Player? = null,
    val isCreatePostDialogOpen: Boolean = false,
    val showMatchSummaryDialog: Boolean = false,
    val showSponsorsDialog: Boolean = false,
    val showClubFlagEditDialog: Boolean = false,
    val playerForFlagEdit: Player? = null,
    val showReferralDialog: Boolean = false,
    val isAdminUnlocked: Boolean = true,
    val guaranteedWin: Boolean = false,
    val showClubEditorModal: Boolean = false,
    val showLoadingScreenModal: Boolean = false,
    val notificationMessage: String? = null,
    val multiplayerPresence: List<MultiplayerPresence> = emptyList(),
    val multiplayerLobbies: List<MultiplayerLobby> = emptyList(),
    val multiplayerLeaderboard: List<MultiplayerLeaderboardEntry> = emptyList(),
    val activeMultiplayerLobby: MultiplayerLobby? = null,
    val isSearchingMatch: Boolean = false,
    val multiplayerStatusMessage: String? = null,
    val isSupabaseConfigured: Boolean = false
)

class ManagerViewModel(
    private val repository: ManagerRepository = ManagerRepository()
) : ViewModel() {

    val clubs = repository.clubs
    val userSquad = repository.userSquad
    val marketPlayers = repository.marketPlayers
    val tactic = repository.tactic
    val matches = repository.matches
    val forumPosts = repository.forumPosts
    val incomingOffers = repository.incomingOffers
    val sponsors = repository.sponsors

    // Liga Gamma Nerva, 12 Divisions, Scheduled Matches, Theme & Music
    val leagues = repository.leagues
    val divisions = repository.divisions
    val selectedDivisionNumber = repository.selectedDivisionNumber
    val scheduledSlots = repository.scheduledSlots
    val backgroundTheme = repository.backgroundTheme
    val isMusicPlaying = repository.isMusicPlaying
    val currentTrackIndex = repository.currentTrackIndex
    val musicVolume = repository.musicVolume
    val customAudioUrl = repository.customAudioUrl

    private val _uiState = MutableStateFlow(ManagerUiState())
    val uiState: StateFlow<ManagerUiState> = _uiState.asStateFlow()

    private val supabaseService = SupabaseMultiplayerService()

    init {
        _uiState.update { it.copy(isSupabaseConfigured = supabaseService.isConfigured) }
        refreshMultiplayerData()
    }

    // 2D Match Engine
    private var _matchEngine: MatchEngine2D? = null
    val matchEngine: MatchEngine2D? get() = _matchEngine

    private val _liveMatchState = MutableStateFlow<MatchEngineState?>(null)
    val liveMatchState: StateFlow<MatchEngineState?> = _liveMatchState.asStateFlow()

    private var currentSimulatedMatch: Match? = null

    val userClub: Club
        get() = repository.userClub

    fun navigateTo(destination: ScreenDestination) {
        _uiState.update { it.copy(currentScreen = destination) }
    }

    fun selectPlayer(player: Player?) {
        _uiState.update { it.copy(selectedPlayer = player) }
    }

    fun startSwap(player: Player) {
        _uiState.update { it.copy(playerToSwap = player) }
    }

    fun completeSwap(targetPlayer: Player) {
        val first = _uiState.value.playerToSwap
        if (first != null && first.id != targetPlayer.id) {
            repository.swapPlayers(first.id, targetPlayer.id)
            showNotification("Cambio realizado: ${first.name} ⇄ ${targetPlayer.name}")
        }
        _uiState.update { it.copy(playerToSwap = null, selectedPlayer = null) }
    }

    fun cancelSwap() {
        _uiState.update { it.copy(playerToSwap = null) }
    }

    fun updateFormation(formation: Formation) {
        repository.updateFormation(formation)
        showNotification("Nueva formación: ${formation.displayName}")
    }

    fun updatePlayStyle(style: PlayStyle) {
        repository.updatePlayStyle(style)
        showNotification("Estilo: ${style.displayName}")
    }

    fun updateMentality(mentality: Mentality) {
        repository.updateMentality(mentality)
        _matchEngine?.setInGameMentality(mentality)
        showNotification("Mentalidad: ${mentality.displayName}")
    }

    fun updatePressing(pressing: PressingIntensity) {
        repository.updatePressing(pressing)
    }

    // --- MARKET ---
    fun openOfferDialog(player: Player) {
        _uiState.update { it.copy(isOfferDialogOpen = true, playerForOffer = player) }
    }

    fun closeOfferDialog() {
        _uiState.update { it.copy(isOfferDialogOpen = false, playerForOffer = null) }
    }

    fun submitTransferOffer(amount: Long) {
        val player = _uiState.value.playerForOffer ?: return
        val success = repository.buyPlayer(player, amount)
        if (success) {
            showNotification("¡Fichaje cerrado! ${player.name} se unió a NERVA CF.")
        } else {
            showNotification("Presupuesto insuficiente para realizar el fichaje.")
        }
        closeOfferDialog()
    }

    fun toggleTransferList(player: Player) {
        repository.toggleTransferList(player.id)
        val status = if (!player.isTransferListed) "en venta" else "retirado de la venta"
        showNotification("${player.name} puesto $status.")
    }

    fun acceptIncomingOffer(offerId: String) {
        repository.acceptIncomingOffer(offerId)
        showNotification("Oferta aceptada. Fondos añadidos al presupuesto del club.")
    }

    fun rejectIncomingOffer(offerId: String) {
        repository.rejectIncomingOffer(offerId)
        showNotification("Oferta rechazada.")
    }

    // --- FORUM ---
    fun openCreatePostDialog() {
        _uiState.update { it.copy(isCreatePostDialogOpen = true) }
    }

    fun closeCreatePostDialog() {
        _uiState.update { it.copy(isCreatePostDialogOpen = false) }
    }

    fun submitForumPost(title: String, content: String, category: String) {
        if (title.isNotBlank() && content.isNotBlank()) {
            repository.createPost(title, content, category)
            showNotification("Publicación compartida con los directores técnicos.")
        }
        closeCreatePostDialog()
    }

    fun replyToPost(postId: String, text: String) {
        if (text.isNotBlank()) {
            repository.addReply(postId, text)
            showNotification("Respuesta enviada.")
        }
    }

    fun toggleLike(postId: String) {
        repository.toggleLike(postId)
    }

    // --- SPONSORS ---
    fun openSponsorsDialog() {
        _uiState.update { it.copy(showSponsorsDialog = true) }
    }

    fun closeSponsorsDialog() {
        _uiState.update { it.copy(showSponsorsDialog = false) }
    }

    fun toggleSignSponsor(sponsorId: String) {
        val signed = repository.toggleSignSponsor(sponsorId)
        val msg = if (signed) "¡Contrato de patrocinio firmado! +$ 500.000 bono añadido" else "Patrocinio cancelado"
        showNotification(msg)
    }

    // --- FLAGS EDITING ---
    fun openPlayerFlagEditDialog(player: Player) {
        _uiState.update { it.copy(playerForFlagEdit = player) }
    }

    fun openClubFlagEditDialog() {
        _uiState.update { it.copy(showClubFlagEditDialog = true) }
    }

    fun closeFlagEditDialog() {
        _uiState.update { it.copy(playerForFlagEdit = null, showClubFlagEditDialog = false) }
    }

    fun updatePlayerFlag(playerId: String, newFlag: String) {
        repository.updatePlayerFlag(playerId, newFlag)
        closeFlagEditDialog()
        showNotification("Nacionalidad y bandera actualizadas a $newFlag")
    }

    fun updateClubFlag(newFlag: String) {
        repository.updateClubFlag(userClub.id, newFlag)
        closeFlagEditDialog()
        showNotification("Bandera del club actualizada a $newFlag")
    }

    // --- 2D MATCH ENGINE & SIMULATION ---
    fun startNextMatch2D() {
        val upcomingMatch = matches.value.firstOrNull { !it.isPlayed && (it.homeClubId == userClub.id || it.awayClubId == userClub.id) }
        if (upcomingMatch == null) {
            showNotification("No hay partidos pendientes en esta ronda.")
            return
        }

        currentSimulatedMatch = upcomingMatch
        val homeClub = clubs.value.first { it.id == upcomingMatch.homeClubId }
        val awayClub = clubs.value.first { it.id == upcomingMatch.awayClubId }

        val homeSquad: List<Player> = if (homeClub.isUserClub) userSquad.value else InitialData.marketPool.filter { it.clubId == homeClub.id }
        val awaySquad: List<Player> = if (awayClub.isUserClub) userSquad.value else InitialData.marketPool.filter { it.clubId == awayClub.id }

        val finalHomePlayers = if (homeSquad.isEmpty()) InitialData.nervaPlayers else homeSquad
        val finalAwayPlayers = if (awaySquad.isEmpty()) InitialData.marketPool else awaySquad

        val engine = MatchEngine2D(
            homeClub = homeClub,
            awayClub = awayClub,
            homePlayers = finalHomePlayers,
            awayPlayers = finalAwayPlayers,
            mentality = tactic.value.mentality
        )
        _matchEngine = engine

        // Collect engine state
        viewModelScope.launch {
            engine.engineState.collect { state ->
                _liveMatchState.value = state
                if (state.isFinished && !_uiState.value.showMatchSummaryDialog) {
                    onMatchFinished(state)
                }
            }
        }

        navigateTo(ScreenDestination.MATCH_2D)
        engine.startSimulation(viewModelScope)
    }

    fun startMatchAgainst(rivalClub: Club) {
        val homeClub = userClub
        val awayClub = rivalClub
        val homeSquad = userSquad.value
        val awaySquad = InitialData.marketPool.filter { it.clubId == awayClub.id }
        val finalHomePlayers = if (homeSquad.isEmpty()) InitialData.nervaPlayers else homeSquad
        val finalAwayPlayers = if (awaySquad.isEmpty()) InitialData.marketPool else awaySquad

        val engine = MatchEngine2D(
            homeClub = homeClub,
            awayClub = awayClub,
            homePlayers = finalHomePlayers,
            awayPlayers = finalAwayPlayers,
            mentality = tactic.value.mentality
        )
        _matchEngine = engine

        viewModelScope.launch {
            engine.engineState.collect { state ->
                _liveMatchState.value = state
                if (state.isFinished && !_uiState.value.showMatchSummaryDialog) {
                    onMatchFinished(state)
                }
            }
        }

        navigateTo(ScreenDestination.MATCH_2D)
        engine.startSimulation(viewModelScope)
    }

    fun toggleMatchPlayPause() {
        val engine = _matchEngine ?: return
        if (engine.engineState.value.isPlaying) {
            engine.pauseSimulation()
        } else {
            engine.startSimulation(viewModelScope)
        }
    }

    fun setMatchSpeed(speed: Int) {
        _matchEngine?.setSpeedMultiplier(speed)
    }

    fun resumeSecondHalf() {
        _matchEngine?.resumeSecondHalf(viewModelScope)
    }

    fun skipMatchToEnd() {
        _matchEngine?.skipToEnd()
    }

    private fun onMatchFinished(finalState: MatchEngineState) {
        val match = currentSimulatedMatch ?: return
        repository.recordMatchResult(
            matchId = match.id,
            homeScore = finalState.homeScore,
            awayScore = finalState.awayScore,
            homeScorers = finalState.homeScorers,
            awayScorers = finalState.awayScorers,
            stats = finalState.stats,
            commentaries = finalState.commentaries
        )
        _uiState.update { it.copy(showMatchSummaryDialog = true) }
    }

    fun closeMatchSummary() {
        _uiState.update { it.copy(showMatchSummaryDialog = false) }
        navigateTo(ScreenDestination.LEAGUE)
    }

    fun showNotification(message: String) {
        _uiState.update { it.copy(notificationMessage = message) }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notificationMessage = null) }
    }

    // --- REFERRAL & ADMIN PANEL (iamnevulex) ---
    fun openReferralDialog() {
        _uiState.update { it.copy(showReferralDialog = true) }
    }

    fun closeReferralDialog() {
        _uiState.update { it.copy(showReferralDialog = false) }
    }

    fun unlockAdmin() {
        _uiState.update { it.copy(isAdminUnlocked = true) }
        showNotification("🔱 ¡Modo Administrador Supremo Desbloqueado (iamnevulex)!")
    }

    fun addClubFunds(amount: Long) {
        repository.addBudget(amount)
        showNotification("Presupuesto: +€${String.format("%,d", amount)}")
    }

    fun setClubFunds(amount: Long) {
        repository.setBudget(amount)
        showNotification("Presupuesto fijado: €${String.format("%,d", amount)}")
    }

    fun boostSquadStamina() {
        repository.boostAllStamina()
        showNotification("⚡ Físico al 100% para toda la plantilla")
    }

    fun boostSquadOverall() {
        repository.boostAllOverall(5)
        showNotification("🌟 +5 de media general a toda la plantilla")
    }

    fun clearDisciplinaryCards() {
        repository.clearDisciplinaryCards()
        showNotification("🧹 Sanciones y tarjetas disciplinarias eliminadas")
    }

    fun upgradeClubFacilities() {
        repository.upgradeFacilities()
        showNotification("🏗️ Estadio e instalaciones ampliadas al máximo")
    }

    fun updateClubReputation(rep: Int) {
        repository.updateClubReputation(rep)
        showNotification("⭐⭐⭐⭐⭐ Reputación fijada al máximo")
    }

    fun guaranteeNextMatchWin() {
        _uiState.update { it.copy(guaranteedWin = true) }
        showNotification("🏆 Victoria asegurada para el próximo partido")
    }

    fun updatePlayerStats(playerId: String, newOverall: Int, newMarketValue: Long) {
        repository.updatePlayerStats(playerId, newOverall, newMarketValue)
        showNotification("Datos del jugador actualizados correctamente")
    }

    // --- LIGA GAMMA NERVA & DIVISIONS ---
    fun selectDivision(divisionNumber: Int) {
        repository.selectDivision(divisionNumber)
    }

    fun createLeague(
        name: String,
        divisionsCount: Int,
        maxClubs: Int,
        promotesFrom: String?,
        icon: String,
        banner: String
    ) {
        repository.createLeague(name, divisionsCount, maxClubs, promotesFrom, icon, banner)
        showNotification("¡Liga '$name' creada con éxito!")
    }

    fun updateLeagueBannerAndIcon(leagueId: String, banner: String, icon: String) {
        repository.updateLeagueBannerAndIcon(leagueId, banner, icon)
        showNotification("Banner e icono de liga actualizados")
    }

    // --- SCHEDULED MATCHES (5-HOUR: [5:00AM, 10:00AM, 3:00PM, 8:00PM]) ---
    fun simulateScheduledSlot(slotId: String) {
        val result = repository.simulateScheduledSlot(slotId)
        showNotification("Partido oficial simulado: $result")
    }

    // --- CUSTOM PLAYER CREATION ---
    fun createCustomPlayer(
        name: String,
        position: PositionType,
        roleTag: String,
        nationality: String,
        age: Int,
        overall: Int,
        pace: Int,
        shooting: Int,
        passing: Int,
        dribbling: Int,
        defending: Int,
        physical: Int,
        marketValue: Long,
        destination: String
    ) {
        repository.createCustomPlayer(
            name, position, roleTag, nationality, age, overall,
            pace, shooting, passing, dribbling, defending, physical,
            marketValue, destination
        )
        showNotification("¡Futbolista '$name' ($roleTag - $overall OVR) creado!")
    }

    // --- ADVANCED CLUB PROFILE EDIT ---
    fun updateClubProfile(
        name: String,
        shortName: String,
        motto: String,
        managerName: String,
        stadiumName: String,
        primaryColorHex: Long,
        secondaryColorHex: Long,
        badgeShape: String,
        badgeSymbol: String,
        badgeBorderColorHex: Long,
        bannerStyle: String
    ) {
        repository.updateClubProfile(
            name, shortName, motto, managerName, stadiumName,
            primaryColorHex, secondaryColorHex, badgeShape, badgeSymbol,
            badgeBorderColorHex, bannerStyle
        )
        showNotification("¡Club '$name' y lema actualizados!")
    }

    // --- BACKGROUND THEME & MUSIC ---
    fun setBackgroundTheme(theme: AppBackgroundTheme) {
        repository.setBackgroundTheme(theme)
        showNotification("Fondo cambiado a: ${theme.title}")
    }

    fun toggleMusic() = repository.toggleMusic()
    fun nextMusicTrack() = repository.nextMusicTrack()
    fun setMusicVolume(vol: Float) = repository.setMusicVolume(vol)
    fun setCustomAudioUrl(url: String) = repository.setCustomAudioUrl(url)

    // --- MODAL CONTROLS ---
    fun openClubEditor() = _uiState.update { it.copy(showClubEditorModal = true) }
    fun closeClubEditor() = _uiState.update { it.copy(showClubEditorModal = false) }

    fun openLoadingScreen() = _uiState.update { it.copy(showLoadingScreenModal = true) }
    fun closeLoadingScreen() = _uiState.update { it.copy(showLoadingScreenModal = false) }

    // --- SUPABASE MULTIPLAYER CONTROLS ---
    fun refreshMultiplayerData() {
        viewModelScope.launch {
            val presence = supabaseService.getOnlinePresence()
            val lobbies = supabaseService.getActiveLobbies()
            val leaderboard = supabaseService.getLeaderboard()
            _uiState.update {
                it.copy(
                    multiplayerPresence = presence,
                    multiplayerLobbies = lobbies,
                    multiplayerLeaderboard = leaderboard,
                    isSupabaseConfigured = supabaseService.isConfigured
                )
            }
        }
    }

    fun startQuickMatchmaking() {
        _uiState.update { it.copy(isSearchingMatch = true, multiplayerStatusMessage = "Buscando rival online...") }
        viewModelScope.launch {
            val myClub = repository.userClub
            supabaseService.sendPresence(
                MultiplayerPresence(
                    managerId = "mgr_user",
                    managerName = myClub.managerName,
                    clubName = myClub.name,
                    clubDivision = 12,
                    eloRating = 1220,
                    status = "looking_for_match"
                )
            )

            kotlinx.coroutines.delay(1600) // Radar search animation

            val activeLobbies = supabaseService.getActiveLobbies()
            val available = activeLobbies.firstOrNull { it.status == LobbyStatus.WAITING && it.hostClubName != myClub.name }

            if (available != null) {
                val joined = supabaseService.joinLobbyByCode(available.roomCode, "mgr_user", myClub.name, 1220)
                _uiState.update {
                    it.copy(
                        isSearchingMatch = false,
                        activeMultiplayerLobby = joined ?: available,
                        multiplayerStatusMessage = "¡Rival encontrado! Conectando a sala ${available.roomCode}"
                    )
                }
                showNotification("¡Rival encontrado! Iniciando partido multijugador...")
                val rivalClub = clubs.value.firstOrNull { it.name == available.hostClubName }
                    ?: clubs.value.first { !it.isUserClub }
                startMatchAgainst(rivalClub)
            } else {
                val roomCode = "NERVA-" + (1000..9999).random()
                val created = supabaseService.createLobby(
                    roomCode = roomCode,
                    hostManagerId = "mgr_user",
                    hostClubName = myClub.name,
                    hostElo = 1220
                )
                _uiState.update {
                    it.copy(
                        isSearchingMatch = false,
                        activeMultiplayerLobby = created,
                        multiplayerStatusMessage = "Sala pública creada ($roomCode). Esperando rival..."
                    )
                }
                showNotification("Sala creada: $roomCode. Esperando rival...")
                refreshMultiplayerData()
            }
        }
    }

    fun createPrivateLobby() {
        viewModelScope.launch {
            val myClub = repository.userClub
            val roomCode = "NERVA-" + (1000..9999).random()
            val created = supabaseService.createLobby(
                roomCode = roomCode,
                hostManagerId = "mgr_user",
                hostClubName = myClub.name,
                hostElo = 1220,
                isPrivate = true
            )
            _uiState.update {
                it.copy(
                    activeMultiplayerLobby = created,
                    multiplayerStatusMessage = "Sala privada lista: $roomCode (Comparte el código)"
                )
            }
            showNotification("Sala privada: $roomCode")
            refreshMultiplayerData()
        }
    }

    fun joinLobbyByCode(code: String) {
        if (code.isBlank()) return
        viewModelScope.launch {
            val myClub = repository.userClub
            val joined = supabaseService.joinLobbyByCode(code.trim(), "mgr_user", myClub.name, 1220)
            if (joined != null) {
                _uiState.update {
                    it.copy(
                        activeMultiplayerLobby = joined,
                        multiplayerStatusMessage = "¡Conectado exitosamente a sala $code!"
                    )
                }
                showNotification("¡Conectado a la sala $code!")
                val rivalClub = clubs.value.firstOrNull { it.name == joined.hostClubName }
                    ?: clubs.value.first { !it.isUserClub }
                startMatchAgainst(rivalClub)
            } else {
                showNotification("No se encontró la sala $code")
            }
        }
    }

    fun challengeOnlineManager(presence: MultiplayerPresence) {
        viewModelScope.launch {
            showNotification("Desafiando a ${presence.managerName} (${presence.clubName})...")
            val rivalClub = clubs.value.firstOrNull { it.name.contains(presence.clubName, ignoreCase = true) }
                ?: clubs.value.first { !it.isUserClub }
            startMatchAgainst(rivalClub)
        }
    }

    fun cancelMatchmaking() {
        _uiState.update { it.copy(isSearchingMatch = false, multiplayerStatusMessage = null) }
    }
}
