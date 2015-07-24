饿了么餐费分解程序

------v1.1   update:2015年7月24日9:28:11

现在支持两种订单格式的分拆。

1.复制订单内容

1.1  http://ele.me/profile/order 获得的 老格式分拆。复制饿单从 *美食篮子* 开始 ， 到 *合计* 行 结尾。

 ![zong](http://7sblu6.com1.z0.glb.clouddn.com/elemezong.png)

 ![textarea](http://7sblu6.com1.z0.glb.clouddn.com/elemetextarea.png)

1.2  http://ele.me/profile2/order 获得的 新格式分拆。 进入http://ele.me/profile2/order 点击 *订单详情* 复制饿单从 *菜品* 到 *实际支付* 行 结尾。

![profile2_order](http://7sblu6.com1.z0.glb.clouddn.com/elemeprofile2_order.jpg)

![profile2_orderdetail](http://7sblu6.com1.z0.glb.clouddn.com/elemeprofile2_orderdetail.jpg)

![profile2_paste](http://7sblu6.com1.z0.glb.clouddn.com/elemeprofile2_paste.jpg)

2.粘贴到页面文本输入框 点击 *提交* 按钮， 出现 *美食表格* 。

![mingxi](http://7sblu6.com1.z0.glb.clouddn.com/elememingxi.png)

3.填写 *美食表格* 中所属人(多人使用逗号隔开，中英文逗号都可以)，点击 *按人分拆* 出现 *按人分拆表格* 。

![person](http://7sblu6.com1.z0.glb.clouddn.com/elemeperson.png)





运行环境要求：jdk7

bug反馈：me#roytrack.com

在使用过程中有bug出现，请反馈给上面那个邮箱撒。 如果有兴趣也可以自己pull request！