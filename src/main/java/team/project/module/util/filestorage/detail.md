# 文件存储模块的设计细节

## 介绍

文件存储模块为其他业务模块提供文件存储服务，它是一个服务模块，而不是一个业务模块。

## 接口设计

文件存储模块提供 service 层的接口，包括文件上传、获取、删除等基础操作。
上传文件后接口返回 fileId，之后只能通过 fileId 操作文件。
文件存储模块不帮记录 fileId，各业务模块应自行设计存储方式来保存 fileId。

不提供 controller 层的 api，
因为不同的业务模块对文件存取有不同的权限校验或文件验证逻辑，没有办法为不同模块提供定制化的 api 接口。

> ### 例子
> 
> 假如某业务模块有这样的需求：
> - “管理员”可以上传内部公告
> - 一篇公告可以携带一组附件文件
> - “小组成员”可以下载附件文件
> 
> 则该模块的 controller 至少应提供两个与文件存储相关的接口：
> - /upload   上传某公告的附件文件
> - /download 下载附件文件
> 
> 可以这样设计数据表：
> - id 标识一篇公告
> - file_id_list 记录这组附件的 fileId（用字符串拼接的方式连接多个 fileId）
> 
> 对于 /upload 接口，先校验用户的“管理员”角色，可能还要验证文件内容合法合规？
> 校验通过后，将文件交由文件存储模块保存，保存成功后拿到 fileId，再将 fileId 存入数据库。
> 
> 对于 /download 接口，先校验用户的“小组成员”角色，可能还要检测下载次数？
> 校验通过后，查询数据库拿到对应文件的 fileId，在把 fileId 交由文件存储模块获取 url，将 url 返给前端。
> 如果要触发浏览器下载机制，后端可以通过重定向的方式，将 /download 转到访问文件的 url。

## fileId

业务模块调用文件存储模块上传文件时，文件存储模块会根据指定目标路径和存储类型生成 fileId 并返回。
业务模块需要存储 fileId，之后要操作文件（读取、删除等），只能通过 fileId。

> ### fileId 的生成规则
> 
> fileId 仅仅是对 “访问文件的 url” 的一层包装，目前 fileId 的生成规则很简单：
> 
> fileId = 标识存储类型的前缀 + 目标路径
> 
> - 如果选择本地存储，则标识存储类型的前缀是“/f”
> - 如果选择云存储，则标识存储类型的前缀是“/a”（因为目前云存储使用 aliyunOSS）
> 
> 不同的存储类型的前缀不同，直接看前缀就能区分文件用的是什么存储方式

> ### 为什么要存储 fileId，而不存储文件 url？
> 
> - 云存储提供的 url 可能有时间限制（或其他限制），url 可能会过期（失效），所以数据库不能直接存储 url。
> - 之后可能会添加或变更存储方式，如果直接保存 url，存储方式一旦变更 url 就不能用了。
>   但如果保存的是 fileId，可以更改 fileId 到 url 的规则以应对存储方式的变化。

## 上传文件

> 安装 [mermaid](https://mermaid.js.org/) 插件后，标有 mermaid 的代码块将被渲染成 UML 图

``` mermaid
sequenceDiagram
    box 其他模块
        participant x_service as Service
    end
    box 文件存储模块
        participant f_service as 文件存储 Service
        participant l_DAO as 本地文件存储 DAO
        participant c_DAO as 阿里云 OSS DAO
    end

    x_service ->> f_service : 给定文件、目标存储路径、存储类型，上传文件
    f_service ->> f_service : 依据目标路径和存储类型生成 file_id
    alt  如果存储类型是 LOCAL
        f_service ->> l_DAO : 保存文件
    else 如果存储类型是 CLOUD
        f_service ->> c_DAO : 上传文件
    end
    f_service -->> x_service : 返回file_id
```

## 获取访问文件的 url

``` mermaid
sequenceDiagram
    box 其他模块
        participant x_service as Service
    end
    box 文件存储模块
        participant f_service as 文件存储 Service
        participant l_DAO as 本地文件存储 DAO
        participant c_DAO as 阿里云 OSS DAO
    end

    x_service ->> f_service : 给定 file_id，获取文件
    f_service ->> f_service : 依据 file_id 辨别存储类型，提取出文件路径
    alt  如果存储类型是 LOCAL
        f_service ->> l_DAO : 给定文件路径，获取url
        l_DAO -->> f_service : 访问文件的 url
    else 如果存储类型是 CLOUD
        f_service ->> c_DAO : 给定文件路径，获取url
        c_DAO -->> f_service : 返回访问文件的 url
    end
    f_service -->> x_service : 返回访问文件的 url
```
