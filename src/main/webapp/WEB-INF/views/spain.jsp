<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/spain.css">
<link rel='shortcut icon' href='resources/css/img/icon.png'>
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://www.amcharts.com/lib/3/amcharts.js" type="text/javascript"></script>
<script src="https://www.amcharts.com/lib/3/serial.js" type="text/javascript"></script>
<script src="https://www.amcharts.com/lib/3/pie.js" type="text/javascript"></script>
<script src="https://www.amcharts.com/lib/3/radar.js" type="text/javascript"></script>
<script src="resources/js/html2canvas.min.js"></script>
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
				$("#selectcomplete").show();
				$("#plzlogin").hide();
				$("#logininfo").text("Welcome " + id + " !!!");
				$("#logininfo").show();
			}
			else if(d.result == 0){
				$("#bl").css("display", "block");
				$("#br").css("display", "block");
				$("#blogout").css("display", "none");
				$("#selectcomplete").hide();
				$("#plzlogin").show();
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
		$("#chartimgdown").on("click", function(){
			location.href="/spain";
		});
		
		
		var league = "SpainTable"
		
			function teamlogo(data) {
    		$("#chart_1").empty();
		   var html = "";
		   html += '<div id="logodiv">';
           html += '<img class="clublogo" src="resources/css/img/teamlogo/' + data + '.png">';
           html += '<h1 class="clubname">' + data + '</h1>';
           html += '</div>';
           $("#chart_1").append(html); 
		}

		function makewinrate(data) {
			var played = data[0];
			var win = data[1];
			var draw = data[2];
			var lose = data[3];
			AmCharts.makeChart("chart_2",
					{
						"type": "radar",
						"categoryField": "Count",
						"colors": [
							"#67b7dc",
							"#fdd400",
							"red",
							"red",
							"red",
							"red",
							"red",
							"red",
							"red",
							"red",
							"red"
						],
						"startDuration": 2,
						"fontSize": 12,
						"theme": "dark",
						"graphs": [
							{
								"balloonText": "[[Count]] : [[value]]",
								"bullet": "round",
								"id": "AmGraph-1",
								"fillAlphas": 0.3,
								"valueField": "litres"
							}
						],
						"guides": [],
						"valueAxes": [
							{
								"axisTitleOffset": 20,
								"gridType": "circles",
								"id": "ValueAxis-1",
								"minimum": 0,
								"axisAlpha": 0.15,
								"dashLength": 3
							}
						],
						"allLabels": [],
						"balloon": {},
						"titles": [{"size": 20, "text": "총경기 & 승 & 무 & 패 Count"}],
						"dataProvider": [
							{
								"Count": "총 경기수",
								"litres": played
							},
							{
								"Count": "승",
								"litres": win
							},
							{
								"Count": "무승부",
								"litres": draw
							},
							{
								"Count": "패",
								"litres": lose
							}
						]
					}
				);
		}
		
		function maketeamlist(data) {
    		$("#teamlistrow").empty();
			for(var i = 0; i < data.length; i++) {
			   var html = "";
	           html += '<tr class="teamlist">';
	           html += '<td class="teamname">' + data[i] + '</td>';
	           html += '</tr>';
	           $("#teamlistrow").append(html); 
			}
		
			$(".teamlist").on("click", function(){
				$("#loading").css("display" , "block");
				$('#edge').on('scroll touchmove mousewheel', function(event) {
					  event.preventDefault();
					  event.stopPropagation();
					  return false;
				});
				var index = $(".teamlist").index(this);
				var teamname = $(this).find(".teamname").text();
				
				teamlogo(teamname);
				$.ajax({
					type : "post",
					url : "/analysisData",
					data : {
							"league" : league,
							"teamname" : teamname,
							}
				}).done(function(data) {
					var winrateData = data.winrate;
					var goallosegoalrateData = data.goallosegoalrate;
					var playedpergoalData = data.playedpergoal;
					var teamrankdata = data.teamrank;
					var topteamrankdata = data.topteamrank;
					
					console.log(topteamrankdata.data);
					var topchartdata = JSON.parse(topteamrankdata.data);
					call1(winrateData, teamname);
					call2(goallosegoalrateData, teamname);
					call3(playedpergoalData);
					call4(playedpergoalData);
					call5(teamrankdata);
					call6(topchartdata);
					
					
			      
					$(".container").css("display" , "block");
					$("#teamlistrow").css("display" , "none");
					$("#teamlistrowdiv h1").text("Chart");
					$("#loading").css("display" , "none");
					$('#edge').off('scroll touchmove mousewheel');
					
				});
			});
		}
		function call1(data, teamname){
			var windata = data.winresult;						
			var losedata = data.loseresult;						
			var playeddata = data.playedresult;	
			var wincount = "";
			for (var i = 0; i < windata.length; i++) {
				if (windata[i][0] == teamname){
					wincount = Number(windata[i][1]);
				}
			}
			var losecount = "";
			for (var j = 0; j < losedata.length; j++) {
				if (losedata[j][0] == teamname){
					losecount = Number(losedata[j][1]);
				}
			}
			var playedcount = "";
			for (var k = 0; k < playeddata.length; k++) {
				if (playeddata[k][0] == teamname){
					playedcount = Number(playeddata[k][1]);
				}
			}
			var winrate = data.winrateresult[0]["winrate"];
			var loserate = data.loserateresult[0]["loserate"]
			var drawrate = data.drawrateresult[0]["drawrate"]
			var drawcount = playedcount - wincount - losecount;
			var winratedata = [];
			winratedata.push(playedcount);
			winratedata.push(wincount);
			winratedata.push(drawcount);
			winratedata.push(losecount);
			makewinrate(winratedata);
			AmCharts.makeChart("chart_4",
					{
						"type": "pie",
						"balloonText": "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
						"radius": 130,
						"fontSize" : 15,
						"colors": [
							"skyblue",
							"orange",
							"pink",
							"#FCD202",
							"#F8FF01",
							"#B0DE09",
							"#04D215",
							"#0D8ECF",
							"#0D52D1",
							"#2A0CD0",
							"#8A0CCF",
							"#CD0D74",
							"#754DEB",
							"#DDDDDD",
							"#999999",
							"#333333",
							"#000000",
							"#57032A",
							"#CA9726",
							"#990000",
							"#4B0C25"
						],
						"titleField": "category",
						"valueField": "column-1",
						"theme" : "chalk",
						"allLabels": [],
						"balloon": {},
						"legend": {
							"enabled": true,
							"align": "center",
							"markerType": "circle"
						},
						"titles": [{"size": 20, "text": "승률 & 무승부율 & 패배율"}],
						"dataProvider": [
							{
								"category": "승률",
								"column-1": winrate
							},
							{
								"category": "무승부율",
								"column-1": drawrate
							},
							{
								"category": "패배율",
								"column-1": loserate
							}
						]
					}
				);
		}
		
		function call2(data, teamname){
			var goalcount = data.goalcountresult[0]["goalcount"];
			var losegoalcount = data.losegoalcountresult[0]["losegoalcount"];
			var gmglcount = data.gmlgcountresult[0]["gmlgcount"];
			AmCharts.makeChart("chart_6",
				{
					"type": "radar",
					"categoryField": "Count",
					"colors": [
						"#67b7dc",
						"#fdd400",
						"red",
						"red",
						"red",
						"red",
						"red",
						"red",
						"red",
						"red",
						"red"
					],
					"startDuration": 2,
					"fontSize": 12,
					"theme": "dark",
					"graphs": [
						{
							"balloonText": "[[Count]] : [[value]]",
							"bullet": "round",
							"id": "AmGraph-1",
							"fillAlphas": 0.3,
							"valueField": "litres"
						}
					],
					"guides": [],
					"valueAxes": [
						{
							"axisTitleOffset": 20,
							"gridType": "circles",
							"id": "ValueAxis-1",
							"minimum": 0,
							"axisAlpha": 0.15,
							"dashLength": 3
						}
					],
					"allLabels": [],
					"balloon": {},
					"titles": [{"size": 20, "text": "골 수 & 실점 수 & 득실차"}],
					"dataProvider": [
						{
							"Count": "골 수",
							"litres": goalcount
						},
						{
							"Count": "실점 수",
							"litres": losegoalcount
						},
						{
							"Count": "득실차",
							"litres": gmglcount
						}
					]
				}
			);


		}
		function call3(data){
			var totalplay = parseFloat(data["countlist"][0]);
			var totalgoal = parseFloat(data["countlist"][1]);
			var goalperplayed = parseFloat(data["goalperplayedresult"][0]["goalperplayedrate"]);
			google.charts.load('current', {'packages':['table']});
		    google.charts.setOnLoadCallback(drawTable);

		      function drawTable() {
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', '카테고리');
		        data.addColumn('number', '수치');
		        data.addRows([
		          ['총 경기수',  {v: totalplay, f: String(totalplay) + " 경기"}],
		          ['총 득점',   {v: totalgoal,   f: String(totalgoal) + " 골"}],
		          ['한 경기당 득점 수', {v: goalperplayed, f: String(goalperplayed) + " 골"}]
		        ]);
		        
		        var cssClassNames = {
	                    'headerRow': 'cssHeaderRow',
	                    'tableRow': 'cssTableRow',
	                    'oddTableRow': 'cssOddTableRow',
	                    'selectedTableRow': 'cssSelectedTableRow',
	                    'hoverTableRow': 'cssHoverTableRow',
	                    'headerCell': 'cssHeaderCell',
	                    'tableCell': 'cssTableCell',
	                    'rowNumberCell': 'cssRowNumberCell'
	                };
	                
	                var options = {
	                    showRowNumber: false,
	                    width : "100%",
	                    height : "100%",
	                    cssClassNames: cssClassNames
	                };
		        var table = new google.visualization.Table(document.getElementById('chart_3'));
		        table.draw(data, options);
		      }
		}
		function call4(data){
			var totalplay = parseFloat(data["countlist"][0]);
			var totallosegoal = parseFloat(data["countlist"][2]);
			var losegoalperplayed = parseFloat(data["losegoalperplayedresult"][0]["losegoalperplayedrate"]);
			google.charts.load('current', {'packages':['table']});
		    google.charts.setOnLoadCallback(drawTable);

		      function drawTable() {
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', '카테고리');
		        data.addColumn('number', '수치');
		        data.addRows([
		          ['총 경기수',  {v: totalplay, f: String(totalplay) + " 경기"}],
		          ['총 실점 수',   {v: totallosegoal,   f: String(totallosegoal) + " 골"}],
		          ['한 경기당 실점 수', {v: losegoalperplayed, f: String(losegoalperplayed) + " 골"}]
		        ]);
		        
		        var cssClassNames = {
	                    'headerRow': 'cssHeaderRow',
	                    'tableRow': 'cssTableRow',
	                    'oddTableRow': 'cssOddTableRow',
	                    'selectedTableRow': 'cssSelectedTableRow',
	                    'hoverTableRow': 'cssHoverTableRow',
	                    'headerCell': 'cssHeaderCell',
	                    'tableCell': 'cssTableCell',
	                    'rowNumberCell': 'cssRowNumberCell'
	                };
	                
	                var options = {
	                    showRowNumber: false,
	                    width : "100%",
	                    height : "100%",
	                    cssClassNames: cssClassNames
	                };
		        var table = new google.visualization.Table(document.getElementById('chart_5'));
		        table.draw(data, options);
		      }
		}
		function call5(data){
			var first = data["first"]["first"];
			var second = data["second"]["second"];
			var stay = data["stay"]["stay"];
			var degrade = data["degrade"]["degrade"];
			if(first == undefined) {
				first = 0;
			}
			if(second == undefined) {
				second = 0;
			}
			if(stay == undefined) {
				stay = 0;
			}
			if(degrade == undefined) {
				degrade = 0;
			}
			google.charts.load('current', {'packages':['table']});
		    google.charts.setOnLoadCallback(drawTable);

		      function drawTable() {
		        var data = new google.visualization.DataTable();
		        data.addColumn('string', '우승/준우승/잔류/강등 횟수');
		        data.addColumn('number', '횟수');
		        data.addRows([
		          ['우승',  {v: first, f: String(first) + " 번"}],
		          ['준우승',   {v: second,   f: String(second) + " 번"}],
		          ['잔류', {v: stay, f: String(stay) + " 번"}],
		          ['강등', {v: degrade, f: String(degrade) + " 번"}]
		        ]);
		        
		        var cssClassNames = {
	                    'headerRow': 'cssHeaderRow',
	                    'tableRow': 'cssTableRow',
	                    'oddTableRow': 'cssOddTableRow',
	                    'selectedTableRow': 'cssSelectedTableRow',
	                    'hoverTableRow': 'cssHoverTableRow',
	                    'headerCell': 'cssHeaderCell',
	                    'tableCell': 'cssTableCell',
	                    'rowNumberCell': 'cssRowNumberCell'
	                };
	                
	                var options = {
	                    showRowNumber: false,
	                    width : "100%",
	                    height : "100%",
	                    cssClassNames: cssClassNames
	                };
		        var table = new google.visualization.Table(document.getElementById('chart_7'));
		        table.draw(data, options);
		      }
		}
		function call6(data){
			AmCharts.makeChart("chart_8",
					{
						"type": "serial",
						"categoryField": "category",
						"startDuration": 1,
						"categoryAxis": {
							"gridPosition": "start"
						},
						"trendLines": [],
						"graphs": [
							{
								"balloonText": "[[category]]:[[value]]",
								"fillAlphas": 1,
								"id": "AmGraph-1",
								"title": "우승 횟수",
								"type": "column",
								"valueField": "column-1"
							}
							
						],
						"guides": [],
						"valueAxes": [
							{
								"id": "ValueAxis-1",
								"title": "Champion Count"
							}
						],
						"allLabels": [],
						"balloon": {},
						"legend": {
							"enabled": true,
							"useGraphSettings": true
						},
						"titles": [
							{
								"id": "Title-1",
								"size": 20,
								"text": "Top Champion Team"
							}
						],
						"dataProvider": data
					}
				);
		}
		
		$("#selectcomplete").on("click", function(){
			$.ajax({
				type : "post",
				url : "/teamlist",
				data : {
						"league" : league,
						}
			}).done(function(data) {
				var d = JSON.parse(data);
				var teamlist = d.teamlist;
				$("#englandcontent").css("display" , "none");
				$("#year").css("display" , "none");
				$("#teamlistrowdiv").css("display" , "block");
				maketeamlist(teamlist);
			});
		});
	});
