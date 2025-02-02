package com.jnb.graph.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.jnb.graph.R;

public class MBox extends DialogFragment {
    public static final int ERROR = 0;
    public static final int SUCCESS = 1;
    public static final int QUESTION = 2;
    public static final int WARNING = 3;

    public static final int OK = 0;
    public static final int OK_CANCEL = 1;
    public static final int YES_NO = 2;
    public static final int RETRY_CANCEL = 3;
    public static final int RETRY_SETTINGS = 4;
    public static final int SETTINGS_CANCEL = 5;

    private final String title, message;
    private final int icon;
    private int button = 0;
    private CallBack callBack;

    private ImageView ivImage;
    private TextView lblTitle, lblMessage;
    private Button btnNegative, btnPositive;
    private View vwDivider;

    /**
     *
     * @param title Message title
     * @param message Message
     * @param icon Top icon of message
     */
    private MBox(@NonNull String title, @NonNull String message, int icon) {
        this.title = title;
        this.message = message;
        this.icon = icon;
    }

    /**
     *
     * @param title Message title
     * @param message Message
     * @param icon Top icon of message
     * @param callBack Callback abstract class
     */
    private MBox(@NonNull String title, @NonNull String message, int icon, CallBack callBack) {
        this.title = title;
        this.message = message;
        this.icon = icon;
        this.callBack = callBack;
    }

    /**
     *
     * @param title Message title
     * @param message Message
     * @param icon Top icon of message
     * @param button Message buttons
     * @param callBack Callback abstract class
     */
    private MBox(@NonNull String title, @NonNull String message, int icon, int button, CallBack callBack) {
        this.title = title;
        this.message = message;
        this.icon = icon;
        this.button = button;
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_box, container, false);
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivImage = view.findViewById(R.id.image);
        lblTitle = view.findViewById(R.id.title);
        lblMessage = view.findViewById(R.id.message);
        btnNegative = view.findViewById(R.id.cancel);
        btnPositive = view.findViewById(R.id.ok);
        vwDivider = view.findViewById(R.id.divider);

        setData();

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        lblTitle.setText(title);
        lblMessage.setText(message);

        switch (icon) {
            case ERROR:
                ivImage.setImageResource(R.drawable.error);
                break;
            case SUCCESS:
                ivImage.setImageResource(R.drawable.success);
                break;
            case QUESTION:
                ivImage.setImageResource(R.drawable.question);
                break;
            case WARNING:
                ivImage.setImageResource(R.drawable.warning);
                break;
            default:
                break;
        }

        switch (button) {
            case OK_CANCEL:
                btnNegative.setText("CANCEL");
                btnPositive.setText("OK");
                break;
            case YES_NO:
                btnNegative.setText("NO");
                btnPositive.setText("YES");
                break;
            case RETRY_CANCEL:
                btnNegative.setText("CANCEL");
                btnPositive.setText("RETRY");
                break;
            case RETRY_SETTINGS:
                btnNegative.setText("SETTINGS");
                btnPositive.setText("RETRY");
                break;
            case SETTINGS_CANCEL:
                btnNegative.setText("CANCEL");
                btnPositive.setText("SETTINGS");
                break;
            default:
                btnNegative.setVisibility(View.GONE);
                vwDivider.setVisibility(View.GONE);
                btnPositive.setText("OK");
                break;
        }

        if (callBack == null) {
            btnNegative.setOnClickListener(view -> dismiss());
            btnPositive.setOnClickListener(view -> dismiss());
        }
        else {
            btnNegative.setOnClickListener(view -> {
                callBack.onNegative();
                dismiss();
            });
            btnPositive.setOnClickListener(view -> {
                callBack.onPositive();
                dismiss();
            });
        }
    }

    public static void showDialog(@NonNull FragmentActivity fragmentActivity, @NonNull String title, @NonNull String message, int icon) {
        DialogFragment dialog = new MBox(title, message, icon);
        dialog.setCancelable(false);
        dialog.show(fragmentActivity.getSupportFragmentManager(), "MessageBox");
    }

    public static void showDialog(@NonNull FragmentActivity fragmentActivity, @NonNull String title, @NonNull String message, int icon, CallBack callBack) {
        DialogFragment dialog = new MBox(title, message, icon, callBack);
        dialog.setCancelable(false);
        dialog.show(fragmentActivity.getSupportFragmentManager(), "MessageBox");
    }

    public static void showDialog(@NonNull FragmentActivity fragmentActivity, @NonNull String title, @NonNull String message, int icon, int button, CallBack callBack) {
        DialogFragment dialog = new MBox(title, message, icon, button, callBack);
        dialog.setCancelable(false);
        dialog.show(fragmentActivity.getSupportFragmentManager(), "MessageBox");
    }

    public static class CallBack {
        public void onPositive() { }
        public void onNegative() { }
    }
}
