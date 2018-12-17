package com.nowcoder.controller;

import com.nowcoder.service.NewsService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

/**
 * 资讯相关，上传图片
 *
 * @author wangleifu
 * @create 2018-12-17 14:35
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    NewsService newsService;

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imgName,
                           HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMG_DIR + imgName)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片失败" + e.getMessage());
        }

    }

    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file")MultipartFile file) {
        try {
            String fileURL = newsService.saveImage(file);
            if (fileURL == null) {
                return ToutiaoUtil.getJSONString(1, "上传图片失败！");
            }
            return ToutiaoUtil.getJSONString(0, fileURL);
        } catch (Exception e) {
            logger.error("上传图片失败！" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "上传失败！");
        }
    }
}
