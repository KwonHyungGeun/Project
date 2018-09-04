<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/css/boardupdate.css">
<link rel='shortcut icon' href='resources/css/img/icon.png'>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script type="text/javascript">
	var data={};
	$(document).ready(function(){
		var queryString = window.location.href.split('?');
    	var params = queryString[1].split('&');
    	for (var i=0; i < params.length; i++){
    		var param = params[i].split('=');
    		data[param[0]] = param[1];
    	}
    	var boardNo = data.boardNo;
    	var id = data.id;
    	console.log(id);
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
		$("#blogout").on("click", function(){
			location.href="/logoutcomplete";
		});
		$("#back").on("click", function(){
			location.href="boardlist";
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
		function boardHTML(data) {
    		var title = data.title;
    		var content = data.content;
    		$("#titlebox").val(title);
    		$("#contentbox").val(content);
    	}  
		
		$.ajax({
			type : "post",
			data : {"boardNo": boardNo},
			url : "/boarddetail"
		}).done(function(data) {
			var d = JSON.parse(data)
			var boardData = d.boardData;
			boardHTML(boardData);
		});
		$("#writebtn").on("click",function(){
			if ($("#titlebox").val() != false && $("#contentbox").val() != false){
				$.ajax({
					type:"post",
					url:"/boardupdate",
					data: {
						"boardNo" : boardNo,
						"title":$("#titlebox").val(),
						"content":$("#contentbox").val(),
						"id" : id
						}
				}).done(function(data){
					var d = JSON.parse(data)
					if(d.status == 1){
						alert("수정이 완료되었습니다.");
						location.href="boarddetail?boardNo=" + boardNo;
					}else if(d.status == 0) {
						alert("수정도중 오류가 발생했습니다.작성자 본인이 아니거나 제목, 내용을 모두 입력하셨는지 확인해 주세요.");
						location.href="boarddetail?boardNo=" + boardNo;
					}	
				});
			}else if ($("#titlebox").val() == false || $("#contentbox").val() == false) {
				alert("제목 내용을 모두 입력해주세요.")
			}
		});
	});
</script>
	<title> Soccer Chart BoardUpdate </title>
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
            <div id="boardwrite">
                <div id="boardwritecontainer">
                    <h1>Update</h1>
                    <div id="formdiv">
                        <form id="writecontet">
                            <div id="titletxt">제목</div>
                            <br>
                            <input id="titlebox" type="text" method="post" placeholder="제목을 입력하세요."  maxlength="50"><br><br>
                            <div id="contenttxt">내용</div>
                            <br>
                            <textarea id="contentbox" type="text" method="post" placeholder="내용을 입력하세요."  maxlength="2000"></textarea>
                        </form>
                        <div id="boardbtn">
                            <button id="writebtn">Update</button>
                            <button id="back">To List</button>
                        </div>
                    </div>
                    
                </div>
            </div>
            
            <footer id="footer">
                <div id="contentfooter"><img id="fc" src="resources/css/img/fc.png"></div>
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