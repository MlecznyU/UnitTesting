package testdoublesfundamentals.example4.authtoken;

public interface AuthTokenCache {

    void cacheAuthToken(String authToken);

    String getAuthToken();
}
