package kr.co.takensoft.aiCamera.common.service;
import kr.co.takensoft.aiCamera.data.dao.DataDAO;
import kr.co.takensoft.aiCamera.data.service.ErrorListComparison;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * 초기화 데이터 등록 및 스케줄러 설정
 *
 * @author 이세현
 * @since 2024.01.17
 */

@Component
@EnableScheduling
//@RequiredArgsConstructor
@AllArgsConstructor
public class SchedulerService implements InitializingBean {

    private DataDAO dataDAO;
    private ErrorListComparison errorListComparison;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("-------------------------------------시스템시작-----------------------------------");

        //1. 장비 리스트 불러 오기.
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();
        for(HashMap<String, Object> equipmentOne : equipmentList){
            String eqpmn_id = equipmentOne.get("eqpmn_id").toString();
            List<HashMap<String,Object>> errorList = dataDAO.errorUnrecoveredSelectList(equipmentOne);

            if(errorList.size() > 0){
                errorListComparison.putEquipmentErrorData(eqpmn_id,errorList);
            }
        }
//        monthlyAnalysisDataInsert();
//        yearlyAnalysisDataInsert();
    }
//
//    @PostConstruct
//    public void init() throws Exception {
//        System.out.println("-------------------------------------시스템시작-----------------------------------");
//
//        //1. 장비 리스트 불러 오기.
//        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();
//        for(HashMap<String, Object> equipmentOne : equipmentList){
//            String eqpmn_id = equipmentOne.get("eqpmn_id").toString();
//            List<HashMap<String,Object>> errorList = dataDAO.errorUnrecoveredSelectList(equipmentOne);
//
//            if(errorList.size() > 0){
//                errorListComparison.putEquipmentErrorData(eqpmn_id,errorList);
//            }
//        }
//    }

    //10초간격 스케쥴러
//    @Scheduled(fixedRate = 10000)
    public void errorCheckSchedule() throws Exception {
        //1.장비 리스트를 불러오기
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();
        for(HashMap<String, Object> equipmentOne : equipmentList){
            //2. 30초전 데이터 유무 확인
            int thirtySecondsAgoCount = dataDAO.thirtySecondsAgoCount(equipmentOne);

            //3. 해당 장비의 연결오류 유무 파악           
            HashMap<String, Object> connectionErrorSelect = dataDAO.connectionErrorSelect(equipmentOne);
            //4. 30초전 데이터(연결오류 여부)로 분기해서 처리
            if(thirtySecondsAgoCount > 0){
                if(connectionErrorSelect != null){
                    //30초전 데이터가 있고(연결문제가 없음), DB에서 에러 조회가 되면 에러를 복구
                    dataDAO.connectionErrorUpdateRecoveryToY(connectionErrorSelect);
                }
            } else {
                if(connectionErrorSelect == null){
                    //30초전 데이터가 없고(연결문제 발생), DB에서 에러 조회가 안되면 장비에러 정보를 등록
                    dataDAO.connectionErrorInsert(equipmentOne);
                }
            }
        }
    }

    /**
     * 분석용 데이터 1시간
     *
     * @author 이세현
     * @since 2024.01.23
     */
