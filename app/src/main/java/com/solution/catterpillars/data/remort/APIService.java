package com.solution.catterpillars.data.remort;


import com.solution.catterpillars.ui.home.content.HomeFragmentViewModel;
import com.solution.catterpillars.ui.home.dashboard.income.model.IncomeViewModel;
import com.solution.catterpillars.ui.home.dashboard.member.model.MemberViewModel;
import com.solution.catterpillars.ui.home.dashboard.model.DashboardViewModel;
import com.solution.catterpillars.ui.home.dashboard.model.VerifyMobileModel;
import com.solution.catterpillars.ui.home.dashboard.task.model.TaskViewModel;
import com.solution.catterpillars.ui.home.dashboard.wallet.model.WalletViewModel;
import com.solution.catterpillars.ui.home.model.ResultHome;
import com.solution.catterpillars.ui.home.setting.SettingViewModel;
import com.solution.catterpillars.ui.home.setting.change_password.model.ChangePasswordViewModel;
import com.solution.catterpillars.ui.home.setting.edit_profile.model.EditProfileViewModel;
import com.solution.catterpillars.ui.location.model.LocationViewModel;
import com.solution.catterpillars.ui.lockScreen.model.LockScreenViewModel;
import com.solution.catterpillars.ui.login.model.ResultLogin;
import com.solution.catterpillars.ui.registration.model.ResultRegistration;
import com.solution.catterpillars.ui.home.task.model.ResultTask;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Arvindo Mondal on 14/04/17.
 * Email : arvindomondal@gmail.com
 * Designation : Programmer and Developer
 * Skills : Logic and Algorithm Creator and Inventor
 * Logo : Never Give Up
 */

