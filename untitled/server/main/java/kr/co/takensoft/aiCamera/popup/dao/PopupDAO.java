package kr.co.takensoft.aiCamera.popup.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * popup DAO 입니다.
 *
 * @author 이세현
 * @since 2023.11.26
 */
@Mapper
public interface PopupDAO {

    /**
     * 팝업 등록
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupInsert(HashMap<String, Object> params) throws Exception;

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
     * 사용자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public List<HashMap<String, Object>> userPopupList() throws Exception;

    /**
     * 팝업 게시글의 이전 게시물 불러오기
     *
     * @author 이세현
     * @since 2023.11.09
     */
    public HashMap<String, Object> findPrevPopup (HashMap<String, Object> params) throws Exception;

    /**
     * 팝업 게시글의 다음 게시물 불러오기
     *
     * @author 이세현
     * @since 2023.11.09
     */
    public HashMap<String, Object> findNextPopup (HashMap<String, Object> params) throws Exception;

    /**
     * 팝업 삭제
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupDelete (HashMap<String, Object> params) throws Exception;

    /**
     * 팝업 수정
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupUpdate (HashMap<String, Object> params) throws Exception;
}
