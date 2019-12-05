package com.solution.catterpillars.ui.home.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.solution.catterpillars.R;
import com.solution.catterpillars.data.local.prefs.AppPreferencesService;
import com.solution.catterpillars.ui.home.dashboard.model.DataList;
import com.solution.catterpillars.util.CustomAlertDialog;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 01-Nov-18
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
public class ShareRefralDialog extends DialogFragment {
    private Context context;
    public AppPreferencesService preferencesService;
    private TextView linkViewText, codeText;


    public static final String TAG = ShareRefralDialog.class.getSimpleName();
    String whatsAppPackage = "com.whatsapp";
    String facebookPackage = "com.facebook.katana";
    String gmailPackage = "com.google.android.gm";
    String twitterPackage = "com.twitter.android";
    String googlePlusPackage = "com.google.android.gms.plus";
    //DataList mAffiliateLink;
    int type;

    public ShareRefralDialog() {
        super();
    }

    //Type
    // 0 -> Signup Task
    //1-> Daily Task
    @SuppressLint("ValidFragment")
    public ShareRefralDialog(Context context, int type) {
        this();
        this.context = context;
        this.type = type;
       // this.mAffiliateLink = mAffiliateLink;
        preferencesService = new AppPreferencesService(context);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);*/
    }


    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.dialog_refer_earn, container, false);

     /*   View backBtn = v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/

        TextView title = v.findViewById(R.id.title);
        TextView description = v.findViewById(R.id.description);
        TextView subDescription = v.findViewById(R.id.subDescription);
        TextView visit = v.findViewById(R.id.visit);
        LinearLayout visitView = v.findViewById(R.id.visitView);
        ImageView whatsappImage = v.findViewById(R.id.whatsapp);
        ImageView facebookImage = v.findViewById(R.id.facebook);
        ImageView gmailImage = v.findViewById(R.id.gmail);
        ImageView twitterImage = v.findViewById(R.id.twitter);
        ImageView goolePlusImage = v.findViewById(R.id.google_plus);
        codeText = v.findViewById(R.id.code);
        CheckBox checkBoxChangeLink = v.findViewById(R.id.chaeckBox);
        linkViewText = v.findViewById(R.id.linkView);
        TextView copyLinkView = v.findViewById(R.id.copyLink);
        TextView shareNow = v.findViewById(R.id.shareNow);
        //if (mAffiliateLink != null) {
            linkViewText.setText(preferencesService.getAffiletdLink2());

            try {
                String code = preferencesService.getAffiletdLink2();
                code = code.substring(code.lastIndexOf("/") + 1);
                codeText.setText(code);
            } catch (StringIndexOutOfBoundsException ignore) {
            }

            if (type == 1) {
                title.setText(context.getString(R.string.daily_task_compleated));
                description.setText(context.getString(R.string.daily_task_msg));
                subDescription.setVisibility(View.VISIBLE);
                visitView.setVisibility(View.GONE);
            } else if (type == 0) {
                title.setText(context.getString(R.string.signup_task_completed));
                description.setText(context.getString(R.string.signup_task_msg));
                subDescription.setVisibility(View.GONE);
                visitView.setVisibility(View.VISIBLE);
                visit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                context.getString(R.string.billadda_link))));
                    }
                });
            }
        //}

        checkBoxChangeLink.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    linkViewText.setText(preferencesService.getAffiletdLink1());
                } else {
                    linkViewText.setText(preferencesService.getAffiletdLink2());
                }
            }
        });


        whatsappImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(whatsAppPackage);
            }
        });

        facebookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(facebookPackage);
            }
        });

        gmailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(gmailPackage);
            }
        });

        twitterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(twitterPackage);
            }
        });

        goolePlusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(googlePlusPackage);
            }
        });

        copyLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(linkViewText.getText().toString());
            }
        });


        shareNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareContent(null);
            }
        });

        getInstalledApp(whatsAppPackage, whatsappImage);
        getInstalledApp(facebookPackage, facebookImage);
        getInstalledApp(gmailPackage, gmailImage);
        getInstalledApp(twitterPackage, twitterImage);
        getInstalledApp(googlePlusPackage, goolePlusImage);

        return v;
    }




    private void shareContent(String packageName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String extraText = "<h>Hey your friend</h> <b>" + preferencesService.getUName() +
                "</b> <h>Inviting you to join</h> <b>" + context.getResources().getString(R.string.app_name) +
                "</b> <h>click on given link " + linkViewText.getText().toString() +
                " and create account with using </h><b>" +
                linkViewText.getText().toString().replace("https://catterpillars.in/home/useraddreferal/", "") +
                "</b> <h>as a sponserid</h>";
        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(extraText).toString());
        sendIntent.setType("text/plain");
        String subText = context.getResources().getString(R.string.app_name) + " Refral link";
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subText);
        if (packageName != null) {
            sendIntent.setPackage(packageName);
        }
        startActivity(Intent.createChooser(sendIntent, "Share via"));
    }


    private void getInstalledApp(String packageName, ImageView appIcon) {
        try {

            Drawable icon = getContext().getPackageManager().getApplicationIcon(packageName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException ne) {
            appIcon.setVisibility(View.GONE);
        }
    }


    private void setClipboard(String text) {

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Share Link", text);
                clipboard.setPrimaryClip(clip);
            }

            Toast.makeText(context, "Text Copied to clipboard", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }

}
