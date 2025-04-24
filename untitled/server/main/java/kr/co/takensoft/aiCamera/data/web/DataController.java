package kr.co.takensoft.aiCamera.data.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.takensoft.aiCamera.data.domain.StatusInfoDTO;
import kr.co.takensoft.aiCamera.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.core.MultivaluedHashMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * 현장데이터 관리 DataController 입니다.
 *
 * @author 이세현
 * @since 2024.01.15
 */

@Controller
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;
    private final ObjectMapper objectMapper;

    /**
     * 현장 - 객체인식 이벤트 발생
     *
     * @author 이세현
     * @since 2024.01.15
     */

    @RequestMapping(value = "/aiCamera/data/objectDetectEvent.json", method = RequestMethod.POST)
    public void objectDetectEvent(@RequestBody HashMap<String, Object> params) throws Exception {
        ModelAndView mav = new ModelAndView("jsonView");
        dataService.ObjectDetectEventInsert(params);
    }

    /**
     * 현장 - 현황정보 등록
     *
     * @author 이세현
     * @since 2024.01.16
     */
//1번
//    @RequestMapping(value = "/aiCamera/data/statusInfo.json", method = RequestMethod.POST)
//    public void statusInfomation(@RequestBody HashMap<String, Object> params) throws Exception {
//        System.out.println("params = " + params);
//        ModelAndView mav = new ModelAndView("jsonView");
//        dataService.statusInformation(params);
//    }
//2번
//    @RequestMapping(value = "/aiCamera/data/statusInfo.json", method = RequestMethod.POST)
//    public void statusInfomation(@RequestBody StatusInfoDTO statusInfoDTO) throws Exception {
//        System.out.println("DataController.statusInfomation is called");
//        System.out.println("statusInfoDTO = " + statusInfoDTO);
//    }
//3번
//    @RequestMapping(value = "/aiCamera/data/statusInfo.json", method = RequestMethod.POST)
//    public void statusInfomation(@RequestBody HashMap<String ,Object> params) throws Exception {
//        System.out.println("DataController.statusInfomation is called");
//        System.out.println("statusInfoDTO = " + params);
//    }

//4번 임시(나중에 DTO로 수정할 것)
    @PostMapping(value = "/aiCamera/data/statusInfo.json")
    public void statusInfomation(HttpEntity httpEntity) throws Exception {
        try {
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            String messageBody = String.valueOf(httpEntity.getBody());
            StatusInfoDTO statusInfoDTO = objectMapper.readValue(messageBody, StatusInfoDTO.class);
            System.out.println("statusInfoDTO = " + statusInfoDTO);
            HashMap<String, Object> params = new HashMap<>();
            params.put("eqpmn_id",statusInfoDTO.getEqpmn_id());
            params.put("cpu_usage",statusInfoDTO.getCpu_usage());
            params.put("get_mem_usage",statusInfoDTO.getGet_mem_usage());
            params.put("datetime",statusInfoDTO.getDatetime());
            dataService.statusInformation(params);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
