package common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 서영석
 * @since 2021.12.06
 *
 * 회원 정보 VO입니다.
 */
public class User extends Org {

	/**
	 * 유니크 아이디
	 */
	private String userId;

	/**
	 * 로그인 아이디
	 */
	private String loginId;

	/**
	 * 패스워드
	 */
	private String password;

	/**
	 * 회원명
	 */
	private String name;

	/**
	 * 생년월일
	 */
	private String birthDate;

	/**
	 * 휴대전화
	 */
	private String mobile;

	/**
	 * 이메일
	 */
	private String email;

	/**
	 * 성별
	 */
	private String gender;

	/**
	 * 주소
	 */
	private String address;

	/**
	 * 상세주소
	 */
	private String detailAddress;

	/**
	 * 회원가입일시
	 */
	private String registrationDatetime;

	/**
	 * 탈퇴일시
	 */
	private String withdrawDatetime;

	/**
	 * 탈퇴여부
	 */
	private boolean isWithdraw;

	/**
	 * 로그인 실패 횟수
	 */
	private int loginFailedCount;

	/**
	 * 로그인 잠금 여부
	 */
	private boolean isAccountLock;


	/**
	 * 회원의 권한's
	 */
	private List<String> userAuthotities = new ArrayList<String>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getRegistrationDatetime() {
		return registrationDatetime;
	}

	public void setRegistrationDatetime(String registrationDatetime) {
		this.registrationDatetime = registrationDatetime;
	}

	public String getWithdrawDatetime() {
		return withdrawDatetime;
	}

	public void setWithdrawDatetime(String withdrawDatetime) {
		this.withdrawDatetime = withdrawDatetime;
	}

	public boolean getIsWithdraw() {
		return isWithdraw;
	}

	public void setIsWithdraw(boolean withdraw) {
		isWithdraw = withdraw;
	}

	public int getLoginFailedCount() {
		return loginFailedCount;
	}

	public void setLoginFailedCount(int loginFailedCount) {
		this.loginFailedCount = loginFailedCount;
	}

	public boolean getIsAccountLock() {
		return isAccountLock;
	}

	public void setIsAccountLock(boolean accountLock) {
		isAccountLock = accountLock;
	}

	public List<String> getUserAuthotities() {
		return userAuthotities;
	}

	public void setUserAuthotities(List<String> userAuthotities) {
		this.userAuthotities = userAuthotities;
	}
}
