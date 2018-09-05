<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/css/boarddetail.css">
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
		
		$.ajax({
			type : "post",
			url : "/sessioncheck"
		}).done(function(data) {
			var d = JSON.parse(data);
			var id = d.id;
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
		var boardNo = data.boardNo;
		function boardHTML(data) {
    		var boardNo = data.boardNo;
    		var title = data.title;
    		var id = data.id;
    		var writeDate = data.writeDate;
    		var content = data.content;
    		var viewCount = data.viewCount;
    		$("#boardNo").text(boardNo);
    		$("#title").text(title);
    		$("#id").text(id);
    		$("#writeDate").text(writeDate);
    		$("#viewCount").text(viewCount);
    		$("#viewcontent").text(content);
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
		$("#update").on("click", function(){
			var id = $("#id").text();
			$.ajax({
				type : "post",
				data : {"boardNo": boardNo,
						"id" : id,
						},
				url : "/updatebtn"
			}).done(function(data) {
				var d = JSON.parse(data);
				console.log(d);
				if(d.result == 1) {
					location.href="/boardupdate?boardNo=" + boardNo + "&id=" + id;
				}else if(d.result == 0) {
					alert("작성자 본인이 아닙니다.");
				}else{
					alert("로그인이 필요합니다.");
					location.href="login"
				}
				
			});
		});
		$("#delete").on("click", function(){
			$.ajax({
				type : "post",
				data : {"boardNo": boardNo,
						"id" : 	$("#id").text(),
						},
				url : "/boarddelete"
			}).done(function(data) {
				var d = JSON.parse(data);
				console.log(d);
				if(d.result == 1) {
					if(d.status == 1) {
						alert("삭제가 완료되었습니다.");
						location.href="boardlist";
					}else {
						alert("삭제도중 오류가 발생했습니다.");
					}
					
				}else if(d.result == 0) {
					alert("작성자 본인이 아닙니다.");
				}else{
					alert("로그인이 필요합니다.");
					location.href="login"
				}
			});
		});
		function makecommenttable(data) {
    		$(".commenttview").empty();
			for(var i = 0; i < data.length; i++) {
			   var html = "";
	           html += '<tr class="commentresource">';
	           html += '<td class="comment_no">' + data[i].commentNo + '</td>';
	           html += '<td class="commentcontent">' + data[i].content + '</td>';
	           html += '<td class="commentid">' + data[i].id + '</td>';
	           html += '<td class="commentdate">' + data[i].writeDate + '</td>';
	           html += '<td class="commentbtn"><button class="commentdelbtn">삭제</button></td>';
	           html += '</tr>';
	           $(".commenttview").append(html); 
	           $(".comment_no").hide();
	           $(".comment_num").hide();
			}
			$(".commentdelbtn").on("click", function(){
	 			var index = $(".commentdelbtn").index(this);
	 			var id = $(".commentid").eq(index).text();
	 			var commentNo = $(".comment_no").eq(index).text();
	 			$.ajax({
 					type : "post",
 					url : "/commentdelete",
 					data : {"commentNo" : commentNo,
 							"id" : id,
 							}
 				}).done(function(data) {
 					var d = JSON.parse(data);
 					if(d.result == 0) {
 						alert("로그인이 필요합니다");
 						location.href="login";
 					}else if(d.result == 1) {
 						alert("삭제 완료하였습니다.");
 						location.href="/boarddetail?boardNo=" + boardNo;
 					}else {
 						alert("작성자 본인이 아닙니다.");
 						location.href="/boarddetail?boardNo=" + boardNo;
 					}
 				});
			});
		}
		$(document).off().on("click", ".pagebtn", function(){  
			var index = $(".pagebtn").index(this);
			console.log(index);
			console.log(this);
			$(".pagebtn").css("background-color", "black");
			$(".pagebtn").css("color", "white");
			
			$(".pagebtn").eq(index).css("background-color", "white");
			$(".pagebtn").eq(index).css("color", "black");
			start = index * 5;
			callData();
		});
		function makepage(data) {
    		$("#pagediv").empty();
			for(var i = 0; i < data; i++) {
			   var html = "";
	           html += '<button class="pagebtn">' + (i + 1) + '</tr>';
	           $("#pagediv").append(html); 
			}
			$(".pagebtn").eq(0).css("background-color", "white");
			$(".pagebtn").eq(0).css("color", "black"); 
// 			$(".pagebtn").off().on("click", function(){
// 				var index = $(".pagebtn").index(this);
// 				start = index * 5;
// 				callData();
// 			});
    	}
		
		
		$.ajax({
			type : "post",
			data : {"boardNo" : boardNo},
			url : "/commentlistcount"
		}).done(function(data) {
			var d = JSON.parse(data)
			var list = d.list;
			var count = list[0];
			var cnt = count.cnt;
			var pagecnt = Math.ceil(cnt /5);
			makepage(pagecnt);
			callData();
		});
		var start = 0;
		function callData(){
			$.ajax({
				type : "post",
				data : {"boardNo": boardNo,
						"start":start,
						},
				url : "/commentlist"
			}).done(function(data) {
				var d = JSON.parse(data);
				var list = d.list;
				makecommenttable(list);
			});
			
		}
		$("#commentcomplete").on("click", function() {
			event.preventDefault();
			if ($("#commentcontent").val() != false) {
				$.ajax({
					type : "post",
					data : {"boardNo": boardNo,
							"content": $("#commentcontent").val(),
							},
					url : "/commentwrite"
				}).done(function(data) {
					var d = JSON.parse(data);
					console.log(d);
					if(d.status == 0){
						alert("로그인이 필요합니다.");
						location.href="login";
					}else if(d.status == 1) {
						alert("댓글 작성을 완료하였습니다.");
						location.href="/boarddetail?boardNo=" + boardNo;
					}
				});
			}else if ($("#commentcontent").val() == false) {
				alert("댓글 내용을 입력해주세요.")
			}
		});
	});
</script>
	<title> Title </title>
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
            <div id="boarddetail">
                <div id="boardcontainer">
                    <h1>Board Detail</h1>
                    <table id="detailtable">
                        <thead id="thead">
                            <tr id="detailinfo">
                                <th class="view_num">No</th>
                                <th class="view_title">Title</th>
                                <th class="view_name">Writer</th>
                                <th class="view_date">Wrtie Date</th>
                                <th class="view_count">Count</th>
                            </tr>
                        </thead>
                        <tbody class="boardview">
                        <tr id="detailresource">
                            <td id="boardNo"></td>
                            <td id="title"></td>
                            <td id="id"></td>
                            <td id="writeDate"></td>
                            <td id="viewCount"></td>
                        </tr>
                        <tr id="content">
                           <td id="contenttitle">내용</td>
                            <td id="viewcontent" colspan="4"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="detailbtn">
                    <button id="back">To List</button>
                    <button id="update">Update</button>
                    <button id="delete">Delete</button>
                </div>
                <h1>Comment Reply</h1>
                <div id="comment">
                    <form id="writecomment">
                        <input id="commentcontent" type="text" method="post" placeholder="댓글을 입력하세요." maxlength="120">
                        <input id="commentcomplete" type="submit" value="등록">
                    </form>
                </div>
                
                <div id="commentlist">
                    <h1>Comment List</h1>
                    
                    <table id="commenttable">
                        <thead id="commentthead">
                            <tr id="commentinfo">
                            	<th class="comment_num">No</th>
                                <th class="comment_content">Content</th>
                                <th class="comment_name">Writer</th>
                                <th class="comment_date">Wrtie Date</th>
                                <th class="comment_btn">Button</th>
                            </tr>
                        </thead>
                        <tbody class="commenttview">
                        
                        </tbody>
                    </table>
                    <div id="page">
                    	<div id="pagediv">
                    		
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
