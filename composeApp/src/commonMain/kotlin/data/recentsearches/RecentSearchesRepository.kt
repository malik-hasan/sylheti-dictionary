package data.recentsearches

import models.search.settings.SearchScript

class RecentSearchesRepository(private val dao: RecentSearchesDao) {

    suspend fun getRecentSearches(term: String, script: SearchScript) =
        if (term.isBlank()) {
            if (script == SearchScript.AUTO) {
                dao.getRecentSearches()
            } else dao.getRecentSearches(script)
        } else if (script == SearchScript.AUTO) {
            dao.getRecentSearches(term)
        } else dao.getRecentSearches(term, script)

    suspend fun cacheSearch(term: String, script: SearchScript) = dao.cacheSearch(term, script)
}