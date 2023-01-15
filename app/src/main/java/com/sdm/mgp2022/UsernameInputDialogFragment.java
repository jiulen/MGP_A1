package com.sdm.mgp2022;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class UsernameInputDialogFragment  extends DialogFragment {
    public static boolean IsShown = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        IsShown = true;
        //Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText usernameInput = new EditText(getActivity());
        usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        usernameInput.setHint("Username");
        builder.setView(usernameInput);
        builder.setMessage("Enter your username to save your score (Username must have 1 - 10 characters)")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (usernameInput.getText().toString().length() <= 0 || usernameInput.getText().toString().length() > 10)
                        {
                            // Dont accept empty username - open another dialog
                            UsernameInputDialogFragment newUsernameInput = new UsernameInputDialogFragment();
                            newUsernameInput.show(WinLoseScreen.Instance.getSupportFragmentManager(), "WinLose Username Input");
                        }
                        IsShown = false;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        IsShown = false;
                    }
                });
        //Create the AlertDialog object and return it
        return builder.create();
    }
}
