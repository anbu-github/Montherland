package com.dev.montherland.listeners;

/**
 * Interface to notify when the view holder item's selection event
 *
 * Decouple MyAdapter.ViewHolder from MyItemTouchCallback in case that
 * there are other RecyclerView.Adapter.ViewHolder(s) using MyItemTouchCallback as
 * their item touch listener.
 */
public interface OnItemSelectedListener {
    void onItemSelected();
    void onItemClear();
}
