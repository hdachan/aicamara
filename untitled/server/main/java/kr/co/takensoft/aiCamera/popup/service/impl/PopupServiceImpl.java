package kr.co.takensoft.aiCamera.popup.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.util.AuthUtil;
import common.util.FileUtil;
import common.util.idgn.IdGenerate;
import common.vo.CheckMessage;
import common.vo.CommonFile;
import common.vo.SystemCode;
import kr.co.takensoft.aiCamera.common.PaginationSupport;
import kr.co.takensoft.aiCamera.commonFile.dao.CommonFileDAO;
import kr.co.takensoft.aiCamera.popup.dao.PopupDAO;
import kr.co.takensoft.aiCamera.popup.service.PopupService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *  Popup 관련 서비스 로직 입니다.
 *
 * @author 김성훈
 * @since 2023.11.08
 */

@Service
public class PopupServiceImpl implements PopupService {

    private final PopupDAO popupDAO;
    private final IdGenerate idGenerate;
    private final CommonFileDAO commonFileDAO;

    public PopupServiceImpl(PopupDAO popupDAO, IdGenerate idGenerate, CommonFileDAO commonFileDAO) {
        this.popupDAO = popupDAO;
        this.idGenerate = idGenerate;
        this.commonFileDAO = commonFileDAO;
    }

    /**
     * 팝업 등록(첨부파일 포함)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @Override
    public int popupInsert(MultipartFile file, HashMap<String, Object> params) throws Exception {
        int result=0;
        int file_sn = 1;
        //fileID set
        String fileID = idGenerate.getNextId("cmmn_file");
        HashMap<String, Object> fileParams = new HashMap<>();
        fileParams.put("file_id", fileID);

        CommonFile fileUpload = new CommonFile();
        FileUtil fileUtil = new FileUtil();
        commonFileDAO.commonFileInsert(fileParams);

        try {
            //프로젝트 내부 경로
            String absolutePath = SystemCode.FileUploadPath.POPUP_ABSOLUTE_PATH.getOSFileUploadPath();
            //확장자를 제거한 원본 파일명
            String realFileName = file.getOriginalFilename();
            //원본 파일의 확장자
            String fileExtension = fileUtil.getFileExtension(file.getOriginalFilename());
            //원본 파일의 파일크기(byte단위)
            long fileSize = file.getSize();//원본 파일의 파일크기(byte단위)
            //새로 생성될 파일명 설정
            String saveFileName = UUID.randomUUID().toString() + "_" + FilenameUtils.getBaseName(file.getOriginalFilename());
            //파일 크기
            fileParams.put("file_size", fileSize);
            //실제 파일 이름
            fileParams.put("real_file_nm", realFileName);
            //파일 확장자
            fileParams.put("file_extn_nm",fileExtension);
            //설정 파일 이름
            fileParams.put("file_nm",saveFileName);
            //파일 실제 경로
            fileParams.put("file_path",absolutePath);
            //파일 번호
            fileParams.put("file_sn", file_sn);
            //파일 타입
            fileParams.put("file_ty", "popup");
            //파일 DB 저장
            result =+ commonFileDAO.fileInsert(fileParams);
            //파일을 절대 경로에 저장(시작)
            //저장 경로
            fileUpload.setFileAbsolutePath(absolutePath);
            //원본 파일 이름
            fileUpload.setFileOriginName(realFileName);
            //저장 파일 이름
            fileUpload.setFileMaskName(saveFileName);
            fileUpload.setFileExtension(fileExtension);
            fileUpload.setFileSize(fileSize);
            fileUpload.setFileStatus(CommonFile.FileStatus.BEFORE_INSERT);
            fileUpload.setCheckMessage(new CheckMessage(true, "파일 정보 조회 성공"));
            fileUtil.absoluteFileCreate(fileUpload, file);

        } catch (Exception e) {
            fileUpload.setCheckMessage(new CheckMessage(false, "파일 정보 조회 에러", e.getMessage()));
        }

        //시작일 문자열 -> Date
        String start_dt = params.get("start_dt").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(format.parse(start_dt).getTime());
        params.replace("start_dt",startDate);

        //종료일 문자열 -> Date
        String end_dt = params.get("end_dt").toString();
        Date endDate = new Date(format.parse(end_dt).getTime());
        params.replace("end_dt",endDate);

        //게시판ID set
        String postID = idGenerate.getNextId("popup");
        params.put("post_id", postID);
        //파일ID set
        params.put("file_id",fileID);
        //등록자ID set
        params.put("rgtr_id", AuthUtil.getLoginManagerId());
        result += popupDAO.popupInsert(params);

        return result;
    }

    /**
     * 관리자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @Override
    public List<HashMap<String, Object>> adminPopupSelectList(HashMap<String, Object> params) throws Exception {
        int currentPage = (int) params.get("currentPage");
        int perPage = (int) params.get("perPage");
        int pagingRowIndex = PaginationSupport.pagingRowIndexForPostgreSql(currentPage, perPage);

        params.put("pagingRowIndex", pagingRowIndex);

        return popupDAO.adminPopupSelectList(params);
    }

    /**
     * 관리자 팝업 목록 count 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @Override
    public int adminPopupSelectListCount(HashMap<String, Object> params) throws Exception {
        return popupDAO.adminPopupSelectListCount(params);
    }

    /**
     * 게시글 상제 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @Override
    public HashMap<String, Object> popupSelectOne (HashMap<String, Object> params) throws Exception{
        HashMap<String, Object> post = popupDAO.popupSelectOne(params);
        HashMap<String, Object> prevPopup = popupDAO.findPrevPopup(params);
        HashMap<String, Object> nextPopup = popupDAO.findNextPopup(params);
        HashMap<String, Object> result = new HashMap<>();
        result.put("post", post);
        result.put("prevPopup", prevPopup);
        result.put("nextPopup", nextPopup);

        return result;
    };

    /**
     * 파일ID로 첨부파일 찾기
     *
     * @author 이세현
     * @since 2023.11.03
     */
    public List<HashMap<String, Object>> selectFileList(HashMap<String, Object> params) throws Exception{
        List<HashMap<String, Object>> result = commonFileDAO.selectFileList(params);
        if(!result.isEmpty()) {
            if("C".equals(result.get(0).get("file_path").toString().substring(0,1))) {
                for(int i = 0; i < result.size(); i ++){
                    HashMap<String, Object> fileOne = result.get(i);

                    String path = fileOne.get("file_path").toString().substring(2);
                    fileOne.replace("file_path",path);
                }
            }
        }
        return result;
    }


