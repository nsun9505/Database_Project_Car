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

function test(){
	alert("test123");
	loginForm.action= contextPath+"/account/login.do";
	loginForm.submit();
}

function idDuplicateCheck(){
	var id = document.getElementById("user_id");
	var re = /^[a-zA-Z]{1}[a-zA-Z0-9]{4,15}$/;
	alert("test");
	if(re.test(id.value)){
		joinForm.action = contextPath+"/account/idDupCheck.do";
		alert(joinForm.action);
		joinForm.submit();
		return true;
	} else {
		alert("아이디는 5~15자리의 숫자, 영어 대소문자의 조합으로만 가능하며 시작 문자는 영어입니다.")
		id.value="";
		id.focus();
		return false;
	}
}

function checkInfo(){
	var fname = document.getElementById("fname");
	var re = /^[a-zA-Z]{2,10}/;
	
	if(check(re, fname, "First Name : 4 ~ 10자리(영어)") == false){
		return;
	}
	
	var lname = document.getElementById("lname");
	if(check(re, lname, "Last Name : 4 ~ 10자리(영어)") == false){
		return;
	}
	
	var pw = document.getElementById("user_pw");
	re = /^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,15}$/;
	if(check(re, pw, "Password : 비밀번호는 특수문자[@$!#%*], 영어 대소문자, 숫자를 조합하여 8~15자리 입니다.") == false){
		return;
	}
}