package oats.mobile.sylhetidictionary.data.recentsearches

import oats.mobile.sylhetidictionary.models.search.settings.SearchScript

class RecentSearchesDataSource(private val dao: RecentSearchesDao) {

    suspend fun getRecentSearches(suggestionQuery: String?, script: SearchScript) = with(dao) {
        if (suggestionQuery == null) {
            if (script == SearchScript.AUTO) {
                getRecentSearches()
            } else getRecentSearches(script)
        } else if (script == SearchScript.AUTO) {
            getRecentSearches(suggestionQuery)
        } else getRecentSearches(suggestionQuery, script)
    }

    suspend fun cacheSearch(term: String, script: SearchScript) = dao.cacheSearch(term, script)
}
