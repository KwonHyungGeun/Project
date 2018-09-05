<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="resources/css/boardlist.css">
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
			console.log(d)
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
			hs = d.result;
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
		$("#writebtn").on("click", function(){
			$.ajax({
				type : "post",
				url : "/sessioncheck"
			}).done(function(data) {
				var d = JSON.parse(data);
				console.log(d.result);
				if(d.result == 1){
					location.href="boardwrite"
				}
				else if(d.result == 0){
					alert("로그인이 필요합니다.")
					location.href="login"
				}
			});
		});
		function maketable(data) {
    		$("#tablerow").empty();
			for(var i = 0; i < data.length; i++) {
			   var html = "";
	           html += '<tr class="tablerowcontent">';
	           html += '<td class="no">' + data[i].boardNo + '</td>';
	           html += '<td class="title">' + data[i].title + '</td>';
	           html += '<td class="writer">' + data[i].id + '</td>';
	           html += '<td class="writedate">' + data[i].writeDate + '</td>';
	           html += '<td class="count">' + data[i].viewCount + '</td>';
	           html += '</tr>';
	           $("#tablerow").append(html); 
			}
			$(".tablerowcontent").on("click", function(){
				var index = $(".tablerowcontent").index(this);
				var boardNo = $(this).find(".no").text();
 				$.ajax({
 					type : "post",
 					url : "/viewcountup",
 					data : {"boardNo" : boardNo}
 				}).done(function(data) {
 					location.href="/boarddetail?boardNo=" + boardNo;
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
			start = index * 10;
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
// 				$(".pagebtn").css("background-color", "white");
// 				$(".pagebtn").css("color", "black");
// 				start = index * 10;
// 				callData();
// 			});
    	}
		$.ajax({
			type : "post",
			url : "/boardlistcount"
		}).done(function(data) {
			var d = JSON.parse(data)
			var list = d.list;
			var count = list[0];
			var cnt = count.cnt;
			var pagecnt = Math.ceil(cnt /10);
			makepage(pagecnt);
			callData();
		});
		
		var start = 0;
		function callData(){
			$.ajax({
				type : "post",
				url : "/boardlist",
				data : {
						"start":start,
						}
			}).done(function(data) {
				var d = JSON.parse(data)
				var list = d.list;
				maketable(list);
				
			});
		}
// 		function search(){
// 			$.ajax({
//     			type : "post",
//     			url : "/search",
//     			data : {"keyword" : $("#inputstyle").val(),
//     					"menu" : $("#select").val(),
//     					}
//     		}).done(function(data) {
//     			var d = JSON.parse(data)
//     			var list = d.list;
//     			console.log(list);
//     			makepage();
//     			maketable(list);
//     		});
// 		}
// 		$("#searchbtn").click(function(event){
//             search();
//     	  });
         });
</script>
	<title> Soccer Chart Boardlist </title>
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
            <div id="boardlist">
                <div id="boardlistcontainer">
                    <h1>자유 게시판</h1>
                    <table id="freebaordList">
                    <thead>
                        <tr id="column">
                            <th class="no">No</th>
                            <th class="title">Title</th>
                            <th class="writer">Writer</th>
                            <th class="writedate">WriteDate</th>
                            <th class="count">Count</th>
                        </tr>    
                    </thead>
                    <tbody id="tablerow">
                        
                    </tbody>
                    </table>
                    <div id="page">
                    	<div id="pagediv">
                    		
                    	</div>
                    </div>
                </div>
            </div>
            <div id="tableoption">
<!--                 <div id="searchdiv"> -->
<!--                         <select id="select"> -->
<!--                             <option value="title">Title</option> -->
<!--                             <option value="content">Contents</option> -->
<!--                             <option value="writer">Writer</option> -->
<!--                         </select> -->
<!--                         <input id="inputstyle" type="text" name="search" placeholder="검색어를 입력하세요."> -->
<!--                         <input id="searchbtn"  type="submit" value="Search"> -->
<!--                     </div> --> 
                    <div id="write">
                        <button id="writebtn">글 쓰기</button>
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