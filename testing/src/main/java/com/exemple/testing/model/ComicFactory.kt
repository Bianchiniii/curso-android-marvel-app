package com.exemple.testing.model

import com.bianchini.vinicius.matheus.core.domain.model.Comic

class ComicFactory {

    fun create(comic: FakeComic) = when (comic) {
        FakeComic.FakeComic1 -> Comic(
            2211506,
            "http://fakecomigurl.jpg"
        )
    }

    sealed class FakeComic {
        object FakeComic1 : FakeComic()
    }
}