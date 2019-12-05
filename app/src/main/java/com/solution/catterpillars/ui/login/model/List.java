package com.solution.catterpillars.ui.login.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseViewModel;
import com.squareup.picasso.Picasso;

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
public class List extends BaseViewModel {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("CountryName")
    @Expose
    private String countryName;
    @SerializedName("ISOCode1")
    @Expose
    private String iSOCode1;
    @SerializedName("ISOCode2")
    @Expose
    private String iSOCode2;
    @SerializedName("Capital")
    @Expose
    private String capital;
    @SerializedName("PhonePrefix")
    @Expose
    private String phonePrefix;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;

    @BindingAdapter({"bind:ImagePath"})
    public static void setFlag(ImageView view, String ImagePath) {

       /* String tmp = "https://helpx.adobe.com/content/dam/help/en/stock/how-to" +
                "/visual-reverse-image-search/_jcr_content/main-pars/image/visual" +
                "-reverse-image-search-v2_1000x560.jpg";*/

        Picasso.get()
                .load(ImagePath)
                .placeholder(R.drawable.placeholder)
//                .resize(150,100)
                .error(R.drawable.placeholder)
                .into(view);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getISOCode1() {
        return "("+iSOCode1+")";
    }

    public void setISOCode1(String iSOCode1) {
        this.iSOCode1 = iSOCode1;
    }

    public String getISOCode2() {
        return iSOCode2;
    }

    public void setISOCode2(String iSOCode2) {
        this.iSOCode2 = iSOCode2;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getPhonePrefix() {

        if(phonePrefix.contains("+")){
            return phonePrefix;
        }else{
            return "+"+phonePrefix;
        }

    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
