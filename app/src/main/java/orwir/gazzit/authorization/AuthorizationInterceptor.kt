package orwir.gazzit.authorization

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import orwir.gazzit.REDDIT_AUTH_URL

class AuthorizationInterceptor : Interceptor, KoinComponent {

    private val repository: AuthorizationRepository by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (request.url.host == REDDIT_AUTH_URL) {
            val token = repository.obtainToken()
            request = request.newBuilder()
                .addHeader("Authorization", "${token.type} ${token.access}")
                .build()
        }
        return chain.proceed(request)
    }

}