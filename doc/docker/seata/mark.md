对于 seata 集成至项目中时，注意事项：

在Seata中，seata.enable-auto-data-source-proxy配置项控制着Seata是否自动为数据源创建代理。这个配置的具体作用如下：
自动代理创建：当seata.enable-auto-data-source-proxy设置为true时，Seata会自动为你的应用配置的数据源创建一个代理（DataSourceProxy）。这个代理数据源对于应用来说是透明的，它会在后台拦截SQL操作，使得Seata能够管理SQL的执行过程，这对于实现分布式事务的协调、提交和回滚至关重要。通过这种方式，Seata能够确保事务操作的一致性，即使这些操作跨越了多个微服务或数据库。
手动控制：如果设置为false，则Seata不会自动创建数据源代理。这通常意味着你需要手动配置数据源代理，或者你的应用已经通过其他方式（比如自定义的AOP切面）来集成Seata的事务管理逻辑。这种情况下，开发者需确保所有数据访问都经过了适当的手动代理，以便Seata能够介入事务管理。
总结来说，该配置项是为了适应不同应用架构和集成需求的灵活性。在大多数标准集成场景下，将其设为true可以让Seata的集成更加简便直接。但在一些特殊场景下，比如应用已经有了自己的数据源代理机制，或者需要更细粒度的控制时，可以设为false并采取相应手动集成措施。


多数据源配置，需要自行实现代理，不推荐
    seata.enable-auto-data-source-proxy=false
单个数据源配置　
    seata.enable-auto-data-source-proxy=true

测试发现单数据库的情况下设置为false时，分布式事务也会生效
