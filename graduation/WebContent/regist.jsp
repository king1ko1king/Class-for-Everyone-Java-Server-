<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/H-ui.css">
<title>用户注册</title>
<script type="text/javascript" src="javascript/jquery.js"></script>
<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.slim.js"></script>
<script type="text/javascript">
	var smsVeriCode;//短信验证码
	var InterValObj; //timer变量，控制时间 
	var count = 10; //间隔函数，1秒执行 
	var curCount;//当前剩余秒数 
	$(document).ready(function() {
		$("#username").focus(function() {
			$("#usernameError").html("输入正确的11位手机号码").removeClass();
		});
		$("#nickname").focus(function() {
			$("#nicknameError").html("推荐使用“姓名+学号”").removeClass();
		});
		$("#password").focus(function() {
			$("#passwordError").html("6~16个字符，区分大小写").removeClass();
		});
		$("#password").blur(function() {
			checkPassword();
		});
		$("#confirmPassword").focus(function() {
			$("#confirmPasswordError").html("再次输入密码，与前面保持一致").removeClass();
		});
		$("#confirmPassword").blur(function() {
			checkConfirmPassword();
		});
		$("#email").focus(function() {
			$("#emailError").html("忘记密码时，可通过此邮箱找回密码并修改密码").removeClass();
		});
		$("#email").blur(function() {
			checkEmail();
		});
		$("#veriCode").focus(function() {
			$("#veriCodeError").html("输入收到的短信验证码").removeClass();
		});

		$("#getVeriCode").click(function() {
			sendMessage();
		});
		
	});
	
	//倒计时程序，发送验证码
	function sendMessage() { 
		curCount = count; 
		//设置button效果，开始计时 
		if (checkUsernameFormat()) {
			$("#getVeriCode").attr("disabled", "true"); 
			$("#getVeriCode").val(curCount + "秒后可重新发送"); 
			InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次 　  
			//请求后台发送验证码 TODO
			getVeriCode();
		}
	} 
		  
	//timer处理函数 
	function SetRemainTime() { 
		if (curCount == 0) {         
			window.clearInterval(InterValObj);//停止计时器 
			$("#getVeriCode").removeAttr("disabled");//启用按钮 
			$("#getVeriCode").html("重新发送验证码"); 
		} else { 
			curCount--; 
			$("#getVeriCode").html(curCount + "秒后可重新发送"); 
		} 
	}
	
	//异步获取验证码
	function getVeriCode() {
		//1.创建异步交互对象
		var xhr = createXmlHttp();
		var username = $("#username").val();
		//2.设置监听
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					//$("#nicknameError").html(xhr.responseText);
					smsVeriCode = xhr.responseText;
					smsVeriCode = smsVeriCode.trim();
					alert(smsVeriCode);
				}
			}
		}
		//3.打开链接, 传三个参数，第一个参数指定请求方式，第二个参数指定路径，第三个true表示异步方式
		//添加new Date().getTime()是为了避免浏览器的缓存
		xhr.open("GET","${pageContext.request.contextPath}/registUser_getSmsVeriCode.action?time="
				+new Date().getTime()+"&username="+username,true);
		//4.发送
		xhr.send(null);
	}
	
	//校验密码格式是否正确
	function checkPassword() {
		var password = $("#password").val();
		if (password == null || password == '') {
			$("#passwordError").html("密码不能为空").addClass("c-error");
			return false;
		} else {
			if (password.length < 6 || password.length > 16) {
				$("#passwordError").html("密码必须由6~16个字符组成，区分大小写").addClass("c-error");
				return false;
			}
		}
		return true;
	}
	
	//校验两次密码是否一致
	function checkConfirmPassword() {
		var password = $("#password").val();
		var confirmPassword = $("#confirmPassword").val();
		if (password != confirmPassword) {
			$("#confirmPasswordError").html("两次密码不一致,请重新输入").addClass("c-error");
			return false;
		}
	}
	
	//校验邮箱格式是否正确，正确则发送异步请求校验是否可用
	function checkEmail() {
		var email = $("#email").val();
		if (email == null || email == '') {
			$("#emailError").html("邮箱不能为空").addClass("c-error");
			return false;
		} else {
			var emailreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			if (!emailreg.test(email)) {
				$("#emailError").html("请输入正确的邮箱").addClass("c-error");
				return false;
			}
		}
		//1.创建异步交互对象
		var xhr = createXmlHttp();
		//2.设置监听
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				if (xhr.status == 200) {
					$("#emailError").html(xhr.responseText);
				}
			}
		}
		//3.打开链接, 传三个参数，第一个参数指定请求方式，第二个参数指定路径，第三个true表示异步方式
		//添加new Date().getTime()是为了避免浏览器的缓存
		xhr.open("GET","${pageContext.request.contextPath}/registUser_findByEmail.action?time="
				+new Date().getTime()+"&email="+email,true);
		//4.发送
		xhr.send(null);
	}
	
	//校验输入的手机号码格式是否正确
	function checkUsernameFormat() {
		//获得用户名文本框的值
		var username = $("#username").val();
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
		if (username == null || username == '') {
			$("#usernameError").html("手机号码不能为空").addClass("c-error");
			return false;
		} else {
			var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
			if (!reg.test(username)) {
				$("#usernameError").html("请输入正确的11位手机号码").addClass("c-error");
				return false;
			}
		}
		return true;
	}
	
	//发送异步请求校验手机号码是否可用
	function checkUsername() {
		//获得用户名文本框的值
		var username = $("#username").val();
		if (checkUsernameFormat()) {
			//1.创建异步交互对象
			var xhr = createXmlHttp();
			//2.设置监听
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					if (xhr.status == 200) {
						$("#usernameError").html(xhr.responseText);
					}
				}
			}
			//3.打开链接, 传三个参数，第一个参数指定请求方式，第二个参数指定路径，第三个true表示异步方式
			//添加new Date().getTime()是为了避免浏览器的缓存
			xhr.open("GET","${pageContext.request.contextPath}/registUser_findByName.action?time="
					+new Date().getTime()+"&username="+username,true);
			//4.发送
			xhr.send(null);
		}
	}
	
	//校验昵称格式是否正确，正确则发送异步请求校验昵称是否可用
	function checkNickname() {
		var nickname = $("#nickname").val();
		if (nickname == null || nickname == '') {
			$("#nicknameError").html("昵称不能为空").addClass("c-error");
			return false;
		}
		//1.创建异步交互对象
		var xhr = createXmlHttp();
		//2.设置监听
		xhr.onreadystatechange = function() {
			//判断对象的状态是否交互完成，等于4则表示交互完成
			if (xhr.readyState == 4) {
				//判断http的交互是否成功，等于200表示交互成功
				if (xhr.status == 200) {
					$("#nicknameError").html(xhr.responseText);
				}
			}
		}
		//3.打开链接, 传三个参数，第一个参数指定请求方式，第二个参数指定路径，第三个true表示异步方式
		//添加new Date().getTime()是为了避免浏览器的缓存
		xhr.open("GET","${pageContext.request.contextPath}/registUser_findByNickname.action?time="
				+new Date().getTime()+"&nickname="+nickname,true);
		//4.发送
		xhr.send(null);
	}
	
	//创建XMLHttpRequest对象，用来发送异步请求
	function createXmlHttp() {
		var xmlHttp;
		try { //Firefox, Opera8.0+, Safari
			xmlHttp = new XMLHttpRequest();
		} catch(e) {
			try { //Internet Explorer，new ActiveXObject("Msxml2.XMLHTTP");设置对高版本IE浏览器的支持
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch(e) {
				try {
					//new ActiveXObject("Microsoft.XMLHTTP");设置对低版本IE浏览器的支持
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch(e) {}
			}
		}
		return xmlHttp;
	}
	
	//表单验证方法
	function check() {
		var veriCode = $("#veriCode").val().trim();

		//验证昵称
		checkNickname();
		//验证密码
		checkPassword();
		//验证确认密码
		checkConfirmPassword();
		//验证邮箱
		checkEmail();
		//验证手机号码
		checkUsername();
		//验证短信验证码
		if (veriCode == null || veriCode == '') {
			$("#veriCodeError").html("验证码不能为空").addClass("c-error");
			return false;
		} else {
			if (smsVeriCode != veriCode) {
				$("#veriCodeError").html("验证码不正确").addClass("c-error");
				return false;
			}
		}
		return true;
	}
	
</script>

</head>
<body>
	<center>
		<h2>用户注册</h2>
		<a id="sendEmail" class="hide"></a>
		<form action="" method="post" id="sendEmail" class="hide">
			<input type="submit" id="sendButton" value="Submit"/>
		</form>
		<div class="codeView docs-example">
			<form action="${pageContext.request.contextPath }/registUser_addUser" method="post" id="registerForm" onsubmit="return check();">
				<table style="border-collapse:separate; border-spacing:0px 10px;">
					
					<thead>
						<tr>
							<td  width="15%"></td>
							<td></td>
							<td width="10%"></td>
						</tr>
					</thead>
					
					<tr>
						<td class="text-r f-18">昵称：</td>
						<td colspan="2"><input id="nickname" type="text" name="nickname" placeholder="请输入昵称" class="input-text radius size-L" onblur="checkNickname()"></input></td>
					</tr>
					<tr>
						<td></td>
						<td id="nicknameError">推荐使用“姓名+教师手机号”</td>
					</tr>
				
					<tr>
						<td class="text-r f-18">密码：</td>
						<td colspan="2"><input id="password" type="password" name="password" placeholder="密码" class="input-text radius size-L"/></td>
					</tr>
					<tr>
						<td></td>
						<td id="passwordError">6~16个字符，区分大小写</td>
					</tr>
					
					<tr>
						<td class="text-r f-18">确认密码：</td>
						<td colspan="2"><input id="confirmPassword" type="password" name="confirmPassword" placeholder="再一次输入密码" class="input-text radius size-L"/></td>
					</tr>
					<tr>
						<td></td>
						<td id="confirmPasswordError">再次输入密码，与前面保持一致</td>
					</tr>
					
					<tr>
						<td class="text-r f-18">邮箱：</td>
						<td colspan="2"><input id="email" type="text" name="email" placeholder="@" class="input-text radius size-L"/></td>
					</tr>
					<tr>
						<td></td>
						<td id="emailError">忘记密码时，可通过此邮箱找回密码并修改密码</td>
					</tr>
					
					<tr>
						<td class="text-r f-18">手机号码：</td>
						<td colspan="2"><input id="username" type="text" name="name" placeholder="请输入用户名" class="input-text radius size-L" onblur="checkUsername()"></input></td>
					</tr>
					<tr>
						<td></td>
						<td id="usernameError">输入正确的11位手机号码</td>
					</tr>
					
					<tr>
						<td class="text-r f-18">短信验证码：</td>
						<td><input id="veriCode" type="text" name="veriCode" placeholder="输入验证码" class="input-text radius size-L"/></td>
						<td><button id="getVeriCode" type="button" class="btn btn-primary-outline radius size-L">免费获取短信验证码</button></td>
					</tr>
					<tr>
						<td></td>
						<td colspan="2" id="veriCodeError">输入正确的短信验证码</td>
					</tr>
				
					<tr class="text-l">
						<td></td>
						<td colspan="2"><button type="submit" class="btn btn-primary-outline radius size-L">立即注册</button></td>
					</tr>
	
				</table>
			</form>
		</div>
	</center>

</body>
</html>