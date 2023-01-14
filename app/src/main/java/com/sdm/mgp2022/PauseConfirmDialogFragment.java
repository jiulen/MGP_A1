package com.sdm.mgp2022;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PauseConfirmDialogFragment extends DialogFragment {
    public static boolean IsShown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        IsShown = true;
        //Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Confirm Pause?")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int id) {
                       //User triggered pause
                       GameSystem.Instance.SetIsPaused(true);
                       IsShown = false;
                       GamePage.Instance.StartPause();
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int id) {
                       //User canceled pause
                       IsShown = false;
                   }
               });
        //Create the AlertDialog object and return it
        return builder.create();
    }
}