//    @Scheduled(cron = "0 5 * * * *")
//    public void oneHourAnalysisData() throws Exception {
//        LocalDateTime localDateTime = LocalDateTime.now().minusHours(1);
//        String dateTimeFormat = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
//
//        //전체 장비 목록 조회
//        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();
//
//        for(HashMap<String, Object> equipmentOne : equipmentList){
//            //데이터 수집 일시
//            String dateTime = dateTimeFormat + ":00:00";
//            equipmentOne.put("dateTime",dateTime);
//
//            int oneHourPedestrianCount = dataDAO.oneHourPedestrianCount(equipmentOne);
//            int oneHourBicycleCount = dataDAO.oneHourBicycleCount(equipmentOne);
//            int oneHourBusCount = dataDAO.oneHourBusCount(equipmentOne);
//            int oneHourCarCount = dataDAO.oneHourCarCount(equipmentOne);
//            int oneHourMotorcycleCount = dataDAO.oneHourMotorcycleCount(equipmentOne);
//            int oneHourTruckCount = dataDAO.oneHourTruckCount(equipmentOne);
//            int oneHourAllErrorCount = dataDAO.oneHourAllErrorCount(equipmentOne);
//            int oneHourConnectionErrorCount = dataDAO.oneHourConnectionErrorCount(equipmentOne);
//            int oneHourCpuUsageErrorCount = dataDAO.oneHourCpuUsageErrorCount(equipmentOne);
//            int oneHourMemoryUsageErrorCount = dataDAO.oneHourMemoryUsageErrorCount(equipmentOne);
//
//
//            //분석용 데이터(1시간) 등록
//            HashMap<String, Object> oneHourAnalysisData = new HashMap<>();
//
//            oneHourAnalysisData.put("eqpmn_id",equipmentOne.get("eqpmn_id"));
//            oneHourAnalysisData.put("clct_dt",dateTime);
//            oneHourAnalysisData.put("person_cnt",oneHourPedestrianCount);
//            oneHourAnalysisData.put("bicycle_cnt",oneHourBicycleCount);
//            oneHourAnalysisData.put("car_cnt",oneHourCarCount);
//            oneHourAnalysisData.put("motorcycle_cnt",oneHourMotorcycleCount);
//            oneHourAnalysisData.put("bus_cnt",oneHourBusCount);
//            oneHourAnalysisData.put("truck_cnt",oneHourTruckCount);
//            oneHourAnalysisData.put("trobl_cnt",oneHourAllErrorCount);
//            oneHourAnalysisData.put("cntn_trobl_cnt",oneHourConnectionErrorCount);
//            oneHourAnalysisData.put("cpu_trobl_cnt",oneHourCpuUsageErrorCount);
//            oneHourAnalysisData.put("mem_trobl_cnt",oneHourMemoryUsageErrorCount);
//
//            dataDAO.oneHourAnalysisDataInsert(oneHourAnalysisData);
//        }
//    }

    public enum Vehicles { PERSON, BICYCLE, CAR, MOTORCYCLE, BUS, TRUCK }

    /**
     * 분석용 데이터 1시간
     *
     * @author 이세현
     * @since 2024.01.23
     */
//    @Scheduled(cron = "0 5 * * * *")
    public void oneHourAnalysisData() throws Exception {
        //현재시간에서 1시간 빼기
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(1);
        String dateTimeFormat = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        //장비 목록 조회
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();

        //장비 별 1시간 데이터 찾기
        for (HashMap<String, Object> equipment : equipmentList) {
            String dateTime = dateTimeFormat + ":00:00";
            equipment.put("dateTime", dateTime);
            HashMap<String, Object> oneHourAnalysisData = new HashMap<>();
            oneHourAnalysisData.put("eqpmn_id", equipment.get("eqpmn_id"));
            oneHourAnalysisData.put("clct_dt", dateTime);
//          enum Vehicles { PERSON, BICYCLE, CAR, MOTORCYCLE, BUS, TRUCK }
            for (Vehicles vehicleType : Vehicles.values()) {
                int count = getVehicleCountByType(vehicleType, equipment);
                oneHourAnalysisData.put(vehicleType.name().toLowerCase() + "_cnt", count);
            }
//            System.out.println("oneHourAnalysisData = " + oneHourAnalysisData);
            //분석 데이터(1시간) 등록
            dataDAO.oneHourAnalysisDataInsert(oneHourAnalysisData);
        }
    }

    private int getVehicleCountByType(Vehicles vehicleType, HashMap<String, Object> equipment) throws Exception {
        equipment.put("vehicleType", vehicleType.name());
        return dataDAO.oneHourVehicleCount(equipment);
    }

    /**
     * 일별 분석 데이터
     *
     * @author 이세현
     * @since 2024.01.25
     */