    /**
     * 사용자 팝업 목록 조회
     *
     * @author 이세현
     * @since 2023.10.31
     */
    @Override
    public List<HashMap<String, Object>> userPopupList() throws Exception {
        List<HashMap<String, Object>> result = popupDAO.userPopupList();
        if (!result.isEmpty()) {
            if ("C".equals(result.get(0).get("file_path").toString().substring(0, 1))) {
                for (int i = 0; i < result.size(); i++) {
                    HashMap<String, Object> selectOne = result.get(i);

                    String path = selectOne.get("file_path").toString().substring(2);
                    selectOne.replace("file_path", path);
                }
            }
        }
        return result;
    }

    /**
     * 팝업 삭제
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupDelete (HashMap<String, Object> params) throws Exception{
        List<HashMap<String, Object>> fileList = commonFileDAO.selectFileList(params);

        commonFileDAO.commonFileDelete(params);
        commonFileDAO.fileAllDelete(params);

        if(fileList != null) {

            for( int i = 0; i < fileList.size(); i++) {
                HashMap<String, Object> deleteOne = fileList.get(i);

                String path;

                if(System.getProperty("os.name").indexOf("Windows") > -1){
                    path = deleteOne.get("file_path").toString() + "\\" +deleteOne.get("file_nm") + "." + deleteOne.get("file_extn_nm");
                } else {
                    path = deleteOne.get("file_path").toString() + "/" +deleteOne.get("file_nm") + "." + deleteOne.get("file_extn_nm");
                }

                commonFileDAO.fileDelete(deleteOne);

                FileUtil fileUtil = new FileUtil();
                fileUtil.fileRemove(path);
            }
        }

        return popupDAO.popupDelete(params);
    }

    /**
     * 팝업 수정(첨부파일X)
     *
     * @author 이세현
     * @since 2023.10.31
     */
    public int popupUpdate (HashMap<String, Object> params) throws Exception{
        params.put("mdfr_id", AuthUtil.getLoginUserId());

        //시작일 문자열 -> Date
        String start_dt = params.get("start_dt").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(format.parse(start_dt).getTime());
        params.replace("start_dt",startDate);

        //종료일 문자열 -> Date
        String end_dt = params.get("end_dt").toString();
        Date endDate = new Date(format.parse(end_dt).getTime());
        params.replace("end_dt",endDate);
        return popupDAO.popupUpdate(params);
    }

