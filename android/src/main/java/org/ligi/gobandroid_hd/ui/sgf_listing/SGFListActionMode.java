package org.ligi.gobandroid_hd.ui.sgf_listing;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.ligi.axt.AXT;
import org.ligi.axt.helpers.dialog.DialogDiscardingOnClickListener;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.ui.Refreshable;
import org.ligi.gobandroid_hd.ui.share.ShareAsAttachmentDialog;

import java.io.File;

public class SGFListActionMode implements ActionMode.Callback {

    final Context context;
    final String fileName;
    final Refreshable refreshable;
    final int menuResource;

    public SGFListActionMode(Context context, String fileName, Refreshable refreshable, int menuResource) {
        this.context = context;
        this.fileName = fileName;
        this.refreshable = refreshable;
        this.menuResource = menuResource;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(menuResource, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        mode.finish();
        switch (item.getItemId()) {
            case R.id.menu_share:
                new ShareAsAttachmentDialog(context, fileName).show();
                return true;

            case R.id.menu_delete:
                new AlertDialog.Builder(context).setMessage("Really delete " + fileName).setTitle("Delete?")
                        .setNegativeButton("NO", new DialogDiscardingOnClickListener())
                        .setPositiveButton("YES", getFileOrDirRemovingOnClickListener())
                        .show();
                return true;

            default:
                return false;
        }
    }

    private DialogInterface.OnClickListener getFileOrDirRemovingOnClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file = new File(fileName);
                AXT.at(file).deleteRecursive();
                refreshable.refresh();
            }
        };
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

}
