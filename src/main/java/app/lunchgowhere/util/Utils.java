package app.lunchgowhere.util;

import jakarta.servlet.http.Cookie;
import org.springframework.http.server.ServletServerHttpRequest;

import java.util.Arrays;
import java.util.Optional;

public class Utils {
    public static Optional<String> extractSessionId(ServletServerHttpRequest request) {
        var cookies = (request.getServletRequest().getCookies());
        return cookies == null ? Optional.empty() : Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("sId"))
                .findFirst()
                .map(Cookie::getValue);
    }
}