    /**
     * 팝업 수정(첨부파일 변경O)
     *
     * @author 이세현
     * @since 2023.11.06
     */
    public int popupFileUpdate(MultipartFile file, String deleteFile, HashMap<String, Object> params) throws Exception{
        int result=0;
        int file_sn = commonFileDAO.fileCount(params) + 1;
        String fileID = null;

        if( deleteFile != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> deleteOne = objectMapper.readValue(deleteFile, new TypeReference<HashMap<String, Object>>() {});

            FileUtil fileUtil = new FileUtil();

            String path;

            if(System.getProperty("os.name").indexOf("Windows") > -1){
                path = deleteOne.get("file_path").toString() + "\\" +deleteOne.get("file_nm") + "." + deleteOne.get("file_extn_nm");
            } else {
                path = deleteOne.get("file_path").toString() + "/" +deleteOne.get("file_nm") + "." + deleteOne.get("file_extn_nm");
            }

            commonFileDAO.fileDelete(deleteOne);
            fileUtil.fileRemove(path);
        }

        if (file != null) {
            CommonFile fileUpload = new CommonFile();
            FileUtil fileUtil = new FileUtil();

            HashMap<String, Object> fileParams = new HashMap<>();
            fileID = params.get("file_id").toString();
            fileParams.put("file_id", fileID);

            try {
                //프로젝트 내부 경로
                String absolutePath = common.vo.SystemCode.FileUploadPath.POPUP_ABSOLUTE_PATH.getOSFileUploadPath();
                //확장자를 제거한 원본 파일명
                String realFileName = file.getOriginalFilename();
                //원본 파일의 확장자
                String fileExtension = fileUtil.getFileExtension(file.getOriginalFilename());
                //원본 파일의 파일크기(byte단위)
                long fileSize = file.getSize();//원본 파일의 파일크기(byte단위)
                //새로 생성될 파일명 설정
                String saveFileName = UUID.randomUUID().toString() + "_" + FilenameUtils.getBaseName(file.getOriginalFilename());
                //파일 크기
                fileParams.put("file_size", fileSize);
                //실제 파일 이름
                fileParams.put("real_file_nm", realFileName);
                //파일 확장자
                fileParams.put("file_extn_nm",fileExtension);
                //설정 파일 이름
                fileParams.put("file_nm",saveFileName);
                //파일 실제 경로
                fileParams.put("file_path",absolutePath);
                //파일 번호
                fileParams.put("file_sn", file_sn);
                //파일 타입
                fileParams.put("file_ty", "popup");
                //파일 DB 저장
                result =+ commonFileDAO.fileInsert(fileParams);

                //파일을 절대 경로에 저장(시작)

                //저장 경로
                fileUpload.setFileAbsolutePath(absolutePath);
                //원본 파일 이름
                fileUpload.setFileOriginName(realFileName);
                //저장 파일 이름
                fileUpload.setFileMaskName(saveFileName);
                fileUpload.setFileExtension(fileExtension);
                fileUpload.setFileSize(fileSize);
                fileUpload.setFileStatus(CommonFile.FileStatus.BEFORE_INSERT);
                fileUpload.setCheckMessage(new CheckMessage(true, "파일 정보 조회 성공"));
                fileUtil.absoluteFileCreate(fileUpload, file);
            } catch (Exception e) {
                fileUpload.setCheckMessage(new CheckMessage(false, "파일 정보 조회 에러", e.getMessage()));
            }
        }

        //수정자ID set
        params.put("mdfr_id", AuthUtil.getLoginManagerId());
        //파일ID set

        //시작일 문자열 -> Date
        String start_dt = params.get("start_dt").toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date(format.parse(start_dt).getTime());
        params.replace("start_dt",startDate);

        //종료일 문자열 -> Date
        String end_dt = params.get("end_dt").toString();
        Date endDate = new Date(format.parse(end_dt).getTime());
        params.replace("end_dt",endDate);

        popupDAO.popupUpdate(params);
        return result;
    }


}
