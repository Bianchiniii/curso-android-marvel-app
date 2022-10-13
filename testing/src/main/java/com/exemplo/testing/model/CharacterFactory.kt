package com.exemplo.testing.model

import com.bianchini.vinicius.matheus.core.domain.model.Character

class CharacterFactory {

    fun create(hero: Hero) = when (hero) {
        Hero.ThreedMan -> Character(
            "3-D Man",
            "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784"
        )
        Hero.Alexandre -> Character(
            "Alexander Pierce",
            "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available"
        )
    }

    companion object {
        sealed class Hero {
            object ThreedMan : Hero()
            object Alexandre : Hero()
        }
    }
}