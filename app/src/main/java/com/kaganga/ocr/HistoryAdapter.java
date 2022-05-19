package com.kaganga.ocr;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    private Context mContext;
    private List<History> historyList;
    private DatabaseHandler databaseHandler;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail, overflow;
        private DatabaseHandler databaseHandler;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public HistoryAdapter(Context mContext, List<History> historyList, DatabaseHandler databaseHandler) {
        this.mContext = mContext;
        this.historyList = historyList;
        this.databaseHandler = databaseHandler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_riwayat, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        final History history = historyList.get(i);
        myViewHolder.title.setText(history.getDate());
        Glide.with(mContext).load(history.getImageuri()).into(myViewHolder.thumbnail);
        myViewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(myViewHolder.overflow, history.getId());
            }
        });
    }

    private void showPopupMenu(View view, int id) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_history, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int id;

        public MyMenuItemClickListener(int id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete_history:
                    if (databaseHandler.deleteHistory(id)){
                        Toast.makeText(mContext, R.string.deleted, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
