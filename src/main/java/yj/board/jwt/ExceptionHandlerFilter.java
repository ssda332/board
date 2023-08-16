package yj.board.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (JWTVerificationException ex) {
            setErrorResponse(HttpStatus.FORBIDDEN, request, response, ex);
        } catch (AuthenticationException ex) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, request, response, ex);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletRequest request,
                                 HttpServletResponse response, Throwable ex) throws IOException {

        if (status == HttpStatus.FORBIDDEN) {
            response.setStatus(status.value());
            response.sendRedirect("/");
        } else if (status == HttpStatus.UNAUTHORIZED) {
            response.setStatus(status.value());
            response.sendRedirect("token/new");
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
