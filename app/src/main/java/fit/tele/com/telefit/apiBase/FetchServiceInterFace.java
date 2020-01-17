package fit.tele.com.telefit.apiBase;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.tele.com.telefit.modelBean.CaloriesBarBean;
import fit.tele.com.telefit.modelBean.CategoryBean;
import fit.tele.com.telefit.modelBean.ConferenceBean;
import fit.tele.com.telefit.modelBean.CountryBean;
import fit.tele.com.telefit.modelBean.CreatePlanApiBean;
import fit.tele.com.telefit.modelBean.CreatePostBean;
import fit.tele.com.telefit.modelBean.CreatedRecipeListBean;
import fit.tele.com.telefit.modelBean.CrossFitBean;
import fit.tele.com.telefit.modelBean.CustomFoodBean;
import fit.tele.com.telefit.modelBean.CustomerDetailBean;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.ExercisesBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.FoodCategoryBean;
import fit.tele.com.telefit.modelBean.GoalBarBean;
import fit.tele.com.telefit.modelBean.GoalBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.MealCategoryBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.NewRecipeBean;
import fit.tele.com.telefit.modelBean.NutritionBarBean;
import fit.tele.com.telefit.modelBean.PaymentInfoBean;
import fit.tele.com.telefit.modelBean.PrivacyBean;
import fit.tele.com.telefit.modelBean.RecipeListBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.RoutinePlanListBean;
import fit.tele.com.telefit.modelBean.SelectedItemsBean;
import fit.tele.com.telefit.modelBean.SubExerciseBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.modelBean.TrainerBean;
import fit.tele.com.telefit.modelBean.UpdatePlanApiBean;
import fit.tele.com.telefit.modelBean.YogaApiBean;
import fit.tele.com.telefit.modelBean.YogaExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.chat.UserModel;
import fit.tele.com.telefit.modelBean.chompBeans.ChompProductBean;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface FetchServiceInterFace {

    @POST("registration")
    Observable<ModelBean<LoginBean>> signUpApi(@Body Map<String, String> params); //done

    @POST("login")
    Observable<ModelBean<LoginBean>> manualLogin(@Body Map<String, String> params); //done

    @POST("forgot_password")
    Observable<ModelBean<LoginBean>> forgotAPI(@Body Map<String, String> params); //done

    @POST("save_new_password")
    Observable<ModelBean<LoginBean>> setNewPasswordAPI(@Body Map<String, String> params); //done

    @Multipart
    @POST("customer/user_update_profile")
    Observable<ModelBean<LoginBean>> editProfileCustomer(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file); //done

    @POST("customer/user_exe_yoga")
    Observable<ModelBean<ArrayList<ExercisesBean>>> getExerciseTestAPI(@Body Map<String, String> params); //done

    @GET("product-search.php")
    rx.Observable<ResponseBody> getChomp(@Query("token") String q, @Query("name") String appid);

    @GET("request.php")
    rx.Observable<ResponseBody> getChompCountry(@Query("token") String q, @Query("keyword") String foodName, @Query("country") String strCountry);

    @POST("customer/user_get_all_subcategory")
    Observable<ModelBean<ArrayList<SubExerciseBean>>> getSubExerciseAPI(@Body Map<String, String> params); //done

    @POST("customer/user_get_all_option")
    Observable<ModelBean<ArrayList<SubOptionsBean>>> getExerciseAPI(@Body Map<String, String> params); //done

    @POST("customer/gym_get_all_subcat_opt")
    Observable<ModelBean<ArrayList<SubExerciseBean>>> getGymExerciseAPI(@Body Map<String, String> params); //done

    @POST("customer/gym_all_subcat_opt_exe")
    Observable<ModelBean<ArrayList<ExercisesListBean>>> getFilteredExercises(@Body SelectedItemsBean request); //done

    @POST("customer/gym_get_exe")
    Observable<ModelBean<ArrayList<ExerciseDetailsBean>>> getExerciseDetailsAPI(@Body Map<String, String> params); //done

    @POST("customer/yoga_all_subcat_opt_exe")
    Observable<ModelBean<ArrayList<ExercisesListBean>>> getYogaFilteredExercises(@Body YogaApiBean request); //done

    @POST("customer/yoga_get_exe")
    Observable<ModelBean<ArrayList<YogaExerciseDetailsBean>>> getYogaExerciseDetailsAPI(@Body Map<String, String> params); //done

    @POST("customer/crossfit_get_all_subcat_opt")
    Observable<ModelBean<ArrayList<CrossFitBean>>> getCrossFitAPI(@Body Map<String, String> params); //done

    @POST("customer/crossfit_all_subcat_opt_exe")
    Observable<ModelBean<ArrayList<ExercisesListBean>>> getCrossFitFilteredExercises(@Body YogaApiBean request); //done

    @POST("customer/crossfit_all_subcat_opt_exe")
    Observable<ModelBean<ArrayList<ExercisesListBean>>> getCrossFitFilteredExercises(@Body SelectedItemsBean request); //done

    @POST("customer/crossfit_get_exe")
    Observable<ModelBean<ArrayList<YogaExerciseDetailsBean>>> getCrossFitExerciseDetailsAPI(@Body Map<String, String> params); //done

    @POST("customer/customer_exe_plane")
    Observable<ModelBean<ExercisesListBean>> createRoutineApi(@Body CreatePlanApiBean request); //done

    @POST("customer/customer_exe_plane_update")
    Observable<ModelBean<ExercisesListBean>> updateRoutineApi(@Body UpdatePlanApiBean request); //done

    @POST("customer/customer_routine_plane_get")
    Observable<ModelBean<ArrayList<RoutinePlanBean>>> getRoutinePlansApi(); //done

    @POST("customer/customer_routine_plane_getexe")
    Observable<ModelBean<ArrayList<RoutinePlanDetailsBean>>> getRoutinePlanDetailsAPI(@Body Map<String, String> params); //done

    @POST("customer/ChargePayment")
    Observable<ModelBean<ArrayList<ExerciseDetailsBean>>> getPaymentAPI(@Body Map<String, String> params); //done

    @POST("customer/cust_check_package")
    Observable<ModelBean<PaymentInfoBean>> getCheckPaymentAPI(); //done

    @POST("customer/user_get_all_category")
    Observable<ModelBean<ArrayList<CategoryBean>>> getCategoryApi(); //done

    @POST("customer/get_all_customer_list")
    Observable<ModelBean<ArrayList<CustomerDetailBean>>> getAllCustomers(@Body Map<String, String> params); //done

    @POST("customer/get_friend_list")
    Observable<ModelBean<ArrayList<CustomerDetailBean>>> getAllFriends(@Body Map<String, String> params); //done


    @POST("customer/send_request_friend")
    Observable<ModelBean<ArrayList<CustomerDetailBean>>> addFriend(@Body Map<String, String> params); //done

    @POST("customer/get_all_friend_request_list")
    Observable<ModelBean<ArrayList<CustomerDetailBean>>> getAllRequests(@Body Map<String, String> params); //done

    @POST("customer/accept_request_friend")
    Observable<ModelBean<ArrayList<CustomerDetailBean>>> acceptRequest(@Body Map<String, String> params); //done

    @Multipart
    @POST("customer/create-post")
    Observable<ModelBean<LoginBean>> createPost(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file); //done

    @POST("customer/post-list")
    Observable<ModelBean<ArrayList<CreatePostBean>>> getAllActivities(@Body Map<String, String> params); //done

    @POST("customer/cust_add_recipe")
    Observable<ModelBean<NewRecipeBean>> createRecipeApi(@Body NewRecipeBean request); //done

    @POST("customer/cust_add_meals")
    Observable<ModelBean<NewRecipeBean>> createMealApi(@Body NewRecipeBean request); //done

    @POST("customer/send-notification")
    Observable<ModelBean<UserModel>> sendNotification(@Body Map<String, String> params); //done

    @POST("customer/customer_exe_plane_delete")
    Observable<ModelBean<ArrayList<RoutinePlanBean>>> deleteRoutine(@Body Map<String, String> params); //done

    @POST("customer/social-settings")
    Observable<ModelBean<LoginBean>> getSocialSetting(@Body Map<String, String> params); //done

    @POST("customer/trainer_list")
    Observable<ModelBean<ArrayList<TrainerBean>>> getTrainerListApi(); //done

    @POST("customer/cust_get_all_meals")
    Observable<ModelBean<ArrayList<FoodCategoryBean>>> getFoodPlansApi(@Body Map<String, String> params); //done

    @POST("customer/add_food_category")
    Observable<ModelBean<ArrayList<FoodCategoryBean>>> setSnackAPI(@Body Map<String, String> params); //done

    @POST("customer/cust_get_recipe")
    Observable<ModelBean<ArrayList<RecipeListBean>>> getRecipeApi(@Body Map<String, String> params); //done

    @POST("customer/cust_get_recipe_details")
    Observable<ModelBean<ChompProductBean>> getRecipeDetailsApi(@Body Map<String, String> params); //done

    @POST("customer/cust_edit_recipe")
    Observable<ModelBean<NewRecipeBean>> editRecipeApi(@Body NewRecipeBean request); //done

    @POST("customer/cust_get_my_food")
    Observable<ModelBean<ArrayList<ChompProductBean>>> getMyFoodApi(@Body Map<String, String> params); //done

    @POST("customer/delete_post")
    Observable<ModelBean<ArrayList<CreatePostBean>>> deletePost(@Body Map<String, String> params); //done

    @POST("customer/cust_food_nutrition")
    Observable<ModelBean<ArrayList<String>>> getNutritionApi(); //done

    @POST("customer/cust_add_food")
    Observable<ModelBean<CustomFoodBean>> addCustomFood(@Body CustomFoodBean request); //done

    @POST("customer/cust_get_food")
    Observable<ModelBean<ArrayList<ChompProductBean>>> getCustomFoodApi(@Body Map<String, String> params); //done

    @POST("customer/cust_delete_food")
    Observable<ModelBean<ArrayList<ChompProductBean>>> deleteFood(@Body Map<String, String> params); //done

    @POST("customer/cust_delete_recipe")
    Observable<ModelBean<ArrayList<RecipeListBean>>> deleteRecipe(@Body Map<String, String> params); //done

    @POST("customer/cust_get_meals_food_category")
    Observable<ModelBean<ArrayList<MealCategoryBean>>> getMealCatApi(@Body Map<String, String> params); //done

    @POST("customer/privacy_policy")
    Observable<ModelBean<PrivacyBean>> getPrivacyApi(); //done

    @POST("customer/term_condition")
    Observable<ModelBean<PrivacyBean>> getTermsApi(); //done

    @POST("customer/cust_help_add")
    Observable<ModelBean<LoginBean>> setHelp(@Body Map<String, String> params); //done

    @POST("customer/cust_goal_add")
    Observable<ModelBean<GoalBean>> setGoals(@Body Map<String, String> params); //done

    @POST("customer/cust_goal_edit")
    Observable<ModelBean<GoalBean>> getGoals(@Body Map<String, String> params); //done

    @POST("customer/cust_set_notification")
    Observable<ModelBean<LoginBean>> setNotificationSettings(@Body Map<String, String> params); //done

    @GET("product-code.php")
    rx.Observable<ResponseBody> getBarCodeChomp(@Query("token") String q, @Query("code") String appid);

    @POST("customer/set-schedule-video-conf")
    Observable<ModelBean<LoginBean>> setConference(@Body Map<String, String> params); //done

    @POST("customer/get-schedule-conf-list")
    Observable<ModelBean<ArrayList<ConferenceBean>>> getConferenceApi(); //done

    @POST("get_country")
    Observable<ModelBean<ArrayList<CountryBean>>> getCountryApi(); //done

    @POST("customer/cust_get_meals")
    Observable<ModelBean<ArrayList<FoodCategoryBean>>> getMealByDateApi(@Body Map<String, String> params); //done

    @POST("customer/add_food_category")
    Observable<ModelBean<ArrayList<FoodCategoryBean>>> getAddCatApi(@Body Map<String, String> params); //done

    @POST("customer/delete_food_category")
    Observable<ModelBean<ArrayList<FoodCategoryBean>>> getDeleteCatApi(@Body Map<String, String> params); //done

    @POST("customer/cust_get_week_calories")
    Observable<ModelBean<CaloriesBarBean>> getCaloriesApi(@Body Map<String, String> params); //done

    @POST("customer/cust_get_meals_details")
    Observable<ModelBean<NewRecipeBean>> getMealApi(@Body Map<String, String> params); //done

    @POST("customer/cust_edit_meals")
    Observable<ModelBean<NewRecipeBean>> updateRecipeApi(@Body NewRecipeBean request); //done

    @POST("customer/cust_get_week_nutrients")
    Observable<ModelBean<NutritionBarBean>> getNutritionApi(@Body Map<String, String> params); //done

    @POST("customer/cust_set_showhide_private")
    Observable<ModelBean<LoginBean>> setPrivacy(@Body Map<String, String> params); //done

    @POST("customer/cust_goal_get")
    Observable<ModelBean<GoalBarBean>> getGoalApi(@Body Map<String, String> params); //done

    @GET("request_token")
    rx.Observable<ResponseBody> getFitbit(@Query("oauth_consumer_key") String ock, @Query("name") String appid);

    @GET("date/{fitDate}")
    rx.Observable<ResponseBody> getFitbitCalories(@Header("Authorization")String accessToken,@Path("fitDate") String fitDate);

    @POST("customer/send_request_cust_to_trainer")
    Observable<ModelBean<TrainerBean>> sendRequest(@Body Map<String, String> params); //done

    @POST("customer/trainer_profile")
    Observable<ModelBean<TrainerBean>> getTrainerApi(); //done

    @POST("token")
    rx.Observable<ResponseBody> getRefreshToken(@Query("grant_type") String q, @Query("refresh_token") String token);

    @POST("customer/set_fitbit")
    Observable<ModelBean<GoalBarBean>> getBurnCalApi(@Body Map<String, String> params); //done


}
