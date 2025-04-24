package kr.co.takensoft.aiCamera.data.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;


/**
 * 현장 데이터 관련 DataDAO 입니다.
 *
 * @author 이세현
 * @since 2024.01.15
 */

@Mapper
public interface DataDAO {

    /**
     * 현장 - 객체인식 이벤트 등록
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
    public int statusInformation(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 - 장비 장애 등록
     *
     * @author 이세현
     * @since 2024.01.18
     */
    public int equipmentErrorInsert (HashMap<String, Object> params) throws Exception;

    /**
     * 현장 - 장비 장애 복구
     *
     * @author 이세현
     * @since 2024.01.18
     */
    public int equipmentRecovery (HashMap<String, Object> params) throws Exception;

    /**
     * 현장 - 장비 장애 복구
     *
     * @author 이세현
     * @since 2024.01.18
     */
    public int equipmentAllRecovery (HashMap<String, Object> params) throws Exception;

    /**
     * 장비 목록 조회
     *
     * @author 이세현
     * @since 2024.01.16
     */
    public List<HashMap<String,Object>> equipmentList() throws Exception;

    /**
     * 장애 미복구 장비 목록 조회
     *
     * @author 이세현
     * @since 2024.01.16
     */
    public List<HashMap<String, Object>> errorUnrecoveredSelectList(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 장비 30초전 데이터 존재 여부 확인
     *
     * @author 이세현
     * @since 2024.01.17
     */
    public int thirtySecondsAgoCount(HashMap<String, Object> params) throws Exception;

    /**
     * 통신연결오류 존재 여부 확인
     *
     * @author 이세현
     * @since 2024.01.17
     */
    public HashMap<String, Object> connectionErrorSelect(HashMap<String, Object> params) throws Exception;

    /**
     *  통신연결오류 복구
     *
     * @author 이세현
     * @since 2024.01.17
     */
    public int connectionErrorUpdateRecoveryToY(HashMap<String, Object> params) throws Exception;

    /**
     *  통신연결오류 등록
     *
     * @author 이세현
     * @since 2024.01.18
     */
    public int connectionErrorInsert(HashMap<String, Object> params) throws Exception;


    /***************************************** 분석용 데이터 1시간 *****************************************/

    /**
     * 현장 실시간 데이터(1시간) 총 보행자 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourPedestrianCount(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 총 자전거 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourBicycleCount(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 총 승용차 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourCarCount(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 총 오토바이 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourMotorcycleCount(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 총 버스 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourBusCount(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 총 트럭 수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourTruckCount(HashMap<String, Object> params) throws Exception;

    /**
     * 장비장애 데이터(1시간) 건수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourAllErrorCount(HashMap<String, Object> params) throws Exception;

    /**
     * 장비장애(연결장애) 데이터(1시간) 건수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourConnectionErrorCount(HashMap<String, Object> params) throws Exception;

    /**
     * 장비장애(CPU사용량) 데이터(1시간) 건수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourCpuUsageErrorCount(HashMap<String, Object> params) throws Exception;

    /**
     * 장비장애(메모리사용량) 데이터(1시간) 건수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourMemoryUsageErrorCount(HashMap<String, Object> params) throws Exception;

    /**
     * 분석용 데이터(1시간) 등록
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourAnalysisDataInsert(HashMap<String, Object> params) throws Exception;

    /**
     * 현장 실시간 데이터(1시간) 객체별 총 갯수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneHourVehicleCount(HashMap<String, Object> params) throws Exception;

    /***************************************** 분석용 데이터 1시간 끝 *****************************************/






    /***************************************** 분석용 데이터 하루 *********************************************/

    /**
     * 현장 실시간 데이터(하루) 객체별 총 갯수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneDayVehicleCount(HashMap<String, Object> params) throws Exception;

    /**
     * 분석용 데이터(하루) 등록
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int oneDayAnalysisDataInsert(HashMap<String, Object> params) throws Exception;

    /***************************************** 분석용 데이터 하루 끝 *********************************************/

    
    
    
    
    /***************************************** 분석용 데이터 한달 *********************************************/

    /**
     * 현장 실시간 데이터(한달) 객체별 총 갯수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int monthlyVehicleCount(HashMap<String, Object> params) throws Exception;

    /**
     * 분석용 데이터(한달) 등록
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int monthlyAnalysisDataInsert(HashMap<String, Object> params) throws Exception;

    /***************************************** 분석용 데이터 한달 끝 *********************************************/




    /***************************************** 분석용 데이터 일년 *********************************************/

    /**
     * 현장 실시간 데이터(1년) 객체별 총 갯수 조회
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int yearlyVehicleCount(HashMap<String, Object> params) throws Exception;

    /**
     * 분석용 데이터(1년) 등록
     *
     * @author 이세현
     * @since 2024.01.23
     */
    public int yearlyAnalysisDataInsert(HashMap<String, Object> params) throws Exception;


    /***************************************** 분석용 데이터 일년  끝 *********************************************/

}
