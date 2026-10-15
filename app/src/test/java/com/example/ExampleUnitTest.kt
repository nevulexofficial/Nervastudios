package com.example

import com.example.data.ManagerRepository
import com.example.model.Formation
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testInitialClubsAndCompetitors() {
        val repo = ManagerRepository()
        val clubs = repo.clubs.value

        assertEquals(8, clubs.size)
        assertTrue(clubs.any { it.name == "NERVA FC" && it.isUserClub })
        assertTrue(clubs.any { it.name == "Chilenos FC" })
        assertTrue(clubs.any { it.name == "La Nazionale" })
        assertTrue(clubs.any { it.name == "Tamber CF" })
    }

    @Test
    fun testFormationUpdate() {
        val repo = ManagerRepository()
        repo.updateFormation(Formation.F_352)

        assertEquals(Formation.F_352, repo.tactic.value.formation)
        assertEquals(11, repo.userSquad.value.filter { it.isStarter }.size)
    }

    @Test
    fun testPlayerSwap() {
        val repo = ManagerRepository()
        val squad = repo.userSquad.value
        val starter = squad.first { it.isStarter }
        val bench = squad.first { !it.isStarter }

        repo.swapPlayers(starter.id, bench.id)

        val updatedSquad = repo.userSquad.value
        val newStarter = updatedSquad.first { it.id == bench.id }
        val newBench = updatedSquad.first { it.id == starter.id }

        assertTrue(newStarter.isStarter)
        assertFalse(newBench.isStarter)
    }
}
