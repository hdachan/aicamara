package kr.co.takensoft.aiCamera.popup.web;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.takensoft.aiCamera.popup.service.PopupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * Popup Controller 입니다.
 *
 * @author 이세현
 * @since 2023.10.30
 */
@Controller
public class PopupController {

    private final PopupService popupService;

    public PopupController(PopupService popupService) {
        this.popupService = popupService;
    }

    /**
     * 팝업 등록(첨부파일 포함)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/popupInsert.file", consumes = "multipart/form-data")
    public ModelAndView postFileInsert (@RequestParam("file") MultipartFile file, @RequestParam("popup") String popupJson) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> params = objectMapper.readValue(popupJson, new TypeReference<HashMap<String, Object>>() {});
        mav.addObject("result", popupService.popupInsert(file, params));
        return mav;
    }

    /**
     * 게시글 목록 조회(관리자)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/popupSelectList.json", method = RequestMethod.POST)
    public ModelAndView postSelectList (@RequestBody HashMap<String, Object> params) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("popupSelectList", popupService.adminPopupSelectList(params));
        mav.addObject("popupSelectListCount", popupService.adminPopupSelectListCount(params));

        return mav;
    }

    /**
     * 게시글 상세 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/popupSelectOne.json", method = RequestMethod.POST)
    public ModelAndView postSelectOne (@RequestBody HashMap<String, Object> params) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("popupSelectOne", popupService.popupSelectOne(params));
        mav.addObject("selectFileList", popupService.selectFileList(params));

        return mav;
    }

    /**
     * 사용자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/userPopupList.json", method = RequestMethod.POST)
    public ModelAndView userPopupList () throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("popupSelectList", popupService.userPopupList());

        return mav;
    }

    /**
     * 팝업 삭제
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/popupDelete.json", method = RequestMethod.POST)
    public ModelAndView popupDelete (@RequestBody HashMap<String, Object> params) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("result", popupService.popupDelete(params));

        return mav;
    }

    /**
     * 게시글 수정(첨부파일 미포함)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @RequestMapping(value = "/popup/popupUpdate.json", method = RequestMethod.POST)
    public ModelAndView postUpdate (@RequestBody HashMap<String, Object> params) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        mav.addObject("result", popupService.popupUpdate(params));

        return mav;
    }

    /**
     * 게시글 수정(첨부파일 포함)
     *
     * @author 이세현
     * @since 2023.11.06
     */
    @RequestMapping(value = "/popup/popupFileUpdate.file", consumes = "multipart/form-data")
    public ModelAndView postFileUpdate (@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "deleteFile", required = false) String deleteFile, @RequestParam("popup") String popupJson) throws Exception {

        ModelAndView mav = new ModelAndView("jsonView");
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> params = objectMapper.readValue(popupJson, new TypeReference<HashMap<String, Object>>() {});
        mav.addObject("result",popupService.popupFileUpdate(file, deleteFile, params));
        return mav;
    }

}

