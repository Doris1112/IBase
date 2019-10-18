package com.doris.ibase;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doris.ibase.adapter.IBaseRecyclerAdapter;
import com.doris.ibase.adapter.IBaseViewHolder;

/**
 * @author Doris
 * @date 018/10/27
 */
public class PoetryAdapter extends IBaseRecyclerAdapter<Poetry> {

    PoetryAdapter(Context context) {
        super(context);
    }

    @Override
    public int getContentItemViewType(int position) {
        return R.layout.item_poetry;
    }

    @Override
    public IBaseViewHolder<Poetry> createContentViewHolder(View root) {
        return new Holder(root);
    }

    private class Holder extends IBaseViewHolder<Poetry> {

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
        public void onBind(final Poetry poetry, int position) {
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
