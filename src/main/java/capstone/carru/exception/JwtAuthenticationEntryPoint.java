package capstone.carru.exception;

import capstone.carru.dto.ErrorCode;
import capstone.carru.dto.JwtErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        JwtErrorResponse jwtErrorResponse = new JwtErrorResponse(
                ErrorCode.INVALID_AUTH_TOKEN.getCode(), ErrorCode.INVALID_AUTH_TOKEN.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jwtErrorResponse);

        response.getWriter().write(result);
    }
}