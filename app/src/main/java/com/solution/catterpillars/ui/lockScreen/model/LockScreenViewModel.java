package com.solution.catterpillars.ui.lockScreen.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.solution.catterpillars.R;
import com.solution.catterpillars.base.BaseViewModel;
import com.solution.catterpillars.ui.lockScreen.LockScreenNavigator;
import com.solution.catterpillars.ui.lockScreen.model.NewsData;
import com.squareup.picasso.Picasso;

import java.util.List;

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
public class LockScreenViewModel extends BaseViewModel<LockScreenNavigator> {

    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("List")
    @Expose
    private List<NewsData> newsList = null;

    //------------------------------------------------
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("PostDate")
    @Expose
    private String postDate;


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return android.text.Html.fromHtml(message).toString();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NewsData> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsData> newsList) {
        this.newsList = newsList;
    }

    public String getTitle() {
        return android.text.Html.fromHtml(title).toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    @BindingAdapter({"bind:Image"})
    public static void newsImage(ImageView view, String image) {
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(view);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
