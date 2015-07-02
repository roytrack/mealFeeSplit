<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<h2>饿了吗餐费分拆计算</h2>
<form >
    <textarea id="origin" name="origin" cols="100" rows="50"></textarea>
<button type="button" id="commit">提交</button>
</form>
<div id="result"></div>
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

        //$("#result").load("/calc",{origin:$("#origin").val()});
    });

</script>
</body>
</html>
