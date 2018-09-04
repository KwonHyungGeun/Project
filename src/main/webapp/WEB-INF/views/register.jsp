<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/css/register.css">
<link rel='shortcut icon' href='resources/css/img/icon.png'>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$.ajax({
			type : "post",
			url : "/sessioncheck"
		}).done(function(data) {
			var d = JSON.parse(data);
			var id = d.id;
			console.log(d.result);
			if(d.result == 1){
				$("#bl").css("display", "none");
				$("#br").css("display", "none");
				$("#blogout").css("display", "block");
				$("#logininfo").text("Welcome " + id + " !!!");
				$("#logininfo").show();
			}
			else if(d.result == 0){
				$("#bl").css("display", "block");
				$("#br").css("display", "block");
				$("#blogout").css("display", "none");
				$("#logininfo").hide();
			}
		});
		
		
		
		$("#mainlogoimg").on("click", function(){
			location.href="main";
		});
		$("#bin").on("click", function(){
			location.href="introduce";
		});
		
		$("#bb").on("click", function(){
			location.href="boardlist";
		});
		$("#bl").on("click", function(){
			location.href="login";
		});
		$("#br").on("click", function(){
			location.href="register";
		});
		$("#facebook").on("click", function(){
			location.href="https://www.facebook.com";
		});
		$("#instagram").on("click", function(){
			location.href="https://www.instagram.com/";
		});
		$("#twitter").on("click", function(){
			location.href="https://twitter.com/";
		});
		$("#goengland").on("click", function(){
			location.href="/england";
		});
		$("#goitaly").on("click", function(){
			location.href="/italy";
		});
		$("#gospain").on("click", function(){
			location.href="/spain";
		});
		$("#gofrance").on("click", function(){
			location.href="/france";
		});
		
		
		
		
		
		$("#idbox").on("change", function() {
			
			if ($("#idbox").val().length >= 8){
				$.ajax({
					type : "post",
					url : "/checkid",
					data : {
						"id":$("#idbox").val()
					}
				}).done(function(data) {
					var d = JSON.parse(data);
					if(d.status == 0) {
						alert("중복된 아이디가 없습니다.");
						$("#idbox").css("background-color", "#1ab188");
					}else if (d.status == 1) {
						alert("아이디가 중복 되었습니다");
						$("#idbox").css("background-color", "#fe4d4d");
						$("#idbox").val("");
					}
				});
			}else if($("#idbox").val().length < 8) {
				alert("아이디는 8글자 이상이어야 합니다.");
				$("#idbox").val("");
			}
			
		});
		
		$("#idbox").keyup(function(event){
			$(this).val($(this).val().replace(/[^a-zA-Z0-9]*$/g,""));
		}); 
		$("#namebox").keyup(function(event){
			$(this).val($(this).val().replace(/[^가-힣]*$/g,""));
		});

		$("#phonebox").keyup(function(event){
			$(this).val($(this).val().replace(/[^0-9-]/g,""));
			if($("#phonebox").val().length == 3){
				this.value = this.value + "-"; 
			}else if ($("#phonebox").val().length == 8) {
				this.value = this.value + "-";
			}else if ($("#phonebox").val().length > 13 ) {
				this.value = this.value.slice(0,-1);
			}
		    if(event.keyCode == 8 && $("#phonebox").val().length == 4){
		    	this.value = this.value.slice(0,-1);
		    }else if (event.keyCode == 8 && $("#phonebox").val().length == 9){
		    	this.value = this.value.slice(0,-1);
		    }
		    if(event.keyCode == 189 && $("#phonebox").val().length != 4){
		    	this.value = this.value.slice(0,-1);
		    }else if (event.keyCode == 189 && $("#phonebox").val().length != 9){
		    	this.value = this.value.slice(0,-1);
		    }else if (event.keyCode == 109 && $("#phonebox").val().length != 4){
		    	this.value = this.value.slice(0,-1);
		    }else if (event.keyCode == 109 && $("#phonebox").val().length != 9){
		    	this.value = this.value.slice(0,-1);
		    }
		});
		    
		
		$("#pwdbox").on("change", function() {
			if($("#pwdbox").val().length < 10) {
				alert("비밀번호는 최소 10자리부터 최대 15자리 입니다.");
				$("#pwdbox").val("");
			}else if($("#pwdbox").val().length > 15) {
				alert("비밀번호는 최소 10자리부터 최대 15자리 입니다.");
				this.value = this.value.slice(0,15);
			}
		});
		$("#checkpwdbox").on("change", function() {
			if($("#checkpwdbox").val() == $("#pwdbox").val()){
				$("#checkpwdbox").css("background-color", "#1ab188");
				$("#pwdbox").css("background-color", "#1ab188");
			}else if ($("#checkpwdbox").val() != $("#pwdbox").val()) {
				$("#checkpwdbox").css("background-color", "#fe4d4d");
				$("#pwdbox").css("background-color", "#fe4d4d");
				alert("비밀번호가 일치하지 않습니다.");
				$("#checkpwdbox").val("");
				$("#pwdbox").val("");
			}
		});
		

		
		
		$("#registerbtn").on("click", function(){
			if($("#pwdbox").val() == $("#checkpwdbox").val() && $("#idbox").val() != false && $("#namebox").val() != false && $("#phonebox").val() != false){
				$.ajax({
					type:"post",
					url:"/register",
					data: {
						"id":$("#idbox").val(),
						"pw":$("#pwdbox").val(),
						"name":$("#namebox").val(),
						"phone":$("#phonebox").val(),
						}
					}).done(function(data){
						var d = JSON.parse(data);
						console.log(d.status);
						if(d.status == 1){
							alert("회원가입을 완료했습니다.");
							location.href="login";
						}else if(d.status == 0) {
							alert("중복된 아이디 입니다.");
							$("#idbox").val("");
							$("#pwdbox").val("");
							$("#checkpwdbox").val("");
						}
					});
							 
			}else if ($("#pwdbox").val() != $("#checkpwdbox").val()){
				alert("비밀번호가 일치하지 않습니다");
				$("#pwdbox").val("");
				$("#checkpwdbox").val("");
			}else if ($("#idbox").val() == false || $("#namebox").val() == false || $("#phonebox").val() == false) {
				alert("ID, PW, NAME, PHONE 을 모두 입력해주세요.")		
			}
		});
		$("#blogout").on("click", function(){
			location.href="/logoutcomplete";
		});
	});
	function check() {
		if ($("#pwdbox").val() == $("#checkpwdbox").val() && $("#idbox").val() != false && $("#namebox").val() != false && $("#phonebox").val().length == 13){
			$("#registerbtn").attr("disabled", false);
			$("#registerbtn").css("background-color", "#1ab188");
		}
	}
