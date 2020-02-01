package testdoublesfundamentals.example4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import testdoublesfundamentals.example4.LoginUseCaseSync.UseCaseResult;
import testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import testdoublesfundamentals.example4.networking.NetworkErrorException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginUseCaseSyncTest {
    LoginUseCaseSync SUT;

    LoginHttpEndpointSyncTd loginHttpEndpointSyncTd;
    AuthTokenCacheTd authTokenCacheTd;
    EventBusPosterTd eventBusPosterTd;

    public static final String username="username";
    public static final String password="password";
    public static final String authToken = "authToken";
    @Before
    public void setUp() throws Exception {
        loginHttpEndpointSyncTd=new LoginHttpEndpointSyncTd();
        authTokenCacheTd=new AuthTokenCacheTd();
        eventBusPosterTd=new EventBusPosterTd();
        SUT=new LoginUseCaseSync(
                loginHttpEndpointSyncTd,
                authTokenCacheTd,
                eventBusPosterTd);
    }

    private static class LoginHttpEndpointSyncTd implements LoginHttpEndpointSync {
        public String mUsername="";
        public String mPassword="";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        private boolean mIsServerError;
        private boolean mIsNetworkError;

        @Override
        public EndpointResult loginSync(String username, String password) throws NetworkErrorException {
            mUsername = username;
            mPassword = password;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "");
            } else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS, authToken);
            }
        }
    }

    private static class AuthTokenCacheTd implements testdoublesfundamentals.example4.authtoken.AuthTokenCache{
        String mAuthToken="";
        @Override
        public void cacheAuthToken(String authToken) {
        mAuthToken=authToken;
        }

        @Override
        public String getAuthToken() {
            return mAuthToken;
        }
    }
    private static class EventBusPosterTd implements testdoublesfundamentals.example4.eventbus.EventBusPoster{
        public Object mEvent;
        public int mInteractionsCount;

        @Override
        public void postEvent(Object event) {
            mInteractionsCount++;
            mEvent = event;
        }
    }

    // username and password are passed to the endpoint
    @Test
    public void loginSync_success_usernameAndPasswordPassedToEndpoint() throws Exception {
        SUT.loginSync(username,password);
        Assert.assertThat(loginHttpEndpointSyncTd.mUsername, is(username));
        Assert.assertThat(loginHttpEndpointSyncTd.mPassword, is(password));
    }
    // if login succeeds -> user's authToken must be cached
    @Test
    public void loginSync_success_userAuthTokenCached()  throws Exception {
        SUT.loginSync(username,password);
        assertThat(authTokenCacheTd.getAuthToken(), is(authToken));
    }
    // if login fails -> user's authToken not changed
    @Test
    public void loginSync_generalError_userAuthTokenNotCached()  throws Exception{
        loginHttpEndpointSyncTd.mIsGeneralError= true;
        SUT.loginSync(username,password);
        assertThat(authTokenCacheTd.getAuthToken(), is(""));
    }
    @Test
    public void loginSync_AuthError_userAuthTokenNotCached()  throws Exception{
        loginHttpEndpointSyncTd.mIsAuthError= true;
        SUT.loginSync(username,password);
        assertThat(authTokenCacheTd.getAuthToken(), is(""));
    }
    @Test
    public void loginSync_ServerError_userAuthTokenNotCached()  throws Exception{
        loginHttpEndpointSyncTd.mIsServerError= true;
        SUT.loginSync(username,password);
        assertThat(authTokenCacheTd.getAuthToken(), is(""));
    }

    // if login succeeds -> login event posted to event bus
    @Test
    public void loginSync_success_loggedInEventPosted()  throws Exception{
        SUT.loginSync(username,password);
        assertThat(eventBusPosterTd.mEvent, is(instanceOf(LoggedInEvent.class)));
    }
    // if login fails -> no login event posted
    @Test
    public void loginSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        loginHttpEndpointSyncTd.mIsGeneralError=true;
        SUT.loginSync(username,password);
        assertThat(eventBusPosterTd.mInteractionsCount, is(0));
    }
    @Test
    public void loginSync_AuthError_noInteractionWithEventBusPoster()  throws Exception{
        loginHttpEndpointSyncTd.mIsAuthError=true;
        SUT.loginSync(username,password);
        assertThat(eventBusPosterTd.mInteractionsCount, is(0));
    }
    @Test
    public void loginSync_serverError_noInteractionWithEventBusPoster()  throws Exception{
        loginHttpEndpointSyncTd.mIsServerError=true;
        SUT.loginSync(username,password);
        assertThat(eventBusPosterTd.mInteractionsCount, is(0));
    }
    // if login succeeds -> success returned
    @Test
    public void loginSync_success_successReturned()  throws Exception{
        UseCaseResult result = SUT.loginSync(username,password);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }
    // if login fails -> fail returned
    @Test
    public void loginSync_serverError_failureReturned()  throws Exception{
        loginHttpEndpointSyncTd.mIsServerError=true;
        UseCaseResult result = SUT.loginSync(username,password);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    @Test
    public void loginSync_authError_failureReturned()  throws Exception{
        loginHttpEndpointSyncTd.mIsAuthError=true;
        UseCaseResult result = SUT.loginSync(username,password);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    @Test
    public void loginSync_generalError_failureReturned()  throws Exception{
        loginHttpEndpointSyncTd.mIsGeneralError=true;
        UseCaseResult result = SUT.loginSync(username,password);
        assertThat(result, is(UseCaseResult.FAILURE));
    }
    // network error - network error returned
    @Test
    public void loginSync_networkError_successReturned()  throws Exception{
        loginHttpEndpointSyncTd.mIsNetworkError=true;
        UseCaseResult result = SUT.loginSync(username,password);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }
}