</script>
	<title> Soccer Chart - Spain </title>
</head>

	<body>
        <div id="edge">
        <div id="loading">
					<img src="resources/css/img/1.svg">
					<h4>분석중입니다.</h4>
		</div>
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
           
            
            <div id="englandcontent">
                <div id="englandlogo">
                    <h1>Spain La Liga 1</h1>
                    <div id="logo">
                        <img src="resources/css/img/spainlogo.png">
                    </div>
                    <div id="explain">
                        <p>    Spain La Liga입니다.</p>
                        <p>    1. 저희 사이트는 0001시즌 ~ 1718시즌 (총 18개시즌)의 데이터를 제공합니다.</p>
                        <p>    2. 팀 선택하기 버튼을 클릭 시 팀 리스트가 보여집니다.</p>
                        <p>    3. 팀 선택시 선택한 해당팀의 18년치 데이터로 차트가 생성됩니다.</p>
                        <p>    4. 단 2부리그로 강등된 시즌에 대한 데이터는 없습니다.</p>
                        <p>    5. 로그인 했을시에만 분석이 가능합니다.</p>
                    </div>
                    
                </div>
            </div>
            <div id="year">
                    <div id="selectbtndiv">
                        <button id="selectcomplete">팀 선택하기</button>   
                        <h3 id="plzlogin"><a id="logina" href="login">로그인</a>이 필요합니다.</h3>          
                    </div>
                </div>
                <div id="teamlistrowdiv">
                    	<h1>Select Team</h1>
                    	<table id="teamlistrow">
                    	
                    	</table>
                </div>
                
			<div class="container">
				  <div class="row" id="chart_body">
				    <div class="col-sm-6 h200" id="chart_1">chart 1</div>
				    <div class="col-sm-6 h200" id="chart_2">chart 2</div>
				    <div class="col-sm-6 h200" id="chart_3">chart 3</div>
				    <div class="col-sm-6 h200" id="chart_4">chart 4</div>
				    <div class="col-sm-6 h200" id="chart_5">chart 5</div>
				    <div class="col-sm-6 h200" id="chart_6">chart 6</div>
				    <div class="col-sm-6 h200" id="chart_7">chart 7</div>
				    <div class="col-sm-6 h200" id="chart_8">chart 8</div>
				  </div>
				  <button id="chartimgdown">뒤로가기</button>
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
