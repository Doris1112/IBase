package com.doris.ibase;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;

/**
 * Created by Doris on 2018/10/27.
 */
public class PoetryAdapter extends IBaseRecyclerAdapter<Poetry.ResultBean> {

    @Override
    public int getContentItemViewType(int position) {
        return R.layout.item_poetry;
    }

    @Override
    public IBaseViewHolder<Poetry.ResultBean> createContentViewHolder(View root) {
        return new Holder(root);
    }

    private class Holder extends IBaseViewHolder<Poetry.ResultBean> {

        private ImageView close;
        private TextView title, authors, content;

        Holder(View itemView) {
            super(itemView);
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