public interface APIService {

//    @POST("/api/MobileApi/UserLogin")
//    Call<in.roundpay.catterpillars.ui.login.model.ResultLogin> login(
//            @Body Map<String, String> body
//    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UserLogin")
    Call<ResultLogin> login(
            @Field("userid") String startType,
            @Field("password") String userId,
            @Field("deviceid") String password,
            @Field("CountryCode") String countryCode
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UserSignup")
    Call<ResultRegistration> registration(
            @Field("SponsorId") String referenceCode,
            @Field("Name") String name,
            @Field("MobileNo") String mobile,
            @Field("EmailId") String email,
            @Field("Password") String password,
            @Field("ConfirmPassword") String confirmPassword,
            @Field("CountryCode") String countryCode,
            @Field("Country") String countryId
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UserLogin")
    Call<ResultLogin> forgetPassword(
            @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UserLogin")
    Call<ChangePasswordViewModel> changePassword(
            @Field("oldPass") String oldPassword,
            @Field("newPass") String newPassword

    );

//    @GET("/api/B2BSecureService.asmx/ForgotPassword?")
//    Call<ResultLogin> userMobile(@Query("_UMobile") String mobile);


    @GET("/Home/MobileLogin")
    Call<com.solution.catterpillars.ui.home.model.ResultHome>
    home(@Query("username") String userId, @Query("password") String password);

    @GET("/api/MobileApi/UserMenu")
    Call<ResultHome> homeMenu(
    );

    @GET("/api/MobileApi/GetMobileAppStatus?")
    Call<ResultTask> loadInstallationTask(
            @Query("userid") String mobile
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UpdateMobileAppStatus")
    Call<ResultTask> responseTaskComplete(
            @Field("userid") String userId,
            @Field("taskid") String taskId,
            @Field("taskname") String taskName,
            @Field("setid") String setId,
            @Field("isappalready") int isAlreadyInstalled
    );


    @GET("/Home/MobileLogin?username=831803762&password=123")
    Call<ResultHome>
    appDashboard(@Query("username") String userId, @Query("password") String password);

    @FormUrlEncoded
    @POST("/api/MobileApi/UpdateFbShareStatus")
    Call<HomeFragmentViewModel> facebookShareUpdate(
            @Field("url") String urlParams,
            @Field("mobile") String mobile

    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UpdateYoutubeSubscribeStatus")
    Call<HomeFragmentViewModel> youtubeUpdate(
            @Field("url") String urlParams,
            @Field("mobile") String mobile,
            @Field("IsAlreadySubscribe") int isAlreadySubscribe

    );

    @FormUrlEncoded
    @POST("/webservice.asmx/GetSubcategoryNews?top=5&category=12")
    Call<LockScreenViewModel> lockScreen(
            @Field("mobile") String mobile
    );



    @GET("/api/MobileApi/ForgetPassword")
    Call<ResultLogin> forgetPassword(
            @Query("mobile") String mobile, @Query("countrycode") String countrycode
    );



    @GET("/api/MobileApi/SendMobileOtp")
    Call<VerifyMobileModel> sendMobileOtp(
            @Query("mobile") String mobile, @Query("ApiIndex") String apiIndex
    );

    @GET("/api/MobileApi/VerifyMobileOtp")
    Call<VerifyMobileModel> verifyMobileOtp(
            @Query("mobile") String mobile, @Query("otp") String otp
    );

    @GET("/api/MobileApi/SendEmailOtp")
    Call<VerifyMobileModel> verifyEmail(
            @Query("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/ChangePassword")
    Call<ChangePasswordViewModel> changePassword(
            @Field("Mobile") String mobile,
            @Field("OldPassword") String oldPassword,
            @Field("NewPassword") String newPassword,
            @Field("ConfirmPassword") String confiormPassword

    );

    @FormUrlEncoded
    @POST("/api/MobileApi/GetVersionCheck")
    Call<ResultLogin> versionCheck(
            @Field("AppVersion") String appVersion
    );

    @GET("/webservice.asmx/GetSubcategoryNews")
    Call<LockScreenViewModel>
    lockScreen(@Query("top") int top,
               @Query("category") int category);

    @GET("/api/MobileApi/GetCountry")
    Call<ResultLogin>
    countryCode();

    @FormUrlEncoded
    @POST("/api/MobileApi/LoginLocation")
    Call<LocationViewModel> locationCapture(
            @Field("mobile") String mobile,
            @Field("Latitude") double latitude,
            @Field("Longitude") double longitude
    );

    @GET("/api/MobileApi/GetDashboard?")
    Call<DashboardViewModel> getDashboardData(
            @Query("mobile") String mobile
    );

    @GET("/api/MobileApi/GetDailyTaskList?")
    Call<TaskViewModel> getDailyTaskList(
            @Query("mobile") String mobile
    );

    @GET("/api/MobileApi/GetSignUpTaskList?")
    Call<TaskViewModel> getSignUpTaskList(
            @Query("mobile") String mobile
    );

    @GET("/api/MobileApi/TaksCompleted?")
    Call<TaskViewModel> setTaskComplete(
            @Query("mobile") String mobile,
            @Query("TaskId") String taskId
    );

    @GET("/api/MobileApi/TaksCompleted?")
    Call<EditProfileViewModel> setUserProfileData(
            @Query("mobile") String mobile,
            @Query("TaskId") String taskId
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/GetWalletDetails")
    Call<WalletViewModel> walletDetails(
            @Field("Mobile") String mobile,
            @Field("FDate") String startDate,
            @Field("TDate") String endDate,
            @Field("type") String walletType,
            @Field("PageIndex") String pageIndex,
            @Field("TopCount") String topCount,
            @Field("TransactionId") String transactionId
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/GetIncomeLog")
    Call<IncomeViewModel> incomeDetails(
            @Field("Mobile") String mobile,
            @Field("FDate") String startDate,
            @Field("TDate") String endDate,
            @Field("type") String walletType,
            @Field("PageIndex") String pageIndex,
            @Field("TopCount") String topCount,
            @Field("TransactionId") String transactionId
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/GetMemberDetails")
    Call<MemberViewModel> membersDetails(
            @Field("Mobile") String mobile,
            @Field("ChildMobile") String childMobile,
            @Field("FDate") String startDate,
            @Field("TDate") String endDate,
            @Field("type") String walletType,
            @Field("PageIndex") String pageIndex,
            @Field("TopCount") String topCount,
            @Field("SignupTaskStatus") String signUpTaskStatus,
            @Field("DailyTaskStatus") String dailyTaskStatus
    );

    @FormUrlEncoded
    @POST("/api/MobileApi/UpdateStatus")
    Call<SettingViewModel> updateStatus(
            @Field("Mobile") String mobile,
            @Field("Type") String type,
            @Field("Status") String status
    );


}