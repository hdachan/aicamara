package kr.co.takensoft.aiCamera.data.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.takensoft.aiCamera.data.dao.DataDAO;
import kr.co.takensoft.aiCamera.data.service.DataService;
import kr.co.takensoft.aiCamera.data.service.ErrorListComparison;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final ObjectMapper objectMapper;
    private final DataDAO dataDAO;
    private final ErrorListComparison errorListComparison;

    /**
     * 객체인식 이벤트 등록
     *
     * @author 이세현
     * @since 2024.01.15
     */
    @Override
    public int ObjectDetectEventInsert(HashMap<String, Object> params) throws Exception {
        System.out.println("params = " + params);
        HashMap<String, Object> bboxes = objectMapper.readValue((String) params.get("bboxes"), new TypeReference<HashMap<String, Object>>(){});

//        bboxes.put("ocrn_dt",params.get("datetime"));
//        String isoDate = params.get("datetime").toString();

        String isoDateTime = params.get("datetime").toString();
        OffsetDateTime odt = OffsetDateTime.parse(isoDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String formattedDate = odt.format(formatter);

        bboxes.put("ocrn_dt",formattedDate);
        bboxes.put("eqpmn_id",params.get("eqpmn_id"));
        bboxes.put("image",params.get("image"));

        if(!objectDetectionNullCheck(bboxes)){
            return dataDAO.ObjectDetectEventInsert(bboxes);
        }
        return 0;
    }


    private boolean objectDetectionNullCheck(HashMap<String, Object> bboxes){
        System.out.println("bboxes = " + bboxes);
        String[] keys = {"person_cnt", "bicycle_cnt", "car_cnt", "motorbike_cnt", "bus_cnt", "truck_cnt"};
        for(String key : keys){
            if(bboxes.containsKey(key)){
                int value = (Integer) bboxes.get(key);
                if(value != 0){
                    return false;
                }
            }
        }
        return true;
    };


    /**
     * 현장 - 현황정보 등록
     *
     * @author 이세현
     * @since 2024.01.16
     */
    @Override
    public void statusInformation(HashMap<String, Object> params) throws Exception {
        System.out.println("params = " + params);
        errorListComparison.currentErrorOldListCheck();
        String timeStamp = params.get("datetime").toString();
        String eqpmn_id = params.get("eqpmn_id").toString();
        params.put("timestamp",timeStamp);
        List<HashMap<String, Object>> newErrorList = new ArrayList<>();

        if((double)params.get("cpu_usage") >= 80) {
            HashMap<String, Object> error = new HashMap<>();
            error.put("eqpmn_id",eqpmn_id);
            error.put("eqpmn_trobl_cd","ERR-CPU-USAGE");
            newErrorList.add(error);
        }
        if((double)params.get("get_mem_usage") >= 80) {
            HashMap<String, Object> error = new HashMap<>();
            error.put("eqpmn_id",eqpmn_id);
            error.put("eqpmn_trobl_cd","ERR-MEM-USAGE");
            newErrorList.add(error);
        }
        //장비 고장 유무 추가
        String eqpmn_trobl_yn = ((double)params.get("cpu_usage") >= 80 || (double)params.get("get_mem_usage") >= 80) ? "Y" : "N";
        params.put("eqpmn_trobl_yn", eqpmn_trobl_yn);

        String isDevError = newErrorList.isEmpty()? "N" : "Y";
        System.out.println("isDevError = " + isDevError);
        params.put("isDevError", isDevError);
        dataDAO.statusInformation(params);

        if("Y".equals(isDevError)){
            equipmentErrorHandler(newErrorList, eqpmn_id, timeStamp);
        } else {
            //장애발생 정보가 없는 경우 복구한거로 간주 + oldErrorList에서 삭제하는 로직 필요
            //문제 newErrorList에 없다고 계속 비워버리는게 문제

            if(errorListComparison.hasEquipment(eqpmn_id)){
                HashMap<String, Object> recoveryOne = new HashMap<>();
                recoveryOne.put("eqpmn_id",eqpmn_id);
                recoveryOne.put("timestamp",timeStamp);
                //복구한거로 간주
                int allRecovery = dataDAO.equipmentAllRecovery(recoveryOne);
                //oldErrorList에서 삭제
                if(allRecovery > 0 ){
                    errorListComparison.removeEquipment(eqpmn_id);
                }
            }
        }


    }

    @Override
    public void equipmentErrorHandler(List<HashMap<String, Object>> errorList, String eqpmn_id, String timestamp) throws Exception {
        HashMap<String, List<HashMap<String, Object>>> errorData = errorListComparison.comparison(errorList, eqpmn_id);
        System.out.println("errorData = " + errorData);
        if(errorData == null){
            return;
        } else {
            if(errorData.containsKey("insert")){
                List<HashMap<String, Object>> insertList = errorData.get("insert");
                System.out.println("insertList = " + insertList);

//                for( HashMap<String, Object>insertOne : insertList){
//                   insertOne.put("detailDevErrCode",insertOne.get("eqpmn_trobl_cd"));
//                   insertOne.replace("eqpmn_id",eqpmn_id);
//                   insertOne.put("timestamp",timestamp);
//                   System.out.println("insertOne = " + insertOne);
//                   dataDAO.equipmentErrorInsert(insertOne);
//                }
                for(int i = 0; i < insertList.size(); i++){
                    HashMap<String, Object> insertOne = new HashMap<>(insertList.get(i));
                    insertOne.put("timestamp",timestamp);
                    insertOne.replace("eqpmn_id",eqpmn_id);
                    System.out.println("insertOne = " + insertOne);
                    dataDAO.equipmentErrorInsert(insertOne);
                }
            }
            if(errorData.containsKey("recovery")){
                List<HashMap<String, Object>> recoveryList = errorData.get("recovery");

//                for(HashMap<String, Object> recoveryOne : recoveryList){
//                    recoveryOne.put("eqpmn_id",eqpmn_id);
//                    recoveryOne.put("timestamp",timestamp);
//                    System.out.println("recoveryOne = " + recoveryOne);
//                    dataDAO.equipmentRecovery(recoveryOne);
//                }
                for(int i = 0; i < recoveryList.size(); i++){
                    HashMap<String, Object> recoveryOne = new HashMap<>(recoveryList.get(i));
                    recoveryOne.put("eqpmn_id",eqpmn_id);
                    recoveryOne.put("timestamp",timestamp);
                    System.out.println("recoveryOne = " + recoveryOne);
                    dataDAO.equipmentRecovery(recoveryOne);
                }
            }
        }

    }

}
