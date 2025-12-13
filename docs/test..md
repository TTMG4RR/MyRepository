| 测试阶段 | 模块         | 测试接口                                        | 请求方式   | 前置条件             | 核心测试点                               | 异常场景测试                                 |
|------|------------|---------------------------------------------|--------|------------------|-------------------------------------|----------------------------------------|
| 第一步  | 用户管理       | `/api/user/add`                             | POST   | 无                | 1. 返回成功提示；2. 数据库新增用户记录              | 传空 name/phone、重复手机号                    |
|      |            | `/api/user/findById/{id}`                   | GET    | 已新增用户            | 返回用户信息与新增一致                         | 传不存在的 ID、ID≤0                          |
|      |            | `/api/user/findByPhone/{phone}`             | GET    | 已新增用户            | 返回对应手机号的用户信息                        | 传不存在的手机号                               |
|      |            | `/api/user/findAll`                         | GET    | 已新增至少 1 个用户      | 返回列表包含所有新增用户                        | 无用户时返回空列表                              |
|      |            | `/api/user/update`                          | PUT    | 已新增用户            | 1. 返回成功提示；2. 查询用户信息已更新              | 传不存在的用户 ID、更新字段为空                      |
|      |            | `/api/user/delete/{id}`                     | DELETE | 已新增用户            | 1. 返回成功提示；2. 查询列表无该用户               | 传不存在的 ID、ID≤0                          |
|      |            | `/api/user/deleteUserAll`                   | DELETE | 测试环境（最后测）        | 查询列表为空                              | 无（仅验证数据清空）                             |
| 第二步  | 商品管理       | `/api/product/add`                          | POST   | 无                | 1. 返回成功提示；2. 数据库新增商品记录              | 传空 name、price≤0                        |
|      |            | `/api/product/findById/{id}`                | GET    | 已新增商品            | 返回商品信息与新增一致                         | 传不存在的 ID、ID≤0                          |
|      |            | `/api/product/findByName/{name}`            | GET    | 已新增商品            | 返回对应名称的商品信息（支持模糊查询则验证模糊匹配）          | 传不存在的商品名                               |
|      |            | `/api/product/findAll`                      | GET    | 已新增至少 1 个商品      | 返回列表包含所有新增商品                        | 无商品时返回空列表                              |
|      |            | `/api/product/update`                       | PUT    | 已新增商品            | 1. 返回成功提示；2. 查询商品信息已更新              | 传不存在的商品 ID、更新字段为空                      |
|      |            | `/api/product/deleteById/{id}`              | DELETE | 已新增商品            | 1. 返回成功提示；2. 查询列表无该商品               | 传不存在的 ID、ID≤0                          |
|      |            | `/api/product/deleteAll`                    | DELETE | 测试环境（最后测）        | 查询列表为空                              | 无（仅验证数据清空）                             |
| 第三步  | 订单基础       | `/api/order/add`                            | POST   | 已新增用户（获取 userId） | 1. 返回成功提示；2. 数据库新增订单记录（userId 关联正确） | 传不存在的 userId、空 orderNo                 |
|      |            | `/api/order/findAll`                        | GET    | 已新增至少 1 个订单      | 返回列表包含所有新增订单                        | 无订单时返回空列表                              |
|      |            | `/api/order/findUserWithOrders/{id}`        | GET    | 已新增用户 + 该用户的订单   | 返回用户信息 + 关联的订单列表（数量 / 内容匹配）         | 传不存在的用户 ID、无订单的用户 ID                   |
| 第四步  | 订单 - 商品多对多 | `/api/order/{orderId}/products/{productId}` | POST   | 已新增订单 + 商品       | 1. 返回成功提示；2. 中间表 order_product 新增记录 | 传不存在的 orderId/productId、重复添加同一商品       |
|      |            | `/api/order/{orderId}/products`             | GET    | 订单已关联商品          | 返回订单信息 + 关联的商品列表（数量 / 内容匹配）         | 传不存在的 orderId、无商品的订单 ID                |
|      |            | `/api/order/{orderId}/products/{productId}` | DELETE | 订单已关联该商品         | 1. 返回成功提示；2. 中间表删除对应记录              | 传不存在的 orderId/productId、移除未关联的商品       |
|      |            | `/api/product/{productId}/orders`           | GET    | 商品已被订单关联         | 返回商品信息 + 关联的订单列表（数量 / 内容匹配）         | 传不存在的 productId、无订单的商品 ID              |
| 第五步  | 全局异常       | 所有接口                                        | 对应方式   | 无                | -                                   | 1. 传非数字 ID；2. 接口路径错误；3. Token 失效（若有鉴权） |
| 第六步  | 批量 / 危险接口  | `/api/order/deleteAll`                      | DELETE | 测试环境（最后测）        | 查询订单列表为空                            | 无                                      |