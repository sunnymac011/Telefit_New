package fit.tele.com.telefit.apiBase;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.tele.com.telefit.modelBean.CreatePlanApiBean;
import fit.tele.com.telefit.modelBean.CrossFitBean;
import fit.tele.com.telefit.modelBean.ExerciseDetailsBean;
import fit.tele.com.telefit.modelBean.ExercisesBean;
import fit.tele.com.telefit.modelBean.ExercisesListBean;
import fit.tele.com.telefit.modelBean.LoginBean;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePlanBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.RoutinePlanListBean;
import fit.tele.com.telefit.modelBean.SelectedItemsBean;
import fit.tele.com.telefit.modelBean.SubExerciseBean;
import fit.tele.com.telefit.modelBean.SubOptionsBean;
import fit.tele.com.telefit.modelBean.YogaApiBean;
import fit.tele.com.telefit.modelBean.YogaExerciseDetailsBean;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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

    @POST("customer/customer_routine_plane_get")
    Observable<ModelBean<RoutinePlanListBean>> getRoutinePlansApi(); //done

    @POST("customer/customer_routine_plane_getexe")
    Observable<ModelBean<ArrayList<RoutinePlanDetailsBean>>> getRoutinePlanDetailsAPI(@Body Map<String, String> params); //done
}
