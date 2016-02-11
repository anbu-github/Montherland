package com.dev.montherland.listeners;

/**
 * Interface to notify the adapter when view holder item's touch event fire.
 *
 * Decouple the MyAdapter from MyItemTouchCallback in case that
 * there are other RecyclerView.Adapter(s) using MyItemTouchCallback as
 * their item touch listener.
 */
public interface OnItemTouchListener {
    void onItemMove(int fromIndex, int toIndex);
    void onItemRemove(int position);
}
