// JScript ファイル
Ext.getMsg = function(ID,param1,param2,param3){
    var returnValue="";
    switch(ID)
    {
        case "I0001":
            returnValue = "正しい社員番号を入力してください。";
            break;
        case "I0005":
            returnValue =  "名札印刷";
            break;
        case "I0011":
            returnValue = "ネットワークに接続されていません。";
            break;                         
        default:
            break;
    }
    return Ext.String.format(returnValue,param1,param2,param3);
}