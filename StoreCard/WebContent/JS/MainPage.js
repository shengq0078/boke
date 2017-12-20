/*
    作成者:10060815　李靜
    作成日:2015/01/15
*/
var GridHeight = "";
var originalEmpCD = "";
var EmployeeCode = "";

//System load
Ext.onReady(function() {
    GridHeight = document.documentElement.clientHeight - 60; //screen.availHeight*0.6;//get the suitable height

    //Employee Model
    Ext.define('MainPage.model.EmployeeInfo', {
        extend: 'Ext.data.Model',
        fields: [
            { name: 'EmployeeCode', type: 'string' },
            { name: 'EmployeeName', type: 'string' },
            { name: 'EmployeeName_syllabary', type: 'string' },
            { name: 'Employment_title', type: 'string' },
            { name: 'Company_name', type: 'string' }
        ]
    });

    //Employee Store
    var store = new Ext.data.Store({
        autoDestroy: true, //销毁store当组件的store已经被绑定并销毁;销毁组件在内存中的占用，释放内存空间
        model: MainPage.model.EmployeeInfo,
        storeId: 'MainPageStore',
        data: { 'items':
           [{ 'EmployeeCode': EmployeeCode}]
        }, //用于显示空行
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                root: 'items'
            }
        }
    });

    var cellEditing = new Ext.grid.plugin.CellEditing({
        id: 'cellEditing',
        clicksToEdit: 1 //单击进入编辑状态
    });


    var Grid = Ext.define('MainPage.CellEditing', {
        extend: 'Ext.grid.Panel',
        sortableColumns: false, //根据表头的字段进行排序
        requires: [
            'Ext.selection.CellModel',
            'Ext.grid.*',
            'Ext.data.*',
            'Ext.util.*',
            'Ext.form.*'
        ],
        id: 'MainGrid',
        xtype: 'cell-editing',
        frame: true,
        listeners: {
            "beforeedit": function(t, e) {//给特定的行、列 设置编辑与否
                if (e.record.data.StopStatus != "停止") {
                    if (e.colIdx != 0) {//除了第0列，其他列都不可以编辑
                        return false;
                    }
                    else {
                        return true;
                    }
                }
            }
        },
        initComponent: function() {
            Ext.apply(this, {
                width: '100%',
                height: GridHeight,
                style: 'padding:6px;',
                plugins: [cellEditing],
                columnLines: true,
                store: store,
                columns: [
                {
                    text: '従業員ID',
                    align: 'left',
                    width: '10%',
                    style: "font-size:18px;font-family:ＭＳ Ｐゴシック",
                    dataIndex: 'EmployeeCode',
                    renderer: function(value, meta, record, rowIdx, colIdx, store) {
                        return record.data.EmployeeCode;
                    },
                    editor: {
                        xtype: 'textfield',
                        id: 'EmployeeCode',
                        allowBlank: false, //是否允许为空
                        allowNegative: false, //是否允许负数
                        allowDecimals: false, //是否允许小数点 
                        hideTrigger: true,
                        enableKeyEvents: true, //keypress事件：输入字段键盘按下时的事件，为true时该事件有效
                        listeners: {
                            focus: function(t, e, eOpts) {
                                originalEmpCD = t.value;
                            },
                            'blur': function(e, t, eOpts) {
                                var splitChar = ' ';
                                if (!navigator.onLine) {
                                    Ext.Msg.setIcon(Ext.MessageBox.ERROR);
                                    Ext.MessageBox.alert(Ext.getMsg('I0005'), Ext.getMsg('I0011'), function() { Ext.getCmp('EmployeeCode').focus(); });
                                    return;
                                }
                                var EmpCD = e.value;
                                if (Ext.isIE && null != window.clipboardData.getData("text")) {
                                    EmpCD = window.clipboardData.getData("text");
                                    splitChar = '\n';
                                    window.clipboardData.clearData("text");
                                }
                                Ext.getCmp('EmployeeCode').setValue("");
                                if (null == EmpCD || Ext.Trim(EmpCD) == "") {
                                    return;
                                }
                                if (EmpCD == originalEmpCD) {
                                    return;
                                }
                                
                                var store100 = Ext.getCmp('MainGrid').getStore();
                                var count = store100.getCount();
                                var EmpID100 = "";
                                if (count != 1) {
                                	
                                	for (var i = 0; i < count - 1; i++) {
                                		EmpID100 += store100.getAt(i).data.EmployeeCode + ",";
                                	}
                                	var reg = /,$/gi;
                                	EmpID100 = EmpID100.replace(reg, "");
                                }
                                
                                
                                var arr = new Array();
        
                                arr = EmpCD.split(splitChar);
                                var strEmpID="";
                                for (var i = 0; i < arr.length; i++) {
          
                                    strEmpID += arr[i] + ","
                                }
	                            var reg = /,$/gi;
	                            strEmpID = strEmpID.replace(reg, "");
	                            if(EmpID100!="")
	                            { 
	                            	strEmpID=EmpID100+","+strEmpID;
	                            }
	                            
	                   
                                    if (strEmpID == "") {
                                        //continue;
                                    }
                                    
                                    Ext.Ajax.request({
                                        url: '../StoreCard/rest/info/getEmployeeByCD',
                                        async: false,
                                       // params: {"name":"10012606,10012607"},
                                        jsonData:'{"name":"'+strEmpID+'"}',
                                        method: 'POST',
                                        fields: ['VALUE'],
                                        success: function(response) {
                                            if (response.responseText != "") {
                                               // var janObj = eval(response.responseText)[0];
                                            	var janObj = Ext.decode(response.responseText);
                                                if (janObj != null) {
                                                    /*var curObject = Ext.getCmp('MainGrid').getSelectionModel().getSelection()[0].data;
                                                    curObject.EmployeeCode = arrEmpCD;
                                                    curObject.EmployeeName = janObj.EmployeeName;
                                                    curObject.EmployeeName_syllabary = janObj.EmployeeName_syllabary;
                                                    curObject.Employment_title = janObj.Employment_title;
                                                    curObject.Company_name = janObj.Company_name;*/
                                                    
                                                	var storesq=Ext.getCmp('MainGrid').getStore();
                                                	store.loadData(janObj);
                                                    var store2 = Ext.getCmp('MainGrid').getStore();
                                                    var count = store2.getCount() - 1;
                                                    var at = store2.getAt(count);
                                                    var name = at.data.EmployeeName;
                                                    if (name != "") {
                                                        var model = new MainPage.model.EmployeeInfo({
                                                            EmployeeCode: '',
                                                            EmployeeName: '',
                                                            EmployeeName_syllabary: '',
                                                            Employment_title: '',
                                                            Company_name: ''
                                                        });
                                                        store.insert(store.data.length, model); //insert a new row
                                                        var rowNum = Ext.getCmp('MainGrid').getStore().getCount();
                                                        Ext.getCmp('MainGrid').getView().refresh();
                                                        cellEditing.startEditByPosition({ row: rowNum - 1, column: 0 });
                                                        cellEditing.cancelEdit();
                                                    } else {
                                                        var curRow = Ext.getCmp('MainGrid').getStore().indexOf(Ext.getCmp('MainGrid').getSelectionModel().getSelection()[0]);
                                                        //Ext.getCmp('MainGrid').getView().refresh();
                                                        cellEditing.startEditByPosition({ row: curRow + 1, column: 0 });
                                                        cellEditing.cancelEdit();
                                                    }
                                                } else {
                                                    Ext.getCmp('MainGrid').getSelectionModel().getSelection()[0].data.EmployeeCode = originalEmpCD;
                                                   Ext.getCmp('MainGrid').getView().refresh();
                                                    cellEditing.cancelEdit();
                                                    //Ext.MessageBox.alert(Ext.getMsg('I0005'), Ext.getMsg("I0001"));
                                                }
                                            }
                                        },
                                        failure: function(result, request) {
                                            Ext.getCmp('MainGrid').getSelectionModel().getSelection()[0].data.EmployeeCode = originalEmpCD;
                                            Ext.getCmp('MainGrid').getView().refresh();
                                            Ext.MessageBox.alert(Ext.getMsg('I0005'), Ext.getMsg("I0001"));
                                            Ext.Msg.setIcon(Ext.MessageBox.ERROR);
                                        }
                                    });
                                }
                            } //blur
}//listeners
//}//editor
                        }, {
                            text: '氏名',
                            align: 'left',
                            width: '17%',
                            style: "font-size:18px;font-family:ＭＳ Ｐゴシック",
                            dataIndex: 'EmployeeName',
                            renderer: function(value, meta, record, rowIdx, colIdx, store) {
                                meta.tdCls = "gray";
                                if (this.getStore().getCount() == (rowIdx + 1)) {
                                    return '';
                                } else {
                                    return record.data.EmployeeName;
                                }
                            }
                        }, {
                            text: 'カタカナ',
                            align: 'left',
                            width: '15%',
                            style: "font-size:18px;font-family:ＭＳ Ｐゴシック",
                            //dataIndex: 'EmployeeName_syllabary',
                            renderer: function(value, meta, record, rowIdx, colIdx, store) {
                                meta.tdCls = "gray";
                                if (this.getStore().getCount() == (rowIdx + 1)) {
                                    return '';
                                } else {
                                    return record.data.EmployeeName_syllabary;
                                }
                            }
                         }, {
                            text: '担当',
                            align: 'left',
                            width: '34.75%',
                            style: "font-size:18px;font-family:ＭＳ Ｐゴシック",
                            //dataIndex: 'Employment_title',
                            renderer: function(value, meta, record, rowIdx, colIdx, store) {
                                meta.tdCls = "gray";
                                if (this.getStore().getCount() == (rowIdx + 1)) {
                                    return '';
                                } else {
                                    return record.data.Employment_title;
                                }
                            }
                        }, {
                            text: '印刷フレーム',
                            align: 'left',
                            width: '20%',
                            style: "font-size:18px;font-family:ＭＳ Ｐゴシック",
                            //dataIndex: 'Company_name',
                            renderer: function(value, meta, record, rowIdx, colIdx, store) {
                                meta.tdCls = "gray";
                                if (this.getStore().getCount() == (rowIdx + 1)) {
                                    return '';
                                } else {
                                    return record.data.Company_name;
                                }
                            }
                        }, {
                            xtype: 'actioncolumn', //the del button
                            align: 'center',
                            width: '3%',
                            menuDisabled: true,
                            items: [{
                                icon: 'image/del.jpg',
                                tooltip: 'Delete Plant',
                                scope: this,
                                handler: this.onRemoveClick
}]
}],
                                selModel: {
                                    selType: 'cellmodel'
                                }
                            });
                            this.callParent();
                        },
                        //remove a line
                        onRemoveClick: function(grid, rowIndex) {
                            if (this.getStore().getCount() > 1 && this.getStore().getCount() != (rowIndex + 1)) {
                                this.getStore().removeAt(rowIndex);
                            }
                        }
                    });

                    //the north and the center view
                    new Ext.Viewport({
                        layout: 'border',
                        items: [{
                            region: "north",
                            height: 50,
                            width: 500,
                            bodyStyle: 'background:#4A708B;',
                            html: "<div id=\"topTitle\" style=\"font-weight:bold;text-align:center;vertical-align:middle\"><font size=\"7\" color=\"White\">名札印刷</font><span><div style=\"text-align:right;\"><input type=\"button\" onclick=\"btnPrint();\"  value=\"印刷\" style=\"width:100px;height:40px;margin-right:10px;margin-top:-43px;font-size:18px;font-family:ＭＳ Ｐゴシック;font-weight: bold;\"></input></div></span></div>"
                        }, {
                            region: "center",
                            items: [Grid]
}]
                        });
                    });

Ext.Trim = function trim(str) { //删除左右两端的空格
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str) { //删除左边的空格
    return str.replace(/(^\s*)/g, "");
}
function rtrim(str) { //删除右边的空格
    return str.replace(/(\s*$)/g, "");
}

//点击打印按钮，生成pdf文件
function btnPrint() {
    var store = Ext.getCmp('MainGrid').getStore();
    var count = store.getCount();
    if (count != 1) {
        var EmpID = "";
        for (var i = 0; i < count - 1; i++) {
            EmpID += store.getAt(i).data.EmployeeCode + ",";
        }
        var reg = /,$/gi;
        EmpID = EmpID.replace(reg, "");
        //印刷のところにクリア
        $("#header").empty();
        //写真作成
        dataMack(EmpID);
       // alert("印刷開始");
        //印刷開始
        pintStart();
   
      //  window.open("Print.aspx?EmpID=" + EmpID);
    }
    else {
        alert("データがありません");
    }
}
