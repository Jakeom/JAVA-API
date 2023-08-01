package com.softpuzzle.angkor.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorKhmerCode implements ErrorCode{
	
	SUCCESS                          (0000, "បានបញ្ចប់",""),

	//공통코드 에러
	EXIST_GROUP_CODE_NAME			 (0100, "common code error","이미 사용중인 그룹코드입니다."),

	//System 에러
	NULL_PARAMETER                (1001, "system error","null-parameter"),

	INVALID_PARAMETER                (1002, "system error","invalid-parameter"),
	RESOURCE_NOT_FOUND               (1003, "system error","리소스가 존재 하지 않습니다"),

	INTERNAL_SERVER_ERROR            (1004, "system error","내부 서버에러"),
	UNI_API_SERVER_ERROR            (1005, "system error","통합인증서버 API 호출 에러"),
	MAIL_TRY_TEN_OVER            (1006, "Verification attempts exceeded","You have exceeded the maximum number of verification attempts. Please try again later."),
	SEND_API_SERVER_ERROR            (1007, "system error","Sendbird API 호출 에러"),
	WRONG_PHONE_NUMBER               (1008, "phone error","잘못된 국가코드 전화번호입니다."),
	MAIL_TIME_OVER               (1009, "លេខកូដផ្ទៀងផ្ទាត់ដែលអ្នកបានបញ្ចូលបានផុតកំណត់ហើយ។ សូមស្នើសុំលេខកូដថ្មី រួចព្យាយាមម្តងទៀត។",""),
	MAIL_24HOURS_FORBID              (1010, "Verification attempts exceeded","You have exceeded the maximum number of verification attempts. Please try again 24hour later."),
	LOCATION_NOT_FOUND              (1011, "location error","나의 위치정보가 없습니다."),
	PROFILE_SAME_ID_ERROR              (1012, "profile error","상대방의 ID와 내 ID가 같습니다."),
	IS_USED_PHONENUMBER              (1013, "phone error","이미 사용중인 휴대폰번호입니다."),
	HEADER_NOT_FOUND                (1014, "header error","null-header"),
	HEADER_NOT_VALID_UUID                (1015, "header error","invalid-uuid"),
	IS_DAU_YESTERDAY                (1016, "dau error","해당 날짜는 이미 dau 수집처리 되었습니다."),
	INVALID_PUBLIC_KEY                    (1017, "user error","publicKey가 등록되지 않은 사용자가 있습니다."),
	IS_USED_USERID            (1018, "The ID you entered is already in use. Please enter a different ID",""),
	QR_SCAN_NO_FOUND            (1019, "រកមិនឃើញអ្នកប្រើប្រាស់ជាមួយលេខកូដ QR នេះទេ។ សូមពិនិត្យម្តងទៀត ហើយព្យាយាមម្តងទៀត។",""),
	FILE_SIZE_OVER            (1020, "You can attach up to 5 files, 30 MB each.",""),
	FILE_FORMAT_ERROR            (1021, "You can attach up jpg, jpeg, png, mp4, mov",""),
	QUESTION_SET_SEVER_ERROR            (1004, "សំណួររបស់អ្នកមិនអាចដាក់ស្នើបានទេ ដោយសារម៉ាស៊ីនមេមានបញ្ហា។ យើងសូមអភ័យទោសចំពោះភាពរអាក់រអួល។ សូមព្យាយាមម្តងទៀតនៅពេលក្រោយ។",""),
	SEARCH_UNAVAILABLE            (1022, "អ្នកប្រើប្រាស់បានជ្រើសរើសរក្សាទុកគណនីរបស់ពួកគេជាឯកជនភាព។អ្នកមិនអាចបន្ថែម ឬស្វែងរកពួកគេបានដោយស្វ័យប្រវត្តិជាមួយលេខទូរស័ព្ទរបស់ពួកគេបានឡើយ។ សូមសាកល្បងវិធីសាស្រ្តផ្សេងទៀត។",""),

	//login
	INVALID_USER                     (2001, "login error","존재하지 않는 사용자 입니다."),
	INVALID_PASSWORD                 (2002, "login error","패스워드가 일치 하지 않습니다."),
	LOGIN_TRY_OVER                   (2003, "login error","로그인 수행시도 횟수가 초과 했습니다. 5분루 접속하세요."),
	SMS_TRY_OVER                     (2004, "SMS error","SMS 인증번호 수신 서비스는 하루 최대 5회 제한입니다."),
	ALREADY_USED_EMAIL               (2005, "email error","이미 등록된 이메일이 있습니다."),
	INVALID_ID                     (2006, "អុប, លេខសម្គាល់នេះហាក់ដូចជាមិនមានទេ។ សូមពិនិត្យមើលការចូលដំណើរការរបស់អ្នក ហើយព្យាយាមម្តងទៀត។.",""),
	SUSPEND_USER                     (2007, "This ID has been suspended. If you think this is a mistake, please contact our support team.",""),

	//api error
	API_400100						(400100, "system error",	"authUserAdd redis error"),
	API_400101						(400101, "system error",	"authUserAdd redis HMSET error"),
	API_400102						(400102, "system error",	"authUserAdd redis SET error"),
	API_400103						(400103, "system error",	"UserModify redis error"),
	API_400104						(400104, "system error",	"ReissueAuthKey redis error"),
	API_400105						(400105, "system error",	"RedisUserCheckByPassword redis error"),
	API_400106						(400106, "system error",	"RedisUserCheckByAuthKey redis error"),
	API_400107						(400107, "system error",	"RedisUserLogout redis error"),
	API_400108						(400108, "system error",	"UserDelete redis error"),
	API_400109						(400109, "system error",	"authUserModify redis HMSet error"),
	API_400110						(400110, "system error",	"getRedisReadWrite redis read-write error"),
	API_400111						(400111, "system error",	"getRedisReadWrite redis write error, read work"),
	API_400112						(400112, "system error",	"UserPasswordUpdate redis error"),
	API_400113						(400113, "system error",	"authUserAdd redis EXPIRE error"),
	API_400114						(400114, "system error",	"authUserAdd redis HSET error"),
	API_400115						(400115, "system error",	"authUserModify redis DEL error"),
	API_400116						(400116, "system error",	"authUserModify redis SET error"),
	API_400117						(400117, "system error",	"usedUserClear redis HDEL error"),
	API_400118						(400118, "system error",	"UserDelete redis DELerror"),
	API_400119						(400119, "system error",	"MakePhoneCertNumber HMSET error"),
	API_400120						(400120, "system error",	"Redis HGET error"),
	API_400121						(400121, "system error",	"Redis HSET error"),
	API_400122						(400122, "system error",	"Redis HMSET error"),
	API_400123						(400123, "system error",	"Redis Transaction error"),
	API_400124						(400124, "system error",	"Redis Transaction HSET error"),
	API_400125						(400125, "system error",	"Redis HGETALL error"),
	API_400200						(400200, "system error",	"UserAdd mysql error"),
	API_400201						(400201, "system error",	"UserModify mysql error"),
	API_400202						(400202, "system error",	"UserInfo mysql error"),
	API_400203						(400203, "system error",	"UserDelete mysql error"),
	API_400204						(400204, "system error",	"UserPasswordUpdate mySQL error"),
	API_400205						(400205, "system error",	"usedUserClear mysql error"),
	API_400206						(400206, "system error",	"mySQL UPDATE error"),
	API_400207						(400207, "system error",	"mySQL SELECT error"),
	API_400210						(400210, "system error",	"mySQL Transaction error"),
	API_400211						(400211, "system error",	"mySQL Transaction UPDATE error"),
	API_400301						(400301, "blocked user", ""),
	API_400302						(400302, "withdrawal user", ""),
	API_400303						(400303, "you are already a registered user", ""),
	API_400304						(400304, "លេខសម្គាល់ដែលអ្នកបានបញ្ចូលគឺត្រូវបានប្រើប្រាស់រួចហើយ។ សូមបញ្ចូលលេខសម្គាល់ផ្សេងទៀត", ""),
	API_400305						(400305, "user id not exist", ""),
	API_400311						(400311, "uuid already exists", ""),
	API_400312						(400312, "uuid not registered", ""),
	API_400401						(400401, "grant type error", ""),
	API_400402						(400402, "request data error", ""),
	API_400403						(400403, "no phone number and email data.", ""),
	API_400411						(400411, "you are not subscribed as a member", ""),
	API_400412						(400412, "user not found",	""),
	API_400413						(400413, "user not found",	"DB not found"),
	API_400414						(400414, "registered phone number or emails do not match",	""),
	API_400415						(400415, "អាសយដ្ឋានអ៊ីមែលមិនត្រឹមត្រូវ",	""),
	API_400416						(400416, "registered phone number do not match",	"registered phone number not found or does not match"),
	API_400417						(400417, "last access time read error", "registered device info do not match, SMS certification is required"),
	API_400418						(400418, "សូមបញ្ចូលអាសយដ្ឋានអ៊ីមែលត្រឹមត្រូវ", ""),
	API_400421						(400421, "header infomation not found", ""),
	API_400422						(400422, "header authorization error", ""),
	API_400441						(400441, "password mismatch", ""),
	API_400442						(400442, "angkorId mismatch", ""),
	API_400443						(400443, "authkey mismatch", ""),
	API_400444						(400444, "phone number mismatch", ""),
	API_400501						(400501, "applicatioin not found",	"redis application data not found"),
	API_400502						(400502, "application setting error",	"redis application setting data error"),
	API_400601						(400601, "phone authentication request exceeded error", ""),
	API_400602						(400602, "error count exceeded", ""),
	API_400603						(400603, "លេខកូដមិនត្រឹមត្រូវ។ សូមពិនិត្យមើលអ៊ីមែលរបស់អ្នក ហើយព្យាយាមម្តងទៀត។", ""),
	API_400603_SMS					(400603, "លេខកូដបញ្ជាក់ទទួលស្គាល់មិនត្រឹមត្រូវ។ សូមពិនិត្យមើលសារ SMS របស់អ្នកដើម្បីស្វែងរកលេខកូដដែលត្រឹមត្រូវ", ""),
	API_400604						(400604, "authentication number entry timeout", ""),
	API_400611						(400611, "system error",	"sms session error"),
	API_400612						(400612, "system error",	"sms publish error"),
	API_400613						(400613, "លេខទូរស័ព្ទមិនត្រឹមត្រូវ។ សូមបញ្ចូលលេខទូរស័ព្ទត្រឹមត្រូវ។",	""),
	API_400701						(400701, "network error",	"sendbird network error"),
	API_400702						(400702, "create angkorid error",	"sendbird create user error"),

	;
	private final int errorcode;
	private final String gmessage;
	private final String dmessage;
	

}
