package ir.bvar.imenfood.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.bvar.imenfood.R;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public class ConfirmDialog extends AppCompatDialog {

    @BindView(R.id.dialog_confirm_titleTextView)
    AppCompatTextView titleTextView;

    @BindView(R.id.dialog_confirm_negativeButton)
    AppCompatTextView negativeButton;

    @BindView(R.id.dialog_confirm_positiveButton)
    AppCompatTextView positiveButton;

    private ConfirmDialogResponseListener confirmDialogResponseListener;

    public ConfirmDialog(Context context) {
        super(context);
    }

    public ConfirmDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        init();
    }


    public void show(ConfirmDialogResponseListener confirmDialogResponseListener,String title,String positiveText,String negativeText) {
        show();
        this.confirmDialogResponseListener = confirmDialogResponseListener;
        titleTextView.setText(title);
        positiveButton.setText(positiveText);
        negativeButton.setText(negativeText);
    }

    private void init() {
        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmDialogResponseListener != null) {
                    confirmDialogResponseListener.onConfirmDialogPositiveClicked(ConfirmDialog.this);
                }

                dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmDialogResponseListener != null) {
                    confirmDialogResponseListener.onConfirmDialogNegativeClicked(ConfirmDialog.this);
                }

                dismiss();
            }
        });
    }

    public interface ConfirmDialogResponseListener {
        void onConfirmDialogPositiveClicked(ConfirmDialog confirmDialog);

        void onConfirmDialogNegativeClicked(ConfirmDialog confirmDialog);
    }
}
