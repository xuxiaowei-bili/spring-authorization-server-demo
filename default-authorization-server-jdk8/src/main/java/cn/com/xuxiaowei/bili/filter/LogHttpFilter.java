package cn.com.xuxiaowei.bili.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
