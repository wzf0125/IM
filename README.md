# 基于WebSocket的简单IM

## 技术栈

vue2 + netty

目前前端页面来源于: https://gitee.com/mao-yongyao/chatroom

1.0版本先做单体项目

存在的缺陷:
- 分布式会出现需要广播的用户连接到了不同的im服务器，需要服务器直接进行通讯才能将消息传递给其他服务

todo:
- 实现获取用户列表
- 发起单点聊天
- 发起群组聊天
- 加密通讯