</script>
	<title> Soccer Chart Register </title>
</head>

	<body>
        <div id="edge">
            <header id="mainheader">
                <div id="mainlogo">
                	<img id="mainlogoimg" src="resources/css/img/main.png">
                </div>
                <div id="menu">
                	<div id="logininfo" style="color:white; float:right; clear:both; margin-right:300px;  font-weight:bold; font-size:15px; margin-top:10px;"></div>
                    <div id="menudiv">
                        <div class= "menubtn" id="bin">
                            <div class="menuicon"><img id="inimg" src="resources/css/img/introduce.png"></div>
                            <div class="menutext">Introduce</div>
                        </div>
                        <div class= "menubtn dropdown" id="bc">
                            <div class="menuicon"><img id="bcimg" src="resources/css/img/chart.png"></div>
                            <div class="menutext">Analysis</div>
                            <div class="dropdown-content">
						    	<div id="goengland" class="dropdowndiv animation"><img class="drowimg" src="resources/css/img/englandlogo.png">England</div>
						    	<div id="goitaly" class="dropdowndiv animation"><img class="drowimg" src="resources/css/img/italylogo.png">Italy</div>
						    	<div id="gospain" class="dropdowndiv animation"><img class="drowimg" src="resources/css/img/spainlogo.png">Spain</div>
						    	<div id="gofrance" class="dropdowndiv animation"><img class="drowimg" src="resources/css/img/francelogo.png">France</div>
						    </div>
                        </div>
                        <div class= "menubtn" id="bb">
                            <div class="menuicon"><img id="bbimg" src="resources/css/img/freeboard.png"></div>
                            <div class="menutext">Free Board</div>
                        </div>
                        <div class= "menubtn" id="bl">
                            <div class="menuicon"><img id="blimg" src="resources/css/img/login.png"></div>
                            <div class="menutext">Login</div>
                        </div>
                        <div class= "menubtn" id="br">
                            <div class="menuicon"><img id="brimg" src="resources/css/img/register.png"></div>
                            <div class="menutext">Register</div>
                        </div>
                        <div class= "menubtn" id="blogout">
                            <div class="menuicon"><img id="blogoutimg" src="resources/css/img/logout.png"></div>
                            <div class="menutext">Logout</div>
                        </div>
                    </div>
                </div>
            </header>
            <div id="register">
                <div id="registercontainer">
                    <h1>Register</h1>
                    <div id="regsterdiv">
                        <form id="userinfo">
                            <div id="idtext">아이디  </div>
                            <br>
                            <input id="idbox" type="text" method="post" placeholder="   아이디(8자리 ~ 15자리)" type="password" maxlength="15" onkeyup="check()"><br><br>
                            <div id="pwdtext">비밀번호  </div>
                            <br>
                            <input id="pwdbox"  method="post" placeholder="   비밀번호(10자리 ~ 15자리)" type="password" maxlength="15" onkeyup="check()"><br><br>
                            <div id="checkpwdtext">비밀번호 재확인  </div>
                            <br>
                            <input id="checkpwdbox"  method="post" placeholder="   비밀번호 확인(10자리 ~ 15자리)" type="password" maxlength="15" onkeyup="check()"><br><br>
                            <div id="nametext">이름  </div>
                            <br>
                            <input id="namebox" type="text" method="post" placeholder="   이름(한글만 가능합니다.)" maxlength="5" onkeyup="check()"><br><br>
                            <div id="phonetext">휴대폰번호  </div>
                            <br>
                            <input id="phonebox" type="text" method="post" placeholder="   휴대폰번호 000-0000-0000" maxlength="13" onkeyup="check()"><br><br>
                        </form>
                        <div id="buttondiv">
                            <button id="registerbtn" disabled="disabled">Register</button>
                        </div>
                    </div>
                    
                </div>
            </div>
            <footer id="footer">
                <div id="contentfooter"><img id="fc" src="resources/css/img/fc.png"> </div>
                <div id="snsfooter">
                    <div id="footericoncontainer">
                        <div class="footericonbtn">
                            <div id="facebook" class="footericon"><img src="resources/css/img/facebook.png"></div>
                        </div>
                        <div class="footericonbtn">
                            <div id="instagram" class="footericon"><img src="resources/css/img/instagram.png"></div>
                        </div>
                        <div class="footericonbtn">
                            <div id="twitter" class="footericon"><img src="resources/css/img/twitter.png"></div>
                        </div>
                    </div>
                    
                </div>
            
            </footer>
        </div>
        
        
        
    </body>
</html>