package com.solution.catterpillars.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.login.Login;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static cn.pedant.SweetAlert.SweetAlertDialog.CUSTOM_IMAGE_TYPE;
import static cn.pedant.SweetAlert.SweetAlertDialog.ERROR_TYPE;
import static cn.pedant.SweetAlert.SweetAlertDialog.SUCCESS_TYPE;
import static cn.pedant.SweetAlert.SweetAlertDialog.WARNING_TYPE;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 * About : I am a mathematician
 * Quote : Only brain can make anything possible
 * Strength : Never give up
 */
public class CustomAlertDialog {

    private Context context;
    private SweetAlertDialog alertDialog;
    boolean isScreenOpen;

    public CustomAlertDialog(Context context, boolean isScreenOpen) {
        this.context = context;
        this.isScreenOpen = isScreenOpen;
        alertDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                TextView text = (TextView) alertDialog.findViewById(R.id.content_text);
                text.setSingleLine(false);
                text.setMaxLines(10);
                text.setLines(3);

            }
        });
       /* TextView text = (TextView) alertDialog.findViewById(R.id.content_text);
        text.setGravity(Gravity.CENTER);
        //text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setSingleLine(false);
        text.setMaxLines(10);
        text.setLines(6);*/

    }

    public void Failed(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(WARNING_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_error_red_24dp);
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void Successful(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(SUCCESS_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_success);
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void SuccessfulWithFinsh(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(SUCCESS_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_success);
                alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((Activity) context).finish();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void SuccessfulWithCallBack(final String message, DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(SUCCESS_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_success);
                alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onPositiveClick();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void Warning(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(WARNING_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_error_red_24dp);
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void WarningWithCallBack(final String message,DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(WARNING_TYPE);
                alertDialog.setContentText(message);
                alertDialog.setCancelButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onNegativeClick();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void Error(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(ERROR_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_error_red_24dp);
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void ErrorUpdateAppWithCallBack(final String message, DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(ERROR_TYPE);
                alertDialog.setContentText(message);
                //  alertDialog.setCustomImage(R.drawable.ic_error_red_24dp);
                alertDialog.setConfirmButton("Update", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onPositiveClick();
                    }
                });
                alertDialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onNegativeClick();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void ErrorUpdateAppWithCallBack(int icon, final String negtiveBtn, final String positiveBtn, final String message, DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                alertDialog.setContentText(message);
                if (icon != 0) {
                    alertDialog.setCustomImage(icon);
                }
                alertDialog.setConfirmButton(positiveBtn, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onPositiveClick();
                    }
                });
                alertDialog.setCancelButton(negtiveBtn, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onNegativeClick();
                    }
                });
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void ErrorWithFinsh(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(ERROR_TYPE);
                alertDialog.setContentText(message);
                // alertDialog.setCustomImage(R.drawable.ic_error_red_24dp);
                alertDialog.setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((Activity) context).finish();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            } catch (Exception e) {

            }
        }
    }

    public void Logout(final String message) {
        if (isScreenOpen) {
            try {
                alertDialog.setContentText(message);
                alertDialog.changeAlertType(CUSTOM_IMAGE_TYPE);
                alertDialog.setCustomImage(R.drawable.ic_exit_to_app_red_24dp);

                alertDialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                alertDialog.setConfirmButton("Logout", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        AppPreferencesService appPreferencesService = new
                                AppPreferencesService(context.getApplicationContext());
                        appPreferencesService.setSession(false);
                        // appPreferencesService.setTaskCompleted(true);
                        appPreferencesService.setLocationCapture(false);
                        appPreferencesService.setTaskStatus(false);

                        Intent intent = Login.newIntent(context);
                        context.startActivity(intent);
                        sweetAlertDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }


    public void verifyPhone(final String message, DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                alertDialog.setContentText(message);
                alertDialog.changeAlertType(ERROR_TYPE);
                alertDialog.setCustomImage(R.drawable.ic_error);

                alertDialog.setCancelButton("Later", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onNegativeClick();
                    }
                });
                alertDialog.setConfirmButton("Send OTP", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                        mDialogCallBack.onPositiveClick();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void registrationDialog(String msg) {
        if (isScreenOpen) {
            try {
                alertDialog.changeAlertType(SUCCESS_TYPE);
                alertDialog.setContentText(msg);
                //  alertDialog.setCustomImage(R.drawable.ic_success);
                alertDialog.setConfirmButton("Login now", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = Login.newIntent(context);
                        context.startActivity(intent);
                        sweetAlertDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }


    public void EnableLockScreenWithCallBack(DialogCallBack mDialogCallBack) {
        if (isScreenOpen) {
            try {
                //final View deleteDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_enable_lock_screen, null);
                Dialog alertDialog = new Dialog(context);
                alertDialog.setContentView(R.layout.dialog_enable_lock_screen);
        /*alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

            }
        });*/
        /*alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
               // mDialogCallBack.onNegativeClick();
            }
        });*/
                TextView negativeText = alertDialog.findViewById(R.id.negativeMsg);
                TextView positiveText = alertDialog.findViewById(R.id.positiveMsg);
                negativeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mDialogCallBack.onNegativeClick();
                    }
                });
                positiveText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        mDialogCallBack.onPositiveClick();
                    }
                });
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alertDialog.show();
            } catch (WindowManager.BadTokenException bte) {

            }
        }
    }

    public void dissmiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public interface DialogCallBack {
        void onPositiveClick();

        void onNegativeClick();
    }


}
