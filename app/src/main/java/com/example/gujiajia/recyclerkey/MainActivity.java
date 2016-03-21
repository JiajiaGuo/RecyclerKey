package com.example.gujiajia.recyclerkey;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(new MyAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    class MyAdapter extends Adapter{

        private int focusedItem = 0;

        private RecyclerView mRecyclerView;

        @Override
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            mRecyclerView = recyclerView;
            // Handle key up and key down and attempt to move selection
            recyclerView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

                    // Return false if scrolled to the bounds and allow focus to move off the list
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            return tryMoveSelection(lm, 1);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            return tryMoveSelection(lm, -1);
                        }
                    }

                    return false;
                }
            });
        }

        private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {

            int tryFocusItem = focusedItem + direction;
            Log.d(TAG, "tryFocusItem=" + tryFocusItem);
            // If still within valid bounds, move the selection, notify to redraw, and scroll
            if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
                notifyItemChanged(focusedItem);
                focusedItem = tryFocusItem;
                lm.smoothScrollToPosition(mRecyclerView, null, focusedItem);
                notifyItemChanged(focusedItem);
//                lm.scrollToPosition(focusedItem);
                return true;
            }
            return false;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fun, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final View view = holder.itemView;
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    view.setSelected(focusedItem == position);
                }
            });

            ((MyViewHolder)holder).setPosition(position);
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        class MyViewHolder extends ViewHolder{

            private TextView mTitleTv;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
                // Handle item click and set the selection
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Redraw the old selection and the new
                        notifyItemChanged(focusedItem);
                        focusedItem = getLayoutPosition();
                        notifyItemChanged(focusedItem);
                    }
                });
            }

            public void setPosition(int position){
                mTitleTv.setText("haha" + position);
            }
        }
    }

}
