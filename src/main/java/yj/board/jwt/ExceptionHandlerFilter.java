package yj.board.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException ex) {
            log.debug("JWTVerificationException");
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ex);
        } catch (AuthenticationException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ex);
        } catch (AccessDeniedException ex) {
            setErrorResponse(HttpStatus.FORBIDDEN, request, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletRequest request,
                                 HttpServletResponse response, Throwable ex) throws IOException {

        if (status == HttpStatus.FORBIDDEN) {
            log.debug("403 forbidden : {}", status.value());
            response.setStatus(status.value());
            response.sendError(status.value());
        } else if (status == HttpStatus.UNAUTHORIZED) {
            log.debug("401 unauthorized : {}", status.value());
            /*response.setStatus(status.value());
            response.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            response.sendRedirect("/token/new");*/
//            response.sendError(status.value());
            response.setStatus(status.value());
            response.setContentType("application/json; charset=UTF-8");

            JSONObject responseJson = new JSONObject();
            responseJson.put("HttpStatus", HttpStatus.UNAUTHORIZED);
            responseJson.put("message", ex.getMessage());
            responseJson.put("status", 401);
            responseJson.put("statusCode", 401);
            responseJson.put("code", "401");
            response.getWriter().print(responseJson);
        }

        //response.setContentType("application/json; charset=UTF-8");

        /*response.getWriter().write(
                ErrorResponse.of(
                                HttpStatus.UNAUTHORIZED,
                                ex.getMessage(),
                                request
                        )
                        .convertToJson()
        );*/
    }
}
