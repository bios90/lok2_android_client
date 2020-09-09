package com.dimfcompany.akcsl.base

enum class LoadBehavior
{
    FULL_RELOAD,
    UPDATE,
    SCROLL_TO_ID,
    NONE;

    open var id_to_scroll: Int? = null

    companion object
    {
        fun getScrollToId(id: Int): LoadBehavior
        {
            val behavior = SCROLL_TO_ID
            behavior.id_to_scroll = id
            return behavior
        }
    }
}

class FeedDisplayInfo<T>
    (
        val items: List<T> = arrayListOf(),
        var load_behavior: LoadBehavior = LoadBehavior.NONE
)
{
    companion object
    {
        fun <T> getDefault(): FeedDisplayInfo<T>
        {
            return FeedDisplayInfo(arrayListOf(), LoadBehavior.FULL_RELOAD)
        }
    }
}