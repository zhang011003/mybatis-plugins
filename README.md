# mybatis-plugins
主要包括在使用mybatis的过程中自己写的一些插件，如果发现还需要编写插件生成代码，则会继续在这里添加新的插件。

目前这里包含如下插件：
- com.study.mybatisplugins.RenameExampleMethodPlugin

    该插件主要是将默认带example字符串的方法替换为指定的字符串。
    
    mybatis默认生成的example类名为xxxExample，并且对应的方法格式为xxxByExamplexxx。
    
    mybatis默认提供了将example类名替换的插件，详见[Mybatis Supplied Plugins](http://www.mybatis.org/generator/reference/plugins.html)，却没提供生成方法名替换的插件，需要自己编写插件来替换。
    
    用法：
    
    ```
	<plugin type="com.study.mybatisplugins.RenameExampleMethodPlugin">
            <property name="searchString" value="Example"/>
            <property name="replaceString" value="Criteria"/>
        </plugin>
    ```
    
- com.study.mybatisplugins.ExtractClientMethodToInterfacePlugin

    该插件主要是将client接口中的方法提取到公共接口中
    
    mybatis生成的client接口方法大多雷同，比如countByExample，deleteByExample等等。可以将这些类似的方法提出到公共的接口中，相应的model用范性替换即可，client只需要继承该公共接口即可。
    
    这样做最大的好处就是如果需要添加自己的新方法时，直接添加在client中，将自己添加的方法和mybatis自动生成的方法最大限度的分开。
    
    该插件没有参数，直接申明使用即可。
    
