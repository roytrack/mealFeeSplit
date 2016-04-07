<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <link rel="shortcut icon" href="http://7sblu6.com1.z0.glb.clouddn.com/icon_favicon.ico">
</head>
<body>
<h2>饿了吗&美团餐费分拆计算</h2>
<h3><a href="/s/readme.html" target="_blank">饿了么使用说明</a> </h3>
<h3><a href="/s/readme2.html" target="_blank">美团使用说明</a> </h3>
<form >
    <textarea id="origin" name="origin" cols="100" rows="50"></textarea>
<button type="button" id="commit">提交</button>
    <button type="button" id="split">按人分拆</button>
</form>
<div id="result"></div>

<div id="splitResult"></div>
<!--free jquery image-->
<script  src="http://7sblu6.com1.z0.glb.clouddn.com/jquery_jquery-1.11.3.min.js"></script>
<script>
    $("#commit").click(function(){
        $.ajax({
            url: "/calc",
            method:"post",
            data: {origin:$("#origin").val()},
            success:function(data){
                console.log(data);
                $("#result").html(data);
            }});
    });

    $("#split").click(function(){
        var sum=$("#tab1").attr("class");
        console.log(" sum is "+sum);
        var lineNum=sum.substr(3);
        var personInfo="";

        for(var i=1;i<lineNum;i++){
            console.log("indicate element  "+ i+"  " +$(".owner1").length);
            if($(".owner"+i).length==1){
                personInfo+=i+"@"+$(".owner"+i).val()+"#";
                console.log("personInfo :"+personInfo);
            }
        }
        $.ajax({
            url: "/split",
            method:"post",
            data: {personInfo:personInfo},
            success:function(data){
                console.log(data);
                $("#splitResult").html(data);
            }});

    });

</script>
</body>
</html>
