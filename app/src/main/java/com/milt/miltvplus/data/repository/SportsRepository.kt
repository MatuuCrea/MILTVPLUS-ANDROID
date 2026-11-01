package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.SportsEvent
import com.milt.miltvplus.domain.model.SportsStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SportsRepository {

    private val sportsEvents = MutableStateFlow(
        listOf(
            SportsEvent(
                id = "sp_1",
                title = "Eliminatorias Sudamericanas - Fecha 12",
                league = "CONMEBOL World Cup Qualifiers",
                status = SportsStatus.LIVE,
                time = "Live 72'",
                team1Name = "Argentina",
                team1Flag = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=300&auto=format&fit=crop",
                team2Name = "Brasil",
                team2Flag = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=300&auto=format&fit=crop",
                score = "1 - 0",
                channelName = "TyC Sports / TV Pública",
                streamUrl = "", // Stream de transmisión en vivo no configurado en demo
                isFavorite = true
            ),
            SportsEvent(
                id = "sp_2",
                title = "El Clásico - La Liga Española",
                league = "La Liga Santander",
                status = SportsStatus.LIVE,
                time = "Live 35'",
                team1Name = "Real Madrid",
                team1Flag = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&auto=format&fit=crop",
                team2Name = "FC Barcelona",
                team2Flag = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=300&auto=format&fit=crop",
                score = "2 - 1",
                channelName = "ESPN 1 HD",
                streamUrl = "",
                isFavorite = true
            ),
            SportsEvent(
                id = "sp_3",
                title = "UEFA Champions League Final",
                league = "UEFA Champions League",
                status = SportsStatus.UPCOMING,
                time = "Today 21:00",
                team1Name = "Manchester City",
                team1Flag = "https://images.unsplash.com/photo-1522869635100-9f4c5e86aa37?w=300&auto=format&fit=crop",
                team2Name = "Inter Milan",
                team2Flag = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=300&auto=format&fit=crop",
                score = "0 - 0",
                channelName = "ESPN / Star+",
                streamUrl = "",
                isFavorite = false
            ),
            SportsEvent(
                id = "sp_4",
                title = "NBA Finals Game 7",
                league = "NBA Basketball",
                status = SportsStatus.UPCOMING,
                time = "Tomorrow 22:30",
                team1Name = "LA Lakers",
                team1Flag = "https://images.unsplash.com/photo-1546519638-68e109498ffc?w=300&auto=format&fit=crop",
                team2Name = "Boston Celtics",
                team2Flag = "https://images.unsplash.com/photo-1519861531473-9200262188bf?w=300&auto=format&fit=crop",
                score = "0 - 0",
                channelName = "ESPN / NBA TV",
                streamUrl = "",
                isFavorite = false
            )
        )
    )

    fun getSportsEvents(): Flow<List<SportsEvent>> = sportsEvents

    fun toggleFavorite(id: String) {
        sportsEvents.value = sportsEvents.value.map { item ->
            if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
        }
    }
}
