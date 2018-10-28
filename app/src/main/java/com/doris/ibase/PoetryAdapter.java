package com.doris.ibase;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;

/**
 * Created by Doris on 2018/10/27.
 */
public class PoetryAdapter extends IBaseRecyclerAdapter<Poetry.ResultBean> {

    @Override
    public IBaseViewHolder<Poetry.ResultBean> createViewHolder(ViewGroup parent, int viewType, Poetry.ResultBean resultBean) {
        return new Holder(parent);
    }

    private class Holder extends IBaseViewHolder<Poetry.ResultBean> {

        private ImageView close;
        private TextView title, authors, content;

        Holder(ViewGroup parent) {
            super(parent, R.layout.item_poetry);
            close = $(R.id.close);
            title = $(R.id.title);
            authors = $(R.id.authors);
            content = $(R.id.content);
        }

        @Override
        public void onBind(final Poetry.ResultBean poetry, int position) {
            title.setText(poetry.getTitle());
            authors.setText(poetry.getAuthors());
            String contentValue = poetry.getContent().replace("|", "\n");
            content.setText(contentValue);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(poetry);
                }
            });
        }
    }
}
