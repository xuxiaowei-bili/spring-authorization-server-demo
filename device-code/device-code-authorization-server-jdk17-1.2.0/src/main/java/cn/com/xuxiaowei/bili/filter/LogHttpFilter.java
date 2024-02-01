package cn.com.xuxiaowei.bili.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 日志 过滤器
 *
 * @author xuxiaowei
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LogHttpFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestId = RandomStringUtils.randomAlphanumeric(6);
        // String requestId = RandomStringUtils.randomNumeric(6);
        // String requestId = RandomStringUtils.randomAlphabetic(6);
        // String requestId = RandomStringUtils.randomAscii(6);
        // String requestId = RandomStringUtils.randomGraph(6);
        // String requestId = RandomStringUtils.randomPrint(6);

        MDC.put("REQUEST_ID", requestId);

        String requestURI = request.getRequestURI();
        log.debug("requestURI：{}", requestURI);

        String queryString = request.getQueryString();
        log.debug("queryString：{}", queryString);

        super.doFilter(request, response, chain);
    }

}
