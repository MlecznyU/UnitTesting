package testdoublesfundamentals.exercise4;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;
import testdoublesfundamentals.example4.networking.NetworkErrorException;
import testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import testdoublesfundamentals.exercise4.users.User;
import testdoublesfundamentals.exercise4.users.UsersCache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {
    UsersCacheTd usersCacheTd;
    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    FetchUserProfileUseCaseSync SUT;

    public static final String userId = "userId";
    public static final String userFullName= "fullName";
    public static final String userImageUrl= "imageUrl";
    public String mUserId;


    @Before
    public void setUp() throws Exception {
        usersCacheTd = new UsersCacheTd();
        userProfileHttpEndpointSyncTd=new UserProfileHttpEndpointSyncTd();
        SUT=new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd,usersCacheTd);
    }
    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync{

        public String mUserId;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsGeneralError;
        public boolean mNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId=userId;

            if (mIsAuthError){
                return  new EndpointResult(EndpointResultStatus.AUTH_ERROR,"","","");
            }else if(mIsServerError){
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR,"","","");
            }else if (mIsGeneralError){
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR,"","","");
            }else if (mNetworkError){
                throw new NetworkErrorException();
            }else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, userId,userFullName,userImageUrl);
            }
        }
    }
    private static class UsersCacheTd implements UsersCache{


        @Override
        public void cacheUser(User user) {

        }

        @Override
        public @Nullable User getUser(String userId) {
            return null;
        }
    }

    // userId passed to Endpoint
    @Test
    public void fetchUserProfileSync_success_userIdPassedToEndpoint() throws Exception{
        SUT.fetchUserProfileSync(userId);
        assertThat(userProfileHttpEndpointSyncTd.mUserId,is(userId));
    }
    // user cached
    @Test
    public void fetchUserProfileSync_success_userCached() throws Exception{
        SUT.fetchUserProfileSync(userId);
        User userCached = new UsersCacheTd().getUser(userId);
        assertThat(userCached.getUserId(), is(userId));
        assertThat(userCached.getFullName(), is(userFullName));
        assertThat(userCached.getImageUrl(), is(userImageUrl));
    }
    // user succeeded
    @Test
    public void fetchUserProfileSync_success_successReturned() throws Exception{
        FetchUserProfileUseCaseSync.UseCaseResult result = SUT.fetchUserProfileSync(userId);
        assertThat(result, is(FetchUserProfileUseCaseSync.UseCaseResult.SUCCESS));
    }
    // user failed
    // user not cached
    @Test
    public void fetchUserProfileSync_AuthError_userNotCached() throws Exception{
        userProfileHttpEndpointSyncTd.mIsAuthError = true;
        SUT.fetchUserProfileSync(userId);
        assertThat(usersCacheTd.getUser(userId), is(nullValue()));
    }
    @Test
    public void fetchUserProfileSync_ServerError_userNotCached() throws Exception{
        userProfileHttpEndpointSyncTd.mIsServerError = true;
        SUT.fetchUserProfileSync(userId);
        assertThat(usersCacheTd.getUser(userId), is(nullValue()));
    }
    @Test
    public void fetchUserProfileSync_GeneralError_userNotCached() throws Exception{
        userProfileHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.fetchUserProfileSync(userId);
        assertThat(usersCacheTd.getUser(userId), is(nullValue()));
    }
    // Network Error
}