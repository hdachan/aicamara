package kr.co.takensoft.aiCamera.popup.service;


import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 *  팝업 관련 서비스 interface 입니다.
 *
 * @author 이세현
 * @since 2023.10.30
 */
public interface PopupService {
    /**
     * 팝업 등록(첨부파일 포함)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupInsert (MultipartFile file, HashMap<String, Object> params) throws Exception;

    /**
     * 관리자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public List<HashMap<String, Object>> adminPopupSelectList(HashMap<String, Object> params) throws Exception;

    /**
     * 관리자 팝업 목록 count 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int adminPopupSelectListCount (HashMap<String, Object> params) throws Exception;

    /**
     * 게시글 상제 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public HashMap<String, Object> popupSelectOne (HashMap<String, Object> params) throws Exception;

    /**
     * 파일ID로 첨부파일 찾기
     *
     * @author 이세현
     * @since 2023.11.03
     */
    public List<HashMap<String, Object>> selectFileList(HashMap<String, Object> params) throws Exception;

    /**
     * 사용자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public List<HashMap<String, Object>> userPopupList() throws Exception;

    /**
     * 팝업 삭제
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupDelete (HashMap<String, Object> params) throws Exception;

    /**
     * 팝업 수정(첨부파일 변경X)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupUpdate (HashMap<String, Object> params) throws Exception;

    /**
     * 팝업 수정(첨부파일 변경O)
     *
     * @author 이세현
     * @since 2023.11.06
     */
    public int popupFileUpdate(MultipartFile file, String deleteFile, HashMap<String, Object> params) throws Exception;


}