//    @Scheduled(cron = "0 59 23 * * *")
    public void dailyAnalysisDataInsert() throws Exception {
//        LocalDate localDate = LocalDate.now().minusDays(1);
        LocalDate localDate = LocalDate.now();
        String dateFormat = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("dateFormat = " + dateFormat);
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();

        for (HashMap<String, Object> equipment : equipmentList) {
            HashMap<String, Object> dailyAnalysisData = new HashMap<>();
            dailyAnalysisData.put("eqpmn_id", equipment.get("eqpmn_id"));
            dailyAnalysisData.put("clct_dt", dateFormat);

            for (Vehicles vehicleType : Vehicles.values()) {
                int dailyCount = getOneDayVehicleCountByType(vehicleType, equipment, dateFormat);
                dailyAnalysisData.put(vehicleType.name().toLowerCase() + "_cnt", dailyCount);
            }
//            System.out.println("dailyAnalysisData = " + dailyAnalysisData);
            dataDAO.oneDayAnalysisDataInsert(dailyAnalysisData);
        }
    }

    private int getOneDayVehicleCountByType(Vehicles vehicleType, HashMap<String, Object> equipment, String date) throws Exception {
        equipment.put("vehicleType", vehicleType.name());
        equipment.put("date", date);
        return dataDAO.oneDayVehicleCount(equipment);
    }

    /**
     * 월별 분석 데이터
     *
     * @author 이세현
     * @since 2024.01.23
     */
//    @Scheduled(cron = "0 0 0 1 * *")
    public void monthlyAnalysisDataInsert() throws Exception {
//        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        YearMonth lastMonth = YearMonth.now();
        String monthFormat = lastMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        System.out.println("monthFormat = " + monthFormat);
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();

        for (HashMap<String, Object> equipment : equipmentList) {
            HashMap<String, Object> monthlyAnalysisData = new HashMap<>();
            monthlyAnalysisData.put("eqpmn_id", equipment.get("eqpmn_id"));
            monthlyAnalysisData.put("clct_dt", monthFormat);

            for (Vehicles vehicleType : Vehicles.values()) {
                int monthlyCount = getMonthlyVehicleCountByType(vehicleType, equipment, monthFormat);
                monthlyAnalysisData.put(vehicleType.name().toLowerCase() + "_cnt", monthlyCount);
            }
//            System.out.println("monthlyAnalysisData = " + monthlyAnalysisData);
            dataDAO.monthlyAnalysisDataInsert(monthlyAnalysisData);
        }
    }

    private int getMonthlyVehicleCountByType(Vehicles vehicleType, HashMap<String, Object> equipment, String month) throws Exception {
        equipment.put("vehicleType", vehicleType.name());
        equipment.put("month", month);
        return dataDAO.monthlyVehicleCount(equipment);
    }

    /**
     * 년도별 분석 데이터
     *
     * @author 이세현
     * @since 2024.01.25
     */
//    @Scheduled(cron = "0 0 0 1 1 *")
    public void yearlyAnalysisDataInsert() throws Exception {
//        int lastYear = Year.now().minusYears(1).getValue();
        int lastYear = Year.now().getValue();
        System.out.println("lastYear = " + lastYear);
        List<HashMap<String, Object>> equipmentList = dataDAO.equipmentList();

        for (HashMap<String, Object> equipment : equipmentList) {
            HashMap<String, Object> yearlyAnalysisData = new HashMap<>();
            yearlyAnalysisData.put("eqpmn_id", equipment.get("eqpmn_id"));
            yearlyAnalysisData.put("clct_dt", lastYear);

            for (Vehicles vehicleType : Vehicles.values()){
                int yearlyCount = getYearlyVehicleCountByType(vehicleType, equipment, lastYear);
                yearlyAnalysisData.put(vehicleType.name().toLowerCase() + "_cnt", yearlyCount);
            }
//            System.out.println("yearlyAnalysisData = " + yearlyAnalysisData);
            dataDAO.yearlyAnalysisDataInsert(yearlyAnalysisData);
        }
    }

    private int getYearlyVehicleCountByType(Vehicles vehicleType, HashMap<String, Object> equipment, int year) throws Exception {
        equipment.put("vehicleType", vehicleType.name());
        equipment.put("year", year);
        return dataDAO.yearlyVehicleCount(equipment);
    }



}
