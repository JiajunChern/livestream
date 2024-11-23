package org.livestream.api.controller;

import jakarta.annotation.Resource;
import org.livestream.api.service.IHomePageService;
import org.livestream.api.vo.HomePageVO;
import org.livestream.common.interfaces.vo.WebResponseVO;
import org.livestream.web.starter.context.QiyuRequestContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author idea
 * @Date: Created in 08:56 2023/6/25
 * @Description
 */
@RestController
@RequestMapping("/home")
public class HomePageController {

    @Resource
    private IHomePageService homePageService;

    @PostMapping("/initPage")
    public WebResponseVO initPage() {
        Long userId = QiyuRequestContext.getUserId();
        HomePageVO homePageVO = new HomePageVO();
        homePageVO.setLoginStatus(false);
        if (userId != null) {
            homePageVO = homePageService.initPage(userId);
            homePageVO.setLoginStatus(true);
        }
        return WebResponseVO.success(homePageVO);
    }
}
