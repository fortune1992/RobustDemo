package com.bikan.reading.net;

import com.bikan.base.model.ModeBase;
import com.bikan.base.model.NewsModeBase;
import com.bikan.coinscenter.model.TaskResultModel;
import com.bikan.coordinator.router.coinscenter.entity.LocalTeamItemModel;
import com.bikan.reading.circle.model.AwardCoinModel;
import com.bikan.reading.model.AtlasDataModel;
import com.bikan.reading.model.AwardFeedModel;
import com.bikan.reading.model.BackUserGuideModel;
import com.bikan.reading.model.BindInfo;
import com.bikan.reading.model.BoardModel;
import com.bikan.reading.model.BubbleModel;
import com.bikan.reading.model.CardNumModel;
import com.bikan.reading.model.ChannelData;
import com.bikan.reading.model.ChannelListModel;
import com.bikan.reading.model.ClassifyMessageItemModel;
import com.bikan.reading.model.CloudDialogModel;
import com.bikan.reading.model.CombinedModel;
import com.bikan.reading.model.CommentDetailListModel;
import com.bikan.reading.model.CommentListModel;
import com.bikan.reading.model.DiscussionHistoryModel;
import com.bikan.reading.model.DiversionAwardModel;
import com.bikan.reading.model.FeedbackDetailModelList;
import com.bikan.reading.model.FeedbackHistoryModelList;
import com.bikan.reading.model.GameTabItemModel;
import com.bikan.reading.model.GroupModel;
import com.bikan.reading.model.GuideConditionModel;
import com.bikan.reading.model.HotTopics;
import com.bikan.reading.model.ItemModel;
import com.bikan.reading.model.LetterModel;
import com.bikan.reading.model.LoginInfo;
import com.bikan.reading.model.MainFloatingItem;
import com.bikan.reading.model.MessageBaseModel;
import com.bikan.reading.model.MessageListModel;
import com.bikan.reading.model.MineActivityItem;
import com.bikan.reading.model.MineBannerItemModel;
import com.bikan.reading.model.MsgSupportModel;
import com.bikan.reading.model.NewDeviceModel;
import com.bikan.reading.model.NewsDetailModel;
import com.bikan.reading.model.NewsGroupDetailModel;
import com.bikan.reading.model.NormalNewsItem;
import com.bikan.reading.model.NormalNewsItems;
import com.bikan.reading.model.NotificationModel;
import com.bikan.reading.model.OpenColorEggModel;
import com.bikan.reading.model.PublishGroupCombinedModel;
import com.bikan.reading.model.ReadTaskConfigModel;
import com.bikan.reading.model.RecUserListModel;
import com.bikan.reading.model.ReplyModel;
import com.bikan.reading.model.ReviewPrePubModel;
import com.bikan.reading.model.SearchDiscussionModel;
import com.bikan.reading.model.ShareReportModel;
import com.bikan.reading.model.SimpleDocumentModel;
import com.bikan.reading.model.SmallVideoModel;
import com.bikan.reading.model.SubTopicModel;
import com.bikan.reading.model.SuggestionItem;
import com.bikan.reading.model.SupportModel;
import com.bikan.reading.model.SyncTopicModel;
import com.bikan.reading.model.TabLabel;
import com.bikan.reading.model.TodayHotNewsPageModel;
import com.bikan.reading.model.TodayHotNewsPosterModel;
import com.bikan.reading.model.TopicModel;
import com.bikan.reading.model.TopicRankCard;
import com.bikan.reading.model.UpdateModel;
import com.bikan.reading.model.VideoDetailModel;
import com.bikan.reading.model.VideoUploadModel;
import com.bikan.reading.model.WebResourceModel;
import com.bikan.reading.model.WeekTaskListModel;
import com.bikan.reading.model.ZhuanTiModel;
import com.bikan.reading.model.author.AuthorModel;
import com.bikan.reading.model.collect.CollectModel;
import com.bikan.reading.model.invitation.InvitationCodeCheckResultModel;
import com.bikan.reading.model.invitation.MasterInfoModel;
import com.bikan.reading.model.invitation.MasterTaskModel;
import com.bikan.reading.model.read_history.ReadHistoryDataModel;
import com.bikan.reading.model.topic.SubTopicInfo;
import com.bikan.reading.model.user.CommentInfoModel;
import com.bikan.reading.model.user.CommentInfosModel;
import com.bikan.reading.model.user.ComplaintResultModel;
import com.bikan.reading.model.user.UserModel;
import com.bikan.reading.newuser.AwardCoin;
import com.bikan.reading.newuser.RedPacketStatus;
import com.bikan.reading.publish.model.AlbumMaterial;
import com.bikan.reading.task.ReadResult;
import com.bikan.reading.utils.Constants;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface CommonService {

    @GET("/api/v4/config/state")
    Observable<ModeBase<Map<String, String>>> getConfigStatus();

    @GET("api/v1/management/ota/pkg/latest/get/version")
    Observable<ModeBase<UpdateModel>> updateApk(@Query("id") int versionCode);

    @GET("api/v1/management/ota/pkg/resource")
    Observable<Response<ModeBase<List<WebResourceModel>>>> getWebResource();

    @GET("api/v1/management/my/center")
    Observable<ModeBase<List<ItemModel>>> getItems();

    @GET("api/v1/module/myadbanner")
    Observable<ModeBase<List<MineBannerItemModel>>> getMineAdBanner();

    @GET("api/v1/module/tabtopbanner")
    Observable<ModeBase<List<GameTabItemModel>>> getGameTabInfo();

    @POST("/api/v4/game/join/award")
    Observable<ModeBase<JsonObject>> getGameJoinAward();

    @POST("/api/v4/game/play")
    Observable<ModeBase<TaskResultModel>> getGamePlayAward(@Query("action") String action);

    @GET("api/v1/news/channel/video")
    Observable<Response<NewsModeBase<List<NormalNewsItems>>>> requestVideoFeed(
            @Query("loadMore") boolean loadMore,
            @Query("parameters") String params,
            @Query("channelId") String channelId,
            @Query("location") String location,
            @Query("tailFeedCount") int tailFeedCount);

    @GET("api/v1/news/channel/video")
    Observable<NewsModeBase<List<NormalNewsItems>>> requestVideoFeed(
            @Query("loadMore") boolean loadMore,
            @Query("parameters") String params,
            @Query("channelId") String channelId,
            @Query("location") String location);

    /**
     * 获取首页推荐频道的数据
     */
    @GET("api/v1/news/channel/rec")
    Observable<Response<NewsModeBase<List<NormalNewsItems>>>> requestRecChanelFeed(
            @QueryMap Map<String, String> query);

    /**
     * 获取首页普通频道的数据
     */
    @GET("api/v1/news/channel")
    Observable<Response<NewsModeBase<List<NormalNewsItems>>>> requestNormalChanelFeed(
            @Query("loadMore") boolean loadMore,
            @Query("parameters") String params,
            @Query("channelId") String channelId,
            @Query("location") String location,
            @Query("tailFeedCount") int tailFeedCount,
            @Query("page") int page,
            @Query("path") String path);

    /**
     * 锁屏阅读Feed数据
     */
    @GET("/api/v1/news/lock")
    Observable<NewsModeBase<List<SimpleDocumentModel>>> requestLockFeed();

    /**
     * 获取话题tab的数据
     */
    @POST("api/v4/topics/rec")
    @FormUrlEncoded
    Observable<Response<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>>> requestTopicFeed(
            @Field("location") String location,
            @Field("offset") int offset,
            @Field("count") int count);

    @GET("api/v4/task/readCallback")
    Observable<ModeBase<ReadResult>> uploadReadTimes(@Query("readInfoList") String readList);

    /**
     * 围观-本地tab
     */
    @POST("/api/v4/topics/local")
    @FormUrlEncoded
    Observable<Response<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>>> requestlocalFeed(
            @Field("location") String location,
            @Field("since") String since,
            @Field("count") int count);

    @GET("api/v4/topics/sub/hot")
    Observable<ModeBase<TopicRankCard>> getHotSubTopicRankCard();

    /**
     * 获取关注用户的话题评论
     *
     * @return
     */
    @POST("api/v4/topics/focus")
    @FormUrlEncoded
    Observable<Response<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>>> requestFocusTopicFeed(
            @Field("location") String location,
            @Field("offset") int offset,
            @Field("count") int count);

    /**
     * 获取关注用户自某时间以来新的话题动态条数
     *
     * @param minTime
     * @return
     */
    @POST("api/v4/topics/focuscount")
    @FormUrlEncoded
    Observable<NewsModeBase<JsonObject>> requestFocusTopicCount(@Field("minTime") long minTime);

    /**
     * 获取话题详情页精选数据
     */
    @POST("api/v4/topics/detail/featured")
    @FormUrlEncoded
    Observable<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>> requestTopicFeatureData(
            @Field("location") String location,
            @Field("since") String since,
            @Field("topicId") String topicId,
            @Field("offset") int offset,
            @Field("count") int count);

    /**
     * 获取话题详情页推荐数据
     */
    @POST("api/v4/topics/detail/rec")
    @FormUrlEncoded
    Observable<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>> requestTopicRecommendData(
            @Field("location") String location,
            @Field("since") String since,
            @Field("topicId") String topicId,
            @Field("offset") int offset,
            @Field("count") int count);

    /*获取子话题详情*/

    @POST("api/v4/topics/subtopic")
    @FormUrlEncoded
    Observable<NewsModeBase<SubTopicModel<SubTopicInfo, List<CommentInfoModel>>>> requestSubTopicDetail(
            @Field("groupId") String topicId,
            @Field("topicName") String topicName,
            @Field("offset") int offset,
            @Field("count") int count);

    /**
     * 获取话题详情页头部数据
     */
    @POST("api/v4/group/detail/top")
    @FormUrlEncoded
    Observable<NewsModeBase<HotTopics>> requestTopicTopInfo(
            @Field("location") String location,
            @Field("groupId") String groupId);

    /**
     * 获取话题位置聚合页的数据
     */
    @POST("api/v4/topics/location")
    @FormUrlEncoded
    Observable<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>> requestTopicLocationDetail(
            @Field("location") String location,
            @Field("since") String since,
            @Field("topicId") String topicId,
            @Field("offset") int offset,
            @Field("count") int count);

    @GET("api/v1/news/channel")
    Observable<Response<NewsModeBase<List<NormalNewsItems>>>> requestFollowChannel(
            @Query("channelId") String channelId,
            @Query("loadMore") boolean loadMore,
            @Query("timeMax") long timeMax,
            @Query("timeMin") long timeMin,
            @Query("location") String location,
            @Query("checkUpdate") boolean checkUpdate);

    @GET("api/v1/news/channel")
    Observable<Response<NewsModeBase<List<NormalNewsItems>>>> requestFollowChannel(@QueryMap Map<String, String> map);

    @POST("api/v1/report/news")
    Observable<ModeBase<String>> report(@Query("docId") String docId, @Query("url") String url,
            @Query("title") String title);

    @POST("api/v1/management/favou/add")
    Observable<ModeBase<String>> addFavorite(@Query("data") String data);

    @POST("api/v1/management/favou/del")
    Observable<ModeBase<String>> delFavorite(@Query("docId") String docId);

    @GET("api/v1/management/favou/list")
    Observable<NewsModeBase<List<CollectModel<String>>>> getCollectList(@Query("start") int start);

    @GET("api/v1/management/favou/list")
    Observable<NewsModeBase<List<CollectModel<String>>>> getCollectList(@Query("start") int start,
                                                                        @Query("startRow") String startRow);

    @GET("api/v1/news/channel/video/mini")
    Observable<ModeBase<ArrayList<CommentInfoModel>>> getVideoTabSmallVideo(@Query("loadMore") boolean loadMore);

    // 新用户前7天红包状态
    @GET("api/v4/weektask/weekcard/status")
    Observable<ModeBase<RedPacketStatus>> redPacketStatus();

    // 领取新用户前7天红包
    @GET("api/v4/weektask/read/finish")
    Observable<ModeBase<AwardCoin>> finishRedPacket(@Query("taskId") int taskId, @Query("stageId") int stageId);

    @GET("api/v4/weektask/uploadtime")
    Observable<ModeBase> uploadReadTime(@Query("taskId") int taskId, @Query("time") long time);

    // 获取获得奖励的用户
    @GET("api/v4/weektask/award-users")
    Observable<ModeBase<Map<String, String>>> getAwardUsers();

    @FormUrlEncoded
    @POST("api/v1/management/history/add")
    Observable<ModeBase<String>> addReadHistory(@Field("docId") String docId,
            @Field("time") long time, @Field("doc") String doc, @Field("type") String type);

    @POST("api/v1/management/history/delete/batch")
    Observable<ModeBase<String>> deleteReadHistoryList(@Query("keys") String keys);

    @GET("api/v1/management/history/list")
    Observable<ModeBase<ReadHistoryDataModel>> getReadHistoryList(@Query("start") int start);

    @POST("api/v1/management/history/clear")
    Observable<ModeBase<String>> clearReadHistoryList();

    @FormUrlEncoded
    @POST("api/v1/management/favou/delebacth")
    Observable<ModeBase<String>> deleteCollectList(@Field("keys") String keyListString);

    @GET("api/v1/news/detail")
    Observable<NewsModeBase<List<AtlasDataModel>>> requestAtlasDetail(@Query("type") String type,
            @Query("docId") String docId,
            @Query("parameters") String params);

    @GET("api/v1/news/detail?type=news")
    Observable<ModeBase<List<NewsDetailModel>>> getNewsDetail(@QueryMap Map<String, String> map);

    @GET("api/v1/news/detail?type=video")
    Observable<ModeBase<List<VideoDetailModel>>> getVideoDetail(@Query("docId") String docId, @Query("path") String path, @Query("parameters") String params);

    @GET("api/v1/news/detail?type=video")
    Observable<ModeBase<List<VideoDetailModel>>> getVideoCommentDetail(@Query("docId") String docId, @Query("parameters") String params, @Query("source") String source);

    @FormUrlEncoded
    @POST("api/v1/news/brief")
    Observable<ModeBase<SimpleDocumentModel>> getNewsBrief(@Field("docId") String docId);

    @FormUrlEncoded
    @POST("api/v1/management/channel/upload")
    Observable<ModeBase> uploadChannel(@Field("channel") String channelModels,
            @Field("channelVersion") int version, @Field("local") boolean local);

    @GET("api/v1/news/popular")
    Observable<ModeBase<TodayHotNewsPageModel>> getTodayHotNews(@Query("startTime") long startTime);

    @GET("api/v1/news/poster")
    Observable<ModeBase<TodayHotNewsPosterModel>> getPosterInfo(@Query("startTime") long startTime);

    @FormUrlEncoded
    @POST("/api/push/register")
    Observable<ModeBase<String>> registerPush(@Field("regId") String regId);

    @FormUrlEncoded
    @POST("/api/push/clear")
    Observable<ModeBase<String>> unRegisterPush(@Field("imei") String imei,
            @Field("userId") String userId);

    @GET("api/v1/management/channel")
    Observable<ModeBase<ChannelData>> getCustomChannel(@Query("firstStart") boolean firstStart);

    @POST("/api/v4/feed-coin/award")
    Observable<ModeBase> getFeedCoinAward(@Query("number") int number, @Query("coin") int coin);

    @POST("/api/v4/feed-coin/readnotify")
    Observable<ModeBase> feedCoinReadNotify(@Query("number") int number);

    @GET("/api/v1/task/unsign")
    Observable<ModeBase<Integer>> getSignOrIntBox();

    @GET("/api/user/checknewdevice")
    Observable<ModeBase<NewDeviceModel>> checkNewDevice();

    @GET("api/v4/config")
    Observable<ModeBase<JsonObject>> getConfigInfo();

    @GET("api/v4/config/android")
    Observable<ModeBase<String>> getAndroidConfig(@Query("key") String key);

    @GET("/api/v4/task/readtime/exp")
    Observable<ModeBase<String>> getFudaiConfig();

    @POST("/api/tracking")
    @FormUrlEncoded
    Observable<ModeBase<String>> uploadEid(@Field("value") String value);

    @GET("/api/v1/sug/list")
    Observable<ModeBase<List<String>>> getSugList(@Query("query") String query);

    @GET("/api/v4/user/home")
    Observable<ModeBase<CommentInfosModel>> getUserHomeV4(@Query("userId") String userId,
            @Query("endMark") String endMark, @Query("docType") int docType);

    @GET("/api/v1/user/info")
    Observable<ModeBase<UserModel>> getLoginUserInfo();

    @POST("/api/v4/user/unban")
    Observable<ModeBase<ComplaintResultModel>> accountComplaint(@Query("captcha_action") String action, @Query("captcha_token") String token, @Query("user") String userId);

    @POST("/api/v1/user/edit/icon")
    @FormUrlEncoded
    Observable<ModeBase<String>> uploadImage(@Field("icon") String imgByte);

    @POST("/api/v1/user/edit/name")
    @FormUrlEncoded
    Observable<ModeBase<String>> postUserName(@Field("name") String name);

    @POST("/api/v1/user/edit/baseinfo")
    @FormUrlEncoded
    Observable<ModeBase<String>> postUserInfo(@Field("gender") int gender,
            @Field("birthday") String birthday);

    @GET("/api/v1/notice/mail")
    Observable<ModeBase<List<LetterModel>>> getLetterList(@Query("page") long page);

    @GET("/api/v4/notice/mail/list")
    Observable<ModeBase<List<ClassifyMessageItemModel>>> getClassifyMessageList();

    @GET("/api/v4/notice/mail/detail")
    Observable<ModeBase<MessageListModel>> getMessageList(@Query("nickName") String nickName, @Query("unreadCount") int unreadCount, @Query("page") int page);

    @POST("/api/v1/notice/read")
    @FormUrlEncoded
    Observable<ModeBase<List<LetterModel>>> setRead(@Field("mailId") String mailId);

    @GET("/api/v4/notice/unread/count")
    Observable<ModeBase<Integer>> getUnReadCount();

    @GET("/api/v4/invitation/codecheck")
    Observable<ModeBase<InvitationCodeCheckResultModel>> invitationCodeCheck(
            @Query("invitationCode") String invitationCode);

    @POST("/api/v4/invitation/invited")
    Observable<ModeBase> commitInvitationCode(@Query("invitationCode") String invitationCode);

    @GET("/api/v4/invitation/masterinfo")
    Observable<ModeBase<MasterInfoModel>> getMasterInfo(@Query("invitationCode") String invitationCode,
            @Query("inviteType") String inviteType);

    @POST("/api/v4/invitation/relation/create")
    Observable<ModeBase> createRelation(@Query("inviteCode") String inviteCode);

    @GET("/api/v4/invitation/task")
    Observable<ModeBase<MasterTaskModel>> getMasterTaskModel();

    @GET("/api/v1/user/device/status/{type}")
    Observable<String> getDeviceConfig(@Path("type") String type);

    @POST("/api/v1/award/reflux")
    Observable<ModeBase<BackUserGuideModel>> getReflux();

    @GET("/api/v4/config/user/readTask")
    Observable<ModeBase<ReadTaskConfigModel>> getReadTask();

    @GET("/api/v4/task/browse-time/status")
    Observable<ModeBase<JsonObject>> getReadTaskStatus();

    @FormUrlEncoded
    @POST("api/v4/review/support")
    Observable<ModeBase<String>> sendSupport(@FieldMap Map<String, String> map);

    /**
     * @param dynamicsId       动态的ID，即发表动态成功后服务端返回的reviewId
     * @param dynamicsAuthorId 该条动态对应的作者的userId
     * @return
     */
    @FormUrlEncoded
    @POST("api/v4/review/pre/pub")
    Observable<ModeBase<ReviewPrePubModel>> preComment(@Field("docId") String docId,
            @Field("documents") String documents, @Field("appId") String appId,
            @Field("source") String source, @Field("location") String location,
            @Field("dynamicsId") String dynamicsId, @Field("dynamicsAuthorId") String dynamicsAuthorId,
            @Field("imagesInfo") String imagesInfo, @Field("videosInfo") String videosInfo,
            @Field("title") String title, @Field("extra") String extra, @Field("uuid") String uuid);

    @FormUrlEncoded
    @POST("api/v4/review/add")
    Observable<ModeBase<JsonObject>> addComment(@Field("docId") String docId,
            @Field("documents") String documents, @Field("appId") String appId,
            @Field("source") String source, @Field("location") String location,
            @Field("dynamicsId") String dynamicsId, @Field("dynamicsAuthorId") String dynamicsAuthorId,
            @Field("imagesInfo") String imagesInfo, @Field("videosInfo") String videosInfo,
            @Field("title") String title, @Field("extra") String extra, @Field("uuid") String uuid);

    @POST("api/v4/review/add")
    @FormUrlEncoded
    Observable<ModeBase<JsonObject>> replyComment(@Field("appId") String appid,
            @Field("docId") String docId, @Field("reviewId") String reviewId,
            @Field("documents") String document, @Field("source") String source,
            @Field("dynamicsId") String dynamicsId, @Field("dynamicsAuthorId") String dynamicsAuthorId,
            @Field("title") String title, @Field("extra") String extra);

    @GET("/api/push/realimei")
    Observable<ModeBase<String>> getImei(@Query("regId") String regId);

    @GET("/api/v4/video/upload/address/v2")
    Observable<ModeBase<VideoUploadModel>> requestVideoUploadUrl();

    @GET("/api/v4/review/reviewDetail?appId=bikan")
    Observable<ModeBase<CommentDetailListModel>> getCommentDetail(@Query("docId") String docId,
            @Query("reviewId") String reviewId, @Query("currentReviewId") String currentReviewId,
            @Query("offset") int offset, @Query("source") String source, @Query("pageSize") int pageSize,
            @Query("rankType") int rankType, @Query("topReviewId") String topReviewId, @Query("since") String since);

    @GET("api/v1/search/hot")
    Observable<ModeBase<List<SuggestionItem>>> getSuggestion(@Query("size") int size,
            @Query("offse") int offset);

    @GET("/api/v1/news/discussion/latest")
    Observable<ModeBase<SearchDiscussionModel>> getLatestDiscussion();

    @GET("/api/v1/search/query")
    Observable<ModeBase<List<NormalNewsItem>>> getSearchResult(@Query("query") String query, @Query("offset") int offset, @Query("size") int size);

    @GET("/api/v1/search/user")
    Observable<ModeBase<List<UserModel>>> getSearchUserResult(@Query("searchSession") String searchSession, @Query("query") String query, @Query("page") int page);

    @GET("/api/v4/location/address")
    Observable<ModeBase<JsonObject>> getAddress(@Query("location") String location);

    @GET("/api/v4/location/cities")
    Observable<ModeBase<JsonObject>> getCityList();

    @GET("/api/v4/bubble")
    Observable<ModeBase<BubbleModel>> checkNewUserDialogDisplay(@Query("type") String type);

    @GET("/api/v4/user/tabActivities")
    Observable<ModeBase<List<MineActivityItem>>> getMinePigActivityInfo();

    @GET("/api/v4/user/rec")
    Observable<ModeBase<RecUserListModel>> getRecUserList(@Query("location") String location, @Query("offset") int offset, @Query("count") int count);

    @FormUrlEncoded
    @POST("/api/v4/review/sync")
    Observable<ModeBase<SyncTopicModel>> syncTopic(@Field("docId") String docId, @Field("title") String title, @Field("uuid") String uuid);

    //新闻详情页评论
    @GET("/api/v4/review/listV2")
    Observable<ModeBase<CommentListModel>> getNewsCommentList(@QueryMap Map<String, String> map);

    //视频详情页评论
    @GET("/api/v4/review/list")
    Observable<ModeBase<CommentListModel>> getSortCommentList(@QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/api/v1/subscription/dislike")
    Observable<ModeBase<String>> sendDislikeAuthor(@Field("id") String id);

    @GET("/api/v1/subscription/category")
    Observable<NewsModeBase<List<CombinedModel<AuthorModel>>>> getRecommendAuthorList(@Query("offset") int offset,
                                                                                      @Query("count") int count,
                                                                                      @Query("categoryName") String categoryName);

    @GET("/api/v4/config/tabframe")
    Observable<ModeBase<List<CloudDialogModel>>> getCloudDialogConfig();

    @GET("/api/v4/invitation/mainFloating")
    Observable<ModeBase<List<MainFloatingItem>>> getMainFloating();

    @Streaming
    @GET()
    Observable<ResponseBody> downloadFileWithDynamicUrl(@Url String fileUrl);

    // 所有圈子
    @GET("/api/v4/group/category")
    Observable<NewsModeBase<List<CombinedModel<HotTopics>>>> requestCategoryGroup(
            @Query("ignoreLocal") boolean ignoreLocal,
            @Query("offset") int offset,
            @Query("count") int count,
            @Query("category") String category);

    // 发布圈子
    @GET("/api/v4/group/list")
    Observable<NewsModeBase<PublishGroupCombinedModel>> requestPublishGroupList();

    // 加入圈子
    @FormUrlEncoded
    @POST("/api/v4/group/join")
    Observable<ModeBase<String>> enterGroup(@Field("topicId") String topicId);

    // 退出圈子
    @FormUrlEncoded
    @POST("/api/v4/group/unjoin")
    Observable<ModeBase<String>> exitGroup(@Field("topicId") String topicId);

    // 用户圈子列表
    @GET("/api/v4/user/grouplist")
    Observable<ModeBase<GroupModel<HotTopics>>> getUserGroups(
            @Query("userId") String userId,
            @Query("offset") int offset,
            @Query("count") int count,
            @Query("order") String order);

    // 用户圈子列表
    @GET("/api/v4/user/focusGroupList")
    Observable<ModeBase<GroupModel<HotTopics>>> getUserGroups(@QueryMap Map<String, String> map);

    // 话题列表
    @GET("/api/v4/group/subtopiclist")
    Observable<ModeBase<List<SubTopicInfo>>> getSubTopicList(@Query("groupId") String groupId);

    // 用户列表
    @FormUrlEncoded
    @POST("/api/v4/group/listuser")
    Observable<ModeBase<List<UserModel>>> getGroupUsers(
            @Field("topicId") String topicId,
            @Field("offset") int offset,
            @Field("count") int count);

    // 看友编号
    @FormUrlEncoded
    @POST("/api/v4/group/generateAddressNo")
    Observable<ModeBase<CardNumModel>> getGroupCardNum(@Field("location") String location);

    // 统计用户分享圈子的次数
    @FormUrlEncoded
    @POST("/api/tracking/share")
    Observable<ModeBase> trackTopicShare(@Field("sharedBy") String sharedBy,
            @Field("authorId") String authorId, @Field("reviewId") String reviewId);

    // 分享上报
    @FormUrlEncoded
    @POST("api/v4/task/share/report")
    Observable<ModeBase<ShareReportModel>> shareReport(@Field("docid") String docid);

    @GET("/api/v1/news/notice")
    Observable<ModeBase<NotificationModel>> getNotification();

    @GET("/api/v1/news/special/detail")
    Observable<ModeBase<ZhuanTiModel<SmallVideoModel>>> requestSmallVideoZhuanti(@Query("docId") String docId);

    @GET("/api/v1/news/special/detail")
    Observable<ModeBase<ZhuanTiModel<BoardModel>>> requestSpecialTopicDetail(@Query("docId") String docId);

    // 内容源点赞
    @POST("/api/v1/news/user_action/add")
    @FormUrlEncoded
    Observable<String> sendFeedBack(@Field("data") String data, @Field("authorId") String authorId);

    @GET("api/v4/msg/replylist")
    Observable<ModeBase<MessageBaseModel<ReplyModel>>> getCommentReplyList(
            @Query("appId") String appId, @Query("since") String since,
            @Query("pageSize") int pageSize);

    @GET("api/v4/msg/newsupportlist")
    Observable<ModeBase<MessageBaseModel<SupportModel>>> getCommentSupportList(
            @Query("appId") String appId, @Query("showSenderCount") int count,
            @Query("offset") int offset, @Query("pageSize") int pageSize);

    @GET("api/v4/review/supportby")
    Observable<ModeBase<MessageBaseModel<MsgSupportModel>>> getMsgSupportUserList(@Query("appId") String appId,
            @Query("reviewId") String reviewId, @Query("pageSize") int pageSize, @Query("since") String since);

    @GET("api/v4/task/browse-time/reward")
    Observable<ModeBase<AwardCoinModel>> requestCircleAwardCoin(@Query("docId") String docId, @Query("type") int type, @Query("rewardedTimes") int times);

    @GET("api/v4/task/awardFeed")
    Observable<ModeBase<AwardFeedModel>> requestAwardFeed();

    @POST("api/v4/feedback/add")
    @FormUrlEncoded
    Observable<ModeBase> commitFeedback(@Field("type") String type, @Field("category") String category, @Field("userName") String userName,
            @Field("content") String content, @Field("imageInfo") String imageInfo, @Field("wechat") String wechat, @Field("phone") String phone);

    @GET("api/v4/feedback/list")
    Observable<ModeBase<FeedbackHistoryModelList>> requestFeedbackList(@Query("page") int page);

    @GET("api/v4/feedback/detail")
    Observable<ModeBase<FeedbackDetailModelList>> requestFeedbackDetail(@Query("id") String id, @Query("page") int page);

    @POST("api/v4/feedback/reply")
    Observable<ModeBase<String>> replyFeedback(@Query("id") String id, @Query("userName") String userName, @Query("content") String content, @Query("imageInfo") String images);

    @GET("api/v4/feedback/unread/count")
    Observable<ModeBase<Integer>> getFeedbackUnReadCount();

    @GET("/api/tracking/geo")
    Observable<String> uploadLocation(@Query("lat") double lat, @Query("lon") double lon);

    @POST("/api/v4/bonusscene/open")
    Observable<ModeBase<OpenColorEggModel>> openColorEgg();

    /**
     * 获取视频流数据
     */
    @POST("api/v4/topics/video/continuous")
    @FormUrlEncoded
    Observable<NewsModeBase<TopicModel<List<HotTopics>, List<CommentInfoModel>>>> requestVideoFlow(
            @Field("offset") int offset,
            @Field("count") int count,
            @Field("reviewId") String reviewId,
            @Field("path") String groupPath);

    @GET("/api/v4/device/imei")
    Observable<ModeBase<String>> getImeiByOaid(@Query("oaid") String oaid);

    @GET("/api/v4/tourist/status")
    Observable<ModeBase<Map<String, Object>>> getTouristStatus();

    @POST("/api/v4/tourist/oauth")
    Observable<ModeBase<LoginInfo>> getTouristInfo();

    @GET("/api/v4/oauth/unLogin/userInfo")
    Observable<ModeBase<JsonObject>> getLogOutUserInfo();

    @GET("/api/v4/user/logoutHint")
    Observable<ModeBase<Integer>> queryNotReceivedCoin();

    @POST("/api/v1/ad/getAppInstallWhiteList")
    Observable<ModeBase<List<String>>> getAppInsatllWhiteList();

    @GET("/api/v4/oauth/login/status")
    Observable<ModeBase<BindInfo>> getBindInfo(@Query("type") String type, @Query("key") String key);

    @POST("/api/v4/pace/report")
    Observable<ModeBase> reportStep(@Query("steps") int steps);

    @GET("/api/v4/task/tab/label")
    Observable<ModeBase<TabLabel>> getTabTypeData();

    /**
     * 获取更新patch包
     *
     * @param keyPatch String 格式需符合android_patch_versionCode_robustApkHash，如android_patch_10200_xxxx
     * @return Observable<Response < ModelBase < String>>>
     */
    @GET("/api/v4/config/android")
    Observable<Response<ModeBase<String>>> getPatchInfo(@Query("key") String keyPatch);

    @GET("api/v4/user/unNewUser")
    Observable<ModeBase<Boolean>> checkAllowShowAd();

    @POST("/api/v4/uid/get")
    @FormUrlEncoded
    Observable<ModeBase<Map<String, String>>> getUid(@Field("sn") String sn, @Field("uid") String uid);

    @GET("api/v4/weektask/task-list")
    Observable<ModeBase<WeekTaskListModel>> getWeekTaskList();

    /**
     * @param type wechat or phonenumber
     * @param cash 单位: 分
     */
    @POST("/api/v4/exchange/withdraw")
    @FormUrlEncoded
    Observable<ModeBase> withdraw(
            @Field("type") String type,
            @Field("cash") int cash,
            @Query("captcha_action") String action,
            @Query("captcha_token") String token,
            @Query("user") String userId);

    @GET("/api/v4/review/template/list")
    Observable<ModeBase<List<AlbumMaterial>>> getAlbumMaterial();

    // 每天首次从渠道启动的奖励
    @POST("/api/v4/diversion/award")
    @FormUrlEncoded
    Observable<ModeBase<DiversionAwardModel>> getDiversionAward(@Field("startSource") @Constants.StartSourceType String startSource);

    @GET("/api/v1/news/discussion/history")
    Observable<ModeBase<List<DiscussionHistoryModel>>> getDiscussionHistory(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    @GET("/api/v4/chat/team/nearest")
    Observable<ModeBase<LocalTeamItemModel>> getLocalTeam(@Query("channelCity") String city);

    @GET("/api/v4/group/newslist")
    Observable<ModeBase<NewsGroupDetailModel>> getGroupNewsList(@QueryMap Map<String, String> map);

    @GET("/api/v4/user/guide")
    Observable<ModeBase<GuideConditionModel>> getUserGuideCondition();

    @GET("/api/v1/management/channel/list")
    Observable<ModeBase<ChannelListModel>> getChannelList(@Query("userId") String userId);
}
