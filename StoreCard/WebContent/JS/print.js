/*$(function(){
	var aa=ipreturn();
	alert(aa)
});
*/
//获得后台反馈回来的ip
function ipreturn()
{
	var ip=null;
		$.ajax({
			type : "get",
			url : "../StoreCard/IPServlet",
			async:false,
	        //data: "name="+name+"&password="+password,
			success : function(data) {	
				ip=data;
			}
		
		});
	return ip;
}

function getEmployeeInfo(EmpID){
	var EmployeeInfo;
	//var ip=ipreturn()
	$.ajax({
		  type:"post",
		  url:"../StoreCard/rest/info/getEmployee",
		  data: '{"name":"'+EmpID+'","flg":"1"}',
		  contentType:'application/json',
		  datatype: 'json',
		  async : false,
	      success:function(data){
	    	  EmployeeInfo=data;
	      },
	      error:function(){
	    	  alert("写真作成を失敗しました");
		      }
		    });
	return EmployeeInfo;
}

function intoCard(EmployeeCode,EmployeeName1,EmployeeName2,EmployeeName_syllabary1,EmployeeName_syllabary2,StoreName,Company_code){
	var card="";
	card="<div id='card_"+EmployeeCode+"' class='card'>"+
			"<div id='store' class='store'>" +
				"<div style='margin:0px;padding:0px;font-size:34px;font-weight:400;'>"+StoreName+"</div>" +
			"</div>"+
			"<div class='Ename'>" +
			"<table cellpadding='0' cellspacing='0'>"+
				"<tr>"+
					"<td style='vertical-align:middle';>" +
						"<div class='Ename1' style='font-size:27px;font-weight:600;'>" +
							""+EmployeeName_syllabary1+"" +
						"</div>" +
					"</td>"+
					"<td style='vertical-align:middle';>" +
						"<div class='Ename1' style='margin-left:20px;font-size:27px;font-weight:600;'>" +
							""+EmployeeName_syllabary2+"" +
						"</div>" +
					"</td>"+
				"</tr>"+
				"<tr>"+
					"<td style='vertical-align:middle';>" +
						"<div class='Ename1' style='font-size:50px;font-weight:800;'>" +
							""+EmployeeName1+"" +
						"</div>" +
					"</td>"+
					"<td style='vertical-align:middle';>" +
						"<div class='Ename1' style='margin-left:20px;font-size:50px;font-weight:800;'>" +
							""+EmployeeName2+"" +
						"</div>" +
					"</td>"+
				"</tr>"+
				"</table>"+
			"</div>"+
		 	"<div id='bcTarget_"+EmployeeCode+"' class='barcodeImg'></div>"+
		 "</div>";
	$("#header").append(card);
	if(Company_code=="10017")
	{
		 $("#card_"+EmployeeCode).css("background-image","url(image/triwel.gif)")
	}
}

function dataMack(EmpID){
	var data=getEmployeeInfo(EmpID);
	for(var i=1;i<data.length+1;i++){
		if(i==1){
			$("#header").append("<div class='Middle2'></div>");
			$("#header").append("<div class='Middle'></div>");
		}
		else if(i%2==1){
			$("#header").append("<div class='Middle'></div>");
		}
		else{
		}
		intoCard(data[i-1].EmployeeCode,data[i-1].EmployeeName1,
			data[i-1].EmployeeName2,data[i-1].EmployeeName_syllabary1,
			data[i-1].EmployeeName_syllabary2,data[i-1].StoreName,data[i-1].Company_code);
		
		if(i%2==0&&i%10!=0){
			$("#header").append("<div class='Middle3'></div>");
			$("#header").append("<div class='Middle1'></div>");
			$("#header").append("<div class='Middle4'></div>");
		}
		else if(i%10==0){
			$("#header").append("<div class='Middle3'></div>");
			$("#header").append("<div class='Middle2'></div>");
			$("#header").append("<div class='Middle2'></div>");
		}
		else{
			$("#header").append("<div class='Middle'></div>");
			$("#header").append("<div class='Middle5'></div>");
			
		}
		if(i==data.length&&i%2!=0){
			$("#header").append("<div class='Middle2'></div>");
		}
		ean13(data[i-1].EmployeeCode);
	}
}

function pintStart(){
	$("#header").printArea(); 
}
function ean13(EmployeeCode){
	$("#bcTarget_"+EmployeeCode).empty().barcode(EmployeeCode, "ean13",{barWidth:1.5, barHeight:35,showHRI:true});
}

function barcode13(){
	 $(".ean").EAN13("200010047518");
}