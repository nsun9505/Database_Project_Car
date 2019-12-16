/**
 * 
 */
var contextPath="/Phase4";
function check(re, what, message){
	if(re.test(what.value)){
		return true;
	}
	alert(message);
	what.value="";
	what.focus();
	return false;
}

function goodBye(){
	alert("진심 탈퇴???");
}

function cancel(){
	alert("취소 누름~")
}

function validCheck(){
	var name = document.getElementById("user_name");
	var re = /^[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/;
	
	if(check(re, name, "Name : FirstName(영어2~10) LastName(영어2~10), FirstName과 Lastname 사이 공백 필요") == false){
		return;
	}
	
	var pw = document.getElementById("user_pw");
	var pwcheck = document.getElementById("user_pwCheck");
	if(pw.value != pwcheck.value){
		pw.value = "";
		pwcheck.value ="";
		alert("password와 password confirm이 다름!!!");
		return;
	}
	if(check(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,15}$/, pw, "Password : 비밀번호는 특수문자[@$!#%*], 영어 대소문자, 숫자를 조합하여 8~15자리 입니다.") == false){
		return;
	}
	
	if(check(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,15}$/, pwcheck, "Password confirm : 비밀번호는 특수문자[@$!#%*], 영어 대소문자, 숫자를 조합하여 8~15자리 입니다.") == false){
		return;
	}
	
	var phone = document.getElementById("user_phone");
	if(check(/^01(?:0|1[6-9])-(?:\d{3}|\d{4})-\d{4}$/, phone, "핸드폰 번호는 XXX-XXX-XXXX 또는 XXX-XXXX-XXXX 형식입니다.") == false){
		return;
	}
	
	var address = document.getElementById("user_address");;
	if(address.value.length != 0 && check(/^[a-zA-Z0-9\s]{0,100}$/, address, "주소(최대 100자)에는 특수문자 및 공백을 입력할 수 없습니다.") == false){
		return;
	}
	modifyForm.submit();
}