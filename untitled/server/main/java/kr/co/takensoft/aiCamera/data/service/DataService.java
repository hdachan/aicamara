package kr.co.takensoft.aiCamera.data.service;

import java.util.HashMap;
import java.util.List;

public interface DataService {

    /**
     * 객체인식 이벤트 등록
     *
     * @author 이세현
     * @since 2024.01.15
     */
    public int ObjectDetectEventInsert(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 - 현황정보 등록
     *
     * @author 이세현
     * @since 2024.01.16
     */
    public void statusInformation(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 - 센터 장애 처리(장애등록, 장애복구)
     *
     * @author 이세현
     * @since 2024.01.18
     */
    public void equipmentErrorHandler (List<HashMap<String, Object>> newErrorList, String eqpmn_id, String timestamp) throws Exception;



}
