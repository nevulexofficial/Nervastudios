package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppBackgroundTheme
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NervaSidebarDarker
import com.example.viewmodel.ManagerViewModel
import com.example.viewmodel.ScreenDestination

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NervaApp()
            }
        }
    }
}

@Composable
fun NervaApp(
    viewModel: ManagerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clubs by viewModel.clubs.collectAsStateWithLifecycle()
    val userSquad by viewModel.userSquad.collectAsStateWithLifecycle()
    val marketPlayers by viewModel.marketPlayers.collectAsStateWithLifecycle()
    val tactic by viewModel.tactic.collectAsStateWithLifecycle()
    val matches by viewModel.matches.collectAsStateWithLifecycle()
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val incomingOffers by viewModel.incomingOffers.collectAsStateWithLifecycle()
    val liveMatchState by viewModel.liveMatchState.collectAsStateWithLifecycle()
    val sponsors by viewModel.sponsors.collectAsStateWithLifecycle()

    // Liga Gamma Nerva, 12 Divisions, 5h Scheduled Matches, Theme & Music
    val leagues by viewModel.leagues.collectAsStateWithLifecycle()
    val divisions by viewModel.divisions.collectAsStateWithLifecycle()
    val selectedDivisionNumber by viewModel.selectedDivisionNumber.collectAsStateWithLifecycle()
    val scheduledSlots by viewModel.scheduledSlots.collectAsStateWithLifecycle()
    val backgroundTheme by viewModel.backgroundTheme.collectAsStateWithLifecycle()
    val isMusicPlaying by viewModel.isMusicPlaying.collectAsStateWithLifecycle()
    val currentTrackIndex by viewModel.currentTrackIndex.collectAsStateWithLifecycle()
    val musicVolume by viewModel.musicVolume.collectAsStateWithLifecycle()
    val customAudioUrl by viewModel.customAudioUrl.collectAsStateWithLifecycle()

    val userClub = viewModel.userClub

    // Back handling
    BackHandler(enabled = uiState.currentScreen != ScreenDestination.HOME && uiState.currentScreen != ScreenDestination.SPLASH) {
        viewModel.navigateTo(ScreenDestination.HOME)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.notificationMessage) {
        uiState.notificationMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(backgroundTheme.secondaryColorHex))
    ) {
        // 1. SPLASH SCREEN (Initial app loading experience)
        if (uiState.currentScreen == ScreenDestination.SPLASH) {
            SplashScreen(
                onFinished = { viewModel.navigateTo(ScreenDestination.HOME) }
            )
        } else {
            // 2. MAIN APPLICATION SHELL (Landscape Left Sidebar + Content Area)
            Row(modifier = Modifier.fillMaxSize()) {
                // Left Sidebar (except during full-screen 2D match)
                if (uiState.currentScreen != ScreenDestination.MATCH_2D) {
                    AppSidebar(
                        currentScreen = uiState.currentScreen,
                        onNavigate = { dest -> viewModel.navigateTo(dest) },
                        userClub = userClub,
                        isMusicPlaying = isMusicPlaying,
                        onToggleMusic = { viewModel.toggleMusic() }
                    )
                }

                // Main Content View Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    when (uiState.currentScreen) {
                        ScreenDestination.SPLASH -> {}

                        ScreenDestination.HOME -> {
                            HomeScreen(
                                userClub = userClub,
                                squad = userSquad,
                                onNavigate = { dest -> viewModel.navigateTo(dest) },
                                onOpenMail = { viewModel.openReferralDialog() },
                                onOpenClubEditor = { viewModel.navigateTo(ScreenDestination.MI_CLUB) },
                                onPlayerClick = { viewModel.selectPlayer(it) }
                            )
                        }

                        ScreenDestination.LIGA -> {
                            CompetitionsScreen(
                                userClub = userClub,
                                leagues = leagues,
                                divisions = divisions,
                                selectedDivisionNumber = selectedDivisionNumber,
                                scheduledSlots = scheduledSlots,
                                onSelectDivision = { viewModel.selectDivision(it) },
                                onSimulateSlot = { viewModel.simulateScheduledSlot(it) },
                                onPlayMatch = { viewModel.startNextMatch2D() },
                                onNavigate = { dest -> viewModel.navigateTo(dest) },
                                onClubClick = {}
                            )
                        }

                        ScreenDestination.PATROCINADORES -> {
                            SponsorsScreen(
                                userClub = userClub,
                                sponsors = sponsors,
                                onToggleSponsor = { viewModel.toggleSignSponsor(it) },
                                onOpenMail = { viewModel.openReferralDialog() }
                            )
                        }

                        ScreenDestination.MULTIJUGADOR -> {
                            MultiplayerScreen(
                                presenceList = uiState.multiplayerPresence,
                                lobbiesList = uiState.multiplayerLobbies,
                                leaderboardList = uiState.multiplayerLeaderboard,
                                activeLobby = uiState.activeMultiplayerLobby,
                                isSearching = uiState.isSearchingMatch,
                                statusMessage = uiState.multiplayerStatusMessage,
                                isSupabaseConfigured = uiState.isSupabaseConfigured,
                                onQuickMatchmaking = { viewModel.startQuickMatchmaking() },
                                onCreatePrivateLobby = { viewModel.createPrivateLobby() },
                                onJoinLobby = { viewModel.joinLobbyByCode(it) },
                                onChallengeManager = { viewModel.challengeOnlineManager(it) },
                                onCancelMatchmaking = { viewModel.cancelMatchmaking() },
                                onRefresh = { viewModel.refreshMultiplayerData() }
                            )
                        }

                        ScreenDestination.MI_CLUB -> {
                            ClubEditorScreen(
                                userClub = userClub,
                                onSaveClub = { name, shortName, motto, mgr, std, pCol, sCol, shape, symbol, bCol, banner ->
                                    viewModel.updateClubProfile(
                                        name, shortName, motto, mgr, std, pCol, sCol, shape, symbol, bCol, banner
                                    )
                                },
                                onNavigate = { dest -> viewModel.navigateTo(dest) }
                            )
                        }

                        ScreenDestination.ADMIN -> {
                            AdminScreen(
                                userClub = userClub,
                                squad = userSquad,
                                leagues = leagues,
                                currentBackgroundTheme = backgroundTheme,
                                isMusicPlaying = isMusicPlaying,
                                currentTrackIndex = currentTrackIndex,
                                musicVolume = musicVolume,
                                customAudioUrl = customAudioUrl,
                                onAddFunds = { viewModel.addClubFunds(it) },
                                onSetFunds = { viewModel.setClubFunds(it) },
                                onBoostAllStamina = { viewModel.boostSquadStamina() },
                                onBoostAllOverall = { viewModel.boostSquadOverall() },
                                onClearDisciplinaryCards = { viewModel.clearDisciplinaryCards() },
                                onUpgradeFacilities = { viewModel.upgradeClubFacilities() },
                                onUpdateClubReputation = { viewModel.updateClubReputation(it) },
                                onWinNextMatchGuaranteed = { viewModel.guaranteeNextMatchWin() },
                                onUpdatePlayerStats = { id, ov, valM -> viewModel.updatePlayerStats(id, ov, valM) },
                                onSetBackgroundTheme = { viewModel.setBackgroundTheme(it) },
                                onCreatePlayer = { name, pos, role, nat, age, ov, pac, sho, pas, dri, def, phy, valM, dest ->
                                    val posType = when (pos) {
                                        "DEL" -> com.example.model.PositionType.DEL
                                        "DEF" -> com.example.model.PositionType.DEF
                                        "POR" -> com.example.model.PositionType.POR
                                        else -> com.example.model.PositionType.MED
                                    }
                                    viewModel.createCustomPlayer(
                                        name, posType, role, nat, age, ov, pac, sho, pas, dri, def, phy, valM, dest
                                    )
                                },
                                onCreateLeague = { name, divs, maxC, prom, ic, ban ->
                                    viewModel.createLeague(name, divs, maxC, prom, ic, ban)
                                },
                                onUpdateLeagueBannerAndIcon = { lId, ban, ic ->
                                    viewModel.updateLeagueBannerAndIcon(lId, ban, ic)
                                },
                                onToggleMusic = { viewModel.toggleMusic() },
                                onNextTrack = { viewModel.nextMusicTrack() },
                                onSetMusicVolume = { viewModel.setMusicVolume(it) },
                                onSetCustomAudioUrl = { viewModel.setCustomAudioUrl(it) },
                                onNavigate = { dest -> viewModel.navigateTo(dest) }
                            )
                        }

                        ScreenDestination.PARTIDOS -> {
                            val rivalClub = clubs.firstOrNull { it.id == "c_chilenos" } ?: clubs.first { !it.isUserClub }
                            MatchHubScreen(
                                userClub = userClub,
                                rivalClub = rivalClub,
                                onPlayMatch = { viewModel.startNextMatch2D() },
                                onNavigate = { dest -> viewModel.navigateTo(dest) }
                            )
                        }

                        ScreenDestination.MERCADO -> {
                            MarketScreen(
                                userClub = userClub,
                                marketPlayers = marketPlayers,
                                userSquad = userSquad,
                                incomingOffers = incomingOffers,
                                isOfferDialogOpen = uiState.isOfferDialogOpen,
                                playerForOffer = uiState.playerForOffer,
                                onOpenOfferDialog = { viewModel.openOfferDialog(it) },
                                onCloseOfferDialog = { viewModel.closeOfferDialog() },
                                onSubmitOffer = { viewModel.submitTransferOffer(it) },
                                onToggleTransferList = { viewModel.toggleTransferList(it) },
                                onAcceptOffer = { viewModel.acceptIncomingOffer(it) },
                                onRejectOffer = { viewModel.rejectIncomingOffer(it) },
                                onPlayerClick = { viewModel.selectPlayer(it) },
                                onOpenMail = { viewModel.openReferralDialog() }
                            )
                        }

                        ScreenDestination.PLANTILLA -> {
                            SquadScreen(
                                userClub = userClub,
                                squad = userSquad,
                                onNavigate = { dest -> viewModel.navigateTo(dest) },
                                onPlayerClick = { viewModel.selectPlayer(it) },
                                onOpenSponsors = { viewModel.openSponsorsDialog() },
                                onEditPlayerFlag = { viewModel.openPlayerFlagEditDialog(it) },
                                onEditClubFlag = { viewModel.openClubFlagEditDialog() },
                                onOpenMail = { viewModel.openReferralDialog() }
                            )
                        }

                        ScreenDestination.ESTADISTICAS -> {
                            LeagueScreen(
                                clubs = clubs,
                                matches = matches,
                                onPlayMatch = { viewModel.startNextMatch2D() }
                            )
                        }

                        ScreenDestination.TACTICAS -> {
                            TacticsScreen(
                                tactic = tactic,
                                squad = userSquad,
                                playerToSwap = uiState.playerToSwap,
                                onSelectFormation = { viewModel.updateFormation(it) },
                                onSelectPlayStyle = { viewModel.updatePlayStyle(it) },
                                onSelectMentality = { viewModel.updateMentality(it) },
                                onSelectPressing = { viewModel.updatePressing(it) },
                                onStartSwap = { viewModel.startSwap(it) },
                                onCompleteSwap = { viewModel.completeSwap(it) },
                                onCancelSwap = { viewModel.cancelSwap() },
                                onInspectPlayer = { viewModel.selectPlayer(it) }
                            )
                        }

                        ScreenDestination.FORO_DT -> {
                            ForumScreen(
                                posts = forumPosts,
                                isCreatePostDialogOpen = uiState.isCreatePostDialogOpen,
                                onOpenCreatePostDialog = { viewModel.openCreatePostDialog() },
                                onCloseCreatePostDialog = { viewModel.closeCreatePostDialog() },
                                onSubmitPost = { title, content, cat -> viewModel.submitForumPost(title, content, cat) },
                                onReplyToPost = { postId, text -> viewModel.replyToPost(postId, text) },
                                onToggleLike = { viewModel.toggleLike(it) }
                            )
                        }

                        ScreenDestination.AJUSTES -> {
                            SettingsScreen(userClub = userClub)
                        }

                        ScreenDestination.MATCH_2D -> {
                            val rivalClub = clubs.firstOrNull { it.id == "c_chilenos" } ?: clubs.first { !it.isUserClub }
                            MatchScreen(
                                state = liveMatchState,
                                homeClub = userClub,
                                awayClub = rivalClub,
                                onTogglePlayPause = { viewModel.toggleMatchPlayPause() },
                                onSetSpeed = { viewModel.setMatchSpeed(it) },
                                onChangeMentality = { viewModel.updateMentality(it) },
                                onResumeSecondHalf = { viewModel.resumeSecondHalf() },
                                onSkipToEnd = { viewModel.skipMatchToEnd() },
                                onCloseSummary = { viewModel.closeMatchSummary() },
                                showSummaryDialog = uiState.showMatchSummaryDialog
                            )
                        }
                    }
                }
            }
        }

        // Sponsors Modal Dialog
        if (uiState.showSponsorsDialog) {
            SponsorsModal(
                sponsors = sponsors,
                onToggleSponsor = { viewModel.toggleSignSponsor(it) },
                onDismiss = { viewModel.closeSponsorsDialog() }
            )
        }

        // Club Flag Picker Dialog
        if (uiState.showClubFlagEditDialog) {
            FlagPickerModal(
                title = "Editar Bandera de ${userClub.name}",
                currentFlag = userClub.flag,
                onSelectFlag = { viewModel.updateClubFlag(it) },
                onDismiss = { viewModel.closeFlagEditDialog() }
            )
        }

        // Player Flag Picker Dialog
        uiState.playerForFlagEdit?.let { player ->
            FlagPickerModal(
                title = "Nacionalidad de ${player.name}",
                currentFlag = player.nationality,
                onSelectFlag = { flag -> viewModel.updatePlayerFlag(player.id, flag) },
                onDismiss = { viewModel.closeFlagEditDialog() }
            )
        }

        // Player Evolution & Physical State Analytics Modal (Chart Visualization)
        uiState.selectedPlayer?.let { player ->
            PlayerEvolutionModal(
                player = player,
                onDismiss = { viewModel.selectPlayer(null) }
            )
        }

        // Advanced Club Editor Modal (Lema, Logo personalizado, Nombre, etc.)
        if (uiState.showClubEditorModal) {
            ClubEditorModal(
                userClub = userClub,
                onSaveClub = { name, shortName, motto, mgr, std, pCol, sCol, shape, symbol, bCol, banner ->
                    viewModel.updateClubProfile(
                        name, shortName, motto, mgr, std, pCol, sCol, shape, symbol, bCol, banner
                    )
                },
                onDismiss = { viewModel.closeClubEditor() }
            )
        }

        // Referral Codes & Admin Panel Modal (iamnevulex)
        if (uiState.showReferralDialog) {
            ReferralAdminModal(
                userClub = userClub,
                squad = userSquad,
                leagues = leagues,
                isAdminUnlocked = uiState.isAdminUnlocked,
                currentBackgroundTheme = backgroundTheme,
                isMusicPlaying = isMusicPlaying,
                currentTrackIndex = currentTrackIndex,
                musicVolume = musicVolume,
                customAudioUrl = customAudioUrl,
                onUnlockAdmin = { viewModel.unlockAdmin() },
                onAddFunds = { viewModel.addClubFunds(it) },
                onSetFunds = { viewModel.setClubFunds(it) },
                onBoostAllStamina = { viewModel.boostSquadStamina() },
                onBoostAllOverall = { viewModel.boostSquadOverall() },
                onClearDisciplinaryCards = { viewModel.clearDisciplinaryCards() },
                onUpgradeFacilities = { viewModel.upgradeClubFacilities() },
                onUpdateClubReputation = { viewModel.updateClubReputation(it) },
                onWinNextMatchGuaranteed = { viewModel.guaranteeNextMatchWin() },
                onUpdatePlayerStats = { id, ov, valM -> viewModel.updatePlayerStats(id, ov, valM) },
                onSetBackgroundTheme = { viewModel.setBackgroundTheme(it) },
                onCreatePlayer = { name, pos, role, nat, age, ov, pac, sho, pas, dri, def, phy, valM, dest ->
                    viewModel.createCustomPlayer(
                        name, pos, role, nat, age, ov, pac, sho, pas, dri, def, phy, valM, dest
                    )
                },
                onCreateLeague = { name, divs, maxC, prom, ic, ban ->
                    viewModel.createLeague(name, divs, maxC, prom, ic, ban)
                },
                onUpdateLeagueBannerAndIcon = { lId, ban, ic ->
                    viewModel.updateLeagueBannerAndIcon(lId, ban, ic)
                },
                onToggleMusic = { viewModel.toggleMusic() },
                onNextTrack = { viewModel.nextMusicTrack() },
                onSetMusicVolume = { viewModel.setMusicVolume(it) },
                onSetCustomAudioUrl = { viewModel.setCustomAudioUrl(it) },
                onDismiss = { viewModel.closeReferralDialog() }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(16.dp)
        )
    }
}
