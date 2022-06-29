package com.goldze.mvvmhabit.aioui.http

import com.goldze.mvvmhabit.aioui.bean.CommentRequestBean
import com.goldze.mvvmhabit.aioui.bean.TypeResponseBean
import com.goldze.mvvmhabit.aioui.bean.list.*
import com.goldze.mvvmhabit.aioui.clazz.bean.ClazzListResponseBean
import com.goldze.mvvmhabit.aioui.kepu.KepuBean
import com.goldze.mvvmhabit.aioui.kepu.content.KepuItemBean
import com.goldze.mvvmhabit.aioui.knows.KnowsBean
import com.goldze.mvvmhabit.aioui.main.bean.*
import com.goldze.mvvmhabit.aioui.test.bean.AnserReponseData
import com.goldze.mvvmhabit.aioui.test.bean.AnserRequestData
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsRequestBean
import com.goldze.mvvmhabit.aioui.test.bean.ScaDetailsResponseBean
import com.goldze.mvvmhabit.aioui.zixun.input.InputRequestBean
import com.goldze.mvvmhabit.aioui.zixun.phone.PhoneListBean
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Android Studio.
 * User: fengao
 * Date: 2022/6/19
 * Time: 4:59 下午
 */
interface Api {
    @POST("/client/scaRec/notLogin/create")
    fun getScaDetails(@Body bean: ScaDetailsRequestBean): Observable<ScaDetailsResponseBean>


    @POST("/client/scaRec/notLogin/calc")
    fun commit(@Body bean: AnserRequestData): Observable<AnserReponseData>

    @POST("/client/api/announcement/getPageList")
    fun getAnnounList(@Body bean: GetAnnounListRequestBean): Observable<GetAnnounListReponseBean>

    @GET("/client/moduleType/getList")
    fun queryType(@Query("moduleCode") moduleCode: String): Observable<TypeResponseBean>

    @POST("/client/api/banner/getList")
    fun loadBanner(@Body bean: CommentRequestBean): Observable<BannerBean>

    /**
     * 获取课程列表
     */
    @POST("/client/api/course/getPageList")
    fun getClazzList(@Body bean: CommentRequestBean): Observable<ClazzListResponseBean>

    @POST("/client/api/music/getPageList")
    fun getMusicPageList(@Body bean: CommentRequestBean): Observable<CommonListResponseBean<MusicRecord>>

    @POST("/client/api/cartoon/getPageList")
    fun getCartoonPageList(@Body bean: CommentRequestBean): Observable<CommonListResponseBean<CartoonRecord>>

    @POST("/client/api/meditation/getPageList")
    fun getMeditationPageList(@Body bean: CommentRequestBean): Observable<CommonListResponseBean<MeditationRecord>>

    @POST("/client/api/film/getPageList")
    fun getFilmPageList(@Body bean: CommentRequestBean): Observable<CommonListResponseBean<FilmRecord>>

    @POST("/client/api/equipment/activation")
    fun activation(@Body bean: CommentRequestBean): Observable<ActivationResponseBean>

    @POST("/client/api/info/getPageList")
    fun getKnowsList(@Body bean: CommentRequestBean): Observable<KnowsBean>

    @POST("/client/api/theme/getPageList")
    fun getKePuList(@Body bean: CommentRequestBean): Observable<KepuBean>

    @POST("/client/api/equipment/detail")
    fun getEquipmentDetail(@Body bean: CommentRequestBean): Observable<ShebeiXQBean>

    @POST("/client/api/themeItem/getPageList")
    fun getThemeItemList(@Body bean: CommentRequestBean): Observable<KepuItemBean>

    @POST("/client/api/hotline/getPageList")
    fun getPhoneList(@Body bean: CommentRequestBean): Observable<PhoneListBean>


    @POST("/client/api/appointment/create")
    fun commitInput(@Body bean: InputRequestBean): Observable<PhoneListBean>

    @POST("/client/api/sca/getPageList")
    fun getTestList(@Body bean: CommentRequestBean): Observable<CommonListResponseBean<TestRecord>>

}