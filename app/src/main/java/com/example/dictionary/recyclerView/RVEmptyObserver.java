package com.example.dictionary.recyclerView;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * From
 * https://gist.github.com/sheharyarn/5602930ad84fa64c30a29ab18eb69c6e
 */

public class RVEmptyObserver extends RecyclerView.AdapterDataObserver {
    private View emptyView;
    private RecyclerView recyclerView;

    public RVEmptyObserver(RecyclerView rv, View ev) {
        this.recyclerView = rv;
        this.emptyView    = ev;
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (emptyView != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onChanged() {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        checkIfEmpty();
    }
}