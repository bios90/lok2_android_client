package com.dimfcompany.akcslclient.logic

import android.content.Context
import com.dimfcompany.akcslclient.base.AppClass
import com.dimfcompany.akcslclient.base.Constants
import com.dimfcompany.akcslclient.base.extensions.Optional
import com.dimfcompany.akcslclient.base.extensions.asOptional
import com.dimfcompany.akcslclient.base.extensions.toJsonMy
import com.dimfcompany.akcslclient.logic.models.ModelUser
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences

object SharedPrefsManager
{
    private val prefs = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
    private val rx_prefs = RxSharedPreferences.create(prefs)

    val pref_fb_token = rx_prefs.getObject("fb_token", Optional<String>(null), GlobalPrefsAdapter(String::class.java))
    val pref_current_user = rx_prefs.getObject("current_user", Optional<ModelUser>(null), GlobalPrefsAdapter(ModelUser::class.java))
    val pref_favorite_docs = rx_prefs.getObject("favorite_documents", Optional<ModelUserFavorites>(null), GlobalPrefsAdapter(ModelUserFavorites::class.java))

    fun hasInFavorites(id: Long): Boolean
    {
        val saved_favorites = pref_favorite_docs.get().value?.favorites_ids ?: return false
        return saved_favorites.contains(id)
    }

    fun saveIdToFavorite(id: Long)
    {
        val set = pref_favorite_docs.get().value?.favorites_ids ?: hashSetOf()
        set.add(id)
        pref_favorite_docs.asConsumer().accept(ModelUserFavorites(set).asOptional())
    }

    fun removeFromFavorite(id: Long)
    {
        val set = pref_favorite_docs.get().value?.favorites_ids ?: hashSetOf()
        if (set.remove(id))
        {
            pref_favorite_docs.asConsumer().accept(ModelUserFavorites(set).asOptional())
        }
    }
}

class ModelUserFavorites(val favorites_ids: HashSet<Long> = hashSetOf())

class GlobalPrefsAdapter<T>(private val clazz: Class<T>) : Preference.Converter<Optional<T>>
{
    override fun deserialize(serialized: String): Optional<T>
    {
        return AppClass.gson.fromJson(serialized, clazz).asOptional()
    }

    override fun serialize(value: Optional<T>): String
    {
        if (value.value == null)
        {
            return "null"
        }
        return value.value.toJsonMy()!!
    }
}