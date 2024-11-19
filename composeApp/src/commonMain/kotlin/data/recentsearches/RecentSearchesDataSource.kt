package data.recentsearches

import models.search.settings.SearchScript

class RecentSearchesDataSource(private val dao: RecentSearchesDao) {

    suspend fun getRecentSearches(term: String, script: SearchScript) = with(dao) {
        if (term.isBlank()) {
            if (script == SearchScript.AUTO) {
                getRecentSearches()
            } else getRecentSearches(script)
        } else if (script == SearchScript.AUTO) {
            getRecentSearches(term)
        } else getRecentSearches(term, script)
    }

    suspend fun cacheSearch(term: String, script: SearchScript) = dao.cacheSearch(term, script)
}