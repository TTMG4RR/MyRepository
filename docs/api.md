# DrawBlueCup 接口文档

docs是项目中常用的 **“文档 / 示例文件目录”**


保持项目结构整洁：
把 “文档 / 示例类文件” 集中放在docs里，
和src（业务代码）分开，后期维护时更容易找。
> 通过 `http://localhost:8080/swagger-ui/index.html` 可以查看实时在线文档（SpringDoc 自动生成）。

## 统一响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

## 用户模块

| 接口 | 方法 | 描述 | 备注 |
| --- | --- | --- | --- |
| `/api/user/findAll` | GET | 查询所有用户 | 支持分页由前端控制 |
| `/api/user/findById/{id}` | GET | 根据 ID 查询用户 | path 变量 |
| `/api/user/findByPhone/{phone}` | GET | 根据手机号查询 | 自定义 @Phone 校验 |
| `/api/user/query` | GET | 组合条件查询 | name/phone/createTime/updateTime + pageNum/pageSize |
| `/api/user/add` | POST | 新增用户 | `@Valid User` 请求体 |
| `/api/user/update` | PUT | 修改用户 |  |
| `/api/user/delete/{id}` | DELETE | 删除单个用户 |  |
| `/api/user/deleteUserAll` | DELETE | 清空用户表 | 慎用 |

## 商品模块

| 接口 | 方法 | 描述 |
| --- | --- | --- |
| `/api/product/findAll` | GET | 查询所有商品 |
| `/api/product/findById/{id}` | GET | 根据 ID 查询 |
| `/api/product/findByName/{name}` | GET | 模糊查询 |
| `/api/product/add` | POST | 新增商品 |
| `/api/product/update` | PUT | 修改商品 |
| `/api/product/deleteById/{id}` | DELETE | 删除指定商品 |
| `/api/product/deleteAll` | DELETE | 清空商品表 |

## 订单模块

| 接口 | 方法 | 描述 |
| --- | --- | --- |
| `/api/order/findAll` | GET | 查询所有订单 |
| `/api/order/findUserWithOrders/{id}` | GET | 查看用户及其订单 |
| `/api/order/add` | POST | 新增订单 |
| `/api/order/deleteAll` | DELETE | 清空订单 |


## 跨域验证

- 运行 `docs/cors-fetch-demo.html`（例如使用 VS Code Live Server 监听 `http://127.0.0.1:5500`）
- 点击按钮后可看到接口返回结果，无浏览器 CORS 报错即为通过。


