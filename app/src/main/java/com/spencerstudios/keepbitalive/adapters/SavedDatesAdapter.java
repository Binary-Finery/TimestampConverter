package com.spencerstudios.keepbitalive.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.spencerstudios.keepbitalive.R;
import com.spencerstudios.keepbitalive.models.Date;
import com.spencerstudios.keepbitalive.utilities.TextUtils;

import java.util.List;

import spencerstudios.com.jetdblib.JetDB;

public class SavedDatesAdapter extends BaseAdapter {

    private LayoutInflater li;
    private List<Date> dateList;

    public SavedDatesAdapter(Context context, List<Date> dateList) {
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dateList = dateList;
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        v = li.inflate(R.layout.list_view_item, null);

        TextView tvLabel = v.findViewById(R.id.tv_label);
        TextView tvDate = v.findViewById(R.id.tv_date);
        ImageView ivMenu = v.findViewById(R.id.iv_card_menu);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu pum = new PopupMenu(v.getContext(), v);
                pum.getMenu().add("share");
                pum.getMenu().add("delete");
                pum.getMenu().add("edit label");

                pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("share")) {
                            share(v.getContext(), dateList.get(position));
                        } else if (item.getTitle().equals("delete")) {
                            dateList.remove(position);
                            JetDB.putListOfObjects(v.getContext(), dateList, "dates");
                            notifyDataSetChanged();
                        } else if (item.getTitle().equals("edit label")) {
                            dispSaveDialog(v.getContext(), dateList.get(position), position);
                        }

                        return false;
                    }
                });
                pum.show();
            }
        });


        Date date = dateList.get(position);

        tvLabel.setText(date.getLabel());
        tvDate.setText(date.getDate());

        return v;
    }

    private void share(Context context, Date date) {
        new TextUtils(context, date.getLabel() + "\n" + date.getDate()).share();
    }

    private void dispSaveDialog(Context context, final Date date, final int pos) {

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        @SuppressLint("InflateParams") View v = LayoutInflater.from(context).inflate(R.layout.save_date_dialog, null);
        final EditText etLabel = v.findViewById(R.id.et_label);
        Button btnCancel = v.findViewById(R.id.btn_cancel);
        Button btnSave = v.findViewById(R.id.btn_save);
        etLabel.setText(date.getLabel());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = etLabel.getText().toString().trim();
                if (label.length() > 0) {
                    date.setLabel(label);
                    dateList.set(pos, date);
                    JetDB.putListOfObjects(v.getContext(), dateList, "dates");
                    notifyDataSetChanged();
                    dialog.dismiss();

                }
            }
        });

        dialog.setView(v);

        dialog.show();
    }
}
