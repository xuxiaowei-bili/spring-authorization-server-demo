package cn.com.xuxiaowei.bili;

import cn.com.xuxiaowei.bili.properties.AuthorizationServerProperties;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefaultAuthorizationServerJdk8031ApplicationTests {

    @Autowired
    private WebClient webClient;

    @LocalServerPort
    private int port;

    @Autowired
    private AuthorizationServerProperties authorizationServerProperties;

    @Test
    void contextLoads() throws IOException, InterruptedException {

        String username = authorizationServerProperties.getUsername();
        String password = authorizationServerProperties.getPassword();

        // 访问登陆页面
        HtmlPage loginPage = webClient.getPage("/login");

        // 输入用户名、密码
        HtmlInput usernameInput = loginPage.querySelector("input[name=\"username\"]");
        HtmlInput passwordInput = loginPage.querySelector("input[name=\"password\"]");
        usernameInput.type(username);
        passwordInput.type(password);

        // 登录
        HtmlButton signInButton = loginPage.querySelector("button");
        HtmlPage homePage = signInButton.click();

        // 授权
        HtmlAnchor authorize = homePage.querySelector("#authorize");
        HtmlPage authorizePage = authorize.click();
        log.info("授权后的路径:{}", authorizePage.getUrl());

        // 测试按钮
        click(authorizePage, "#check-bearer-button", "#check-bearer-div", "无效", false);
        click(authorizePage, "#check-url-param-button", "#check-url-param-div", "无效", false);
        click(authorizePage, "#refresh-button", "#refresh-div", "access_token", true);
    }

    void click(HtmlPage htmlPage, String button, String div, String contains, boolean expected) throws IOException, InterruptedException {

        log.info("htmlPage：{}, button：{}, div：{}, contains：{}, expected：{}", htmlPage, button, div, contains, expected);

        HtmlButton checkButton = htmlPage.querySelector(button);
        HtmlPage checkButtonAfter = checkButton.click();

        // 休眠：等待 ajax 响应
        Thread.sleep(1000);

        HtmlDivision checkButtonDiv = checkButtonAfter.querySelector(div);
        String checkButtonDivText = checkButtonDiv.asNormalizedText();

        assertThat(checkButtonDivText.contains(contains)).isEqualTo(expected);
    }

}
