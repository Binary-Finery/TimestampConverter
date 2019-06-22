package com.spencerstudios.keepbitalive.utilities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.spencerstudios.keepbitalive.R;

import static android.content.Context.CLIPBOARD_SERVICE;

public class TextUtils {

    private Context context;
    private String text;

    public TextUtils(Context context, String text){
        this.context = context;
        this.text = text;
    }

    public void copy(){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, context.getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    public void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        Intent i = new Intent(Intent.createChooser(sharingIntent, context.getString(R.string.share_intent_title)));
        i.addFlags((Intent.FLAG_ACTIVITY_NEW_TASK));
        context.startActivity(i);
    }


}
