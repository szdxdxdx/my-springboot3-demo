/**
 * <h1> 介绍
 * <p>  本模块只提供一个关于“模块内部各包如何划分”的参考，不包含任何业务逻辑
 * <p>
 * <h1> internal
 * <p>  internal 包下的所有内容，为模块内部私有
 * <p>  模块间不能相互调用 internal 包下的任何内容
 * <p>
 * <h1> export
 * <p>  export 包下的所有内容，是本模块对外公布的接口
 * <p>  模块间交流，只能在出现在 service 层
 * <p>  export 包只提供 service 层的接口，用 model 封装 service 的入参和出参
 * <p>  export 不提供 dao、mapper 等其他层的接口
 * <p>
 * <h1> tmp
 * <p>  如果开发过程中，需要使用其他模块的接口，但它还没有开发完成，无法提供接口
 * <p>  可以在本模块内建立 tmp 包，在其中先自行实现所需的 service 层接口的代码
 * <p>  将本模块中的对其他模块 service 的依赖，改为对自己模块 tmp 包中 service 的依赖
 * <p>  等到其他模块开发完成，可以提供接口后，再将 tmp 包删除
 * <p>
 */
package team.project.module._template;
