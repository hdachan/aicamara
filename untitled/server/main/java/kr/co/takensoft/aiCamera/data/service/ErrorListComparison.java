package kr.co.takensoft.aiCamera.data.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *  장애 데이터 insert 및 recovery 구분 하기 위한 클래스
 *
 * @author 이세현
 * @since 2024.01.16
 */
@Component
public class ErrorListComparison {

    public static HashMap<String, List<HashMap<String,Object>>> errorOldList = new HashMap<>();

    // errorOldList에 등록되지 않은 장애 장비 등록
    public void putEquipmentErrorData(String eqpmn_id, List<HashMap<String, Object>> errorList){
        errorOldList.put(eqpmn_id,errorList);
        System.out.println("errorOldList = " + errorOldList);
    }
    
    public void currentErrorOldListCheck(){
        System.out.println("errorOldList = " + errorOldList);
    }

    public boolean hasEquipment(String key) {
        return errorOldList.containsKey(key);
    }

    public void removeEquipment(String key) {
        errorOldList.remove(key);
        System.out.println("after remove: errorOldList = " + errorOldList);
    }

    // 장애 목록에 이미 등록되어 있는 장비의 value 바꾸기
    public void replaceEquipmentErrorData(String eqpmn_id, List<HashMap<String, Object>> replaceErrorList) {
        System.out.println("before errorOldList = " + errorOldList);
        errorOldList.replace(eqpmn_id, replaceErrorList);
        System.out.println("after errorOldList = " + errorOldList);
    }

    public HashMap<String, List<HashMap<String, Object>>> comparison (List<HashMap<String, Object>> newData, String eqpmn_id){
        if(errorOldList.isEmpty() || !errorOldList.containsKey(eqpmn_id)){
            putEquipmentErrorData(eqpmn_id,newData);

            HashMap<String, List<HashMap<String, Object>>> errorData = new HashMap<>();
            errorData.put("insert",newData);

            return errorData;
        } else {
            List<HashMap<String, Object>> oldData = errorOldList.get(eqpmn_id);

            System.out.println("oldData = " + oldData);
            System.out.println("newData = " + newData);
            
            List<HashMap<String, Object>> recoveryList = oldData.stream().filter(i -> newData.stream()
                    .noneMatch(Predicate.isEqual(i))).collect(Collectors.toList());

            List<HashMap<String, Object>> insertList = newData.stream().filter(i -> oldData.stream()
                    .noneMatch(Predicate.isEqual(i))).collect(Collectors.toList());

            System.out.println("recoveryList = " + recoveryList);
            System.out.println("insertList = " + insertList);

            if(recoveryList.isEmpty() && insertList.isEmpty()){
                return null;
            } else if(recoveryList.isEmpty()){
                HashMap<String, List<HashMap<String, Object>>> errorData = new HashMap<>();
                errorData.put("insert",insertList);
                replaceEquipmentErrorData(eqpmn_id, newData);
                return errorData;
            } else if(insertList.isEmpty()){
                HashMap<String, List<HashMap<String, Object>>> errorData = new HashMap<>();
                errorData.put("recovery",recoveryList);
                replaceEquipmentErrorData(eqpmn_id, newData);
                return errorData;
            } else {
                HashMap<String, List<HashMap<String, Object>>> errorData = new HashMap<>();
                errorData.put("recovery",recoveryList);
                errorData.put("insert",insertList);
                replaceEquipmentErrorData(eqpmn_id, newData);
                return errorData;
            }
        }
    }
}
