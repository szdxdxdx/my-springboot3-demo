
> 安装 [mermaid](https://mermaid.js.org/) 插件后，标有 mermaid 的代码块将被渲染成 UML 图

业务核心逻辑在 internal 包下。通常，响应一个 http 请求时，会经过下图流程：

``` mermaid
sequenceDiagram
    box 前端
        participant web as web 前端
    end
    box 本业务模块的internal子包
        participant controller as controller
        participant service as service
        participant dao as dao
        participant mapper as mapper
    end
    box 数据库
        participant db as 数据库
    end
    web ->> controller : 发起请求，传递Req
    controller ->> controller : 进行角色鉴权、参数校验
    controller ->> service : Req
    service ->> service : 拆解请求对象Req，组装查询对象QO
    service ->> dao : QO
    dao ->> mapper : QO
    mapper ->> db : 执行SQL
    db -->> mapper : 返回SQL执行结果
    mapper -->> dao : DO
    dao -->> service : DO
    service ->> service : 拆解领域对象DO，组装视图对象VO
    service -->> controller : VO
    controller -->> web : 返回VO，响应请求
```

export包下所有内容是本模块对外公布的接口，模块间交流只能在出现在service层。各个模块间的交流遵循下图流程。

``` mermaid
sequenceDiagram
    participant ohter_service as 其他模块
    box 本模块
        participant my_export_service as export.service
        participant my_internal as internal
    end

    ohter_service ->> my_export_service : QO
    my_export_service ->> my_internal : 拆解参数
    my_internal ->> my_internal : 操纵数据库，或是其他操作
    my_internal -->> my_export_service : 返回结果
    my_export_service -->> ohter_service : DTO
```
